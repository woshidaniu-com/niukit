package com.woshidaniu.basicutils.exception;

import java.text.MessageFormat;

import com.woshidaniu.basicutils.NestedExceptionUtils;

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
public abstract class NestedErrorException extends Error {

	static {
		// Eagerly load the NestedExceptionUtils class to avoid classloader deadlock
		// issues on OSGi when calling getMessage(). Reported by Don Brown; SPR-5607.
		NestedExceptionUtils.class.getName();
	}
	
	public NestedErrorException() {
		fillInStackTrace();
	}
	
	public NestedErrorException(String message) {
		super(message);
	}
	
	public NestedErrorException(Throwable cause) {
		super(cause);
	}
	
	public NestedErrorException(String message,Throwable cause) {
		super(message, cause);
	}
	
	public NestedErrorException(String message,Object... arguments){
		super(getText(message, arguments));
	}

	public NestedErrorException(Throwable cause,String message,Object... arguments){
		super(getText(message, arguments), cause);
	}
	
	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 */
	@Override
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	/**
	 * Retrieve the innermost cause of this exception, if any.
	 * @return the innermost exception, or {@code null} if none
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * Retrieve the most specific cause of this exception, that is,
	 * either the innermost cause (root cause) or this exception itself.
	 * <p>Differs from {@link #getRootCause()} in that it falls back
	 * to the present exception if there is no root cause.
	 * @return the most specific cause (never {@code null})
	 * @since 2.0.3
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	/**
	 * Check whether this exception contains an exception of the given type:
	 * either it is of the given class itself or it contains a nested cause
	 * of the given type.
	 * @param exType the exception type to look for
	 * @return whether there is a nested exception of the specified type
	 */
	public boolean contains(Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof NestedCheckedException) {
			return ((NestedErrorException) cause).contains(exType);
		}
		else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
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