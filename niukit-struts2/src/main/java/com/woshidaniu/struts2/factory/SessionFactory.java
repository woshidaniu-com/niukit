package com.woshidaniu.struts2.factory;

import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.util.ValueStack;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * 
 * @className	： SessionFactory
 * @description	： 会话工厂：只能用于当前线程，EJB端不支持使用（以后可能支持）
 * @author 		： kangzhidong
 * @date		： 2015-6-9 上午11:29:03
 */
public abstract class SessionFactory {
	
	private static ServletContext servletContext = null;
	
	protected SessionFactory() {

	}
	
	/**
	 * 
	 * @description	： 取得ServletContext的简化方法.
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午11:22:01
	 * @return
	 */
	public static ServletContext getServletContext() {
		if(servletContext == null){
			servletContext = ServletActionContext.getServletContext();
		}
		return servletContext;
	}

	public static void setServletContext(ServletContext servletContext) {
		SessionFactory.servletContext = servletContext;
	}

	/**
	 * 
	 * @description	： 取得HttpSession的简化方法.
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:40:06
	 * @return
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 
	 * @description	：取得HttpRequest的简化方法.
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:39:56
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 
	 * @description	： 取得HttpResponse的简化方法.
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:40:15
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 
	 * @description	：取得Request Parameter的简化方法.
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:40:23
	 * @param name
	 * @return
	 */
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}
	
	/**
	 * 
	 * @description	： 得到request中参数Map,相当于request.getParameterMap()
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:40:34
	 * @return
	 */
	public static Map<String, Object> getParametersMap() {
		return ServletActionContext.getContext().getParameters();
	}

	/**
	 * 
	 * @description	： 得到Context中的变量,相当于request.getAttribute()
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:40:44
	 * @return
	 */
	public static Map<String, Object> getContextMap() {
		return ServletActionContext.getContext().getContextMap();
	}
	
	/**
	 * 
	 * @description	：得到Session中的变量Map,相当于request.getSession().getAttribute()
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:40:53
	 * @return
	 */
	public static Map<String, Object> getSessionMap() {
		return ServletActionContext.getContext().getSession();
	}
	
	/**
	 * 
	 * @description	： 得到Application中的变量Map,相当于request.getSession().getServletContext().getAttribute()
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:41:02
	 * @return
	 */
	public static Map<String, Object> getApplicationMap() {
		return ServletActionContext.getContext().getApplication();
	}
	
	/**
	 * 
	 * @description	：得到Struts2的ValueStack
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:41:11
	 * @return
	 */
	public static ValueStack getValueStack(){
		return ServletActionContext.getContext().getValueStack();
	}
	
	/**
	 * 
	 * @description	：得到主机 应用 http请求路径： 如：http://127.0.0.1:8080/webapp_name
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:58:13
	 * @param request
	 * @return
	 */
	public static String getHostPath(){
		return WebRequestUtils.getHostPath(getRequest());
	} 

	/**
	 * 
	 * @description	： 获得本地主机信息【本机IP,端口,本机名称】
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午10:59:47
	 * @return
	 * @throws UnknownHostException
	 */
	public static String[] getLocalHostInfo() throws UnknownHostException {
		return WebRequestUtils.getLocalHostInfo(getRequest());
	}
    
	/**
	 * 
	 * @description	： 获得请求的客户端信息【ip,port,name】
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午11:00:29
	 * @return
	 */
	public static String[] getRemoteInfo() {
		return WebRequestUtils.getRemoteInfo(getRequest());
	}
	
	/**
	 * 
	 * @description	：获取客户端IP地址，支持代理服务器
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午11:01:02
	 * @return
	 */
	public static String getRemoteAddr() {
		// 获取客户端IP地址，支持代理服务器
		return WebRequestUtils.getRemoteAddr(getRequest());
	}

}
