package com.woshidaniu.db.core.dialect.impl;

import com.woshidaniu.basicutils.BlankUtils;

/**
 * 
 * @package com.woshidaniu.fastdb.sqlrender.dialect.impl
 * @className: PostgreDialect
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-4-10
 * @time : 下午03:26:14
 */
public class PostgreDialect extends MySql5Dialect{
	
	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset(){
		return true;
	}
	
	public String getLimitSQL(String originalSQL,long offset,long limit,String sortName,String sortOrder) {
		if(!BlankUtils.isBlank(originalSQL)){
			return new StringBuffer( originalSQL.length()+20 )
			.append(originalSQL)
			.append(offset > 0 ? " limit "+limit+" offset "+offset : " limit "+limit)
			.toString();
		}
		return originalSQL;
	}
}
