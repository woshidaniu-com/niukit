package com.woshidaniu.db.core.dialect.impl;

import com.woshidaniu.basicutils.BlankUtils;


public class SQLServerDialect extends MySql5Dialect {

	static int getAfterSelectInsertPoint(String originalSQL) {
		int selectIndex = originalSQL.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = originalSQL.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}

	public String getLimitString(String originalSQL, int offset, int limit) {
		return getLimitString(originalSQL,offset,null,limit,null);
	}

	public String getLimitString(String originalSQL, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
		if ( offset > 0 ) {
			throw new UnsupportedOperationException( "sql server has no offset" );
		}
//		if(limitPlaceholder != null) {
//			throw new UnsupportedOperationException(" sql server not support variable limit");
//		}
		
		return new StringBuffer( originalSQL.length() + 8 )
				.append( originalSQL )
				.insert( getAfterSelectInsertPoint( originalSQL ), " top " + limit )
				.toString();
	}
	
	public String getLimitSQL(String originalSQL,long offset,long limit) {
		if(!BlankUtils.isBlank(originalSQL)){
			originalSQL = trim(originalSQL);
			StringBuilder limitSql = new StringBuilder(originalSQL.length() + 10);
			limitSql.append(" SELECT TOP ").append(limit).append(" * ");
			limitSql.append(" FROM ").append("(").append(originalSQL).append(") ");
			limitSql.append(" WHERE ( ID > ");
			limitSql.append(" ( SELECT MAX(id) ");
			limitSql.append(" FROM ( SELECT TOP ").append(offset).append(" ID ");
			limitSql.append(" FROM ( ").append(originalSQL).append(" ) ");
			limitSql.append(" ORDER BY ID ) AS T ) ) ");
			limitSql.append(" ORDER BY ID ");
			return limitSql.toString();
		}
		return originalSQL;
	}

}
