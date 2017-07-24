package com.struggle.dbm.core;

public class ColumnMetaData {
	private String tableName;
	private String columnName;
	private int dataType;
	private String typeName;
	private int columnSize;
	
	
	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getColumnName() {
		return columnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public int getDataType() {
		return dataType;
	}


	public void setDataType(int dataType) {
		this.dataType = dataType;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public int getColumnSize() {
		return columnSize;
	}


	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}


	@Override
	public String toString() {
		return "ColumnMetaData [tableName=" + tableName + ", columnName="
				+ columnName + ", dataType=" + dataType + ", typeName="
				+ typeName + ", columnSize=" + columnSize + "]";
	}
	
	
	
	

}
