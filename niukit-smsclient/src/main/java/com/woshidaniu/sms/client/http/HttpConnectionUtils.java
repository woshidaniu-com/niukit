package com.woshidaniu.sms.client.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.sms.client.http.handler.ResponseHandler;


/**
 * 
 * @className	： HttpConnectionUtils
 * @description	：使用java内置HttpURLConnection对象进行get,post方式的请求
 * @date		： 2015-6-24 下午05:08:14
 * <pre>
 * 总结：
 * a:) HttpURLConnection的connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。
 * 	        无论是post还是get，http请求实际上直到HttpURLConnection的getInputStream()这个函数里面才正式发送出去。
	       b:) 在用POST方式发送URL请求时，URL请求参数的设定顺序是重中之重，
	    对connection对象的一切配置（那一堆set函数）
	    都必须要在connect()函数执行之前完成。而对outputStream的写操作，又必须要在inputStream的读操作之前。
	    这些顺序实际上是由http请求的格式决定的。
	    如果inputStream读操作在outputStream的写操作之前，会抛出例外：
	    java.net.ProtocolException: Cannot write output after reading input.......
	      
	       c:) http请求实际上由两部分组成，
	    一个是http头，所有关于此次http请求的配置都在http头里面定义，
	           一个是正文content。
	    connect()函数会根据HttpURLConnection对象的配置值生成http头部信息，因此在调用connect函数之前，
	    就必须把所有的配置准备好。
	       d:) 在http头后面紧跟着的是http请求的正文，正文的内容是通过outputStream流写入的，
	    实际上outputStream不是一个网络流，充其量是个字符串流，往里面写入的东西不会立即发送到网络，
	    而是存在于内存缓冲区中，待outputStream流关闭时，根据输入的内容生成http正文。
	    至此，http请求的东西已经全部准备就绪。在getInputStream()函数调用的时候，就会把准备好的http请求
	    正式发送到服务器了，然后返回一个输入流，用于读取服务器对于此次http请求的返回信息。由于http
	    请求在getInputStream的时候已经发送出去了（包括http头和正文），因此在getInputStream()函数
	    之后对connection对象进行设置（对http头的信息进行修改）或者写入outputStream（对正文进行修改）
	    都是没有意义的了，执行这些操作会导致异常的发生。 
 * </pre>
 */
public class HttpConnectionUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpConnectionUtils.class);
	
	//连接超时 单位毫秒
	public static int connectTimeout = 10000;
	//读取超时 单位毫秒
	public static int readTimeout = 3000;
	
	protected static String sessionID = "";
	
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
	

	public static HttpURLConnection getPreparedHttpURLConnection(HttpURLConnection httpConn,String method,Map<String, String> headers,String contentType,String charset) throws IOException {
			    
        // 设定请求的方法为指定方法，默认是GET 
	    httpConn.setRequestMethod(method);  
        // 表示从服务器获取数据
		httpConn.setDoInput(true);
		// 设置是否向服务器写数据  ;如果是post请求，参数要放在 http正文内，因此需要设为true, 默认情况下是false;  
		httpConn.setDoOutput("POST".equalsIgnoreCase(method) ? true : false ); 
		// Post 请求不能使用缓存
		httpConn.setUseCaches(false);  
		 
	    // This method takes effects to
        // every instances of this class.
        // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
        // connection.setFollowRedirects(true);
      
        // This methods only
        // takes effacts to this
        // instance.
        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
	    httpConn.setInstanceFollowRedirects(false);
       
	    //设置Session ID 解决多次请求不同会话问题
	    httpConn.setRequestProperty("Cookie", sessionID);
	    
	    // 设置通用的请求属性 (模拟浏览器请求头) 
	    httpConn.setRequestProperty(HttpHeaders.ACCEPT, "*/*");  
	    httpConn.setRequestProperty(HttpHeaders.CONNECTION, "Keep-Alive");  
		httpConn.setRequestProperty(HttpHeaders.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
		
		/*增加headers*/
		if(headers != null) {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				httpConn.setRequestProperty(key, headers.get(key));
			}
		}
		
		/* 
		 * 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		 * 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
		 */	
		if (contentType != null) {
			httpConn.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType);
		} else {
			httpConn.setRequestProperty(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED + ";charset=" + charset);
		}
		
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
		
		return httpConn;
	}
	
	public static HttpURLConnection getHttpURLConnectionWithGet(String baseURL,Map<String,Object> paramsMap,Map<String, String> headers,String charset) throws IOException{
		URL urlGet = new URL(buildURL(baseURL,paramsMap));
		// 此处的urlConnection对象实际上是根据URL的 请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection,故此处最好将其转化 为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.如下: 
		HttpURLConnection httpConn = (HttpURLConnection) urlGet.openConnection();
						  httpConn = getPreparedHttpURLConnection( httpConn , "GET", headers, ContentType.TEXT_PLAIN, charset);
	    httpConn.connect();
		return httpConn;
	}
	
	public static HttpURLConnection getHttpURLConnectionWithPost(String baseURL,Map<String,Object> paramsMap,Map<String, String> headers, String contentType ,String charset) throws IOException{
		URL urlPost = new URL(baseURL);
		// 此处的urlConnection对象实际上是根据URL的 请求协议(此处是http)生成的URLConnection类 的子类HttpURLConnection,故此处最好将其转化 为HttpURLConnection类型的对象,以便用到HttpURLConnection更多的API.如下: 
		HttpURLConnection httpConn = (HttpURLConnection) urlPost.openConnection();
						  httpConn = getPreparedHttpURLConnection( httpConn , "POST", headers, contentType , charset);
		/* 
	        Post请求的OutputStream实际上不是网络流，而是写入内存，在 getInputStream中才真正把写道流里面的内容作为正文与根据之前的配置生成的http request头合并成真正的http request，并在此时才真正向服务器发送。
	        HttpURLConnection.setChunkedStreamingMode函 数可以改变这个模式，设置了ChunkedStreamingMode后，
			不再等待OutputStream关闭后生成完整的http request一次过发送，而是先发送http request头， 正文内容则是网路流的方式实时传送到服务器。
	                       实际上是不告诉服务器http正文的长度，这种模式适用于向服务器传送较大的或者是不容易 获取长度的数据，如文件上传。
	                       与readContentFromPost()最大的不同，设置了块大小为5字节 
	    */
	    httpConn.setChunkedStreamingMode(5);  
	    /* 
	     * 注意，下面的getOutputStream函数工作方式于在readContentFromPost()里面的不同 ；在readContentFromPost()里面该函数仍在准备http request，没有向服务器发送任何数据 
	     * 而在这里由于设置了ChunkedStreamingMode，getOutputStream函数会根据connect之前的配置  生成http request头，先发送到服务器。 
	     */  
	    if(ContentType.APPLICATION_JAVA_SERIALIZED_OBJECT.equalsIgnoreCase(contentType)){
	    	// 连接，从urlPost.openConnection()至此的配置必须要在connect之前完成，要注意的是connection.getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，  所以在开发中不调用上述的connect()也可以)。  
	        httpConn.connect();
	    	//  现在通过输出流对象构建对象输出流对象，以实现输出可序列化的对象。 
	    	ObjectOutputStream output  =  new ObjectOutputStream(httpConn.getOutputStream()); 
	    	//  向对象输出流写出数据，这些数据将存到内存缓冲区中 
	    	output.writeObject(paramsMap);
	    	//  刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream） 
	    	output.flush(); 
	    	//  关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中, 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器 
	    	output.close(); 
	    }else {
	    	//组织参数内容
			StringBuilder buffer = new StringBuilder();
			if(paramsMap != null && paramsMap.size() > 0 ){
				for (String key : paramsMap.keySet()) {
					buffer.append("&").append(key).append("=").append(URLEncoder.encode(String.valueOf(paramsMap.get(key)), charset));
				}
				buffer.deleteCharAt(0);
			}
			// 获得上传信息的字节大小以及长度  
            //byte[] postdata = buffer.toString().getBytes();  
	        httpConn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.length()));
	        // 连接，从urlPost.openConnection()至此的配置必须要在connect之前完成，要注意的是connection.getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，  所以在开发中不调用上述的connect()也可以)。 
	        httpConn.connect();
	    	//建立输入流，向指向的URL传入参数
		    DataOutputStream output = new DataOutputStream(httpConn.getOutputStream());
		    // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
		    output.writeBytes(buffer.toString());
		    //output.write(postdata , 0 , postdata.length );
		    // flush输出流的缓冲  
		    output.flush();
		    // 到此时服务器已经收到了完整的http request了，而在readContentFromPost()函数里，要等到下一句服务器才能收到http请求。
		    output.close(); 
		}
		return httpConn;
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
		// 创建默认的HttpURLConnection实例.
		HttpURLConnection httpConn = getHttpURLConnectionWithGet(baseURL, paramsMap, headers, charset);
		try {
			// 对HttpURLConnection进行预处理
			handler.preHandle(httpConn);
			// 处理最终的响应结果
			return handler.handleResponse(httpConn, charset);
		} finally {
			//在一次请求发送后获取SessionID
			sessionID = getSessionID(httpConn);
			//释放链接
			HttpIOUtils.closeConnect(httpConn);
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
			Map<String, String> headers, ResponseHandler<T> handler) throws IOException{
		// 创建默认的HttpURLConnection实例.
		HttpURLConnection httpConn = getHttpURLConnectionWithPost(baseURL, paramsMap, headers, contentType, charset);
		try {
			// 对HttpURLConnection进行预处理
			handler.preHandle(httpConn);
			// 处理最终的响应结果
			return handler.handleResponse(httpConn, charset);
		} finally {
			//在一次请求发送后获取SessionID
			sessionID = getSessionID(httpConn);
			//释放链接
			HttpIOUtils.closeConnect(httpConn);
		}
	}
	
	/**
	 * 
	 * ******************************************************************
	 * @description	： 在一次请求发送后获取SessionID
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">vindell</a>
	 * @date 		：Dec 15, 2016 2:53:57 PM
	 * @param httpConn
	 * @return
	 * ******************************************************************
	 */
	public static String getSessionID(HttpURLConnection httpConn){
		// Get Session ID
		String key = "";
		String sessionId = "";
		if (httpConn != null) {
			for (int i = 1; (key = httpConn.getHeaderFieldKey(i)) != null; i++) {
				if (key.equalsIgnoreCase("set-cookie")) {
					sessionId = httpConn.getHeaderField(key);
					sessionId = sessionId.substring(0, sessionId.indexOf(";"));
				}
			}
		}
		return sessionId;
	}
	
}
