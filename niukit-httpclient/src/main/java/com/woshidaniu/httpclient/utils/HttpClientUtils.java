package com.woshidaniu.httpclient.utils;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.httpclient.HttpClientMetricNameStrategies;
import com.codahale.metrics.httpclient.InstrumentedHttpRequestExecutor;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.httpclient.ResponseContent;
import com.woshidaniu.httpclient.connection.HttpClientConnectionManagerBuilder;
import com.woshidaniu.httpclient.handler.AbstractResponseHandler;
import com.woshidaniu.httpclient.handler.BinaryResponseHandler;
import com.woshidaniu.httpclient.handler.ConnectionKeepAliveStrategyHandler;
import com.woshidaniu.httpclient.handler.HttpRequestExceptionRetryHandler;
import com.woshidaniu.httpclient.handler.MultipleResponseHandler;
import com.woshidaniu.httpclient.handler.PlainTextResponseHandler;
import com.woshidaniu.httpclient.handler.StreamResponseHandler;
import com.woshidaniu.httpclient.interceptor.HttpRequestGzipInterceptor;
import com.woshidaniu.httpclient.interceptor.HttpRequestHeaderInterceptor;
import com.woshidaniu.httpclient.interceptor.HttpRequestSummaryInterceptor;
import com.woshidaniu.httpclient.interceptor.HttpResponseGzipInterceptor;

/**
 * 
 * @className 	： HttpClientUtils
 * @description ： 使用 Apache HttpClient 组件进行get,post方式的请求
 * @author 		： kangzhidong
 * @date 		： 2015-6-24 上午09:18:12
 */
public abstract class HttpClientUtils extends HttpRequestUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);
	
    /**
     * 处理响应结果为字符串对象结果工具类
     */
    public static final AbstractResponseHandler<String> DEFAULT_TEXT_HANDLER= new PlainTextResponseHandler(null,UTF_8);
    /**
     * 处理响应结果为byte[]对象结果工具类
     */
    public static final AbstractResponseHandler<byte[]> DEFAULT_BINARY_HANDLER = new BinaryResponseHandler(null,UTF_8);
    /**
     * 处理响应结果为ByteArrayInputStream对象结果工具类
     */
    public static final AbstractResponseHandler<ByteArrayInputStream> DEFAULT_STESAM_HANDLER = new StreamResponseHandler(null,UTF_8);
    /**
     * 处理响应结果为ResponseContent对象结果工具类
     */
    public static final AbstractResponseHandler<ResponseContent> DEFAULT_CONTENT_HANDLER = new MultipleResponseHandler(null,UTF_8);
    
    /**
     * 普通http请求的HttpClient连接池
     */
    protected static HttpClientConnectionManager httpConnectionManager =  null;

	protected static ConnectionKeepAliveStrategy keepAliveStrat = new ConnectionKeepAliveStrategyHandler();
    
    protected static HttpRequestRetryHandler requestRetryHandler = new HttpRequestExceptionRetryHandler(); 
    
    protected static HttpRequestInterceptor summaryInterceptor = new HttpRequestSummaryInterceptor();
    
    protected static HttpRequestInterceptor headerInterceptor = new HttpRequestHeaderInterceptor();
    
    protected static HttpRequestInterceptor gzipInterceptor = new HttpRequestGzipInterceptor();
    
    protected static HttpResponseInterceptor ungzipInterceptor = new HttpResponseGzipInterceptor();
    
    // 创建默认Socket参数
    protected static SocketConfig defaultSocketConfig = null;
    //获取是否启用连接池的标记
    protected static  boolean userManager = false;
	//设置httpclient是否使用NoDelay策略;默认 true
	private static boolean tcpNoDelay = true;
	//连接读取数据超时时间；单位毫秒，默认5000
	private static int so_timeout = 5000;
	
    protected static String registryName = null;
    protected static MetricRegistry metricRegistry = null;
	
    static{
    	
    	try {
    		
			userManager = StringUtils.getSafeBoolean(HttpConfigUtils.getText("http.connection.manager"), "false");
			
			if(userManager){
				httpConnectionManager =  HttpClientConnectionManagerBuilder.getInstance().getHttpConnectionManager();
			}
			
			tcpNoDelay = StringUtils.getSafeBoolean(HttpConfigUtils.getText("http.socket.tcpNoDelay"), "1");
			so_timeout = StringUtils.getSafeInt(HttpConfigUtils.getText("http.socket.so_timeout"), "2000");
			
			registryName = HttpConfigUtils.getText("http.metrics.registry.name");
	        if(!StringUtils.isEmpty(registryName)){
	        	metricRegistry = SharedMetricRegistries.getOrCreate(registryName);
	        } else{
	        	metricRegistry = new MetricRegistry();
	        }
	        
			// 创建默认Socket参数
			defaultSocketConfig = SocketConfig.custom()
			    //nagle算法默认是打开的，会引起delay的问题；所以要手工关掉
			    .setTcpNoDelay(tcpNoDelay)
			    //设置读数据超时时间(单位毫秒) 
			    .setSoTimeout(so_timeout).build();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
		
		
    /**
     * 
     *@描述：创建默认的httpClient实例.优先使用连接池对象，如果连接池对象使用过程出现异常，则使用非安全连接
     *@创建人:kangzhidong
     *@创建时间:Jul 1, 20154:04:47 PM
     *@return
     *@修改人:
     *@修改时间:
     *@修改描述:
     */
    public static CloseableHttpClient getCloseableHttpClient(HttpClientConnectionManager connectionManager){
		
		try {
			
			//创建忽略任何安全校验的httpClient实例.
			HttpClientBuilder clientBuilder =  HttpClients.custom()
									//添加度量监控
									.setRequestExecutor(new InstrumentedHttpRequestExecutor(metricRegistry, HttpClientMetricNameStrategies.METHOD_ONLY))
									.setKeepAliveStrategy(keepAliveStrat)
									.setRetryHandler(requestRetryHandler)
									//设置相关的压缩文件标识，在请求头的信息中  
									.addInterceptorFirst(headerInterceptor)
									.addInterceptorFirst(gzipInterceptor)
									.addInterceptorLast(summaryInterceptor)
									//设置相应相应的拦截器，用于处理接收到的拦截的压缩信息
									.addInterceptorLast(ungzipInterceptor);
			
			/**--------------以下设置为客户端级别，作为所有请求的默认值：-------------------------------------------------------------------------*/
	        
			clientBuilder.setDefaultRequestConfig(defaultRequestConfig).setDefaultSocketConfig(defaultSocketConfig);
			
			//设置链接管理池
			if(connectionManager != null && userManager){
				clientBuilder.setConnectionManager(connectionManager);
			}
			return clientBuilder.build();
		} catch (Exception e) {
			return HttpClients.createDefault();
		}
	}
	
	
	 /**
	  * 
	  * @description	： 使用Apache HttpClient 组件使用Get方式的访问URL
	  * @author 		： kangzhidong
	  * @date 			：Jun 30, 2015 6:20:19 PM
	  * @param baseURL
	  * @return
	  * @throws IOException
	  */
	public static <T> T httpRequestWithGet(String baseURL,AbstractResponseHandler<T> handler) throws IOException {
      return httpRequestWithGet(baseURL, null , handler);
	}
	

	/**
	 * 
	 * @description ：使用Apache HttpClient 组件使用get方式的访问URL
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:23
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithGet(String baseURL, Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws  IOException {
		//连接字符编码格式
        String charset = StringUtils.getSafeStr(HttpConfigUtils.getText("http.connection.config-charset"), UTF_8);
		return httpRequestWithGet(baseURL, paramsMap, charset , handler);
	}
	
	/**
	 * 
	 * @description ：使用apache HttpClient 组件进行get方式的请求
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:23
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static <T> T httpRequestWithGet(String baseURL,Map<String, Object> paramsMap, String charset,AbstractResponseHandler<T> handler) throws  IOException {
		return httpRequestWithGet(baseURL, paramsMap, charset, null , handler);
	}
	
	/**
	 * 
	 * @description ：使用apache HttpClient 组件进行get方式的请求
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:23
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static <T> T httpRequestWithGet(String baseURL,Map<String, Object> paramsMap, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws  IOException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
        //Httpget对象
        HttpGet httpRequest = null;
		try {
			//创建httpget
	        httpRequest = getHttpGet(baseURL, paramsMap, charset , headers);
	        //判断回话上下文对象是否为空
	        if(BlankUtils.isBlank(handler.getContext())){
	        	//创建回话上下文对象
	        	handler.setContext(HttpClientContext.create());
	        }
	        //执行请求
			return httpclient.execute(httpRequest, handler , handler.getContext());
		} finally {
			//没有使用连接池情况下，需要手动释放链接和释放资源
			if (!userManager ){
				releaseQuietly(httpRequest);
				closeQuietly(httpclient);
			}
			/* // When HttpClient instance is no longer needed,
		      // shut down the connection manager to ensure
		      // immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();*/
		}
	}
	
	/**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求;且对参数使用UTF-8编码
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws IOException {
		//连接字符编码格式
        String charset = StringUtils.getSafeStr(HttpConfigUtils.getText("http.connection.config-charset"), UTF_8);
		return httpRequestWithPost(baseURL, paramsMap, charset, handler);
	}
	
	/**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/x-www-form-urlencoded
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,Map<String, Object> paramsMap, String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, charset, APPLICATION_FORM_URLENCODED + "; charset=" + charset, handler);
	}
	
	 /**
     * 
     * @description	： 使用apache HttpClient 组件进行post方式的请求;可上传文件对象
     * @author 		： kangzhidong
     * @date 		：Jun 30, 2015 7:14:54 PM
     * @param baseURL
     * @param paramsMap
     * @param charset
     * @param bodyType
     * @param contentType
     * @return
     * @throws IOException
     */
    public static <T> T httpRequestWithPost(String baseURL, Map<String, Object> paramsMap,final String charset,String contentType,AbstractResponseHandler<T> handler)  throws IOException {
    	return httpRequestWithPost(baseURL, paramsMap, charset, contentType, null, handler);
    }
    
    /**
     * 
     * @description	： 使用apache HttpClient 组件进行post方式的请求;可上传文件对象
     * @author 		： kangzhidong
     * @date 		：Jun 30, 2015 7:14:54 PM
     * @param baseURL
     * @param paramsMap
     * @param charset
     * @param bodyType
     * @param contentType
     * @return
     * @throws IOException
     */
    public static <T> T httpRequestWithPost(String baseURL, Map<String, Object> paramsMap,String charset,String contentType,Map<String, String> headers,AbstractResponseHandler<T> handler)  throws IOException {
    	//定义初始对象
        HttpPost httpRequest =null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
		try {
			httpRequest = getHttpPost(baseURL,charset,headers);
			//判断请求头信息
	        if (contentType != null) {
	            httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
	        }else{
	        	//由于下面使用的是new StringEntity(....),所以默认发出去的请求报文头中CONTENT_TYPE值为text/plain; charset=ISO-8859-1  
	            //这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息  
	        	httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED + ";charset=" + charset);
	        }
	        //有实体对象参数，表示可能有文件上传
        	if(HttpRequestUtils.isMultipart(paramsMap)){
        		//有文件上传
                httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA + ";charset=" + charset);
        	}
	        //得到请求实体对象
	        HttpEntity httpEntity = HttpRequestUtils.getHttpEntity(baseURL, paramsMap, charset);
        	//根据响应头判断是否启用gzip压缩
            boolean isGzip = HttpRequestUtils.isGzip(httpRequest);
            //设置请求实体参数对象
            if(isGzip){
            	httpRequest.setEntity(new GzipCompressingEntity(httpEntity));
            }else{
            	httpRequest.setEntity(httpEntity);
            }
        	//判断回话上下文对象是否为空
	        if(BlankUtils.isBlank(handler.getContext())){
	        	//创建回话上下文对象
	        	handler.setContext(HttpClientContext.create());
	        }
	        //执行请求
			return httpclient.execute(httpRequest, handler , handler.getContext());
		} finally {
			//没有使用连接池情况下，需要手动释放链接和释放资源
			if (!userManager ){
				releaseQuietly(httpRequest);
				closeQuietly(httpclient);
			}
		}
	}

    
    /**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/json
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,String json,AbstractResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, UTF_8 , null ,handler);
	}
	
    /**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/json
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,String json,String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, charset, null ,handler);
	}
	
    /**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/json
	 * @author ： kangzhidong
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,String json, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws IOException {
		//定义初始对象
        HttpPost httpRequest = null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
		try {
			//如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
			httpRequest = HttpRequestUtils.getHttpRequest(new HttpPost(baseURL), headers);
			// 将JSON进行UTF-8编码,以便传输中文
	        String encoderJson = URLEncoder.encode(json != null ? json : "{}" ,charset);
	        //构建字符串参数对象
	        StringEntity entity = new StringEntity(encoderJson);
	        entity.setContentType(TEXT_JSON);
	        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
	        //设置请求头信息
	        httpRequest.setHeader(HttpHeaders.CONTENT_ENCODING , charset);
            httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON );
            //根据响应头判断是否启用gzip压缩
            boolean isGzip = HttpRequestUtils.isGzip(httpRequest);
	        //设置请求实体参数对象
            if(isGzip){
            	httpRequest.setEntity(new GzipCompressingEntity(entity));
            }else{
            	 httpRequest.setEntity(entity);
            }
            //判断回话上下文对象是否为空
	        if(BlankUtils.isBlank(handler.getContext())){
	        	//创建回话上下文对象
	        	handler.setContext(HttpClientContext.create());
	        }
	        //执行请求
			return httpclient.execute(httpRequest, handler , handler.getContext());
		} finally {
			//没有使用连接池情况下，需要手动释放链接和释放资源
			if (!userManager ){
				releaseQuietly(httpRequest);
				closeQuietly(httpclient);
			}
		}
		
	}
	
	/**
	 * 
	 * @description: 无条件的释放链接
	 * <p>
	 * Example Code:
	 * <pre>
	 * HttpPost httpRequest = null;
	 * try {
	 * 	 httpRequest = new HttpPost(baseURL);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	 HttpClientUtils.releaseQuietly(httpRequest);
	 * }
	 * @author : kangzhidong
	 * @date 上午12:15:37 2015-9-4 
	 * @param httpRequest：要释放链接的  HttpRequestBase子对象, 可能为空或者已经关闭.
	 * @return  void 
	 * @throws  
	 */
	public static void releaseQuietly(HttpRequestBase httpRequest) {
		//关闭连接,释放资源
        if (httpRequest != null){
        	try {
        		httpRequest.releaseConnection();
        		httpRequest = null;
        	} catch (Exception ex) {
			}
        }
	}
	
	/**
	 * 
	 * @description: 无条件关闭HttpResponse。
	 *<p>
	 * Example Code:
	 * 
	 * <pre>
	 * HttpResponse httpResponse = null;
	 * try {
	 * 	httpResponse = httpClient.execute(httpGet);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	HttpClientUtils.closeQuietly(httpResponse);
	 * }
	 * </pre>
	 * @author : kangzhidong
	 * @date 上午12:18:50 2015-9-4 
	 * @param response：要释放资源的  HttpResponse对象, 可能为空或者已经关闭.
	 * @return  void 
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void closeQuietly(final HttpResponse response) {
		if (response != null) {
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (final IOException ex) {
				}
			}
		}
	}

	/**
	 * 
	 * @description:  无条件关闭HttpResponse。
	 * <p>
	 * Example Code:
	 * 
	 * <pre>
	 * HttpResponse httpResponse = null;
	 * try {
	 * 	httpResponse = httpClient.execute(httpGet);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	HttpClientUtils.closeQuietly(httpResponse);
	 * }
	 * </pre>
	 * @author : kangzhidong
	 * @date 上午12:17:49 2015-9-4 
	 * @param response：要释放资源的  HttpResponse对象, 可能为空或者已经关闭.
	 * @return  void 
	 * @throws  
	 */
	public static void closeQuietly(CloseableHttpResponse response) {
		if (response != null) {
			try {
				try {
					EntityUtils.consume(response.getEntity());
				} finally {
					response.close();
					response = null;
				}
			} catch (final IOException ignore) {
			}
		}
	}

	/**
	 * 
	 * @description: 无条件关闭httpClient。关闭底层的连接管理器，释放资源。
	 * <p>
	 * Example Code:
	 * 
	 * <pre>
	 * HttpClient httpClient = HttpClients.createDefault();
	 * try {
	 * 	httpClient.execute(request);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	HttpClientUtils.closeQuietly(httpClient);
	 * }
	 * </pre>
	 * @author : kangzhidong
	 * @date 上午12:16:16 2015-9-4 
	 * @param httpClient ：要关闭的  HttpClient对象, 可能为空或者已经关闭.
	 * @return  void 返回类型
	 * @throws  
	 */
	public static void closeQuietly(HttpClient httpClient) {
		if (httpClient != null) {
			if (httpClient instanceof Closeable) {
				try {
					((Closeable) httpClient).close();
					httpClient = null;
				} catch (final IOException ignore) {
				}
			}
		}
	}

}
