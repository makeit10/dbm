package com.struggle.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.struggle.dbm.core.Configuration;
import com.struggle.dbm.core.SessionFactory;
import com.struggle.dbm.core.TransactionHandler;
public class Test {

	public static void main(String[] args) {
		Configuration Configuration=new Configuration().configure();
		SessionFactory sessionFactory = Configuration.buildSessionFactory("SMDB");
		StudentService studentService=new StudentServiceImpl();
		studentService.setSessionFactory(sessionFactory);
        StudentService proxy=(StudentService)Proxy.newProxyInstance(
        		studentService.getClass().getClassLoader(), 
        		studentService.getClass().getInterfaces(), 
        		new TransactionHandler(sessionFactory,studentService));
        proxy.addStudent("b",10);
	}
	

}
