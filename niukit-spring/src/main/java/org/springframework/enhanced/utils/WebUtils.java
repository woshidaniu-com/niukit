package org.springframework.enhanced.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

	/**
	 *  获得请求的客户端信息【ip,port,name】
	 */
	public static String[] getRemoteInfo(HttpServletRequest request) {
		if (request == null) {
			return new String[] { "", "", "" };
		}
		return new String[] { getRemoteAddr(request), request.getRemotePort() + "", request.getRemoteHost()};
	}
	
	/**
	 * 获取请求客户端IP地址，支持代理服务器
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		
		 // 获取客户端IP地址，支持代理服务器
	   String ip = request.getHeader("Cdn-Src-Ip"); 
	   String localIP = "127.0.0.1";
	   
	   if (StringUtils.isEmpty(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getHeader("x-forwarded-for");
	   } 
	    
	   if (StringUtils.isEmpty(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getHeader("Proxy-Client-IP"); 
	   } 
	    
	   if (StringUtils.isEmpty(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getHeader("WL-Proxy-Client-IP"); 
	   } 
	    
	   if (StringUtils.isEmpty(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getHeader("HTTP_CLIENT_IP"); 
	   } 
	    
	   if (StringUtils.isEmpty(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	   } 
	    
	   if (StringUtils.isEmpty(ip) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getRemoteAddr(); 
	   } 
	   
	   if (StringUtils.isEmpty(ip) || (ip.equalsIgnoreCase("localhost")) || "unknown".equalsIgnoreCase(ip)) { 
		   ip = "127.0.0.1";
	   } 
	   return ip;
	}
	
}
