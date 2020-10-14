package com.woshidaniu.db.core.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.Top;
import net.sf.jsqlparser.statement.select.UnionOp;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.AddAliasesVisitor;
import net.sf.jsqlparser.util.TablesNamesFinder;
@SuppressWarnings("unused")
public class JSQLParserUtils {

	private static final TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
	
	private static final AddAliasesVisitor instance = new AddAliasesVisitor();
	
	/**
	 * 
	 * @description: 获得一段SQL中的查询字段集合
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午08:38:36 
	 * @param selectSQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static List<String> getSelectItems(String sql) throws JSQLParserException{
		PlainSelect plainSelect = JSQLParserUtils.getPlainSelect(sql);
		List<String> columnList = new ArrayList<String>();
		for (Object element : plainSelect.getSelectItems()) {
			if(element instanceof SelectExpressionItem){
				SelectExpressionItem item = (SelectExpressionItem)element;
				if(null!=item.getAlias()&&item.getAlias().getName().length()>0){
					columnList.add(item.getAlias().getName());
					//System.out.println(item.getAlias());
				}else{
					Column column = (Column)item.getExpression();
					columnList.add(column.getColumnName());
					//System.out.println(column.getColumnName());
					//System.out.println(column.getWholeColumnName());
					//System.out.println(column.getTable());
				}
			}
		}
		return columnList;
	}
	
	public static List<String> getTables(String sql) throws JSQLParserException{
		Select selectStatement = getSelectStatement(sql);
		if(selectStatement != null){
			return tablesNamesFinder.getTableList(selectStatement);
		}
		return null;
	}
	
	/**
	 * 
	 * @description: 获得top值
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午08:49:04
	 * @param selectSQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Top getTop(String sql) throws JSQLParserException {
		PlainSelect select = JSQLParserUtils.getPlainSelect(sql);
		if( null != select ){
			return select.getTop();
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得解释SQL后的Statement;
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:07:49
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Statement getStatement(String SQL) throws JSQLParserException {
		return CCJSqlParserUtil.parse(new StringReader(SQL));
	}

	/**
	 * 
	 * @description: 获得Insert类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:13:36
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Insert getInsertStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof Insert) {
			// insert case
			return (Insert) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得Delete类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:14:09
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Delete getDeleteStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof Delete) {
			// delete case
			return (Delete) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得Update类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:14:22
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Update getUpdateStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof Insert) {
			// Update case
			return (Update) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得select类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:14:36
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Select getSelectStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof Select) {
			// select case
			return (Select) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得Drop类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:14:36
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Drop getDropStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof Drop) {
			// drop case
			return (Drop) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得Truncate类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:14:36
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Truncate getTruncateStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof Truncate) {
			// Truncate case
			return (Truncate) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得Replace类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:14:36
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Replace getReplaceStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof Replace) {
			// Replace case
			return (Replace) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得CreateTable类型Statement
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:14:36
	 * @param SQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static CreateTable getCreateTableStatement(String SQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(SQL);
		if (statement instanceof CreateTable) {
			// createTable case
			return (CreateTable) statement;
		}
		return null;
	}

	/**
	 * 
	 * @description: 获得一段SQL的Limit
	 * @author : kangzhidong
	 * @date : 2014-5-6
	 * @time : 上午09:20:38
	 * @param selectSQL
	 * @return
	 * @throws JSQLParserException
	 */
	public static Limit getLimit(String selectSQL) throws JSQLParserException {
		Statement statement = JSQLParserUtils.getStatement(selectSQL);
		if (statement instanceof Select) {
			SelectBody select = ((Select) statement).getSelectBody();
			if (select instanceof PlainSelect) {
				return ((PlainSelect) select).getLimit();
			}
		}
		return null;
	}

	public static SelectBody getSelectBody(String statement) throws JSQLParserException {
		Select select = JSQLParserUtils.getSelectStatement(statement);
		if( null != select ){
			return select.getSelectBody();
		}
		return null;
	}

	public static PlainSelect getPlainSelect(String statement) throws JSQLParserException {
		SelectBody body = JSQLParserUtils.getSelectBody(statement);
		if( null != body ){
			return ((PlainSelect) body);
		}
		return null;
	}

	public static UnionOp getUnion(String statement) throws JSQLParserException {
		SelectBody body = JSQLParserUtils.getSelectBody(statement);
		if( null != body ){
			return ((UnionOp) body);
		}
		return null;
	}

}
