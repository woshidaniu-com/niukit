package com.woshidaniu.xmlhub.core.exception;

import com.woshidaniu.basicutils.exception.NestedRuntimeException;

/**
 * 
 *@类名称	: TransformException.java
 *@类描述	： XML,JSON转换异常
 *@创建人	：kangzhidong
 *@创建时间	：Mar 17, 2016 10:36:48 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class TransformException extends NestedRuntimeException {  
	
	public TransformException() {
		super();
	}
	
	public TransformException(String message) {
		super(message);
	}
	
	public TransformException(Throwable cause) {
		super(cause);
	}
	
	public TransformException(String message,Throwable cause) {
		super(message, cause);
	}
}  