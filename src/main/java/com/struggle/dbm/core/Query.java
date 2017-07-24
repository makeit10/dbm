package com.struggle.dbm.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.ArrayUtils;

import com.struggle.dialect.Dialect;

public class Query {
	private StringBuffer sql;
	private List params;
	private ResultSetHandler resultSetHandler;
	private int firstRetrieve;
	private int maxRetrieve;
	private Map<String, String> sortInfo;
	private Dialect dialect;
	public Query() {
	}
	public Query(String sql) {
		this.sql = new StringBuffer(sql);
	}
	public Query(StringBuffer sql) {
		if(sql!=null){
			this.sql = sql;
		}
	}
	public Query(String sql, List params, ResultSetHandler resultSetHandler) {
		super();
		this.sql = new StringBuffer(sql);
		this.params = params;
		this.resultSetHandler = resultSetHandler;
	}
	public Query(String sql, List params, ResultSetHandler resultSetHandler,
			int firstRetrieve, int maxRetrieve, Map<String, String> sortInfo) {
		super();
		this.sql = new StringBuffer(sql);
		this.params = params;
		this.resultSetHandler = resultSetHandler;
		this.firstRetrieve = firstRetrieve;
		this.maxRetrieve = maxRetrieve;
		this.sortInfo = sortInfo;
	}
	public void addParam(Object param){
		if(params==null){
			params=new ArrayList();
		}
		params.add(param);
	}
	public void addParams(List param){
		if(params==null){
			params=new ArrayList();
		}
		if(param!=null){
			params.addAll(param);
		}
	}
	public void addParams(Object[] param){
		if(params==null){
			params=new ArrayList();
		}
		if(param!=null){
			for(Object p:param){
				params.add(p);
			}
		}
	}
	public String getSql(){
		String excuteSql=getSortSql(sql,sortInfo);
		if(dialect!=null&&maxRetrieve>0){
			excuteSql=dialect.getPageSql(excuteSql);
		}
		return excuteSql;
	}
	public void setSql(String sql) {
		this.sql = new StringBuffer(sql);
	}
	public void setSql(StringBuffer sql) {
		if(sql!=null){
			this.sql = sql;
		}
	}
	public String getOriginalSql(){
		return sql.toString();
	}
	public Object[] getParams() {
		List excuteParams=new ArrayList();
		if(params!=null){
			excuteParams.addAll(params);
		}
		if(dialect!=null&&maxRetrieve>0){
			List pageParams=dialect.getPageParams(firstRetrieve, maxRetrieve);
			excuteParams.addAll(pageParams);
		}
		if(excuteParams!=null){
			return excuteParams.toArray();
		}else{
			return null;
		}
		
	}
	public List getOriginalParams(){
		return params;
	}
	public Object[][] getBatchParams() {
		if(params!=null){
			int size=params.size();
			Object[][] objs=new Object[size][];
			for(int i=0;i<size;i++){
				List param=(List) params.get(i);
				objs[i]=param.toArray();
			}
			return objs;

		}else{
			return null;
		}
	}
	public void setParams(List params) {
		this.params = params;
	}
	
	public ResultSetHandler getResultSetHandler() {
		if(resultSetHandler==null){
			resultSetHandler=new MapListHandler();
		}
		return resultSetHandler;
	}
	public void setResultSetHandler(ResultSetHandler resultSetHandler) {
		this.resultSetHandler = resultSetHandler;
	}
	public int getFirstRetrieve() {
		return firstRetrieve;
	}
	public void setFirstRetrieve(int firstRetrieve) {
		this.firstRetrieve = firstRetrieve;
	}
	public int getMaxRetrieve() {
		return maxRetrieve;
	}
	public void setMaxRetrieve(int maxRetrieve) {
		this.maxRetrieve = maxRetrieve;
	}
	public Map<String, String> getSortInfo() {
		return sortInfo;
	}
	public void setSortInfo(Map<String, String> sortInfo) {
		this.sortInfo = sortInfo;
	}
	public Dialect getDialect() {
		return dialect;
	}
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	public Query join(String separator,Query query){
		this.sql=this.sql.append(separator).append(query.getSql());
		addParams(query.getParams());
		return this;
	}
	public Query join(String separator,String sql){
		this.sql=this.sql.append(separator).append(sql);
		return this;
	}
	public Query join(String sql){
		this.sql=this.sql.append(sql);
		return this;
	}
	public static String getSortSql(StringBuffer sql,Map<String,String> sortInfo){
		StringBuffer sortSql = sql;
		if(null!=sortInfo&&sortInfo.size()>0){
			sortSql.append(" ORDER BY ");
			Iterator iterator1 = sortInfo.entrySet().iterator();  
			while (iterator1.hasNext()) { 
				Map.Entry<String, String> me = (Map.Entry<String, String>) iterator1.next();
				sortSql.append(me.getKey()).append(" ").append(me.getValue().toUpperCase());
				if(iterator1.hasNext()){sortSql.append(",");}
				}
			}
		return sortSql.toString();
	}
	@Override
	public String toString() {
		return "Query [sql=" + getSql() + ", params=" + ArrayUtils.toString(getParams()) + "]";
	}
	
}
