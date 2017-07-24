package com.struggle.dialect;

import java.util.ArrayList;
import java.util.List;

import com.struggle.dbm.core.Query;

public class OracleDialect implements Dialect{

	public Query getPageSql(Query query) {
		int maxRetrieve=query.getMaxRetrieve();
		if(maxRetrieve<=0){
			return query;
		}
		Query pageQuery =new Query();
		StringBuffer pageSql=new StringBuffer(); 
		pageSql.append("select ")
	       .append("* ")
	       .append("from ")
	       .append("( select ")
	       .append("row_.*,")
	       .append("rownum rownum_")
	       .append(" from ")
	       .append("(")
	       .append(query.getSql())
	       .append(") row_ ) ")
	       .append("where ")
	       .append("rownum_ <=")
	       .append("?")
	       .append(" and rownum_ > ")
	       .append("?");
		pageQuery.addParam(query.getFirstRetrieve());
		pageQuery.addParam(query.getFirstRetrieve()+query.getMaxRetrieve());
		return pageQuery;
	}

	public String getPageSql(String sql) {
		StringBuffer pageSql=new StringBuffer(); 
		pageSql.append("select ")
	       .append("* ")
	       .append("from ")
	       .append("( select ")
	       .append("row_.*,")
	       .append("rownum rownum_")
	       .append(" from ")
	       .append("(")
	       .append(sql)
	       .append(") row_ ) ")
	       .append("where ")
	       .append("rownum_ > ")
	       .append("?")
	       .append(" and rownum_ <=")
	       .append("?");
		return pageSql.toString();
	}

	public List getPageParams(int firstRetrieve, int maxRetrieve) {
		List pageParams =new ArrayList();
		pageParams.add(firstRetrieve);
		pageParams.add(firstRetrieve+maxRetrieve);
		return pageParams;
	}
}
