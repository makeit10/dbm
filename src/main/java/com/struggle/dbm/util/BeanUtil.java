package com.struggle.dbm.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class BeanUtil {
	/*public final static int KEY_UPPERCASE=1;
	public final static int KEY_LOWERCASE=2;*/
	public final static int VALUE_NULL=4;
	public final static int VALUE_NONULL=8;
	/*public final static int KEY_UPPERCASE_VALUE_NONULL=9;
	public final static int KEY_LOWERCASE_VALUE_NONULL=10;*/
	public static Map<String,Object> toBeanMap(Object object,int type){
		Map<String,Object> beanMap=null;
		if(object!=null){
			Map<String,Field> fieldMap=FieldUtil.getFieldMap(object.getClass());
			beanMap=new CaseInsensitiveMap();
			for(String key:fieldMap.keySet()){
				Field field=fieldMap.get(key);
					String fieldName=field.getName();
					Object value=BeanUtil.getProperty(object, fieldName);
					if((type&BeanUtil.VALUE_NONULL)==BeanUtil.VALUE_NONULL){
						if(value==null){
							beanMap.put(key, value);
						}
					}else{
						beanMap.put(key, value);
					}
			}
			
		}
		return beanMap;
	}
	
	public  static <T> T toBean(Map<String,Object> beanMap,Class<T> clazz){
		T obj=null;
		if(beanMap!=null){
			try {
				obj=clazz.newInstance();
				Map<String,Field> fieldMap=FieldUtil.getFieldMap(clazz);
				for(String key:beanMap.keySet()){
					Object value=beanMap.get(key);
					Field field=fieldMap.get(key);
					try {
						BeanUtils.setProperty(obj, field.getName(),value);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return obj;
	}
	public  static <T> List<T> toBeanList(List<Map<String,Object>> beanMapList,Class<T> clazz){
		List<T> beanList=null;
		if(beanMapList!=null){
			beanList=new ArrayList<T>();
			for(Map<String,Object> beanMap:beanMapList){
				T obj=toBean(beanMap,clazz);
				beanList.add(obj);
			}
		}
		return beanList;
	}
	
	public static Object getProperty(Object object, String fieldName){
		Object result=null;
		try {
			String getMethodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			Method method=object.getClass().getMethod(getMethodName, null);
			result=method.invoke(object, null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
		
	}

}
