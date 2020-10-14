 package com.woshidaniu.httpclient.interceptor;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ConfigUtils;

/**
 * 
 *@类名称	: HttpRequestHeaderInterceptor.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:07:38 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpRequestHeaderInterceptor implements HttpRequestInterceptor {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpRequestHeaderInterceptor.class);
	
	//初始化配置文件
	protected static Properties cachedProperties = new Properties();
	
	public HttpRequestHeaderInterceptor(){
		try {
			cachedProperties = ConfigUtils.getProperties(this.getClass(), "httpclient-header.properties");
		}catch (Exception ex) {
			LOG.warn("Could not load properties from classes/httpclient-header.properties : " + ex.getMessage());
		}
	}
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		//通过配置文件加载响应头信息
		if(!cachedProperties.isEmpty()){
			for(Object key : cachedProperties.keySet()){
				if(key != null){
					request.addHeader(key.toString() , cachedProperties.getProperty(key.toString()));  
				}
			}
		}
	}

}

 
