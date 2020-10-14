 package com.woshidaniu.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;
import com.woshidaniu.web.servlet.http.HttpServletResourceCachedResponseWrapper;
 /**
  * 
  *@类名称		: HttpServletRequestWebResourceCacheFilter.java
  *@类描述		：
  *@创建人		：kangzhidong
  *@创建时间	：Mar 8, 2016 9:03:44 AM
  *@修改人		：
  *@修改时间	：
  *@版本号		: v1.0
  */
public abstract class HttpServletRequestWebResourceCacheFilter  extends OncePerRequestFilter {
	
	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		  //1.得到用户请求的uri
		String uri = httpRequest.getRequestURI();
		//2.看缓存中有没有uri对应的数据
		byte[] cache = this.getCache(uri);
		//3.如果缓存中有，直接拿缓存的数据打给浏览器，程序返回
		if(cache !=null){
		    //根据字节数组和指定的字符编码构建字符串
		    String webResourceHtmlStr = new String(cache,response.getCharacterEncoding());
		    System.out.println(webResourceHtmlStr);
		    response.getOutputStream().write(cache);
		    return;
		}
		//4.如果缓存没有，让目标资源执行，并捕获目标资源的输出
		HttpServletResourceCachedResponseWrapper myresponse = new HttpServletResourceCachedResponseWrapper(httpResponse);
		//执行过滤器
		filterChain.doFilter(request, myresponse);
		//获取缓冲流中的内容的字节数组
		byte[] bytes = myresponse.getBuffer();
		//5.把资源的数据以用户请求的uri为关键字保存到缓存中
		this.setCache(uri, bytes);
		//6.把数据打给浏览器
		response.getOutputStream().write(bytes);
	}
	
	public abstract byte[] getCache(String uri);
	
	public abstract byte[] setCache(String uri,byte[] bytes);
	

}

