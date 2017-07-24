package com.struggle.dialect;

import java.util.ArrayList;
import java.util.List;

import com.struggle.dbm.core.Query;

public class MysqlDialect implements Dialect {
	public String getPageSql(String sql) {
		StringBuffer pageSql=new StringBuffer(); 
		pageSql.append(sql)
	       .append(" LIMIT ")
	       .append("?,?");
		return pageSql.toString();
	}

	public List getPageParams(int firstRetrieve, int maxRetrieve) {
		List pageParams =new ArrayList();
		pageParams.add(firstRetrieve);
		pageParams.add(maxRetrieve);
		return pageParams;
	}


}
