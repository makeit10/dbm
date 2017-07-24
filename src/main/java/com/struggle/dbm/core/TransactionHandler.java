package com.struggle.dbm.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.struggle.dbm.core.impl.SessionImpl;


public class TransactionHandler implements InvocationHandler{
	private final static Logger log = Logger.getLogger(TransactionHandler.class);
	private SessionFactory sessionFactory=null;
	private Object obj=null;
	
	public TransactionHandler() {
		
	}

	public TransactionHandler(Object obj) {
		this.obj = obj;
	}

	public TransactionHandler(SessionFactory sessionFactory, Object obj) {
		this.sessionFactory = sessionFactory;
		this.obj = obj;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Object invoke(Object proxy, Method method, Object[] args){
		Session session=null;
		Object result=null;
		try {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			log.debug("执行方法("+method.getName()+")开启事务");
			result=method.invoke(obj, args);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				session.getTransaction().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				session.getTransaction().commit();
				log.debug("执行方法("+method.getName()+")关闭事务");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

}
