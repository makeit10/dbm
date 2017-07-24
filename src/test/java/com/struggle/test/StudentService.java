package com.struggle.test;

import java.sql.Connection;

import com.struggle.dbm.core.SessionFactory;

public interface StudentService {
	public void setSessionFactory(SessionFactory sessionFactory);
	public abstract void addStudent(String name,int age);
}