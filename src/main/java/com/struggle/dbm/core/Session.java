package com.struggle.dbm.core;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

public interface Session {
	
	public <T> void save(T instance);
	
	public <T> void save(List<T> instances);
	
	public <T> void update(T instance);
	
	public <T> void update(List<T> instances);

	public <T> void delete(T instance);
	
	public <T> void delete(List<T> instances);
	
	public <T> T get(Class<T> clazz,Serializable id);
	
	public <T> T get(Class<T> clazz,ResultSetHandler<T> rsh,Serializable... id);

	public <T> T get(T instance, int firstRetrieve, int maxRetrieve,Map<String, String> sortInfo,ResultSetHandler<T> rsh);

	public <T> T get(Class<T> clazz,String qualification,int firstRetrieve, int maxRetrieve, Map<String, String> sortInfo, ResultSetHandler<T> rsh,Object... params);

	
	public <T> T insert(Query query)throws SQLException;

	public <T> T insertBatch(Query query) throws SQLException;

	public <T> T query(Query query)throws SQLException;

	public int update(Query query) throws SQLException;

	public int[] batch(Query query) throws SQLException;
	
	public TableMetaData getTableMetaData(String tableName);
	
	public void beginTransaction() throws SQLException;

	public Transaction getTransaction();

	public void close() throws SQLException;

	public boolean isClosed() throws SQLException;
	
}
