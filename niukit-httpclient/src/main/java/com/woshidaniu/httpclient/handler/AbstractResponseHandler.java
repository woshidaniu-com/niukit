package com.woshidaniu.httpclient.handler;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * 
 *@类名称	: ResponseAbstractHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:56:43 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <T>
 */
public abstract class AbstractResponseHandler<T> implements ResponseHandler<T> {

	protected HttpClientContext context;
	protected String charsetStr;

	public AbstractResponseHandler(HttpClientContext context, String charset) {
		this.context = context;
		this.charsetStr = charset;
	}

	public HttpClientContext getContext() {

		return context;
	}

	public void setContext(HttpClientContext context) {

		this.context = context;
	}

	public String getCharset() {

		return charsetStr;
	}

	public void setCharset(String charset) {

		this.charsetStr = charset;
	}

}
