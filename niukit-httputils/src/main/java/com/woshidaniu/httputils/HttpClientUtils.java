/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.httputils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.httputils.handler.ResponseHandler;

/**
 * 
 * @类名称 : HttpClientUtils.java
 * @类描述 ：
 * @创建人 ：kangzhidong
 * @创建时间 ：2016年4月26日 上午10:51:54
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
public abstract class HttpClientUtils extends HttpRequestUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);
	protected static IdleConnectionTimeoutThread idleThread = new IdleConnectionTimeoutThread();
	protected static HttpConnectionManager httpConnectionManager;
	protected static HttpConnectionConfig configParams = new HttpConnectionConfig();
	
	static {

		try {
			
			httpConnectionManager = new MultiThreadedHttpConnectionManager();
			httpConnectionManager.setParams(configParams);
			//定时清除失效链接
	        idleThread.setConnectionTimeout(configParams.getSoTimeout());
	        idleThread.setTimeoutInterval(configParams.getTimeoutInterval());
	        idleThread.addConnectionManager(httpConnectionManager);
	        idleThread.start();
	        
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
	}

	public static HttpClient getCloseableHttpClient(HttpConnectionManager httpConnectionManager) {
		HttpClient httpclient = null;
		try {
			httpclient = new HttpClient();
			if (httpConnectionManager == null) {
				httpConnectionManager = new SimpleHttpConnectionManager();
			}
			httpclient.setHttpConnectionManager(httpConnectionManager);
			
			// 设置读取超时时间(单位毫秒)
			// httpClient.getParams().setParameter("http.socket.timeout",socket_timeout);
			// 设置连接超时时间(单位毫秒)
			// httpClient.getParams().setParameter("http.connection.timeout",connection_timeout);
			// httpClient.getParams().setParameter("http.connection-manager.timeout",100000000L);
			
			
		} catch (Exception e) {
			LOG.error("Exception", e);
			httpclient = new HttpClient();
		}
		httpclient.getHostConfiguration().getParams().setParameter("http.default-headers", getDefaultHeaders());
		return httpclient;
	}

	public static <T> T httpRequestWithGet(String baseURL,ResponseHandler<T> handler) throws IOException {
		return httpRequestWithGet(baseURL, null, handler);
	}

	public static <T> T httpRequestWithGet(String baseURL,
			Map<String, Object> paramsMap, ResponseHandler<T> handler)
			throws IOException {
		return httpRequestWithGet(baseURL, paramsMap, ContentType.UTF_8, handler);
	}

	public static <T> T httpRequestWithGet(String baseURL,
			Map<String, Object> paramsMap, String charset,
			ResponseHandler<T> handler) throws IOException {
		return httpRequestWithGet(baseURL, paramsMap, charset, null, handler);
	}

	public static <T> T httpRequestWithGet(String baseURL,
			Map<String, Object> paramsMap, String charset,
			Map<String, String> headers, ResponseHandler<T> handler)
			throws IOException {
		// 创建默认的httpClient实例.
		HttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
		// GetMethod对象
		GetMethod httpMethod = null;
		try {
			//对HttpClient进行预处理
			handler.handleClient(httpclient);
			// 创建httpget
			httpMethod = getHttpGet(baseURL, charset, headers);
			 //初始参数集合对象
	    	List<NameValuePair> nameValueList    = HttpURIUtils.buildNameValuePairs(baseURL , paramsMap);
	        NameValuePair[] nameValuePairs = nameValueList.toArray(new NameValuePair[nameValueList.size()]);
	        // 设置参数
	        httpMethod.setQueryString(nameValuePairs);
			// 执行请求
			int statuscode = httpclient.executeMethod(httpMethod);
			// 最终执行的方法，如果没有重定向,则与原始请求对象是同一个对象
			GetMethod lastMethod = HttpRedirectUtils.stripRedirect(httpclient, httpMethod, statuscode, charset, headers, nameValuePairs);
			// 处理最终的响应结果
			return handler.handleResponse(lastMethod);
		} catch (Exception e) {
			handleException(e);
			return null;
		} finally {
			releaseQuietly(httpMethod);
		}
	}

	public static <T> T httpRequestWithPost(String baseURL,
			Map<String, Object> paramsMap, ResponseHandler<T> handler)
			throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, ContentType.UTF_8, handler);
	}

	/**
	 * 进行post方式的请求；Content-Type 为 application/x-www-form-urlencoded
	 */
	public static <T> T httpRequestWithPost(String baseURL,
			Map<String, Object> paramsMap, String charset,
			ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, charset, ContentType.APPLICATION_FORM_URLENCODED + "; charset=" + charset, handler);
	}

	public static <T> T httpRequestWithPost(String baseURL,Map<String, Object> paramsMap, final String charset, String contentType, ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, charset, contentType, null, handler);
	}

	public static <T> T httpRequestWithPost(String baseURL,
			Map<String, Object> paramsMap, String charset, String contentType,
			Map<String, String> headers, ResponseHandler<T> handler)
			throws IOException {
		// 定义初始对象
		PostMethod httpMethod = null;
		// 创建默认的httpClient实例.
		HttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
		try {
			// 对HttpClient进行预处理
			handler.handleClient(httpclient);
			// 得到请求方法
			httpMethod = getHttpPost(baseURL, charset, headers);
			// 设置参数
			setHttpMathod(httpMethod, baseURL, paramsMap, charset, contentType, headers);
			// 执行请求
			int statuscode = httpclient.executeMethod(httpMethod);
			// 最终执行的方法，如果没有重定向,则与原始请求对象是同一个对象
			PostMethod lastMethod = HttpRedirectUtils.stripRedirect(baseURL, httpclient, httpMethod, statuscode, charset, contentType,  headers, paramsMap);
			// 处理最终的响应结果
			return handler.handleResponse(lastMethod);
		} catch (Exception e) {
			handleException(e);
			return null;
		} finally {
			releaseQuietly(httpMethod);
		}
	}

	/**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为
	 *              application/json
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL, String json,ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, ContentType.UTF_8, null, handler);
	}

	/**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为 application/json
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL, String json,String charset, ResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, charset, null, handler);
	}

	/**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为
	 *              application/json
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL, String json,String charset, Map<String, String> headers,ResponseHandler<T> handler) throws IOException {
		// 定义初始对象
		PostMethod httpMethod = null;
		// 创建默认的httpClient实例.
		HttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
		try {
			//对HttpClient进行预处理
			handler.handleClient(httpclient);
			// 如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
			httpMethod = HttpRequestUtils.getHttpRequest(new PostMethod(baseURL), headers);
			// 将JSON进行UTF-8编码,以便传输中文
			String encoderJson = URLEncoder.encode(json != null ? json : "{}", charset);
			// 构建字符串参数对象
			RequestEntity requestEntity = new StringRequestEntity(encoderJson,ContentType.TEXT_JSON, charset );
			// 设置请求头信息
			httpMethod.setRequestHeader(HttpHeaders.CONTENT_ENCODING, charset);
			httpMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON);
			// 设置参数
			httpMethod.setRequestEntity(requestEntity);
			// 执行请求
			int statuscode = httpclient.executeMethod(httpMethod);
			// 最终执行的方法，如果没有重定向,则与原始请求对象是同一个对象
			PostMethod lastMethod = HttpRedirectUtils.stripRedirect(httpclient, httpMethod, statuscode, charset, headers, requestEntity);
			// 处理最终的响应结果
			return handler.handleResponse(lastMethod);
		} catch (Exception e) {
			handleException(e);
			return null;
		} finally {
			releaseQuietly(httpMethod);
		}

	}

	/**
	 * 
	 * @description: 无条件的释放链接
	 * <p>
	 * Example Code:
	 * <pre>
	 * MethodPost httpRequest = null;
	 * try {
	 * 	 httpRequest = new MethodPost(baseURL);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	 HttpClientUtils.releaseQuietly(httpRequest);
	 * }
	 * @author : kangzhidong
	 * @date 上午12:15:37 2015-9-4 
	 * @param httpRequest：要释放链接的  HttpMethodBase子对象, 可能为空或者已经关闭.
	 * @return  void
	 * @throws
	 */
	public static void releaseQuietly(HttpMethodBase httpRequest) {
		// 关闭连接,释放资源
		if (httpRequest != null) {
			try {
				httpRequest.releaseConnection();
				httpRequest = null;
			} catch (Exception ex) {
			}
		}
	}

	public static void handleException(Exception e) throws IOException {
		if (e instanceof SocketTimeoutException) {
			LOG.error("连接超时:" + e.getLocalizedMessage());
		} else if (e instanceof HttpException) {
			LOG.error("读取外部服务器数据失败:" + e.getLocalizedMessage());
		} else if (e instanceof UnknownHostException) {
			LOG.error("请求的主机地址无效:" + e.getLocalizedMessage());
		} else if (e instanceof IOException) {
			LOG.error("向外部接口发送数据失败:" + e.getLocalizedMessage());
		} 
		throw new IOException(e);
	}
 

}
