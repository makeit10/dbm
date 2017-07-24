package com.struggle.dbm.core;

import java.util.Map;

public class Setting {
	private String dataSourceId;
	private String connectorClass;
	private String dialectClass;
	private Map<String,String> params;
	
	public String getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public String getConnectorClass() {
		return connectorClass;
	}
	public void setConnectorClass(String connectorClass) {
		this.connectorClass = connectorClass;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getDialectClass() {
		return dialectClass;
	}
	public void setDialectClass(String dialectClass) {
		this.dialectClass = dialectClass;
	}

}
