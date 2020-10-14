package com.woshidaniu.basicutils.exception;
/**
 * 
 *@类名称	: DataAccessException.java
 *@类描述	：数据库访问异常(RuntimeException)
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:44:48 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class DataAccessException extends NestedRuntimeException {
	
	public DataAccessException(String message) {
		super(message);
	}
	
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}
	
	public DataAccessException(String key,String defaultMessage) {
		super(getText(key, defaultMessage));
	}

	public DataAccessException(String key,Object... replaceParas){
		super(getText(key, "", replaceParas));
	}

	public DataAccessException(Throwable cause,String key,Object... replaceParas){
		super(getText(key, "", replaceParas), cause);
	}
	
}
