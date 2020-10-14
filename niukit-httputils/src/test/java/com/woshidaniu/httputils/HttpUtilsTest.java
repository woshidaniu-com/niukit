package com.woshidaniu.httputils;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

import com.woshidaniu.httputils.handler.FileResponseHandler;
import com.woshidaniu.httputils.handler.PlainTextResponseHandler;
import com.woshidaniu.httputils.handler.ResponseHandler;

public class HttpUtilsTest extends TestCase{

	protected ResponseHandler<String> textHandler = new PlainTextResponseHandler();
	protected ResponseHandler<File> fileHandler = new FileResponseHandler(new File("d://baidu.html"));
	
	@Test
	public void testHttpUtil()  {
		
		try {
			String baseURL = "http://www.baidu.com ";
			
			String resultHtml = HttpClientUtils.httpRequestWithGet(baseURL, textHandler);
			
			
			System.out.println(resultHtml);
			
			//HttpClientUtils.httpRequestWithGet(baseURL, fileHandler);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
}
