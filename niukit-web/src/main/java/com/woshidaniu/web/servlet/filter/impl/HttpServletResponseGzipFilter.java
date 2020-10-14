package com.woshidaniu.web.servlet.filter.impl;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;
import com.woshidaniu.web.servlet.http.HttpServletGzipResponseWrapper;

public class HttpServletResponseGzipFilter extends OncePerRequestFilter {

	public static Logger LOG = LoggerFactory.getLogger(HttpServletResponseGzipFilter.class);
	
	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String ae = httpRequest.getHeader("accept-encoding");
		if (ae != null && ae.indexOf("gzip") != -1) {
			try {
				LOG.debug("GZIP supported, compressing.");
				HttpServletGzipResponseWrapper wrappedResponse = new HttpServletGzipResponseWrapper(httpResponse);
				filterChain.doFilter(request, wrappedResponse);
				wrappedResponse.finishResponse();
				return;
			} catch (Exception e) {
				LOG.debug("GZIP Error ." , e);
			}
		}
		filterChain.doFilter(request, response);
	}

}
