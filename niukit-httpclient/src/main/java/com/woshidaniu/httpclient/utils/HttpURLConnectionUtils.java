package com.woshidaniu.httpclient.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 *@类名称	: HttpURLConnectionUtils.java
 *@类描述	：使用java内置HttpURLConnection对象进行get,post方式的请求
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:12:46 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpURLConnectionUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpURLConnectionUtils.class);
	
	//连接超时 单位毫秒
	public static int connectTimeout = 10000;
	//读取超时 单位毫秒
	public static int readTimeout = 3000;
	
	public static String buildURL(String baseURL,Map<String,Object> paramsMap){
		if(paramsMap == null){
			return baseURL;
		}
		StringBuilder builder = new StringBuilder(baseURL);
		for (String key : paramsMap.keySet()) {
			builder.append(builder.indexOf("?") > 0 ? "&" : "?").append(key).append("=").append(String.valueOf(paramsMap.get(key)));
		}
		return builder.toString();
	}
	
	public static HttpURLConnection getHttpURLConnectionWithGet(String baseURL,Map<String,Object> paramsMap,String charset) throws IOException{
		URL urlGet = new URL(buildURL(baseURL,paramsMap));
		HttpURLConnection httpConn = (HttpURLConnection) urlGet.openConnection();
		// 必须是get方式请求
		httpConn.setRequestMethod("GET");// 提交模式 
		/* 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		 * 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
		 */		
		httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+ charset);
		httpConn.setConnectTimeout(connectTimeout);//连接超时 单位毫秒
		httpConn.setReadTimeout(readTimeout);//读取超时 单位毫秒
		httpConn.setDoInput(true);
		httpConn.setDoOutput(true);// 是否输入参数
		
		httpConn.setUseCaches(false);  
	    httpConn.setInstanceFollowRedirects(true); 
	    httpConn.connect();  
	    
		return httpConn;
	}
	
	public static String getResultWithGet(String baseURL,Map<String,Object> paramsMap,String charset) throws IOException{
		 HttpURLConnection httpConn = getHttpURLConnectionWithGet(baseURL, paramsMap, charset);
		 InputStream inStream = null;
		 InputStreamReader inputStreamReader = null;
		 // 定义BufferedReader输入流来读取URL的ResponseData  
		 BufferedReader bufferedReader = null;
		 try {
			 // 获取输入流
			 inStream = httpConn.getInputStream(); 
			 inputStreamReader = new InputStreamReader(inStream, charset);
			 //接收返回请求
			 bufferedReader = new BufferedReader(inputStreamReader);
			 String line = "";
			 StringBuffer buffer =new StringBuffer();
			 while((line= bufferedReader.readLine())!=null){
				 buffer.append(line);
			 }
			 return buffer.toString();
		} finally{
			IOUtils.closeQuietly(inStream);
			IOUtils.closeQuietly(inputStreamReader);
			IOUtils.closeQuietly(bufferedReader);
			httpConn.disconnect();
		}
	}
	
	public static HttpURLConnection getHttpURLConnectionWithPost(String baseURL,Map<String,Object> paramsMap,String charset) throws IOException{
		URL urlPost = new URL(baseURL);
		HttpURLConnection httpConn = (HttpURLConnection) urlPost.openConnection();
		httpConn.setRequestMethod("POST");// 提交模式 
		/* 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		 * 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
		 */	
		httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+ charset);
		/*
		HttpURLConnection是基于HTTP协议的，其底层通过socket通信实现。如果不设置超时（timeout），在网络异常的情况下，可能会导致程序僵死而不继续往下执行。可以通过以下两个语句来设置相应的超时：
		System.setProperty("sun.net.client.defaultConnectTimeout", 超时毫秒数字符串);
		System.setProperty("sun.net.client.defaultReadTimeout", 超时毫秒数字符串);
		其中： sun.net.client.defaultConnectTimeout：连接主机的超时时间（单位：毫秒）
		sun.net.client.defaultReadTimeout：从主机读取数据的超时时间（单位：毫秒）
		例如：
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
		JDK 1.5以前的版本，只能通过设置这两个系统属性来控制网络超时。在1.5中，还可以使用HttpURLConnection的父类URLConnection的以下两个方法：
		setConnectTimeout：设置连接主机超时（单位：毫秒）
		setReadTimeout：设置从主机读取数据超时（单位：毫秒）
		*/
		System.setProperty("sun.net.client.defaultConnectTimeout", connectTimeout+"");
		System.setProperty("sun.net.client.defaultReadTimeout", readTimeout+""); 
		httpConn.setConnectTimeout(connectTimeout);
		httpConn.setReadTimeout(readTimeout);
		// 发送POST请求必须设置如下两行
		httpConn.setDoInput(true);
		//  设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true, 默认情况下是false;  
		httpConn.setDoOutput( true ); 
		if(paramsMap != null){
			StringBuilder params = new StringBuilder(baseURL);
			for (String key : paramsMap.keySet()) {
				// 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
				params.append((params.indexOf("?") > 0 || baseURL.indexOf("?") > 0) ? "&" : "?").append(key).append("=").append(URLEncoder.encode(String.valueOf(paramsMap.get(key)), charset));
			}
	        httpConn.setRequestProperty("Content-Length", String.valueOf(params.length()));
	        // 连接，从urlPost.openConnection()至此的配置必须要在connect之前完成，
	        // 要注意的是connection.getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，  所以在开发中不调用上述的connect()也可以)。 
	        httpConn.connect();
		    
		    DataOutputStream outStream = new DataOutputStream(httpConn.getOutputStream());
		    // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
		    outStream.writeBytes(params.toString());
		    // flush输出流的缓冲  
		    outStream.flush();
		    outStream.close(); 
		}
		return httpConn;
	}
	
	public static String getResultWithPost(String baseURL,Map<String,Object> paramsMap,String charset) throws IOException{
		HttpURLConnection httpConn = getHttpURLConnectionWithPost(baseURL, paramsMap, charset);
		InputStream inStream = null;
		InputStreamReader inputStreamReader = null;
		// 定义BufferedReader输入流来读取URL的ResponseData  
		BufferedReader bufferedReader = null;
		try {
			// 根据ResponseCode判断连接是否成功  
			int responseCode = httpConn.getResponseCode();  
			if (responseCode != 200) {  
			    LOG.error(" Error===" + responseCode);  
			} else {  
				LOG.info("Post Success!");  
			}  
			// 获取输入流
			inStream = httpConn.getInputStream(); 
			inputStreamReader = new InputStreamReader(inStream, charset);
			//接收返回请求
			bufferedReader = new BufferedReader(inputStreamReader);
			 
			StringBuffer buffer =new StringBuffer();
			String line = ""; 
			while ((line = bufferedReader.readLine()) != null) {  
				 buffer.append(line);
			}
			return buffer.toString();
		} finally {
			IOUtils.closeQuietly(inStream);
			IOUtils.closeQuietly(inputStreamReader);
			IOUtils.closeQuietly(bufferedReader);
			httpConn.disconnect();
		}  
	}
	
}
