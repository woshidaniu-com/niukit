package com.woshidaniu.basicutils.exception;

import java.text.MessageFormat;

/**
 * 
 *@类名称	: NestedErrorException.java
 *@类描述	：系统错误(Error),重写部分方法提高异常处理效率
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:42:56 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public abstract class OverrideStackErrorException extends Error {

	public OverrideStackErrorException() {
		fillInStackTrace();
	}
	
	public OverrideStackErrorException(String message) {
		super(message);
	}
	
	public OverrideStackErrorException(Throwable cause) {
		super(cause);
	}
	
	public OverrideStackErrorException(String message,Throwable cause) {
		super(message, cause);
	}
	
	public OverrideStackErrorException(String message,Object... arguments){
		super(getText(message, arguments));
	}

	public OverrideStackErrorException(Throwable cause,String message,Object... arguments){
		super(getText(message, arguments), cause);
	}
	
	/**
	 * <pre>
	 * 自定义改进的Exception对象 覆写了 fillInStackTrace方法
	 * 1. 不填充stack
	 * 2. 取消同步
	 * </pre>
	 */
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

	/**
	 * 
	 * ******************************************************************
	 * @description	：根据key从配置文件中获取配置数据
	 * @author 		： kangzhidong
	 * @date 		：Mar 4, 2016 9:07:34 AM
	 * @param message
	 * @param arguments
	 * @return
	 * ******************************************************************
	 */
	protected static String getText(String message, Object... arguments){
		String value = (arguments == null || arguments.length == 0) ? message : MessageFormat.format(message, arguments);
		return (value == null) ? ""  : value;
	}
	
}