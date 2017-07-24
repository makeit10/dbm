package com.struggle.dbm.core;

import org.apache.commons.collections.map.CaseInsensitiveMap;

public class TableMetaData extends CaseInsensitiveMap{
	private String tableName;
	public TableMetaData(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
