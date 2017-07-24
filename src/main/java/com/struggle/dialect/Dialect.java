package com.struggle.dialect;

import java.util.List;

public interface Dialect {
	public String getPageSql(String sql);
	public List getPageParams(int firstRetrieve,int maxRetrieve);
}
