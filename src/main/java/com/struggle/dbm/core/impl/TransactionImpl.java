package com.struggle.dbm.core.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

import org.apache.commons.dbutils.QueryRunner;

import com.struggle.dbm.core.Query;
import com.struggle.dbm.core.Transaction;

public class TransactionImpl implements Transaction{
	private Connection connection;
	private Stack back;
	QueryRunner queryRunner;
	public TransactionImpl(Connection connection) {
		this.connection = connection;
		queryRunner=new QueryRunner();
	}
	public void beginTransaction() throws SQLException {
		connection.setAutoCommit(false);
		
	}
	public void commit() throws SQLException {
		connection.commit();
		connection.close();
	}

	public void rollback() throws SQLException {
		connection.rollback();
		for(int i=0;i<back.size();i++){
			Query query = (Query) back.pop();
			System.out.println("custom roll back :"+query);
			queryRunner.update(connection, query.getSql(), query.getParams());
		}
		
	}
	
	public void setTransactionIsolation(int level) throws SQLException{
		connection.setTransactionIsolation(level);
	}
	public void pushBack(Query query){
		if(back==null){
			back=new Stack();
		}
		if(query!=null){
			back.push(query);
		}
	}

}
