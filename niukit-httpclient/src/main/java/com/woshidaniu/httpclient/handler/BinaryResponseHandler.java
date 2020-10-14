package com.woshidaniu.httpclient.handler;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.woshidaniu.basicutils.BlankUtils;

/**
 * 
 *@类名称	: ResponseBinaryHandler.java
 *@类描述	：http请求响应处理：返回字符串结果对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:56:51 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class BinaryResponseHandler extends AbstractResponseHandler<byte[]> {

	public BinaryResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@Override
	public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();
		// 从response中取出HttpEntity对象
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		ContentType contentType = ContentType.getOrDefault(httpEntity);
		String charset = BlankUtils.isBlank(contentType.getCharset()) ? null : contentType.getCharset().name();
		if (charset == null) {
			charset = getCharset();
		}

		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			byte[] content = null;
			try {
				// 响应内容
				content = EntityUtils.toByteArray(httpEntity);
			} finally {
				if (httpEntity != null) {
					IOUtils.closeQuietly(httpEntity.getContent());
					// 销毁
					EntityUtils.consume(httpEntity);
				}
			}
			return content;
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}