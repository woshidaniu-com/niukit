package com.woshidaniu.db.core.dialect.impl;
/**
 * Dialect for HSQLDB
 */
public class HSQLDialect extends MySql5Dialect{

	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset() {
		return true;
	}

	public String getLimitString(String originalSQL, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
		boolean hasOffset = offset>0;
		return new StringBuffer( originalSQL.length() + 10 )
		.append( originalSQL )
		.insert( originalSQL.toLowerCase().indexOf( "select" ) + 6, hasOffset ? " limit "+offsetPlaceholder+" "+limitPlaceholder : " top "+limitPlaceholder )
		.toString();
	}
    
}
