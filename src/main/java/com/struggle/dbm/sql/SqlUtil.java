package com.struggle.dbm.sql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.struggle.dbm.core.Form;
import com.struggle.dbm.core.Query;

public class SqlUtil {
	private static final Logger log = Logger.getLogger(SqlUtil.class);

	public static String getSortSubSql(Map<String,String> sortInfo){
		StringBuffer sortSubSql = new StringBuffer();
		if(null!=sortInfo&&sortInfo.size()>0){
			sortSubSql.append(" ORDER BY ");
			Iterator iterator1 = sortInfo.entrySet().iterator();  
			while (iterator1.hasNext()) { 
				Map.Entry<String, String> me = (Map.Entry<String, String>) iterator1.next();
				sortSubSql.append(me.getKey()).append(" ").append(me.getValue().toUpperCase());
				if(iterator1.hasNext()){sortSubSql.append(",");}
				}
			}
		return sortSubSql.toString();
	}
	 public static String getInsertSql(String tableName,Object[] columnNames){
		 String columnSubSql=StringUtils.join(columnNames, ",");
		 String valueSubSql=StringUtils.repeat("?", ",", columnNames.length);
		 StringBuffer sql=new StringBuffer("INSERT INTO ")
		 	.append(tableName)
		 	.append("(")
		 	.append(columnSubSql)
		 	.append(")")
		 	.append(" VALUES ")
		 	.append("(")
		 	.append(valueSubSql)
		 	.append(")");
		return sql.toString();
	 }
	 public static List getParams(Map map){
		 List params=null;
		 if(map!=null){
			params=new ArrayList();
			for(Object key:map.keySet()){
				params.add(map.get(key));
			}
		 }
		 return params;
	 }
	 public static List getParams(List<Map> list){
		 List params=null;
		 if(list!=null){
			params=new ArrayList();
			for(Map map:list){
				params.add(getParams(map));
			}
		 }
		 return params;
	 }
	 public static void main(String[] args) {
		 Map m =new HashMap();
		 m.put("1", "w");
		 m.put("2", "e");
		 System.out.println(getParams(m));;
	}
	
}
