package com.woshidaniu.httpclient;

import java.io.File;

import junit.framework.TestCase;

import org.apache.http.client.protocol.HttpClientContext;
import org.junit.Test;

import com.woshidaniu.httpclient.handler.AbstractResponseHandler;
import com.woshidaniu.httpclient.handler.FileResponseHandler;
import com.woshidaniu.httpclient.handler.PlainTextResponseHandler;
import com.woshidaniu.httpclient.utils.HttpClientUtils;

public class HttpClientTest extends TestCase{

	protected AbstractResponseHandler<String> textHandler = new PlainTextResponseHandler(HttpClientContext.create() , "UTF-8");
	protected AbstractResponseHandler<File> fileHandler = new FileResponseHandler(HttpClientContext.create() , new File("d://baidu.html"));
	
	@Test
	public void testHttpUtil()  {
		
		try {
			String baseURL = "http://www.baidu.com ";
			
			String resultHtml = HttpClientUtils.httpRequestWithGet(baseURL, textHandler);
			
			System.out.println(resultHtml);
			
			//File resultFile = HttpClientUtils.httpRequestWithGet(baseURL, fileHandler);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
