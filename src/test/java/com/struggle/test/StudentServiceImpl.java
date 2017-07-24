package com.struggle.test;

import java.sql.SQLException;

import com.struggle.dbm.core.Query;
import com.struggle.dbm.core.Session;
import com.struggle.dbm.core.SessionFactory;

public class StudentServiceImpl implements StudentService {
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void addStudent(String name,int age) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query =new Query("insert into table1 values (?,?)");
			query.addParam(name);
			query.addParam(age);
			session.update(query);
			int i=10/0;
			Query query1 =new Query("insert into table1 values (?,?)");
			query1.addParam(name+"1");
			query1.addParam(age+1);
			session.update(query1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
}
