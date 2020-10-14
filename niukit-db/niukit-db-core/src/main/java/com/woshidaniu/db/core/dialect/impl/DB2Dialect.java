package com.woshidaniu.db.core.dialect.impl;

import com.woshidaniu.basicutils.BlankUtils;

public class DB2Dialect extends MySql5Dialect{

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

	@Override
	public String getCountSQL(String originalSQL) {
	
		StringBuffer rownumber = new StringBuffer(50).append("rownumber() over(");

		int orderByIndex = originalSQL.toLowerCase().indexOf("order by");

		if ( orderByIndex>0 && !hasDistinct(originalSQL) ) {
			rownumber.append( originalSQL.substring(orderByIndex) );
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
		
	}
	
	private String getRowNumber(String originalSQL) {
		StringBuffer rownumber = new StringBuffer(50).append("rownumber() over(");

		int orderByIndex = originalSQL.toLowerCase().indexOf("order by");

		if ( orderByIndex>0 && ! hasDistinct(originalSQL) ) {
			rownumber.append( originalSQL.substring(orderByIndex) );
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
	}
	

	public String getLimitString(String originalSQL, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
		int startOfSelect = originalSQL.toLowerCase().indexOf("select");

		StringBuffer pagingSelect = new StringBuffer( originalSQL.length()+100 )
					.append( originalSQL.substring(0, startOfSelect) ) //add the comment
					.append("select * from ( select ") //nest the main query in an outer select
					.append( getRowNumber(originalSQL) ); //add the rownnumber bit into the outer query select list

		if ( hasDistinct(originalSQL) ) {
			pagingSelect.append(" row_.* from ( ") //add another (inner) nested select
				.append( originalSQL.substring(startOfSelect) ) //add the main query
				.append(" ) as row_"); //close off the inner nested select
		}
		else {
			pagingSelect.append( originalSQL.substring( startOfSelect + 6 ) ); //add the main query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		//add the restriction to the outer select
		if (offset > 0) {
//			int end = offset + limit;
			String endString = offsetPlaceholder+"+"+limitPlaceholder;
			pagingSelect.append("between "+offsetPlaceholder+"+1 and "+endString);
		}
		else {
			pagingSelect.append("<= "+limitPlaceholder);
		}

		return pagingSelect.toString();
	}
	
	
}
