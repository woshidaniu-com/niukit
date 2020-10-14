package com.woshidaniu.httpclient.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;

public abstract class HttpURIUtils {

	public static String buildURL(String baseURL, Map<String, Object> paramsMap,String charset) {
		if (paramsMap == null) {
			return baseURL;
		}
		//初始参数集合对象
    	String[] paramStr = baseURL.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
           return baseURL;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
        	return baseURL;
        }
        //初始参数集合对象
    	List<NameValuePair> nameValuePairs    = buildNameValuePairs(baseURL , paramsMap);
        StringBuilder builder = new StringBuilder(paramStr[0]);
		return builder.append(builder.indexOf("?") > 0 ? "&" : "?").append(URLEncodedUtils.format(nameValuePairs, charset)).toString();
	}
	
	/**
	 * 
	 * @description	：构建普通参数集合
	 * @author 		： kangzhidong
	 * @date 		：Jun 30, 2015 6:57:06 PM
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 */
	public static List<NameValuePair> buildNameValuePairs(String baseURL, Map<String, Object> paramsMap) {
    	//初始参数集合对象
    	List<NameValuePair> nameValuePairs    = new LinkedList<NameValuePair>();
    	if(paramsMap != null && !paramsMap.isEmpty()){
    		//组织参数
            Iterator<String> iterator = paramsMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramsMap.get(key);
                if (value instanceof File) {
                	//什么都不做
                } else if (value instanceof byte[]) {
                	//什么都不做
                } else {
                    if (value != null && !"".equals(value)) {
                    	nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(value) ));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair(key, ""));
                    }
                }
            }
    	}
    	nameValuePairs.addAll(buildNameValuePairs(baseURL));
        return nameValuePairs;
    }
	
	public static List<NameValuePair> buildNameValuePairs(String baseURL) {
    	//初始参数集合对象
    	List<NameValuePair> nameValuePairs    = new LinkedList<NameValuePair>();
    	//初始参数集合对象
    	String[] paramStr = baseURL.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
           return nameValuePairs;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
        	return nameValuePairs;
        }
        for (String param : paramArray) {
            if (param == null || "".equals(param.trim())) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            nameValuePairs.add(new BasicNameValuePair(keyValue[0], keyValue[1]));
        }
        return nameValuePairs;
    }
	
	public static URIBuilder buildURIBuilder(String baseURL, Map<String, Object> paramsMap,String charset) throws URISyntaxException{
		if (baseURL == null) {
			return null;
		}
		//初始参数集合对象
    	String[] paramStr = baseURL.split("[?]", 2);
    	URIBuilder uriBuilder = new URIBuilder(paramStr[0]);
        if (paramStr == null || paramStr.length != 2) {
           return uriBuilder;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
        	return uriBuilder;
        }
        for (String param : paramArray) {
            if (param == null || "".equals(param.trim())) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            uriBuilder.setParameter(keyValue[0],String.valueOf(keyValue[1]));
        }
		if(paramsMap != null && !paramsMap.isEmpty()){
        	for (String key : paramsMap.keySet()) {
                Object value = paramsMap.get(key);
    			if (value instanceof File) {
                	//什么都不做
                } else if (value instanceof byte[]) {
                	//什么都不做
                } else {
                    if (value != null && !"".equals(value)) {
                    	uriBuilder.setParameter(key,String.valueOf(value));
                    } else {
                    	uriBuilder.setParameter(key,"");
                    }
                }
    		}
        }
		try {
			return uriBuilder.setCharset(charset == null ? Consts.UTF_8 : CharsetUtils.get(charset)  );
		} catch (UnsupportedEncodingException e) {
			return uriBuilder;
		}
	}
	
}
