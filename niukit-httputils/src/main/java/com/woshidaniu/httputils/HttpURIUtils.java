package com.woshidaniu.httputils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.EncodingUtil;

public abstract class HttpURIUtils {

	public static String buildURL(String baseURL, Map<String, Object> paramsMap,String charset) throws URIException {
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
    	List<NameValuePair> nameValueList    = buildNameValuePairs(baseURL , paramsMap);
        StringBuilder builder = new StringBuilder(paramStr[0]);
        NameValuePair[] nameValuePairs = nameValueList.toArray(new NameValuePair[nameValueList.size()]);
		return builder.append(builder.indexOf("?") > 0 ? "&" : "?").append(EncodingUtil.formUrlEncode(nameValuePairs, charset)).toString();
	}
	
	/**
	 * 
	 * @description	：构建普通参数集合
	 * @author 		： kangzhidong
	 * @date 		：Jun 30, 2015 6:57:06 PM
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws URIException 
	 */
	public static List<NameValuePair> buildNameValuePairs(String baseURL, Map<String, Object> paramsMap) throws URIException {
    	//初始参数集合对象
    	List<NameValuePair> nameValueList    = new LinkedList<NameValuePair>();
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
						nameValueList.add(new NameValuePair(key, String.valueOf(value)));
					} else {
						nameValueList.add(new NameValuePair(key, ""));
					}
                }
            }
    	}
    	nameValueList.addAll(buildNameValuePairs(baseURL));
        return nameValueList;
    }
	
	public static List<NameValuePair> buildNameValuePairs(String baseURL) throws URIException {
    	//初始参数集合对象
    	List<NameValuePair> nameValueList    = new LinkedList<NameValuePair>();
    	//初始参数集合对象
    	String[] paramStr = baseURL.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
           return nameValueList;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
        	return nameValueList;
        }
        for (String param : paramArray) {
            if (param == null || "".equals(param.trim())) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            nameValueList.add(new NameValuePair(keyValue[0], String.valueOf(keyValue[1])));
        }
        return nameValueList;
    }
	
}
