package com.woshidaniu.rjpj.api;

import java.util.Map;

/**
 * 请求路径构建器
 * 
 * apiUrl请求路径形如 http://a.b.c.d:8080/rjpj/api
 * 
 * @author 1571
 */
public interface RequestUrlBuilder {

	/**
	 * 构建请求路径
	 * @param key
	 * @param apiUrl
	 * @param parameters
	 * 
	 * 必须携带的参数放parameters里面，以key,value键值形式存放，包括如下键:
	 * 
	 * xxdm
	 * xm
	 * sjhm
	 * formCode
	 * 
	 * @return
	 */
	String build(String key,String apiUrl,Map<String,String> parameters);
}
