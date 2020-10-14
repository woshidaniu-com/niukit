package com.woshidaniu.struts2;

import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 *@类名称	: ActionAware.java
 *@类描述	：基于struts.2.x 顶层封装接口
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 10:04:11 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface ActionAware {

	public final static String DIRECTLY_MESSAGE_KEY = "message";
	public final static short[] shorts = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public final static Integer DEFAULT_PAGESIZE = 15;
	public final static Integer DEFAULT_STARTOFRECORD = 0;
	public final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final String EXCEPTIONPATH = "/exception.properties";

	public abstract HttpServletRequest getRequest();

	public abstract HttpServletResponse getResponse();

	public abstract HttpSession getSession();

	public abstract ServletContext getServletContext();
	
	public abstract ValueStack getValueStack();
	
}
