package com.woshidaniu.db.core.dialect.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.dialect.Dialect;
import com.woshidaniu.db.core.dialect.PageBounds;
import com.woshidaniu.db.core.utils.SQLInjectionUtils;
/**
 * 
 * @className: MySql5Dialect
 * @description: 适配MySql5数据库分页SQl组装
 * @author : kangzhidong
 * @date : 上午09:51:23 2015-4-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class MySql5Dialect extends Dialect {
	
	public String getLimitSQL(String originalSQL,PageBounds bounds){
		return getLimitSQL(originalSQL,bounds.getOffset(),bounds.getLimit(),bounds.getSortName(),bounds.getSortOrder());
	}
	
	public String getLimitSQL(String originalSQL,long offset,long limit) {
		return getLimitSQL(originalSQL,offset,limit,null,null);
	}
	
	public String getLimitSQL(String originalSQL,long offset,long limit,String sortName,String sortOrder) {
		if(!BlankUtils.isBlank(originalSQL)){
			originalSQL = trim(originalSQL);
			StringBuilder limitSql = new StringBuilder(originalSQL.length() + 10);
			String orderby = "";
			if (!StringUtils.isEmpty(sortName) ){
				orderby = SQLInjectionUtils.transactSQLInjection(sortName) + " " + SQLInjectionUtils.transactSQLInjection(StringUtils.isEmpty(sortOrder) ? " asc " : sortOrder);
				if (!StringUtils.isEmpty(orderby)){
					limitSql.append(" order by ");
					limitSql.append(SQLInjectionUtils.transactSQLInjection(orderby));
				}
			}
			if (offset > 0) {   
				limitSql.append(originalSQL).append(" limit ").append(offset).append(",").append(limit);
	        } else {   
	        	limitSql.append(originalSQL).append(" limit ").append(limit);
	        }  
			return limitSql.toString();
		}
		return originalSQL;
	}
	
	public String getOnceLimitSQL(String originalSQL, PageBounds bounds){
		return getOnceLimitSQL(originalSQL,bounds.getOffset(),bounds.getLimit(),bounds.getSortName(),bounds.getSortOrder());
	}

	public String getOnceLimitSQL(String originalSQL, long offset, long limit){
		return getOnceLimitSQL(originalSQL,offset,limit,null,null);
	}
	
	public String getOnceLimitSQL(String originalSQL, long offset, long limit,String sortName,String sortOrder){
		return getLimitSQL(originalSQL,offset,limit,sortName,sortOrder);
	}

	public String getCountSQL(String originalSQL) {
		if(!BlankUtils.isBlank(originalSQL)){
			originalSQL = trim(originalSQL);
			String querySql = removeOrders(originalSQL);
			StringBuilder builder = new StringBuilder("select count(1) ");
			int index = querySql.toLowerCase().indexOf("from");
			if (index != -1){
				builder.append(querySql.substring(index));
			}else{
				builder.append(querySql);
			}
			return builder.toString();
			/*
			//记录统计  (mysql要求必须添加 最后的as t) 
			StringBuilder builder = new StringBuilder("select count(1) from  (").append(removeOrders(sql)).append(") as t");*/
		}
		return originalSQL;
	}
 
	/**
	 * 去掉当前SQL 后分号
	 * 
	 * @param sql
	 * @return
	 */
	public String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}

	public String removeOrders(String sql) {
		Matcher m = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2).matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()){
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public StringBuilder getLimitOverSQL(String sql){
		return new StringBuilder("");
	}
	
	
}
