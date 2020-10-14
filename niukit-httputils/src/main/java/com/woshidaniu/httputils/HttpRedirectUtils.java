/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.httputils;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

/**
 *@类名称	: HttpRedirectUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：2016年4月28日 下午2:26:34
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class HttpRedirectUtils {

	public static <T> PostMethod stripRedirect(HttpClient httpclient,PostMethod httpMethod,int statuscode, String charset,
			Map<String, String> headers,RequestEntity requestEntity) throws HttpException, IOException {
		// 如果是重定向则需进一步处理
		PostMethod redirectMethod = HttpRequestUtils.getHttpRedirect(httpMethod, statuscode , charset, headers);
		// 方法被重定向
		if(redirectMethod != null){
			// 设置参数
			redirectMethod.setRequestEntity(requestEntity);
			// 执行请求
			int newStatuscode = httpclient.executeMethod(redirectMethod);
			// 如果是重定向则需进一步处理
			PostMethod newRedirectMethod = HttpRequestUtils.getHttpRedirect(redirectMethod, newStatuscode , charset, headers);
			// 再次方法被重定向
			if(newRedirectMethod != null){
				return stripRedirect(httpclient,httpMethod,statuscode,charset,headers,requestEntity);
			}
			return redirectMethod;
		}
		return httpMethod;
	}

	public static GetMethod stripRedirect(HttpClient httpclient,GetMethod httpMethod, int statuscode, String charset,
			Map<String, String> headers, NameValuePair[] nameValuePairs) throws HttpException, IOException {
		// 如果是重定向则需进一步处理
		GetMethod redirectMethod = HttpRequestUtils.getHttpRedirect(httpMethod, statuscode , charset, headers);
		// 方法被重定向
		if(redirectMethod != null){
			// 设置参数
			httpMethod.setQueryString(nameValuePairs);
			// 执行请求
			int newStatuscode = httpclient.executeMethod(redirectMethod);
			// 如果是重定向则需进一步处理
			GetMethod newRedirectMethod = HttpRequestUtils.getHttpRedirect(redirectMethod, newStatuscode , charset, headers);
			// 再次方法被重定向
			if(newRedirectMethod != null){
				return stripRedirect(httpclient,httpMethod,statuscode,charset,headers,nameValuePairs);
			}
			return redirectMethod;
		}
		return httpMethod;
	}

	public static PostMethod stripRedirect(String baseURL, HttpClient httpclient,PostMethod httpMethod, int statuscode, String charset,
			String contentType, Map<String, String> headers, Map<String, Object> paramsMap) throws HttpException, IOException {
		// 如果是重定向则需进一步处理
		PostMethod redirectMethod = HttpRequestUtils.getHttpRedirect(httpMethod, statuscode , charset, headers);
		// 方法被重定向
		if(redirectMethod != null){
			// 设置参数
			HttpRequestUtils.setHttpMathod(httpMethod, baseURL, paramsMap, charset, contentType, headers);
			// 执行请求
			int newStatuscode = httpclient.executeMethod(redirectMethod);
			// 如果是重定向则需进一步处理
			PostMethod newRedirectMethod = HttpRequestUtils.getHttpRedirect(redirectMethod, newStatuscode , charset, headers);
			// 再次方法被重定向
			if(newRedirectMethod != null){
				return stripRedirect(baseURL, httpclient, httpMethod, statuscode, charset, contentType, headers, paramsMap);
			}
			return redirectMethod;
		}
		return httpMethod;
	}
	
	
}
