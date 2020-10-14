 package com.woshidaniu.orm.ibatis2.interfaces;
 /**
  * 
  *@类名称	: IWrapSQLService.java
  *@类描述	：
  *@创建人	：kangzhidong
  *@创建时间	：Mar 18, 2016 9:32:32 AM
  *@修改人	：
  *@修改时间	：
  *@版本号	:v1.0
  */
public interface IWrapSQLService {
	
	public String getWrapSQL(Object parameterObject,String originalSQL);
	
}

