package com.woshidaniu.web.servlet.filter.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;
import com.woshidaniu.web.servlet.http.HttpServletDirtyWordFilterRequestWrapper;

/**
 * 
 *@类名称	: HttpServletRequestDirtyWordFilter.java
 *@类描述	：敏感词过滤器
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 9:03:33 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpServletRequestDirtyWordFilter extends OncePerRequestFilter {

	List<String> dirtyWords = new ArrayList<String>();

	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		String dirtyWordPath = filterConfig.getInitParameter("dirtyWord");
		InputStream inputStream = filterConfig.getServletContext().getResourceAsStream(dirtyWordPath);
		InputStreamReader is = null;
		try {
			is = new InputStreamReader(inputStream, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(is);
		String line;
		try {
			while ((line = reader.readLine()) != null) {// 如果 line为空说明读完了
				dirtyWords.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		HttpServletDirtyWordFilterRequestWrapper dirtyrequest = new HttpServletDirtyWordFilterRequestWrapper(
				httpRequest, dirtyWords);
		filterChain.doFilter(dirtyrequest, response);

	}

}
