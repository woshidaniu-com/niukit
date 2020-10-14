package com.woshidaniu.web.context;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：Web对象
 *	 <br>class：com.woshidaniu.web.WebContext.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@SuppressWarnings("unchecked")
public class WebContext {
	
	/**
     * Constant for the Servlet config object.
     */
    public static final String SERVLET_CONFIG = "javax.servlet.ServletConfig";
    /**
     * Constant for the Filter config object.
     */
    public static final String FILTER_CONFIG = "javax.servlet.FilterConfig";
    /**
     * Constant for the HTTP request object.
     */
    public static final String HTTP_REQUEST = "javax.servlet.http.HttpServletRequest";
    /**
     * Constant for the HTTP response object.
     */
    public static final String HTTP_RESPONSE = "javax.servlet.http.HttpServletResponse";
    /**
     * Constant for the HTTP locale object.
     */
    public static final String HTTP_LOCALE = "java.util.Locale";
    /**
     * Constant for the {@link javax.servlet.ServletContext servlet context} object.
     */
    public static final String SERVLET_CONTEXT = "javax.servlet.ServletContext";
    /**
     * Constant for the {@link java.util.concurrent.ThreadPoolExecutor} object.
     */
    public static final String HTTP_ASYNC_POOL_EXECUTOR = "java.util.concurrent.ThreadPoolExecutor";
    /**
     * The name of the request attribute under which the original
     * servlet path is made available to the target of a forward
     */
    public static final String FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";
    
	/**
	 * 与线程无关的常量对象
	 */
	protected static ConcurrentMap<String, Object> WEB_OBJECT_LOCAL = new ConcurrentHashMap<String, Object>();

	/**
	 * 绑定数据对象到ThreadLocal
	 */
	private static ThreadLocal<Map<String, Object>> WEB_CONTEXT_LOCAL = new ThreadLocal<Map<String, Object>>(){@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};
	
	public static String CONTEXT_PATH = "/";
	
	/**
	 * 绑定FilterConfig对象到全局ConcurrentMap<String, Object>集合
	 */
	public static ServletConfig bindServletConfig(ServletConfig servletConfig){
		String key = SERVLET_CONFIG + "." + servletConfig.getServletName();
		Object ret = WEB_OBJECT_LOCAL.get(key);
		if (ret != null) {
			return (ServletConfig) ret;
		}
		ret = servletConfig;
		Object  existing = (ServletConfig) WEB_OBJECT_LOCAL.putIfAbsent(key, ret);
		if (existing != null) {
			ret = existing;
		}
		return (ServletConfig) ret;
	}
	
	/**
	 * 绑定FilterConfig对象到全局ConcurrentMap<String, Object>集合
	 */
	public static FilterConfig bindFilterConfig(FilterConfig filterConfig){
		String key = FILTER_CONFIG + "." + filterConfig.getFilterName();
		Object ret = WEB_OBJECT_LOCAL.get(key);
		if (ret != null) {
			return (FilterConfig) ret;
		}
		ret = filterConfig;
		Object  existing = (ServletConfig) WEB_OBJECT_LOCAL.putIfAbsent(key, ret);
		if (existing != null) {
			ret = existing;
		}
		return (FilterConfig) ret;
	}
	
	
	/**
	 * 绑定ServletContext对象到当前上下文
	 */
	public static void bindServletContext(ServletContext servletContext){
		put(SERVLET_CONTEXT, servletContext);
	}
	
	/**
	 * 绑定ServletRequest对象到当前上下文
	 */
	public static void bindRequest(ServletRequest request){
		try {
			CONTEXT_PATH = WebUtils.toHttp(request).getContextPath();
		} catch (Exception e) {
		}
		put(HTTP_REQUEST, request);
	}
	
	/**
	 * 绑定ServletResponse对象到当前上下文
	 */
	public static void bindResponse(ServletResponse response){
		put(HTTP_RESPONSE, response);
	}
	
	/**
	 * 绑定ThreadPoolExecutor对象到当前上下文
	 */
	public static void bindThreadPoolExecutor(ThreadPoolExecutor executor){
		put(HTTP_ASYNC_POOL_EXECUTOR, executor);
	}
	
	/**
	 * 获取指定名称的FilterConfig
	 */
	public static FilterConfig getFilterConfig(String filterName){
		String key = FILTER_CONFIG + "." + filterName;
		return (FilterConfig) WEB_OBJECT_LOCAL.get(key);
	}
	
	/**
	 * 获取指定名称的ServletConfig
	 */
	public static ServletConfig getServletConfig(String servletName){
		String key = SERVLET_CONFIG + "." + servletName;
		return (ServletConfig) WEB_OBJECT_LOCAL.get(key);
	}
	
	/**
	 * 获取ServletRequest
	 */
	public static ServletRequest getRequest(){
		return (ServletRequest) get(HTTP_REQUEST);
	}
	
	/**
	 * 获取HttpSession
	 */
	public static HttpSession getSession() {
		HttpServletRequest request = (HttpServletRequest) getRequest();
		return request.getSession();
	}
	
	/**
	 * 获取ServletResponse
	 */
	public static ServletResponse getResponse(){
		return (ServletResponse) get(HTTP_RESPONSE);
	}
	
	/**
	 * 获取ServletContext
	 */
	public static ServletContext getServletContext(){
		return (ServletContext) get(SERVLET_CONTEXT);
	}
	
	/**
	 * 获取ThreadPoolExecutor
	 */
	public static ThreadPoolExecutor getThreadPoolExecutor(){
		return (ThreadPoolExecutor) get(HTTP_ASYNC_POOL_EXECUTOR);
	}
	
	public static Properties getRuntimeProperties() {
		return ConfigUtils.getProperties(WebContext.class, "runtime.properties");
	}
	
	public static String getRuntimeProperty(String key) {
		return getRuntimeProperties().getProperty(key); 
	}
	
	public static String getRuntimeProperty(String key, String defaultValue) {
		return getRuntimeProperties().getProperty(key, defaultValue); 
	}
	
	/**
	 * 绑定键值对到到当前上下文的ServletContext对象中
	 */
	public static void setAttribute(String name, Object object){
		ServletContext servletContext = getServletContext();
		if( servletContext != null){
			servletContext.setAttribute(name, object);
		}
	}
	
	/**
	 * 获取ServletContext对象中的属性对象
	 */
	public static Object getAttribute(String name){
		ServletContext servletContext = getServletContext();
		if( servletContext != null){
			return servletContext.getAttribute(name);
		}
		return null;
	}
	
	public static <T> T getAttribute(String name,Class<T> targetClass){
		ServletContext servletContext = getServletContext();
		if( servletContext != null){
			return (T) servletContext.getAttribute(name);
		}
		return null;
	}
	
	/**
	 * 绑定键值对到全局ConcurrentMap<String, Object>集合
	 */
	public static Object setData(String name, Object object){
		Object ret = WEB_OBJECT_LOCAL.get(name);
		if (ret != null) {
			return ret;
		}
		ret = object;
		Object  existing = (ServletConfig) WEB_OBJECT_LOCAL.putIfAbsent(name, ret);
		if (existing != null) {
			ret = existing;
		}
		return ret;
	}
	
	/**
	 * 获取指定名称的对象
	 */
	public static Object getData(String name){
		return WEB_OBJECT_LOCAL.get(name);
	}
	
	
    /**
     * Sets the Locale for the current action.
     *
     * @param locale the Locale for the current action.
     */
    public static void setLocale(Locale locale) {
        put(HTTP_LOCALE, locale);
    }

    /**
     * Gets the Locale of the current action. If no locale was ever specified the platform's
     * {@link java.util.Locale#getDefault() default locale} is used.
     * @return the Locale of the current action.
     */
    public static Locale getLocale() {
        Locale locale = (Locale) get(HTTP_LOCALE);

        if (locale == null) {
            locale = Locale.getDefault();
            setLocale(locale);
        }

        return locale;
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
		return WebRequestUtils.getHostPath((HttpServletRequest)getRequest());
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
		return WebRequestUtils.getLocalHostInfo((HttpServletRequest)getRequest());
	}
    
	/**
	 * 
	 * @description	： 获得请求的客户端信息【ip,port,name】
	 * @author 		： kangzhidong
	 * @date 		：2015-6-9 上午11:00:29
	 * @return
	 */
	public static String[] getRemoteInfo() {
		return WebRequestUtils.getRemoteInfo((HttpServletRequest) getRequest());
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
		return WebRequestUtils.getRemoteAddr((HttpServletRequest)getRequest());
	}
	
	public static Map<String, Object> getContext() {
        return WEB_CONTEXT_LOCAL.get();
    }
	
	/**
     * Returns a value that is stored in the current WebContext by doing a lookup using the value's key.
     * @param key the key used to find the value.
     * @return the value that was found using the key or <tt>null</tt> if the key was not found.
     */
    public static Object get(String key) {
        return WEB_CONTEXT_LOCAL.get().get(key);
    }

    /**
     * Stores a value in the current WebContext. The value can be looked up using the key.
     *
     * @param key   the key of the value.
     * @param value the value to be stored.
     */
    public static void put(String key, Object value) {
    	WEB_CONTEXT_LOCAL.get().put(key, value);
    }

	public static String getRequestPath(HttpServletRequest request) {
        String result   =  null;
        
        //Bugfix Servlet 3.1 spec forward
        if (request.getAttribute(FORWARD_SERVLET_PATH) != null) {
        	result = (String) request.getAttribute(FORWARD_SERVLET_PATH);
        } else {
        	result = request.getServletPath();
        }
        
        String pathInfo = request.getPathInfo();

        if (pathInfo != null) {
        	result = result + pathInfo;
        }
        
        // getServletPath() returns null unless the mapping corresponds to a servlet
        if (result == null) {
            String requestURI = request.getRequestURI();
            if (request.getPathInfo() != null) {
                // strip the pathInfo from the requestURI
                return requestURI.substring(0, requestURI.indexOf(request.getPathInfo()));
            } else {
                return requestURI;
            }
        } else if ("".equals(result)) {
            // in servlet 2.4, if a request is mapped to '/*', getServletPath returns null (SIM-130)
            return request.getPathInfo();
        } else {
            return result;
        }
    }

	/**
	 * Dispatch to the actual path. This method can be overriden to provide different ways of dispatching  (such as cross web-app).
	 */
	public void dispatch(HttpServletRequest request, HttpServletResponse response, String path)
	        throws ServletException, IOException {
	    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(path);
	    if (dispatcher == null) {
	        throw new ServletException("Not found: " + path);
	    }
	    dispatcher.forward(request, response);
	}

	public void destroy() {
		Map<String, Object> map = WEB_CONTEXT_LOCAL.get();
		if(map != null && map.isEmpty()){
			map.clear();
		}
	}
	
}
