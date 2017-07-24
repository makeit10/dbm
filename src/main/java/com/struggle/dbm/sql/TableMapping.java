package com.struggle.dbm.sql;

import java.util.HashSet;
import java.util.Set;

public class TableMapping {
	private String tableName;
	private String idName;
	private Set<String> columnNames;
	private Set<String> allColumnNames;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public Set<String> getColumnNames() {
		return columnNames;
	}
	public void addColumnNames(String columnName) {
		if(columnNames==null){
			columnNames=new HashSet<String>();
		}
		columnNames.add(columnName);
		addAllColumnName(columnName);
	}
	public void setColumnNames(Set<String> columnNames) {
		this.columnNames = columnNames;
		addAllColumnNames(columnNames);
	}
	public Set<String> getAllColumnNames() {
		return allColumnNames;
	}
	public void addAllColumnName(String columnName) {
		if(allColumnNames==null){
			allColumnNames=new HashSet<String>();
		}
		allColumnNames.add(columnName);
	}
	public void addAllColumnNames(Set<String> columnNames) {
		if(allColumnNames==null){
			allColumnNames=new HashSet<String>();
		}
		allColumnNames.addAll(columnNames);
	}
	public void setAllColumnNames(Set<String> allColumnNames) {
		this.allColumnNames = allColumnNames;
	}
	
	
}
