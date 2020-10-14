package com.woshidaniu.web.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @类名称 : WebRequestUtils.java
 * @类描述 ：Web请求处理工具
 * @创建人 ：kangzhidong
 * @创建时间 ：Mar 8, 2016 9:20:23 AM
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
public abstract class WebRequestUtils {

	protected final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected final static Logger LOG = LoggerFactory.getLogger(WebRequestUtils.class);
	/**
	 * Constant for HTTP POST method.
	 */
	public static final String POST_METHOD = "POST";

	/**
	 * HTTP content type header name.
	 */
	public static final String CONTENT_TYPE = "Content-type";

	/**
	 * HTTP content disposition header name.
	 */
	public static final String CONTENT_DISPOSITION = "Content-disposition";

	/**
	 * HTTP content length header name.
	 */
	public static final String CONTENT_LENGTH = "Content-length";

	/**
	 * Content-disposition value for form data.
	 */
	public static final String FORM_DATA = "form-data";

	/**
	 * Content-disposition value for file attachment.
	 */
	public static final String ATTACHMENT = "attachment";

	/**
	 * Part of HTTP content type header.
	 */
	public static final String MULTIPART = "multipart/";

	/**
	 * HTTP content type header for multipart forms.
	 */
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";

	/**
	 * HTTP content type header for multiple uploads.
	 */
	public static final String MULTIPART_MIXED = "multipart/mixed";

	/**
	 * Retrieves the current request servlet path. Deals with differences
	 * between servlet specs (2.2 vs 2.3+)
	 *
	 * @param request
	 *            the request
	 * @return the servlet path
	 */
	public static String getServletPath(HttpServletRequest request) {
		String servletPath = request.getServletPath();

		String requestUri = request.getRequestURI();
		// Detecting other characters that the servlet container cut off (like
		// anything after ';')
		if (requestUri != null && servletPath != null && !requestUri.endsWith(servletPath)) {
			int pos = requestUri.indexOf(servletPath);
			if (pos > -1) {
				servletPath = requestUri.substring(requestUri.indexOf(servletPath));
			}
		}

		if (null != servletPath && !"".equals(servletPath)) {
			return servletPath;
		}

		int startIndex = request.getContextPath().equals("") ? 0 : request.getContextPath().length();
		int endIndex = request.getPathInfo() == null ? requestUri.length()
				: requestUri.lastIndexOf(request.getPathInfo());

		if (startIndex > endIndex) { // this should not happen
			endIndex = startIndex;
		}

		return requestUri.substring(startIndex, endIndex);
	}

	public static String getURI(HttpServletRequest request) {
		String uri = getServletPath(request);
		if (uri != null && !"".equals(uri)) {
			return uri;
		}

		uri = request.getRequestURI();
		return uri.substring(request.getContextPath().length());
	}

	/**
	 * 
	 * @description ： 判断请求是否是Ajax 请求
	 * @author ： kangzhidong
	 * @date ：Jan 28, 2016 12:09:17 PM
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		// 判断是否ajax请求
		if (request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}

	public static final boolean isMultipartRequest(HttpServletRequest request) {
		if (!POST_METHOD.equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		String contentType = request.getContentType();
		if (contentType == null) {
			return false;
		}
		if (contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @description ： 取得带相同前缀的Request Parameters.返回的结果的Parameter名已去除前缀.
	 * @author ： kangzhidong
	 * @date ：Jan 28, 2016 12:09:08 PM
	 * @param request
	 * @param prefix
	 * @return
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	public static Map<String, Object> getObjectParameters(ServletRequest request, String... filters) {
		Assert.notNull(request, "Request must not be null");
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> requestMap = new TreeMap<String, Object>();
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] values = request.getParameterValues(paramName);
			if (values == null || values.length == 0) {
				// Do nothing, no values found at all.
			} else if (values.length > 1) {
				requestMap.put(paramName, values);
			} else {
				requestMap.put(paramName, values[0]);
			}
		}
		// 过滤不需要的参数
		if (!BlankUtils.isBlank(filters)) {
			for (String string : filters) {
				requestMap.remove(string);
			}
		}
		return requestMap;
	}

	public static Map<String, String> getParameters(ServletRequest request, String... filters) {
		Assert.notNull(request, "Request must not be null");
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, String> requestMap = new TreeMap<String, String>();
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] values = request.getParameterValues(paramName);
			if (values == null || values.length == 0) {
				// Do nothing, no values found at all.
			} else if (values.length > 1) {
				requestMap.put(paramName, StringUtils.join(values, ","));
			} else {
				requestMap.put(paramName, values[0]);
			}
		}
		// 过滤不需要的参数
		if (!BlankUtils.isBlank(filters)) {
			for (String string : filters) {
				requestMap.remove(string);
			}
		}
		return requestMap;
	}

	public static Map<String, Object> getAttributesStartingWith(ServletRequest request, String prefix,
			String... filters) {
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		Enumeration<?> enu = request.getAttributeNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			if (prefix != null && ("".equals(prefix) || key.startsWith(prefix))) {
				attributeMap.put(key, request.getAttribute(key));
			} else {
				attributeMap.put(key, request.getAttribute(key));
			}
		}
		// 过滤不需要的参数
		if (!BlankUtils.isBlank(filters)) {
			for (String string : filters) {
				attributeMap.remove(string);
			}
		}
		return attributeMap;
	}

	public static Map<String, Object> getAttributes(ServletRequest request, String... filters) {
		return getAttributesStartingWith(request, null, filters);
	}

	public static Map<String, Object> getAttributesStartingWith(HttpSession session, String prefix, String... filters) {
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		Enumeration<?> enu = session.getAttributeNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			if (prefix != null && ("".equals(prefix) || key.startsWith(prefix))) {
				attributeMap.put(key, session.getAttribute(key));
			} else {
				attributeMap.put(key, session.getAttribute(key));
			}
		}
		// 过滤不需要的参数
		if (!BlankUtils.isBlank(filters)) {
			for (String string : filters) {
				attributeMap.remove(string);
			}
		}
		return attributeMap;
	}

	public static Map<String, Object> getAttributes(HttpSession session, String... filters) {
		return getAttributesStartingWith(session, null, filters);
	}

	public static Map<String, Object> getAttributesStartingWith(ServletContext servletContext, String prefix,
			String... filters) {
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		Enumeration<?> enu = servletContext.getAttributeNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			if (prefix != null && ("".equals(prefix) || key.startsWith(prefix))) {
				attributeMap.put(key, servletContext.getAttribute(key));
			} else {
				attributeMap.put(key, servletContext.getAttribute(key));
			}
		}
		// 过滤不需要的参数
		if (!BlankUtils.isBlank(filters)) {
			for (String string : filters) {
				attributeMap.remove(string);
			}
		}
		return attributeMap;
	}

	public static Map<String, Object> getAttributes(ServletContext servletContext, String... filters) {
		return getAttributesStartingWith(servletContext, null, filters);
	}

	public static <M> M getParameters(ServletRequest request, Class<M> clazz) {
		M object = null;
		try {
			// 获取类属性
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			object = clazz.newInstance();// 创建JavaBean对象
			Enumeration<?> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String parameterName = (String) parameterNames.nextElement();
				// 给JavaBean对象的属性赋值
				for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
					String propertyName = descriptor.getName();
					try {
						if (parameterName.equals(propertyName)) {
							Class<?> fieldType = descriptor.getPropertyType();
							String fieldValue = request.getParameter(propertyName);
							Method writeMethod = descriptor.getWriteMethod();
							LOG.info("fieldType:" + fieldType + "fieldValue:" + fieldValue);
							if (fieldType.equals(Date.class)) {
								writeMethod.invoke(object, parameterName, sdf.parse(fieldValue));
							} else {
								writeMethod.invoke(object, parameterName, fieldValue);
							}
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(), e.getCause());
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.getCause());
		}
		return object;
	}

	/**
	 * 
	 * @description ：得到主机 应用 http请求路径： 如：http://127.0.0.1:8080/webapp_name
	 * @author ： kangzhidong
	 * @date ：2015-6-9 上午10:51:07
	 * @return
	 */
	public static String getHostPath(HttpServletRequest request) {
		try {
			String[] hostInfo = getLocalHostInfo(request);
			return "http://" + hostInfo[0] + ":" + hostInfo[0] + request.getServletContext().getContextPath();
		} catch (UnknownHostException e1) {
			return null;
		}
	}

	/**
	 * 
	 * @description ：获得本地主机信息【本机IP,端口,本机名称】
	 * @author ： kangzhidong
	 * @date ：2015-6-9 上午10:48:43
	 * @return
	 * @throws UnknownHostException
	 */
	public static String[] getLocalHostInfo(HttpServletRequest request) throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		if (request == null) {
			return new String[] { "", "", "" };
		}
		return new String[] { inet.getHostAddress().toString(), request.getLocalPort() + "",
				inet.getHostName().toString() };
	}

	/**
	 * 
	 * @description ： 获得请求的客户端信息【ip,port,name】
	 * @author ： kangzhidong
	 * @date ：2015-6-9 上午10:44:28
	 * @param request
	 * @return
	 */
	public static String[] getRemoteInfo(HttpServletRequest request) {
		if (request == null) {
			return new String[] { "", "", "" };
		}
		return new String[] { getRemoteAddr(request), request.getRemotePort() + "", request.getRemoteHost() };
	}

	/**
	 * 
	 * @description ：获取请求客户端IP地址，支持代理服务器
	 * @author ： kangzhidong
	 * @date ：2015-6-9 上午10:56:30
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		return RemoteAddrUtils.getRemoteAddr(request);
	}

	/**
	 * 
	 * @description ： 解析url获得操作代码
	 * @author ： kangzhidong
	 * @date ：2015-6-9 上午10:56:46
	 * @param path
	 * @param suffix
	 * @return
	 */
	public static String getOptCode(String path, String suffix) {
		String method = path.substring(path.indexOf("_") + 1, path.indexOf(suffix) - 1);
		String czdm = null;
		if (method != null) {
			for (int i = 0; i < method.length(); i++) {
				char c = method.charAt(i);
				// 大写
				if (!(c >= 97 && c <= 122)) {
					czdm = method.substring(0, i);
					break;
				}
			}
			if (method.length() > 0 && czdm == null) {
				czdm = method;
			}
		}
		return czdm;
	}

}
