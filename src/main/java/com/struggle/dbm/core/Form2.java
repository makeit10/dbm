package com.struggle.dbm.core;

import java.lang.reflect.Field;

public class Form2  {
	String test;
	public static void main(String[] args) {
		Form2 f=new Form2();
		Class clazz=f.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				System.out.println();
			}
		}
	}
}
