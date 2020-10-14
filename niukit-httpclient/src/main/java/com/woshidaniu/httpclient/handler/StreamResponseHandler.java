package com.woshidaniu.httpclient.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @className: ResponsePlainTextHandler
 * @description: http请求响应处理：返回字符串结果对象
 * @author : kangzhidong
 * @date : 下午01:37:18 2015-7-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class StreamResponseHandler extends AbstractResponseHandler<ByteArrayInputStream> {

	public StreamResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@Override
	public ByteArrayInputStream handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();
		// 从response中取出HttpEntity对象
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			try {
				// 响应内容
				return new ByteArrayInputStream(EntityUtils.toByteArray(httpEntity));
			} finally {
				// 销毁
				EntityUtils.consume(httpEntity);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
