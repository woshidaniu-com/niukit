package com.woshidaniu.httpclient.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.httpclient.connection.HttpClientConnectionManagerBuilder;
import com.woshidaniu.httpclient.handler.AbstractResponseHandler;

/**
 * 
 * @className 	： HttpClientUtils
 * @description ： 使用 Apache HttpClient 组件进行get,post方式的请求
 * @author 		： kangzhidong
 * @date 		： 2015-6-24 上午09:18:12
 */
public abstract class HttpsClientUtils extends HttpClientUtils{
	
	/**
     * 忽略任何安全校验的Http连接池
     */
    protected static HttpClientConnectionManager unsafeSSLConnectionManager =  null;
    
    static {
    	if(userManager){
    		unsafeSSLConnectionManager =  HttpClientConnectionManagerBuilder.getInstance().getUnsafeSSLConnectionManager();
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
	public static <T> T httpsRequestWithGet(String baseURL,AbstractResponseHandler<T> handler) throws IOException {
      return httpsRequestWithGet(baseURL, null,handler);
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
	public static <T> T httpsRequestWithGet(String baseURL, Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws  IOException {
		//连接字符编码格式
        String charset = StringUtils.getSafeStr(HttpConfigUtils.getText("http.connection.config-charset"), UTF_8);
		return httpsRequestWithGet(baseURL, paramsMap, charset,handler);
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
	public static <T> T httpsRequestWithGet(String baseURL,Map<String, Object> paramsMap,String charset,AbstractResponseHandler<T> handler) throws  IOException {
		return httpsRequestWithGet(baseURL, paramsMap, charset, null,handler);
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
	public static <T> T httpsRequestWithGet(String baseURL,Map<String, Object> paramsMap, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws  IOException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpsClientUtils.getCloseableHttpClient(unsafeSSLConnectionManager);
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
	public static <T> T httpsRequestWithPost(String baseURL,Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws IOException {
		//连接字符编码格式
        String charset = StringUtils.getSafeStr(HttpConfigUtils.getText("http.connection.config-charset"), UTF_8);
		return httpsRequestWithPost(baseURL, paramsMap, charset,handler);
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
	public static <T> T httpsRequestWithPost(String baseURL,Map<String, Object> paramsMap, String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpsRequestWithPost(baseURL, paramsMap, charset, APPLICATION_FORM_URLENCODED + "; charset=" + charset,handler);
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
    public static <T> T httpsRequestWithPost(String baseURL, Map<String, Object> paramsMap,String charset,String contentType,AbstractResponseHandler<T> handler)  throws IOException {
    	return httpsRequestWithPost(baseURL, paramsMap, charset, contentType, null,handler);
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
    public static <T> T httpsRequestWithPost(String baseURL, Map<String, Object> paramsMap,String charset,String contentType,Map<String, String> headers,AbstractResponseHandler<T> handler)  throws IOException {
    	//定义初始对象
        HttpPost httpRequest =null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpsClientUtils.getCloseableHttpClient(unsafeSSLConnectionManager);
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
	public static <T> T httpsRequestWithPost(String baseURL,String json,AbstractResponseHandler<T> handler) throws IOException {
		return httpsRequestWithPost(baseURL, json, UTF_8 , null,handler);
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
	public static <T> T httpsRequestWithPost(String baseURL,String json,String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpsRequestWithPost(baseURL, json, charset, null,handler);
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
	public static <T> T httpsRequestWithPost(String baseURL,String json, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws IOException {
		//定义初始对象
        HttpPost httpRequest = null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpsClientUtils.getCloseableHttpClient(unsafeSSLConnectionManager);
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
	
	public static void main(String[] args) throws IOException {
		String baseURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxff92398dd532996b&secret=ffa49d7c2ab854d8b5102a4593b85d46&code=0310b9fc35e9a1781a9bba0b7337eb9o&grant_type=authorization_code";
		HttpsClientUtils.httpsRequestWithGet(baseURL, HttpsClientUtils.DEFAULT_CONTENT_HANDLER).getContentText();
		
	}

}
