 package com.woshidaniu.httpclient.interceptor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * 
 *@类名称	: HttpRequestGzipInterceptor.java
 *@类描述	：增加gzip压缩请求
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:08:11 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpRequestGzipInterceptor implements HttpRequestInterceptor {
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		
		if (!request.containsHeader("Accept-Encoding")) {  
			//设置相关的压缩文件标识，在请求头的信息中
			request.addHeader("Accept-Encoding", "gzip");  
        }  
	}

}

 
