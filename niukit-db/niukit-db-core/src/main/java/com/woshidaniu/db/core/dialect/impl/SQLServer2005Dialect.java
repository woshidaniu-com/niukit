package com.woshidaniu.db.core.dialect.impl;

import com.woshidaniu.basicutils.BlankUtils;

/**
 * 
 * @package com.woshidaniu.fastdb.sqlrender.dialect.impl
 * @className: SQLServer2005Dialect
 * @description: 
 * Hibernate BUG: http://opensource.atlassian.com/projects/hibernate/browse/HHH-2655
 * @author : kangzhidong
 * @date : 2014-4-10
 * @time : 下午03:23:46
 */
public class SQLServer2005Dialect extends MySql5Dialect{

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public boolean supportsLimitOffset() {
		return true;
	}

	/**
	 * Add a LIMIT clause to the given SQL SELECT
	 *
	 * The LIMIT SQL will look like:
	 *
	 * WITH query AS
	 *      (SELECT TOP 100 percent ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__, * from table_name)
	 * SELECT *
	 * FROM query
	 * WHERE __row_number__ BETWEEN :offset and :lastRows
	 * ORDER BY __row_number__
	 * 
	 * @param querySqlString The SQL statement to base the limit query off of.
	 * @param offset         Offset of the first row to be returned by the query (zero-based)
	 * @param last           Maximum number of rows to be returned by the query
	 * @return A new SQL statement with the LIMIT clause applied.
	 */
	@Override
	public String getLimitSQL(String originalSQL,long offset,long limit) {
		if(!BlankUtils.isBlank(originalSQL)){
			StringBuffer pagingBuilder = new StringBuffer();
			String orderby = getOrderByPart(originalSQL);
			String distinctStr = "";

			String loweredString = originalSQL.toLowerCase();
			String sqlPartString = originalSQL;
			if (loweredString.trim().startsWith("select")) {
				int index = 6;
				if (loweredString.startsWith("select distinct")) {
					distinctStr = "DISTINCT ";
					index = 15;
				}
				sqlPartString = sqlPartString.substring(index);
			}
			pagingBuilder.append(sqlPartString);

			// if no ORDER BY is specified use fake ORDER BY field to avoid errors
			if (orderby == null || orderby.length() == 0) {
				orderby = "ORDER BY CURRENT_TIMESTAMP";
			}

			StringBuffer result = new StringBuffer();
			result.append("WITH query AS (SELECT ")
					.append(distinctStr)
					.append("TOP 100 PERCENT ")
					.append(" ROW_NUMBER() OVER (")
					.append(orderby)
					.append(") as __row_number__, ")
					.append(pagingBuilder)
					.append(") SELECT * FROM query WHERE __row_number__ BETWEEN ")
					.append(offset).append(" AND ").append(offset+limit)
					.append(" ORDER BY __row_number__");

			return result.toString();
		}
		return originalSQL;
	}

	static String getOrderByPart(String originalSQL) {
		if(!BlankUtils.isBlank(originalSQL)){
			String loweredString = originalSQL.toLowerCase();
			int orderByIndex = loweredString.indexOf("order by");
			if (orderByIndex != -1) {
				// if we find a new "order by" then we need to ignore
				// the previous one since it was probably used for a subquery
				return originalSQL.substring(orderByIndex);
			} else {
				return "";
			}
		}
		return "";
	}
}
