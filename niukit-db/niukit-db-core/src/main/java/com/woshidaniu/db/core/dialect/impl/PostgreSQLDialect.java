package com.woshidaniu.db.core.dialect.impl;
/**
 * 
 * @package com.woshidaniu.fastdb.sqlrender.dialect.impl
 * @className: PostgreSQLDialect
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-4-10
 * @time : 下午03:26:14
 */
public class PostgreSQLDialect extends MySql5Dialect{
	
	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset(){
		return true;
	}
	
	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		return new StringBuffer( sql.length()+20 )
		.append(sql)
		.append(offset > 0 ? " limit "+limitPlaceholder+" offset "+offsetPlaceholder : " limit "+limitPlaceholder)
		.toString();
	}
}
