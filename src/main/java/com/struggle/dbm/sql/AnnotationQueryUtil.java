package com.struggle.dbm.sql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.struggle.dbm.core.Form;
import com.struggle.dbm.core.Form2;
import com.struggle.dbm.core.Query;

/**
 * @Project <CL-Allocation tool>
 * @version <1.0>
 * @Author  <LIXIAOYU>
 * @Date    <2015-5-22>
 * @description 
 */
public class AnnotationQueryUtil {
	 private static final Logger log = Logger.getLogger(AnnotationQueryUtil.class);

	/* public static Query toInsertQuery(Object entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 Query query=new Query();
		 if(entity!=null){
			 Class clazz=entity.getClass();
			 query.setSql(getInsertSql(clazz));
			 query.addParams(getParams(entity,Id.class,Column.class));
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
	 }
	 
	 
	 public static Query toInsertQuery(List entitys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 Query query=new Query();
		 if(entitys!=null||entitys.size()>0){
			 Class clazz=entitys.get(0).getClass();
			 StringBuffer sql=getInsertSql(clazz); 
			 query.setSql(sql);
			 for(Object entity:entitys){
				query.addParam(getParams(entity,Id.class,Column.class)); 
			 }
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
		
	 }
	 
	 
	 public static Query toUpdateQuery(Object entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		 Query query=new Query();
		 if(entity!=null){
			 Class clazz=entity.getClass();
			 query.setSql(getUpdateSql(clazz));
			 query.addParams(getParams(entity,Column.class));
			 query.addParams(getParams(entity,Id.class));
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
	 }
	 
	 public static Query toUpdateQuery(List entitys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		 Query query=new Query();
		 if(entitys!=null||entitys.size()>0){
			 Class clazz=entitys.get(0).getClass();
			 StringBuffer sql=getInsertSql(clazz); 
			 query.setSql(sql);
			 for(Object entity:entitys){
				query.addParam(getParams(entity,Id.class)); 
			 }
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
	 } 
	 
	 public static Query toUpdateQuery(Object entity,Query qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 Query query=new Query();
		 if(entity!=null){
			 Class clazz=entity.getClass();
			 query.setSql(getUpdateSql(clazz,qualification.getSql()));
			 query.addParams(qualification.getParams());
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
	 }
	 
	 public static Query toDeleteQuery(Object entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		 Query query=new Query();
		 if(entity!=null){
			 Class clazz=entity.getClass();
			 query.setSql(getDeleteSql(clazz));
			 query.addParam(getParams(entity,Id.class));
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
	 }
	
	 public static Query toDeleteQuery(List entitys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		 Query query=new Query();
		 if(entitys!=null||entitys.size()>0){
			 Class clazz=entitys.get(0).getClass();
			 StringBuffer sql=getDeleteSql(clazz); 
			 query.setSql(sql);
			 for(Object entity:entitys){
				query.addParam(getParams(entity,Id.class)); 
			 }
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
	 } 
	 
	public static Query toDeleteQuery(Class clazz,Query qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Query query=new Query();
		if(clazz==null){
				return null;
		}
		query.setSql(getDeleteSql(clazz,qualification.getSql()));
		query.addParams(qualification.getParams());
		if(validateQuery(query)){
			 return query;
		}else{
			 return null;
		}
	}
		
	 public static Query toSelectQuery(Class clazz,Serializable id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			if(clazz==null){
				return null;
			}
			Query query=new Query();
			query.setSql(getSelectSql(clazz));
			query.addParam(id);
			if(validateQuery(query)){
				 return query;
			}else{
				 return null;
			}
	}
	 public static Query toSelectQuery(Class clazz,Serializable... ids) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			if(clazz==null){
				return null;
			}
			TableMapping tableMapping = getTableMapping(clazz);
			String idName=tableMapping.getIdName();
			Query qualification=new Query();
			StringBuffer qualificationStr=new StringBuffer(); 
			if(ids.length==1){
				qualificationStr.append("id=?");
			}else if(ids.length==1){
				qualificationStr.append("id in (")
				.append(StringUtils.repeat("?", ",", ids.length))
				.append(")");
			}
			qualification.setSql(qualificationStr);
			qualification.addParams(ids);
			Query query=toSelectQuery(clazz, qualification);
			if(validateQuery(query)){
				 return query;
			}else{
				 return null;
			}
	}
	 public static Query toSelectQuery(Object entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		 Query query=new Query();
		 if(entity!=null){
			 Class clazz=entity.getClass();
			 query.setSql(getSelectSql(clazz));
			 query.addParam(getParams(entity,Id.class));
		 }
		 if(validateQuery(query)){
			 return query;
		 }else{
			 return null;
		 }
	 }
	
	 public static Query toSelectQuery(Class clazz,Query qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		 if(clazz==null){
				return null;
			}
			Query query=new Query();
			query.setSql(getSelectSql(clazz,qualification.getSql(),qualification.getSortInfo()));
			query.addParams(qualification.getParams());
			if(validateQuery(query)){
				 return query;
			}else{
				 return null;
			}
	 }
	


	

	

	public static Query toSelectQuery(Class clazz,Set<String> fieldNames,Query qualification,Map<String,String> sortList){
		if(clazz==null){
			return null;
		}
		Query query=new Query();
		TableMapping tableMapping=getTableMapping(clazz);
		String tableName=tableMapping.getTableName();
		StringBuffer sql=new StringBuffer("SELECT ");
		if(fieldNames==null){
			fieldNames=tableMapping.getAllColumnNames();
		}
		Iterator iterator = fieldNames.iterator();  
		while (iterator.hasNext()) { 
			String fieldName = (String)iterator.next();
			sql.append(fieldName.toUpperCase());
			if(iterator.hasNext()){sql.append(",");}
		}
		sql.append(" FROM ").append(tableName);
		
		if(qualification!=null){
			sql.append(" WHERE "+qualification.getSql());
		}
		if(null!=sortList&&sortList.size()>0){
			sql.append(" ORDER BY ");
			Iterator iterator1 = sortList.entrySet().iterator();  
			while (iterator1.hasNext()) { 
				Map.Entry<String, String> me = (Map.Entry<String, String>) iterator1.next();
				sql.append(me.getKey().toUpperCase()).append(" ").append(me.getValue().toUpperCase());
				if(iterator1.hasNext()){sql.append(",");}
			}
		}
		query.setSql(sql.toString());
		if(qualification!=null){
			query.setParams(qualification.getParams());
		}
		return query;
	}

	
	public static Query toCountQuery(Class clazz,Query qualification){
		if(clazz==null){
			return null;
		}
		Query query=new Query();
		String tableName=getTableName(clazz);
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) FROM "+tableName);
		if(qualification!=null){
			sql.append(" WHERE "+qualification.getSql());
			query.addParam(qualification.getParams());
		}
		query.setSql(sql.toString());
		
		return query;
	}

	public static Query toCountQuery(Query baseQuery,Query qualification){
		if(baseQuery==null){
			return null;
		}
		StringBuffer sql=new StringBuffer("SELECT COUNT(*) FROM (").append(baseQuery.getSql()).append(")");
		if(qualification!=null){
			sql.append(" WHERE "+qualification.getSql());
			baseQuery.addParam(qualification.getParams());
		}
		baseQuery.setSql(sql.toString());
		
		return baseQuery;
	}
	public static Query toPageQuery(Query query,int firstRetrieve,int maxRetrieve){
		if(maxRetrieve<=0){
			return query;
		}
		int lastRetrieve=firstRetrieve+maxRetrieve;
		StringBuffer pageSql=new StringBuffer(); 
		pageSql.append("select ")
	       .append("* ")
	       .append("from ")
	       .append("( select ")
	       .append("row_.*,")
	       .append("rownum rownum_")
	       .append(" from ")
	       .append("(")
	       .append(query.getSql())
	       .append(") row_ ) ")
	       .append("where ")
	       .append("rownum_ <=")
	       .append("?")
	       .append(" and rownum_ > ")
	       .append("?");
		query.addParam(lastRetrieve);
		query.addParam(firstRetrieve);
	    return query;
	}
*/
	 
	 public static StringBuffer getInsertSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 String tableName=getTableName(clazz);
		 StringBuffer columns=getColumnSubSql(clazz, ",", null,null, Id.class,Column.class);
		 StringBuffer values=getQuestionMarkSubSql(clazz, ",", Id.class,Column.class);
		 StringBuffer sql=new StringBuffer("INSERT INTO ")
		 	.append(tableName)
		 	.append("(")
		 	.append(columns)
		 	.append(")")
		 	.append(" VALUES ")
		 	.append("(")
		 	.append(values)
		 	.append(")");
		return sql;
	 }
	 public static StringBuffer getUpdateSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(clazz==null){
			return null;
		}
		StringBuffer qualification=getColumnSubSql(clazz," AND ",null,"=?",Id.class);
		StringBuffer sql=getUpdateSql(clazz,qualification.toString());
		return sql;
	 }
	 
	 public static StringBuffer getUpdateSql(Class clazz,String qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(clazz==null){
			return null;
		}
		String tableName=getTableName(clazz);
		StringBuffer columns=getColumnSubSql(clazz,",",null,"=?",Column.class);
		StringBuffer sql=new StringBuffer("UPDATE ").append(tableName).append(" SET ").append(columns).append(" WHERE ").append(qualification);
		return sql;
	 }	 

	 public static StringBuffer getDeleteSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 StringBuffer qualification=getColumnSubSql(clazz," AND ",null,"=?",Id.class);
		 StringBuffer sql=getDeleteSql(clazz,qualification.toString());
		 return sql;
	 }
	 
	 public static StringBuffer getDeleteSql(Class clazz,String qualification) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 String tableName=getTableName(clazz);
		 StringBuffer sql=new StringBuffer("DELETE FROM ").append(tableName).append(" WHERE ").append(qualification);
		 return sql;
	 }
	 public static StringBuffer getSelectSql(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 if(clazz==null){
			 return null;
		 }
		 StringBuffer columns=getColumnSubSql(clazz, ",", null,null, Id.class,Column.class);
		 StringBuffer qualification=getColumnSubSql(clazz, ",", null,"=?", Id.class);
		 String tableName=getTableName(clazz);
		 StringBuffer sql=new StringBuffer("SELECT ").append(columns).append(" FROM ").append(tableName).append(" WHERE ").append(qualification);
		 return sql;
	 }
	 
	public static StringBuffer getColumnSubSql(Class clazz,String separator,String prefix,String suffix,Class... annotationClasses){
		 StringBuffer columnValue = new StringBuffer();
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
						if(validateAnnotation(field,annotationClasses)){
							if(StringUtils.isNotBlank(separator)){
								columnValue.append(separator);
							}
							if(StringUtils.isNotBlank(prefix)){
								columnValue.append(prefix);
							}
							columnValue.append(field.getName());
							if(StringUtils.isNotBlank(suffix)){
								columnValue.append(suffix);
							}
						}
				}
		 }
		 if(columnValue.length()>separator.length()){
			 columnValue.delete(0, separator.length());
		 }
		 return columnValue;
	}

	public static StringBuffer getQuestionMarkSubSql(Class clazz,String separator,Class... annotationClasses){
		 StringBuffer columnValue = new StringBuffer();
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
						if(validateAnnotation(field,annotationClasses)){
							if(StringUtils.isNotBlank(separator)){
								columnValue.append(separator);
							}
							columnValue.append("?");
						}
				}
		 }
		 if(columnValue.length()>separator.length()){
			 columnValue.delete(0, separator.length());
		 }
		 return columnValue;
	}

	 public static List getParams(Object entity,Class... annotationClasses) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		 List params=new ArrayList();
		 Class clazz=entity.getClass();
		 for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
				Field[] fields = clazz.getDeclaredFields();
				for (int i=0;i<fields.length;i++) {
					Field field=fields[i];
					if(validateAnnotation(field,annotationClasses)){
						String fieldValue=BeanUtils.getProperty(entity,field.getName());
						params.add(fieldValue);
					}
				}
		 }
		return params;
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
	}
}
