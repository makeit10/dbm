package com.struggle.dbm.core.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.struggle.dbm.core.ColumnMetaData;
import com.struggle.dbm.core.Query;
import com.struggle.dbm.core.Session;
import com.struggle.dbm.core.Setting;
import com.struggle.dbm.core.Table;
import com.struggle.dbm.core.TableMetaData;
import com.struggle.dbm.core.Transaction;
import com.struggle.dbm.sql.AnnotationSqlUtil;
import com.struggle.dbm.sql.SqlUtil;
import com.struggle.dbm.util.BackQueryUtil;
import com.struggle.dialect.Dialect;
import com.struggle.dialect.OracleDialect;

public class SessionImpl implements Session{
	private final static Logger log = Logger.getLogger(SessionImpl.class);
	public QueryRunner queryRunner;
	public Connection connection;
	public Transaction transaction;
	private Setting setting;
	private Dialect dialect;
	public SessionImpl(Connection connection,Setting setting) {
		this.connection = connection;
		this.setting=setting; 
		queryRunner=new QueryRunner();
		try {
			dialect=(Dialect) Class.forName(setting.getDialectClass()).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void beginTransaction() throws SQLException {
		getTransaction().beginTransaction();
	}
	public Transaction getTransaction() {
		if(transaction==null){
			transaction=new TransactionImpl(connection);
		}
		return transaction;
	}
	
    public  void close() throws SQLException
    {
    	DbUtils.close(connection);
    }

	public boolean isClosed() throws SQLException{
		return connection.isClosed();
	}

	public <T> T insert(Query query)
			throws SQLException {
		log.info(query);
		return (T) queryRunner.insert(connection,query.getSql(), query.getResultSetHandler(), query.getParams());
	}

	public <T> T insertBatch(Query query) throws SQLException {
		log.info(query.getSql());
		return (T) queryRunner.insertBatch(connection, query.getSql(), query.getResultSetHandler(), query.getBatchParams());
	}

	public int update(Query query) throws SQLException {
		log.info(query);
		int result = queryRunner.update(connection, query.getSql(), query.getParams());
		/*Query backQuery = BackQueryUtil.getBackQuery(query, this);
		getTransaction().pushBack(backQuery);*/
		return result;
	}

	public int[] batch(Query query) throws SQLException {
		log.info(query.getSql());
		return queryRunner.batch(connection, query.getSql(), query.getBatchParams());
	}
	
	public <T> T query(Query query)
			throws SQLException {
		try {
			Dialect dialect=(Dialect) Class.forName(setting.getDialectClass()).newInstance();
			query.setDialect(dialect);
			log.info(query);
			return (T) queryRunner.query(connection, query.getSql(), query.getResultSetHandler(), query.getParams());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> void save(T instance) {
		try {
			Class clazz=instance.getClass();
			String sql =AnnotationSqlUtil.getInsertSql(clazz).toString();
			Object[] params=AnnotationSqlUtil.getParams(instance,0,Id.class,Column.class).toArray();
			Object id=queryRunner.insert(connection,sql,new ScalarHandler(),params);
			String idName = AnnotationSqlUtil.getTableMapping(instance.getClass()).getIdName();
			if(id!=null&&idName!=null){
				BeanUtils.setProperty(instance, idName, id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public <T> void save(List<T> instances) {
		try {
			if(instances!=null&&instances.size()>0){
				Class clazz=instances.get(0).getClass();
				String sql=AnnotationSqlUtil.getInsertSql(clazz).toString();
				Object[][] params=new Object[instances.size()][];
				for(int i=0;i<instances.size();i++){
					params[i]=AnnotationSqlUtil.getParams(instances.get(i),0,Id.class,Column.class).toArray();
				}
				List ids=queryRunner.insertBatch(connection,sql, new ColumnListHandler(),params);
				String idName = AnnotationSqlUtil.getTableMapping(instances.get(0).getClass()).getIdName();
				if(idName!=null&&ids!=null){
					for(int i=0;i<instances.size();i++){
						Object id=ids.get(i);
						Object instance=instances.get(i);
						BeanUtils.setProperty(instance, idName, id);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
	}

	public <T> void update(T instance) {
		try {
			String sql=AnnotationSqlUtil.getUpdateSql(instance.getClass());
			Object[] cloumnParams=AnnotationSqlUtil.getParams(instance,0,Column.class).toArray();
			Object[] idParams=AnnotationSqlUtil.getParams(instance,0,Id.class).toArray();
			Object[] params=ArrayUtils.addAll(cloumnParams, idParams);
			queryRunner.update(connection,sql,params);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public <T> void update(List<T> instances) {
		try {
			if(instances!=null&&instances.size()>0){
				Class clazz=instances.get(0).getClass();
				String sql=AnnotationSqlUtil.getUpdateSql(clazz).toString();
				Object[][] params=new Object[instances.size()][];
				for(int i=0;i<instances.size();i++){
					T instance=instances.get(i);
					Object[] cloumnParams=AnnotationSqlUtil.getParams(instance,0,Column.class).toArray();
					Object[] idParams=AnnotationSqlUtil.getParams(instance,0,Id.class).toArray();
					params[i]=ArrayUtils.addAll(cloumnParams, idParams);
				}
				queryRunner.batch(connection,sql,params);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public <T> void delete(T instance) {
		try {
			String sql=AnnotationSqlUtil.getDeleteSql(instance.getClass());
			Object[] params=AnnotationSqlUtil.getParams(instance,0,Id.class).toArray();
			queryRunner.update(connection, sql, params);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public <T> void delete(List<T> instances) {
		try {
			 if(instances!=null||instances.size()>0){
				 Class clazz=instances.get(0).getClass();
				 String sql=AnnotationSqlUtil.getDeleteSql(clazz); 
				 Object[][] params=new Object[instances.size()][];
				 for(int i=0;i<instances.size();i++){
					params[i]=AnnotationSqlUtil.getParams(instances.get(i),0,Id.class).toArray();
				 }
				 queryRunner.batch(connection,sql,params);
			 }
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	public <T> T get(Class<T> clazz, Serializable id) {
		try {
			String sql=AnnotationSqlUtil.getSelectSql(clazz);
			return queryRunner.query(connection, sql, new BeanHandler<T>(clazz), id);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T get(Class<T> clazz, ResultSetHandler<T> rsh,
			Serializable... id) {
		try {
			if(rsh==null){
				rsh=new BeanListHandler(clazz);
			}
			String sql=AnnotationSqlUtil.getSelectSql(clazz, id.length);
			return queryRunner.query(connection, sql, rsh, id);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T> T get(Class<T> clazz,String qualification, int firstRetrieve, int maxRetrieve,
			Map<String, String> sortInfo, ResultSetHandler<T> rsh, Object... params) {
		try {
			if(rsh==null){
				rsh=new BeanListHandler(clazz);
			}
			String sql=AnnotationSqlUtil.getSelectSql(clazz, qualification);
			String sortSubSql=SqlUtil.getSortSubSql(sortInfo);
			if(StringUtils.isNoneBlank(sortSubSql)){
				sql+=" "+sortSubSql;
			}
			if(maxRetrieve>0){
				Dialect dialect=(Dialect) Class.forName(setting.getDialectClass()).newInstance();
				sql=dialect.getPageSql(sql);
				List pageParams=dialect.getPageParams(firstRetrieve, maxRetrieve);
				for(Object param:pageParams){
					params=ArrayUtils.add(params, param);	
				}
			}
			return queryRunner.query(connection, sql, rsh, params);
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public <T> T get(T instance, int firstRetrieve, int maxRetrieve,Map<String, String> sortInfo, ResultSetHandler<T> rsh) {
		try {
			if(rsh==null){
				rsh=new BeanListHandler(instance.getClass());
			}
			String sql=AnnotationSqlUtil.getSelectSql(instance);
			Object[] params=AnnotationSqlUtil.getParams(instance, 1, Id.class,Column.class).toArray();
			String sortSubSql=SqlUtil.getSortSubSql(sortInfo);
			if(StringUtils.isNoneBlank(sortSubSql)){
				sql+=" "+sortSubSql;
			}
			if(maxRetrieve>0){
				Dialect dialect=(Dialect) Class.forName(setting.getDialectClass()).newInstance();
				sql=dialect.getPageSql(sql);
				List pageParams=dialect.getPageParams(firstRetrieve, maxRetrieve);
				for(Object param:pageParams){
					params=ArrayUtils.add(params, param);	
				}
			}
			return queryRunner.query(connection, sql, rsh, params);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public TableMetaData getTableMetaData(String tableName) {
		TableMetaData tableMetaData = null;
		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			ResultSet rs= databaseMetaData.getColumns(null, null, tableName, null);
			tableMetaData = new TableMetaData(tableName);
			while(rs.next()) { 
				ColumnMetaData columnMetaData = new ColumnMetaData();
				columnMetaData.setTableName(rs.getString("TABLE_NAME"));
				columnMetaData.setColumnName(rs.getString("COLUMN_NAME"));
				columnMetaData.setDataType(rs.getInt("DATA_TYPE"));
				columnMetaData.setTypeName(rs.getString("TYPE_NAME"));
				columnMetaData.setColumnSize(rs.getInt("COLUMN_SIZE"));
				tableMetaData.put(columnMetaData.getColumnName(), columnMetaData);
			 
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tableMetaData;
	}

}
