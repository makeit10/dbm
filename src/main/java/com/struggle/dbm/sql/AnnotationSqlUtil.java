package com.struggle.dbm.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.unitils.util.AnnotationUtils;

import com.struggle.dbm.core.Form2;
import com.struggle.dbm.core.Query;

/**
 * @Project <CL-Allocation tool>
 * @version <1.0>
 * @Author  <LIXIAOYU>
 * @Date    <2015-5-22>
 * @description 
 */
public class AnnotationSqlUtil {
	 private static final Logger log = Logger.getLogger(AnnotationSqlUtil.class);
	 public static String getInsertSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 String tableName=getTableName(clazz);
		 String columns=getColumnSubSql(clazz, ",", null,null, Id.class,Column.class);
		 String values=getQuestionMarkSubSql(clazz, ",", Id.class,Column.class);
		 StringBuffer sql=new StringBuffer("INSERT INTO ")
		 	.append(tableName)
		 	.append("(")
		 	.append(columns)
		 	.append(")")
		 	.append(" VALUES ")
		 	.append("(")
		 	.append(values)
		 	.append(")");
		return sql.toString();
	 }
	 
	 public static String getUpdateSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(clazz==null){
			return null;
		}
		String qualification=getColumnSubSql(clazz," AND ",null,"=?",Id.class);
		String sql=getUpdateSql(clazz,qualification.toString());
		return sql;
	 }
	 
	 public static String getUpdateSql(Class clazz,String qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(clazz==null){
			return null;
		}
		String tableName=getTableName(clazz);
		String columns=getColumnSubSql(clazz,",",null,"=?",Column.class);
		StringBuffer sql=new StringBuffer("UPDATE ").append(tableName).append(" SET ").append(columns).append(" WHERE ").append(qualification);
		return sql.toString();
	 }	 

	 public static String getDeleteSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 String qualification=getColumnSubSql(clazz," AND ",null,"=?",Id.class);
		 String sql=getDeleteSql(clazz,qualification.toString());
		 return sql;
	 }
	 
	 public static String getDeleteSql(Class clazz,String qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 String tableName=getTableName(clazz);
		 StringBuffer sql=new StringBuffer("DELETE FROM ").append(tableName).append(" WHERE ").append(qualification);
		 return sql.toString();
	 }
	 public static String getSelectSql(Object object) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(object==null){
			 return null;
		 }
		 String qualification=getColumnSubSql(object, ",", null,"=?", Id.class,Column.class);
		 return getSelectSql(object.getClass(),qualification);
	 }
	 public static String getSelectSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 String qualification=getColumnSubSql(clazz, ",", null,"=?", Id.class);
		 return getSelectSql(clazz,qualification);
	 }
	 public static String getSelectSql(Class clazz,int size) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 Set<Field> fields=AnnotationUtils.getFieldsAnnotatedWith(Id.class, clazz);
		 Field field = fields.iterator().next();
		 String idName=field.getName();
		 String qualification=null;
		 if(size==1){
			 qualification=idName+"=?";
		 }else if(size>1){
			 qualification=idName+" IN ("+StringUtils.repeat("?", ",", size)+")";
		}
		 return getSelectSql(clazz,qualification);
	 }
	 public static String getSelectSql(Class clazz,String qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 String columns=getColumnSubSql(clazz, ",", null,null, Id.class,Column.class);
		 String tableName=getTableName(clazz);
		 StringBuffer sql=new StringBuffer("SELECT ").append(columns).append(" FROM ").append(tableName);
		 if(StringUtils.isNotBlank(qualification)){
			 sql.append(" WHERE ").append(qualification);
		 }
		 return sql.toString();
	 } 
	 
	 
	 
	public static String getColumnSubSql(Class clazz,String separator,String prefix,String suffix,Class... annotationClasses){
		StringBuffer columnSubSql = new StringBuffer();
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
						if(validateAnnotation(field,annotationClasses)){
							if(StringUtils.isNotBlank(separator)){
								columnSubSql.append(separator);
							}
							if(StringUtils.isNotBlank(prefix)){
								columnSubSql.append(prefix);
							}
							columnSubSql.append(field.getName());
							if(StringUtils.isNotBlank(suffix)){
								columnSubSql.append(suffix);
							}
						}
				}
		 }
		 if(columnSubSql.length()>separator.length()){
			 columnSubSql.delete(0, separator.length());
		 }
		 return columnSubSql.toString();
	}
	public static String getColumnSubSql(Object entity,String separator,String prefix,String suffix,Class... annotationClasses){
		if(entity==null){return null;}
		Class clazz=entity.getClass();
		StringBuffer columnSubSql = new StringBuffer();
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
					String value=null;
					try {
						value=BeanUtils.getProperty(entity, field.getName());
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						if(validateAnnotation(field,annotationClasses)&&StringUtils.isNotBlank(value)){
							if(StringUtils.isNotBlank(separator)){
								columnSubSql.append(separator);
							}
							if(StringUtils.isNotBlank(prefix)){
								columnSubSql.append(prefix);
							}
							columnSubSql.append(field.getName());
							if(StringUtils.isNotBlank(suffix)){
								columnSubSql.append(suffix);
							}
						}
				}
		 }
		 if(columnSubSql.length()>separator.length()){
			 columnSubSql.delete(0, separator.length());
		 }
		 return columnSubSql.toString();
	}
	public static String getQuestionMarkSubSql(Class clazz,String separator,Class... annotationClasses){
		 StringBuffer questionMarkSubSql = new StringBuffer();
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
						if(validateAnnotation(field,annotationClasses)){
							if(StringUtils.isNotBlank(separator)){
								questionMarkSubSql.append(separator);
							}
							questionMarkSubSql.append("?");
						}
				}
		 }
		 if(questionMarkSubSql.length()>separator.length()){
			 questionMarkSubSql.delete(0, separator.length());
		 }
		 return questionMarkSubSql.toString();
	}

	 public static List getParams(Object entity,int type,Class... annotationClasses) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 List params=new ArrayList();
		 Class clazz=entity.getClass();
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
					String value=null;
					try {
						value=BeanUtils.getProperty(entity, field.getName());
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(validateAnnotation(field,annotationClasses)&&((StringUtils.isNotBlank(value)&&type==1)||(type==0))){
						String fieldValue=BeanUtils.getProperty(entity,field.getName());
						params.add(fieldValue);
					}
				}
		 }
		return params;
	 }
	 
/*	public static TableMapping getTableMapping(Class clazz){
			 TableMapping tableMapping=new TableMapping();
			 String  tableName=clazz.getSimpleName().toUpperCase();
			 if(clazz.isAnnotationPresent(Table.class)){
				 Table table =  (Table) clazz.getAnnotation(Table.class);
				if(!StringUtils.isBlank(table.name())){
					tableName=table.name();
					tableMapping.setTableName(tableName);;
				 }
			 }
			 
			 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
					Field[] fields = clazz.getDeclaredFields();
					for (int i=0;i<fields.length;i++) {
						Field field=fields[i];
						if(field.isAnnotationPresent(Id.class)){
							tableMapping.setIdName(getColumnName(field));
						}
						if(field.isAnnotationPresent(Column.class)||field.isAnnotationPresent(Id.class)){
							tableMapping.addColumnNames(getColumnName(field));
						}
					}
			 }
			return tableMapping;
		} */
	 
	private static String getTableName(Class clazz){
		 String  tableName=clazz.getSimpleName().toUpperCase();
		 if(clazz.isAnnotationPresent(Table.class)){
			 Table table =  (Table) clazz.getAnnotation(Table.class);
			if(!StringUtils.isBlank(table.name())){
				tableName=table.name();
			 }
		 }
		 return tableName;
	}
	
	private static String getColumnName(Field field){
		 String  columnName=field.getName();
		 if(field.isAnnotationPresent(Column.class)){
			 Column column =  field.getAnnotation(Column.class);
			if(!StringUtils.isBlank(column.name())){
				columnName=column.name();
			 }
		 }
		 return columnName;
	}
	
	public static boolean validateAnnotation(Field field,Class[] annotationClasses){
		for(Class annotationClass:annotationClasses){
			if(field.isAnnotationPresent(annotationClass)){
				return true;
			}
		}
		return false;
	}
	public static boolean validateQuery(Query query){
		if(query!=null&&query.getSql()!=null){
			return true;
		}else{
			return false;
		}
	}
	public static TableMapping getTableMapping(Class clazz){
		 TableMapping tableMapping=new TableMapping();
		 String  tableName=clazz.getSimpleName().toUpperCase();
		 if(clazz.isAnnotationPresent(Table.class)){
			 Table table =  (Table) clazz.getAnnotation(Table.class);
			if(!StringUtils.isBlank(table.name())){
				tableName=table.name();
				tableMapping.setTableName(tableName);;
			 }
		 }
		 
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
					if(field.isAnnotationPresent(Id.class)){
						tableMapping.setIdName(getColumnName(field));
					}
					if(field.isAnnotationPresent(Column.class)||field.isAnnotationPresent(Id.class)){
						tableMapping.addColumnNames(getColumnName(field));
					}
				}
		 }
		return tableMapping;
	} 
	/*public List<String> getColumns(Class clazz){
		
		return null;
	}*/
	public static void main(String args[]) throws SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException{
		/*History history=new History();
		history.setItemId(10);
		history.setAgentId("11111111");
		Set<String> fields= new HashSet();
		fields.add("ASSIGNTO");
		Map<String,String> sortList =new HashMap<String,String>();
		sortList.put("ASSIGNTO", "DESC");
		sortList.put("MODIFYCAUSE", "aSC");
		System.out.println(toSelectSql(history.getClass(),fields,"1=1",sortList));*/
		/*Form form=new Form();
		form.setFormId(900);
		Query qualification=new Query();
		qualification.setSql("formId=?");
		qualification.addParam(form.getFormId());
		Query query = new Query();
		query.addParams(qualification.getParams());
		System.out.println(query.getParams());*/
	/*	for(Object o:query.getParams()){
			System.out.println(o);Table
		}*/
//		System.out.println(AnnotationSqlUtil.toInsertQuery(f));
		;
		System.out.println(AnnotationUtils.getClassLevelAnnotation(Table.class, Form2.class).name());
	}
}
