package com.woshidaniu.db.core.dialect.impl;

import com.woshidaniu.basicutils.BlankUtils;


/**
 * 
 *@类名称	: OracleDialect2.java
 *@类描述	：适配Oracle数据库分页SQl组装
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 2:25:56 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class OracleDialect2 extends OracleDialect{

	public String getOnceLimitSQL(String originalSQL,long offset,long limit,String sortName,String sortOrder) {
		if(!BlankUtils.isBlank(originalSQL)){
			originalSQL = trim(originalSQL);
			//判断是否更新的sql
			boolean isForUpdate = false;
			if ( originalSQL.toLowerCase().endsWith(" for update") ) {
				originalSQL = originalSQL.substring( 0, originalSQL.length() - 11 );
				isForUpdate = true;
			}
			
			long max = offset + limit;
			StringBuilder limitSql = new StringBuilder(originalSQL.length() + 10);
			if (offset == 0 && max == -1) {
				limitSql.append("select t.* from ( ").append(getLimitOverSQL(originalSQL)).append(" ) t ").append(getOrderBySQL(sortName,sortOrder)) ;
			} else if (offset == 0) {
				limitSql.append("select t.* from ( ").append(getLimitOverSQL(originalSQL)).append(" ) t where rownum <= ").append(max).append(getOrderBySQL(sortName,sortOrder));
			} else {
				limitSql.append(" with tmp_tb2 as (");
				limitSql.append(originalSQL);
				limitSql.append(" ),");
				limitSql.append("  tmp_tb3 as (select count(1) zs from  (");
				limitSql.append(originalSQL);
				limitSql.append(" ))");
				limitSql.append(" select t.* from (select tmp_tb1.*, ROWNUM rownum_");
				limitSql.append(" from (select tmp_tb2.* ,(select zs from tmp_tb3 ) totalResult");
				limitSql.append(" from  tmp_tb2 ").append(getOrderBySQL(sortName,sortOrder));
				limitSql.append(" ) tmp_tb1");
				if(limit != 0 ){
					limitSql.append(" where rownum <= ").append(max);
				}
				limitSql.append(") t where t.rownum_ > ").append(offset);
				
			}
			if ( isForUpdate ) {
				limitSql.append( " for update" );
			}
			return limitSql.toString();
		}
		return originalSQL;
	}

	public String getBatchUpdateWrapSQL(String originalSQL){
		return originalSQL;
	}
}
