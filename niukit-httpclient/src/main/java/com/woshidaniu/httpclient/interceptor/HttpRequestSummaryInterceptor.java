package com.woshidaniu.httpclient.interceptor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * 
 *@类名称	: HttpRequestSummaryInterceptor.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:07:51 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpRequestSummaryInterceptor implements HttpRequestInterceptor {
	
	public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
		// AtomicInteger是个线程安全的整型类
		//AtomicInteger count = (AtomicInteger) context.getAttribute("count");
		//request.addHeader("Count", Integer.toString(count.getAndIncrement()));
	}
	
}
