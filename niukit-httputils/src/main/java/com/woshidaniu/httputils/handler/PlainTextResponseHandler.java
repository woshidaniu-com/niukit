package com.woshidaniu.httputils.handler;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;

import com.woshidaniu.httputils.exception.HttpResponseException;

/**
 * 
 * @className: PlainTextResponseHandler
 * @description: http请求响应处理：返回字符串结果对象
 * @author : kangzhidong
 * @date : 下午01:37:18 2015-7-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class PlainTextResponseHandler implements ResponseHandler<String> {

	@Override
	public void handleClient(HttpClient httpclient) {
		
	}

	@Override
	public String handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			try {
				// 响应内容
				return httpMethod.getResponseBodyAsString();
			} finally {
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
