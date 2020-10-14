package com.woshidaniu.httpclient.utils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;

import com.woshidaniu.io.utils.FilemimeUtils;

/**
 * 
 *@类名称	: HttpClientRequestUtils.java
 *@类描述	：HttpClient请求准备处理工具；如 构建URL,处理参数
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 11:21:59 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class HttpClientRequestUtils {

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

    public static ContentType getBinaryContentType(String charset) {
		try {
			return ContentType.create( HttpClientUtils.APPLICATION_OCTET_STREAM, charset);
		} catch (UnsupportedCharsetException e) {
			return ContentType.DEFAULT_BINARY;
		}
	}
    
    public static ContentType getTextContentType(String charset) {
		try {
			return ContentType.create( HttpClientUtils.TEXT_PLAIN, charset);
		} catch (UnsupportedCharsetException e) {
			return ContentType.DEFAULT_TEXT;
		}
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
	
	/**
	 * 
	 * @description	： 构建对象参数集合，如上传文件
	 * @author 		： kangzhidong
	 * @date 		：Jun 30, 2015 6:56:42 PM
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 */
	public static Map<String,ContentBody> buildContentBody(Map<String, Object> paramsMap) {
		Map<String,ContentBody> contentBodies          	 = new HashMap<String,ContentBody>();
		if(paramsMap != null && !paramsMap.isEmpty()){
			//组织参数
	        Iterator<String> iterator = paramsMap.keySet().iterator();
	        while (iterator.hasNext()) {
	            String key = iterator.next();
	            Object value = paramsMap.get(key);
	            if (value instanceof File) {
	            	//对象强转
	            	File file = (File) value;
	            	// 把文件转换成流对象FileBody
	                FileBody fileBody = new FileBody(file , ContentType.create( FilemimeUtils.getFileMimeType(file)));
	                contentBodies.put(key, fileBody);
	            } else if (value instanceof InputStream) {
	            	// 把输入流转换成流对象InputStreamBody
	            	InputStreamBody streamBody = new InputStreamBody((InputStream) value,  key);
	                contentBodies.put(key, streamBody);
	            } else if (value instanceof byte[]) {
	                byte[] byteVlue = (byte[]) value;
	                // 把字节数组转换流对象ByteArrayBody
	                ByteArrayBody byteArrayBody = new ByteArrayBody(byteVlue, key);
	                contentBodies.put(key, byteArrayBody);
	            }
	        }
    	}
		return contentBodies;
	}
	
	
	public static boolean isGzip(HttpRequestBase httpRequest){
		 //设置请求实体参数对象
        HeaderElementIterator it = new BasicHeaderElementIterator(httpRequest.headerIterator(HttpHeaders.ACCEPT_ENCODING));
	    boolean isGzip = false;
        while (it.hasNext()) {
	        HeaderElement elem = it.nextElement(); 
	        System.out.println(elem.getName() + " = " + elem.getValue());
	        if("gzip".equalsIgnoreCase(elem.getName())){
	        	isGzip = true;
	        	break;
	        }
	    }
		return isGzip;
	}
}
