package com.woshidaniu.db.core.dialect.impl;

import org.apache.commons.lang.StringUtils;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.utils.SQLInjectionUtils;

public class SQLiteDialect extends MySql5Dialect{

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
				limitSql.append(originalSQL).append(" limit ").append(limit).append(" offset ").append(offset);
	        } else {   
	        	limitSql.append(originalSQL).append(" limit ").append(limit);
	        }  
			return limitSql.toString();
		}
		return originalSQL;
	}
	
}
