package com.woshidaniu.authz.ticket.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 *@类名称		： ResultUtils.java
 *@类描述		： 响应结果包装工具函数
 *@创建人		：wandalong
 *@创建时间	：Sep 6, 2016 10:00:37 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ResultUtils {

	/**
	 * 
	 *@描述		： 包装处理结果状态，并返回JSON值
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 201610:01:12 AM
	 *@param status  ： 状态值或状态码
	 *@param message ： 提示信息
	 *@return		   ： JSON格式的字符串
	 */
	public static String token(String status,String token){
		return JSONObject.toJSONString(tokenMap(status, token));
	}
	
	/**
	 * 
	 *@描述		： 包装处理结果状态，并返回Map对象
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 201610:01:12 AM
	 *@param status  ： 状态值或状态码
	 *@param  message ： 提示信息
	 *@return		   ： Map对象
	 */
	public static Map<String, Object> tokenMap(String status,String token){
		Map<String, Object> rtMap = new HashMap<String, Object>();
		rtMap.put("status", status);
		rtMap.put("token", token);
		return rtMap;
	}
	
	/**
	 * 
	 *@描述		： 包装处理结果状态，并返回JSON值
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 201610:01:12 AM
	 *@param status  ： 状态值或状态码
	 *@param message ： 提示信息
	 *@return		   ： JSON格式的字符串
	 */
	public static String status(String status,String message){
		return JSONObject.toJSONString(statusMap(status, message));
	}
	
	/**
	 * 
	 *@描述		： 包装处理结果状态，并返回Map对象
	 *@创建人		: wandalong
	 *@创建时间	: Sep 6, 201610:01:12 AM
	 *@param status  ： 状态值或状态码
	 *@param  message ： 提示信息
	 *@return		   ： Map对象
	 */
	public static Map<String, Object> statusMap(String status,String message){
		Map<String, Object> rtMap = new HashMap<String, Object>();
		rtMap.put("status", status);
		rtMap.put("message", message);
		return rtMap;
	}
	
	
	
}
