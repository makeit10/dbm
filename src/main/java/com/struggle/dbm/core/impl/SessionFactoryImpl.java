package com.struggle.dbm.core.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.struggle.dbm.core.Session;
import com.struggle.dbm.core.SessionFactory;
import com.struggle.dbm.core.Setting;

public class SessionFactoryImpl implements SessionFactory{
	private DataSource datasource;
	private Setting setting;
	private ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<Session>();
	public SessionFactoryImpl(DataSource datasource,Setting setting) {
		this.datasource = datasource;
		this.setting=setting;
	}

	public Session openSession() throws SQLException {
		Connection connection=datasource.getConnection();
		Session session=new SessionImpl(connection,setting);
		return session;
	}

	public Session getCurrentSession() throws SQLException {
		Session session=sessionThreadLocal.get();
		if(session==null||session.isClosed()){
			Connection connection=datasource.getConnection();
			session=new SessionImpl(connection,setting);
			sessionThreadLocal.set(session);
		}
		return session;
	}


}
