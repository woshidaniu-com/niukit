 package com.woshidaniu.httpclient.utils;

import org.apache.http.StatusLine;

/**
 * 
 *@类名称	: ErrorResponseUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 11:21:48 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ErrorResponseUtils {

	public static String getStatusErrorJSON(StatusLine statusLine){
		int statusCode = statusLine.getStatusCode();
		StringBuilder builder = new StringBuilder();
		//{"errcode":40013,"errmsg":"invalid appid"}
		builder.append("{");
		builder.append("\"errcode\":").append(statusCode).append(",");
		builder.append("\"reason\":\"").append(statusLine.getReasonPhrase()).append("\",");
		builder.append("\"errmsg\":\"").append(HttpConfigUtils.getText(statusCode + "")).append("\"");
		builder.append("}");
		return builder.toString();
	}
	
}

 
