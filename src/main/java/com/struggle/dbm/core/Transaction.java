package com.struggle.dbm.core;

import java.sql.SQLException;

public interface Transaction {
	void beginTransaction() throws SQLException;
	void commit() throws SQLException;
	void rollback() throws SQLException;
	void setTransactionIsolation(int level) throws SQLException;
	void pushBack(Query query);
}
