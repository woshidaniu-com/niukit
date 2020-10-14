package com.woshidaniu.configuration.config;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

 /**
  * 
  *@类名称	: AbstractContext.java
  *@类描述	：抽象的上下文
  *@创建人	：kangzhidong
  *@创建时间：Mar 7, 2016 4:49:25 PM
  *@修改人	：
  *@修改时间：
  *@版本号	:v1.0
  */
public abstract class AbstractContext {
	
	// http 相关对象
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected ServletContext servletContext;
	// 日期格式对象
	protected static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		
	public abstract void initialize(Config config) throws ServletException;
	
	public abstract void destroy();
	
	public abstract AbstractContext setRequest(HttpServletRequest request, HttpServletResponse response);
	
	public abstract void setParameters(Map<String, String> params);

	public abstract Map<String, String> getParameters();
	
	public abstract void setConfigs(Properties config);

	public abstract String getConfigProperty(String key) ;
	
	public abstract String getConfigPath();
	
	public abstract String getConfigXmlPath();
	
	public abstract String getEncoding();
	
	// ----------------预置的参数取值method--------------------------------------------------------

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public HttpSession getSession() {
		return session;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}



