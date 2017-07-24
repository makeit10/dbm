package com.struggle.dbm.core;

import java.sql.SQLException;

public interface SessionFactory {
	public Session openSession() throws SQLException;
	public Session getCurrentSession() throws SQLException;
}
