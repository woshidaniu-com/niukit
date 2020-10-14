package com.woshidaniu.httpclient.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.httpclient.connection.HttpClientConnectionManagerBuilder;
import com.woshidaniu.httpclient.handler.AbstractResponseHandler;

/**
 * 
 * @className 	： HttpsKeyStoreClientUtils
 * @description ： 使用 Apache HttpClient 组件进行get,post方式的请求
 * @author 		： kangzhidong
 * @date 		： 2015-6-24 上午09:18:12
 */
public abstract class HttpsKeyStoreClientUtils extends HttpsClientUtils{

	protected static Logger LOG = LoggerFactory.getLogger(HttpsKeyStoreClientUtils.class);
    
    //初始化配置文件：资源池
	protected static final LoadingCache<KeyStore,HttpClientConnectionManager> cachedConnectionManager;
	
	static {
		
		//CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
		cachedConnectionManager = CacheBuilder.newBuilder()
	        //设置并发级别为8，并发级别是指可以同时写缓存的线程数
	        .concurrencyLevel(8)
	        //设置缓存容器的初始容量为10
	        .initialCapacity(10)
	        //设置写缓存8分钟后自动刷新cache
	        //.refreshAfterWrite(10, TimeUnit.MINUTES) 
	        //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
	        .maximumSize(100)
	        //设置要统计缓存的命中率
	        .recordStats()
	        //设置缓存的移除通知
	        .removalListener(new RemovalListener<KeyStore,HttpClientConnectionManager>() {
	        	
	            @Override
	            public void onRemoval(RemovalNotification<KeyStore,HttpClientConnectionManager> notification) {
	            	LOG.info(notification.getKey().getClass() + " was removed, cause is " + notification.getCause());
	            }

	        })
	        //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
			.build(new CacheLoader<KeyStore,HttpClientConnectionManager>() {
				
				@Override
				public HttpClientConnectionManager load(KeyStore trustStore) throws Exception {
					//安全校验的Http连接池
					return  HttpClientConnectionManagerBuilder.getInstance().getSafeSSLConnectionManager(trustStore);
				}
			});
	}
	
    /**
     * 
     *@描述：创建SSL加密的httpClient实例.优先使用连接池对象，如果连接池对象使用过程出现异常，则使用非安全连接
     *@创建人:kangzhidong
     *@创建时间:Jul 1, 20154:04:47 PM
     *@return
     *@修改人:
     *@修改时间:
     *@修改描述:
     */
    public static CloseableHttpClient getCloseableHttpClient(KeyStore trustStore){
		 
    	try {
			
			//创建忽略任何安全校验的httpClient实例.
			HttpClientBuilder clientBuilder =  HttpClients.custom()
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
			if( userManager){
				//安全校验的Http连接池
				HttpClientConnectionManager connectionManager =  cachedConnectionManager.get(trustStore);
				
				//设置链接管理池
				if(connectionManager != null && userManager){
					clientBuilder.setConnectionManager(connectionManager);
				}
			}
			
			return clientBuilder.build();
		} catch (Exception e) {
			//创建忽略任何安全校验的httpClient实例.
			return HttpClientSSLUtils.getInsecureSSLClient().setDefaultRequestConfig(defaultRequestConfig).setDefaultSocketConfig(defaultSocketConfig).setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE).build();
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
	public <T> T  httpsRequestWithGet(KeyStore trustStore,String baseURL,AbstractResponseHandler<T> handler) throws IOException {
      return httpsRequestWithGet(trustStore,baseURL, null,handler);
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
	public static <T> T  httpsRequestWithGet(KeyStore trustStore,String baseURL, Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws  IOException {
		//连接字符编码格式
        String charset = StringUtils.getSafeStr(HttpConfigUtils.getText("http.connection.config-charset"), UTF_8);
		return httpsRequestWithGet(trustStore,baseURL, paramsMap, charset,handler);
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
	public static <T> T  httpsRequestWithGet(KeyStore trustStore,String baseURL,Map<String, Object> paramsMap, String charset,AbstractResponseHandler<T> handler) throws  IOException {
		return httpsRequestWithGet(trustStore,baseURL, paramsMap, charset, null,handler);
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
	public static <T> T  httpsRequestWithGet(KeyStore trustStore,String baseURL,Map<String, Object> paramsMap, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws  IOException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpsKeyStoreClientUtils.getCloseableHttpClient(trustStore);
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
	public static <T> T  httpsRequestWithPost(KeyStore trustStore,String baseURL,Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws IOException {
		//连接字符编码格式
        String charset = StringUtils.getSafeStr(HttpConfigUtils.getText("http.connection.config-charset"), UTF_8);
		return httpsRequestWithPost(trustStore,baseURL, paramsMap, charset,handler);
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
	public static <T> T  httpsRequestWithPost(KeyStore trustStore,String baseURL,Map<String, Object> paramsMap, String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpsRequestWithPost(trustStore,baseURL, paramsMap, charset, APPLICATION_FORM_URLENCODED + "; charset=" + charset,handler);
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
    public static <T> T  httpsRequestWithPost(KeyStore trustStore,String baseURL, Map<String, Object> paramsMap,String charset,String contentType,AbstractResponseHandler<T> handler)  throws IOException {
    	return httpsRequestWithPost(trustStore,baseURL, paramsMap, charset, contentType, null,handler);
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
    public static <T> T  httpsRequestWithPost(KeyStore trustStore,String baseURL, Map<String, Object> paramsMap,String charset,String contentType,Map<String, String> headers,AbstractResponseHandler<T> handler)  throws IOException {
    	//定义初始对象
        HttpPost httpRequest =null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpsKeyStoreClientUtils.getCloseableHttpClient(trustStore);
		try {
			httpRequest = getHttpPost(baseURL,charset,headers);
			//判断请求头信息
	        if (contentType != null) {
	            httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
	        }else{
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
	public static <T> T  httpsRequestWithPost(KeyStore trustStore,String baseURL,String json,AbstractResponseHandler<T> handler) throws IOException {
		return httpsRequestWithPost(trustStore,baseURL, json, UTF_8, null,handler);
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
	public static <T> T  httpsRequestWithPost(KeyStore trustStore,String baseURL,String json,String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpsRequestWithPost(trustStore,baseURL, json, charset, null,handler);
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
	public static <T> T  httpsRequestWithPost(KeyStore trustStore,String baseURL,String json, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws IOException {
		//定义初始对象
        HttpPost httpRequest = null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpsKeyStoreClientUtils.getCloseableHttpClient(trustStore);
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
	

}
