package com.woshidaniu.sms.client.http.exception;

import java.io.IOException;

/**
 * @className	： HttpResponseException
 * @description	： TODO(描述这个类的作用)
 * @date		： 2017年6月13日 下午9:20:17
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class HttpResponseException extends IOException {
	
	private int statusCode = 200;

	public HttpResponseException(String message) {
		super(message);
	}
	
	public HttpResponseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HttpResponseException(final int statusCode, final String s) {
		super(s);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

}
