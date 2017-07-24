package com.struggle.dialect;

import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.alter.AlterOperation;
import net.sf.jsqlparser.statement.alter.AlterExpression.ColumnDataType;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.drop.Drop;

import com.struggle.dbm.core.ColumnMetaData;
import com.struggle.dbm.core.Query;
import com.struggle.dbm.core.Session;
import com.struggle.dbm.core.TableMetaData;

public abstract class BaseDialect implements Dialect{
	public static Query getBackQuery(Query query,Session session) {
		try {
			String sql = query.getSql();
			Statement statement = CCJSqlParserUtil.parse(sql);
			 if(statement instanceof CreateTable){
				 CreateTable createTableStatement = (CreateTable) statement;
				 return new Query(getCreateTableBackSql(createTableStatement));
	    	 }else if(statement instanceof Alter){
	    		 Alter alterStatement = (Alter) statement;
	    		 return new Query(getAlterBackSql(alterStatement,session));
	    	 }else if(statement instanceof Drop){
	    		 Drop dropStatement = (Drop) statement;
	    		 return new Query(getDropBackSql(dropStatement,session));
	    	 }else if(statement instanceof CreateView){
	    		 CreateView createViewStatement = (CreateView) statement;
	    		 return new Query(getCreateViewBackSql(createViewStatement,session));
	    	 }
		} catch (JSQLParserException e) {
			e.printStackTrace();
		}
    	
		return null;
    }
 public static String getCreateTableBackSql(CreateTable statement){
	 StringBuffer backSql = new StringBuffer("DROP TABLE ");
	 backSql.append(statement.getTable().getName());
	 return backSql.toString();
 }
 public static String getAlterBackSql(Alter statement,Session session){
	 Table table=statement.getTable();
	 TableMetaData tableMetaData = session.getTableMetaData(table.getName());
	 StringBuffer backSql = new StringBuffer("ALTER TABLE ");
	 backSql.append(table.getName()).append(" ");
	 List<AlterExpression> alterExpressions = statement.getAlterExpressions();
	 for(int i=0;i<alterExpressions.size();i++){
		 AlterExpression alterExpression = alterExpressions.get(i);
		 if(alterExpression.getOperation()==AlterOperation.ADD){
			 List<ColumnDataType> columnDataTypes = alterExpression.getColDataTypeList();
			 for(int j=0;j<columnDataTypes.size();j++){
				 ColumnDataType columnDataType = columnDataTypes.get(j);
				 backSql.append("DROP COLUMN ").append(columnDataType.getColumnName());
				 if(j<columnDataTypes.size()-1){
					 backSql.append(",");
				 }
			 }
		 }else if(alterExpression.getOperation()==AlterOperation.MODIFY){
			 List<ColumnDataType> columnDataTypes = alterExpression.getColDataTypeList();
			 backSql.append("MODIFY (");
			 for(int j=0;j<columnDataTypes.size();j++){
				 ColumnDataType columnDataType = columnDataTypes.get(j);
				 ColumnMetaData columnMetaData=(ColumnMetaData) tableMetaData.get(columnDataType.getColumnName());
				 backSql.append(columnDataType.getColumnName()).append(" ").append(columnMetaData.getTypeName());
				 if(columnMetaData.getColumnSize()>0){
					 backSql.append("(").append(columnMetaData.getColumnSize()).append(")");
				 }
				 if(j<columnDataTypes.size()-1){
					 backSql.append(",");
				 }
			 }
			 backSql.append(")");
		 }else if(alterExpression.getOperation()==AlterOperation.DROP){
			 ColumnMetaData columnMetaData=(ColumnMetaData) tableMetaData.get(alterExpression.getColumnName());
			 backSql.append("ADD ").append(columnMetaData.getColumnName()).append(" ").append(columnMetaData.getTypeName());
			 if(columnMetaData.getColumnSize()>0){
				 backSql.append("(").append(columnMetaData.getColumnSize()).append(")");
			 }
		 }
		 if(i<alterExpressions.size()-1){
			 backSql.append(",");
		 }
	 }
	 return backSql.toString();
 }
 
 public static String getDropBackSql(Drop statement,Session session){
	 Table table=statement.getName();
	 TableMetaData tableMetaData = session.getTableMetaData(table.getName());
	 StringBuffer backSql = new StringBuffer("CREATE TABLE ");
	 backSql.append(tableMetaData.getTableName()).append("(");
	 int i = 0;
	 for(Object columnName : tableMetaData.keySet()){
		 ColumnMetaData columnMetaData = (ColumnMetaData) tableMetaData.get(columnName);
		 backSql.append(columnMetaData.getColumnName()).append(" ").append(columnMetaData.getTypeName());
		 if(columnMetaData.getColumnSize()>0){
			 backSql.append("(").append(columnMetaData.getColumnSize()).append(")");
		 }
		 if(i<tableMetaData.size()-1){
			 backSql.append(",");
		 }
		 i++;
	 }
	 backSql.append(")");
	 return backSql.toString();
 }
 
 public static String getCreateViewBackSql(CreateView statement,Session session){
	 StringBuffer backSql = new StringBuffer("DROP VIEW ");
	 backSql.append(statement.getView().getName());
	 return backSql.toString();
 }
 
}
