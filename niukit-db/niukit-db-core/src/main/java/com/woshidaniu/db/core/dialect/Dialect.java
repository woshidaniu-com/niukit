package com.woshidaniu.db.core.dialect;

import org.apache.commons.lang.StringUtils;

import com.woshidaniu.db.core.utils.SQLInjectionUtils;

/**
 * 
 * @className: Dialect
 * @description: 类似hibernate的Dialect,但只精简出分页部分
 * @author : kangzhidong
 * @date : 上午09:57:32 2015-4-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public abstract class Dialect {

	public static final String SQL_END_DELIMITER = ";";
	public static PageBounds bounds = null;
	
	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return supportsLimit();
	}

	public boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}

	/**
	 * 
	 * @description: 获得排序组合SQL片段
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:54:21
	 * @param sortName
	 * @param sortOrder
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public String getOrderBySQL(String sortName,String sortOrder){
		String orderby = "";
		if (!StringUtils.isEmpty(sortName) ){
			orderby = SQLInjectionUtils.transactSQLInjection(sortName) + " " + SQLInjectionUtils.transactSQLInjection(StringUtils.isEmpty(sortOrder) ? " asc " : sortOrder);
		}
		if (!StringUtils.isEmpty(orderby)){
			return " order by " + SQLInjectionUtils.transactSQLInjection(orderby);
		}
		return "";
	}
	
	/**
	 * 
	 * @description: 获取分页SQL
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:54:43
	 * @param sql
	 * @param bounds
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract String getLimitSQL(String sql, PageBounds bounds);

	/**
	 * 
	 * @description: 获取分页SQL
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:55:03
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract String getLimitSQL(String sql, long offset, long limit);
	
	/**
	 * 
	 * @description: 获取分页SQL
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:55:10
	 * @param sql
	 * @param offset
	 * @param limit
	 * @param sortName
	 * @param sortOrder
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract String getLimitSQL(String sql, long offset, long limit,String sortName,String sortOrder);
	
	/**
	 * 
	 * @description: 获取分页SQL:且该SQL中有包含获取总记录数的字段，实现一条SQL查询数据和记录数
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:55:20
	 * @param originalSQL
	 * @param bounds
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract String getOnceLimitSQL(String originalSQL, PageBounds bounds);

	/**
	 * 
	 * @description: 获取分页SQL:且该SQL中有包含获取总记录数的字段，实现一条SQL查询数据和记录数
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:55:26
	 * @param originalSQL
	 * @param offset
	 * @param limit
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract String getOnceLimitSQL(String originalSQL, long offset, long limit);
	
	/**
	 * 
	 * @description: 获取分页SQL:且该SQL中有包含获取总记录数的字段，实现一条SQL查询数据和记录数
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:56:15
	 * @param originalSQL
	 * @param offset
	 * @param limit
	 * @param sortName
	 * @param sortOrder
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract String getOnceLimitSQL(String originalSQL, long offset, long limit,String sortName,String sortOrder);
	
	/**
	 * 
	 * @description: 获取查询总记录数的SQL
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:56:22
	 * @param originalSQL
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract String getCountSQL(String originalSQL);

	/**
	 * 
	 * @description: 获取利用开窗函数获取记录总数的SQL
	 * @author : kangzhidong
	 * @date 2015-4-14 
	 * @time 上午09:56:44
	 * @param originalSQL
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public abstract StringBuilder getLimitOverSQL(String originalSQL);
	
}
