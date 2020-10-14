package com.woshidaniu.qa.exception; 
/** 
* @author shouquan
* @version 创建时间：2017年5月12日 上午8:56:52 
* 类说明：异常类 
*/
public class ZTesterException extends RuntimeException {

	private static final long serialVersionUID = 2374717241059913567L;

	public ZTesterException(String message) {
		super(message);
	}

	public ZTesterException(Throwable throwable) {
		super(throwable);
	}

	public ZTesterException(String error, Throwable throwable) {
		super(error, throwable);
	}
}
 