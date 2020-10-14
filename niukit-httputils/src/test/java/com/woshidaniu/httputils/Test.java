package com.woshidaniu.httputils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *@类名称	: Test.java
 *@类描述	：
 *@创建人	：1270
 *@创建时间	：2016年4月28日 下午12:49:51
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class Test {
	
	
	public static void main(String[] args) throws IOException {

		String baseURL= "http://api.zju.edu.cn:8094/message/openapi/api.do?apiKey=91bd53f871464366f92c6964f98d5aa9&appName=getGrzp&returnType=xml&xgh=3130000202";
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(baseURL);
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 50000);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");
		// 设置Http Post数据
		HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
		// 设置连接超时时间(单位毫秒)
		managerParams.setConnectionTimeout(300000);
		// 设置读数据超时时间(单位毫秒)
		managerParams.setSoTimeout(6000000);
		client.executeMethod(method);
		
			String response = method.getResponseBodyAsString();
			System.out.println(response);
		
		
		/*ResponseHandler<String> textHandler = new PlainTextResponseHandler(){
			
			public void handleClient(HttpClient httpclient){
				
			}
			
		};
		
		//执行Post请求
		String responseXML = HttpClientUtils.httpRequestWithGet(baseURL, textHandler );
		System.out.println(responseXML);*/
		
		
	}
}
