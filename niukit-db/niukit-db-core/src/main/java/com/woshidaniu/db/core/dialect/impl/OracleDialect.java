package com.woshidaniu.db.core.dialect.impl;

import com.woshidaniu.basicutils.BlankUtils;


/**
 * 
 * @className: OracleDialect
 * @description: 适配Oracle数据库分页SQl组装
 * @author : kangzhidong
 * @date : 上午09:50:50 2015-4-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class OracleDialect extends MySql5Dialect{

	public String getLimitSQL(String originalSQL,long offset,long limit,String sortName,String sortOrder) {
		if(!BlankUtils.isBlank(originalSQL)){
			originalSQL = trim(originalSQL);
			//判断是否更新的sql
			boolean isForUpdate = false;
			if ( originalSQL.toLowerCase().endsWith(" for update") ) {
				originalSQL = originalSQL.substring( 0, originalSQL.length()-11 );
				isForUpdate = true;
			}
			/*
			 * ORACLE 分页是通过 ROWNUMBER 进行的，ROWNUMBER 是从 1 开始的
			 */
			offset++;
			long max = offset + limit;
			StringBuilder limitSql = new StringBuilder(originalSQL.length() + 10);
			if (offset == 0 && max == -1) {
				limitSql.append("select t.* from ( ").append(originalSQL).append(" ) t ").append(getOrderBySQL(sortName,sortOrder)) ;
			} else if (offset == 0) {
				limitSql.append("select t.* from ( ").append(originalSQL).append(" ) t where rownum <= ").append(max).append(getOrderBySQL(sortName,sortOrder));
			} else {
				limitSql.append(" select * from (  ");
					limitSql.append(" select row_.*,rownum rownum_ from ( ");
							limitSql.append( originalSQL);
					limitSql.append(" ) row_ ");
					limitSql.append(getOrderBySQL(sortName,sortOrder)) ;
					limitSql.append(" where rownum <= ").append(max);
				limitSql.append(" ) where rownum_ > ").append(offset);
			}
			if ( isForUpdate ) {
				limitSql.append( " for update" );
			}
			return limitSql.toString();
		}
		return originalSQL;
	}
	
	public StringBuilder getLimitOverSQL(String originalSQL){
		StringBuilder limitOverSql = new StringBuilder(originalSQL.length() + 10);
		limitOverSql.append(" select tb2.*,count(0) over (partition by 1) totalResult ");
		limitOverSql.append(" from (");
			limitOverSql.append(originalSQL);
		limitOverSql.append(" ) tb2 ");
		return limitOverSql;
	}
	
	public String getOnceLimitSQL(String originalSQL,long offset,long limit,String sortName,String sortOrder) {
		if(!BlankUtils.isBlank(originalSQL)){
			originalSQL = trim(originalSQL);
			//判断是否更新的sql
			if ( originalSQL.toLowerCase().endsWith(" for update") ) {
				originalSQL = originalSQL.substring( 0, originalSQL.length() - 11 );
			}
			long max = offset + limit;
			StringBuilder limitSql = new StringBuilder(originalSQL.length() + 10);
			if (offset == 0 && max == -1) {
				limitSql.append("select t.* from ( ").append(getLimitOverSQL(originalSQL)).append(" ) t ").append(getOrderBySQL(sortName,sortOrder)) ;
			} else if (offset == 0) {
				limitSql.append("select t.* from ( ").append(getLimitOverSQL(originalSQL)).append(" ) t where rownum <= ").append(max).append(getOrderBySQL(sortName,sortOrder));
			} else {
				limitSql.append(" select * from (  ");
					limitSql.append(" select row_.*,rownum rownum_ from ( ");
							limitSql.append( getLimitOverSQL(originalSQL));
					limitSql.append(" ) row_ ");
					limitSql.append(getOrderBySQL(sortName,sortOrder)) ;
					limitSql.append(" where rownum <= ").append(max);
				limitSql.append(" ) where rownum_ > ").append(offset);
			}
			return limitSql.toString();
		}
		return originalSQL;
	}

	public String getBatchUpdateWrapSQL(String originalSQL){
		return originalSQL;
	}
}
