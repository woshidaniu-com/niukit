package com.woshidaniu.httpclient.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.httpclient.utils.HttpClientUtils;
import com.woshidaniu.io.utils.IOUtils;

/**
 * 
 *@类名称	: ResponseFileHandler.java
 *@类描述	：http请求响应处理：返回字符串结果对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:57:25 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class FileResponseHandler extends AbstractResponseHandler<File> {

	private File destFile;
	
	public FileResponseHandler(HttpClientContext context, File destFile) {
		super(context, HttpClientUtils.UTF_8);
		Assert.notNull(destFile, "destFile is null ");
		this.destFile = destFile;
	}

	@Override
	public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
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
			
			InputStream input = null;
			FileOutputStream output = null;
			try {
				// 先存为临时文件，等全部下完再改回原来的文件名
				File storeFile = new File(destFile.getParent() , destFile.getName()  + ".tmp"); 
				output = new FileOutputStream(storeFile);
				input = httpEntity.getContent();
				IOUtils.copy(input, output);
				storeFile.renameTo(destFile);
				return destFile;
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
				// 销毁
				EntityUtils.consume(httpEntity);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
