package com.struggle.dbm.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.reflect.FieldUtils;

public class FieldUtil extends FieldUtils{
	public static List<Field> getFieldList(Class clazz){
		List<Field> fieldList=new ArrayList<Field>();
		Class superClass=clazz.getSuperclass();
		while(superClass!=null){
			for(java.lang.reflect.Field f:superClass.getDeclaredFields()){
				fieldList.add(f);
			}
			superClass=superClass.getSuperclass();
		}
		for(java.lang.reflect.Field f:clazz.getDeclaredFields()){
			fieldList.add(f);
		}
		return fieldList;
	}
	public static Map<String,Field> getFieldMap(Class clazz){
		if(clazz==null){
			return null;
		}
		Map<String,Field> fieldMap=new CaseInsensitiveMap();
		Class superClass=clazz.getSuperclass();
		while(superClass!=null){
			for(java.lang.reflect.Field f:superClass.getDeclaredFields()){
				String key=f.getName();
				fieldMap.put(key, f);
			}
			superClass=superClass.getSuperclass();
		}
		for(java.lang.reflect.Field f:clazz.getDeclaredFields()){
			String key=f.getName();
			fieldMap.put(key, f);
		}
		return fieldMap;
	}
}
