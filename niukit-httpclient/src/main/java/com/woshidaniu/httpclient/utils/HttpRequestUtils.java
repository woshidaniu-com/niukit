package com.woshidaniu.httpclient.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.util.CharsetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.io.utils.FilemimeUtils;

/**
 * 
 * @className	： HttpRequestUtils
 * @description	： HttpClient请求准备处理工具；如 构建URL,处理参数
 * @author 		：kangzhidong
 * @date		： 2015-7-1 下午10:36:06
 */
public abstract class HttpRequestUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpRequestUtils.class);
	
	// constants
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String APPLICATION_OCTET_STREAM =  "application/octet-stream";
	  
    public static final String APPLICATION_SVG_XML =  "application/svg+xml";
    public static final String APPLICATION_XHTML_XML =  "application/xhtml+xml";
    public static final String APPLICATION_XML = "application/xml";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_JSON = "text/json";
    public static final String TEXT_XML = "text/xml";
    public static final String WILDCARD = "*/*";
    
    public static final String UTF_8 = "UTF-8";
    public static final String ASCII = "US-ASCII";
    public static final String ISO_8859_1 = "ISO-8859-1";
    
    //设置请求和传输超时时间 
    protected static RequestConfig defaultRequestConfig = null;
	//通过网络与服务器建立连接的超时时间。Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间；单位毫秒，默认5000
    protected static int connect_timeout = 5000;
	//Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间；单位毫秒，默认5000
    protected static int socket_timeout = 5000;
	
	static{
    	
    	try {
    		
			connect_timeout = StringUtils.getSafeInt(HttpConfigUtils.getText("http.request.connect_timeout"), "5000");
			socket_timeout = StringUtils.getSafeInt(HttpConfigUtils.getText("http.request.socket_timeout"), "5000"); 
			
			//设置请求和传输超时时间 
			defaultRequestConfig = RequestConfig.custom()
			    //Socket读数据的超时时间 	
				.setSocketTimeout(socket_timeout)
			 	 // 设置连接超时时间(单位毫秒)  
			 	.setConnectTimeout(connect_timeout)
			 	.setConnectionRequestTimeout(connect_timeout)
			 	.setExpectContinueEnabled(false).build();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
	public static <T extends HttpRequestBase> T getHttpRequest(T httpRequest,Map<String, String> headers) {
		//设置默认请求头信息
		httpRequest.addHeader(HttpHeaders.ACCEPT, "*/*");
		httpRequest.addHeader(HttpHeaders.CONNECTION, "keep-alive");
		httpRequest.addHeader(HttpHeaders.CACHE_CONTROL, "max-age=0");
		//httpRequest.addHeader(HttpHeaders.HOST, "mp.weixin.qq.com");
		//httpRequest.addHeader("X-Requested-With", "XMLHttpRequest");
		httpRequest.setHeader(HttpHeaders.ACCEPT_LANGUAGE,"zh-cn,zh;q=0.5");
		httpRequest.setHeader(HttpHeaders.ACCEPT_CHARSET,"GBK,utf-8;q=0.7,*;q=0.7");
		httpRequest.addHeader(HttpHeaders.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; SV1; .NET CLR 1.1.4322)");
		//设置自定义请求头信息
        if (headers != null) {  
            Set<String> keys = headers.keySet();  
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {  
                String key = (String) i.next();  
                httpRequest.setHeader(key, headers.get(key)); 
            }
        }
        //Request不会继承客户端级别的请求配置，所以在自定义Request的时候，需要将客户端的默认配置拷贝过去：
        httpRequest.setConfig(RequestConfig.copy(defaultRequestConfig).build());
        return httpRequest;
	}
    
	/**
	 * 
	 * @description	：构建HttpGet对象
	 * @author 		： kangzhidong
	 * @date 		：2015-7-1 下午11:32:46
	 * @param baseURL
	 * @param paramsMap
	 * @param charset
	 * @return
	 */
	public static HttpGet getHttpGet(String baseURL, Map<String, Object> paramsMap,String charset) {
		return getHttpGet(baseURL, paramsMap, charset, null);
	}
	
	/**
	 * 
	 * @description	：构建HttpGet对象
	 * @author 		： kangzhidong
	 * @date 		：2015-7-1 下午11:32:46
	 * @param baseURL
	 * @param paramsMap
	 * @param charset
	 * @param headers
	 * @return
	 */
	public static HttpGet getHttpGet(String baseURL, Map<String, Object> paramsMap,String charset,Map<String, String> headers) {
		HttpGet httpGet = null;
		try {	
			httpGet  = new HttpGet(HttpURIUtils.buildURIBuilder(baseURL, paramsMap ,charset).build());
		} catch (URISyntaxException e) {
			LOG.error("URISyntaxException", e);
			httpGet = new HttpGet(HttpURIUtils.buildURL(baseURL, paramsMap ,charset)); 
		} 
        return getHttpRequest(httpGet,headers);
	}
	
    /**
	 * 
	 * @description	： 构建HttpPost对象
	 * @author 		： kangzhidong
	 * @date 		：2015-6-28 下午11:03:55
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static HttpPost getHttpPost(String baseURL, String charset,Map<String, String> headers) {
		String[] paramStr = baseURL.split("[?]", 2);
		HttpPost httpPost  = new HttpPost(paramStr[0]); 
		return getHttpRequest(httpPost,headers);
	}
	
	public static HttpEntity getWrapHttpEntity(HttpRequestBase httpRequest, HttpEntity httpEntity){
		//根据响应头判断是否启用gzip压缩
        boolean isGzip = HttpRequestUtils.isGzip(httpRequest);
        //设置请求实体参数对象
        if(isGzip){
        	return new GzipCompressingEntity(httpEntity);
        }else{
        	return httpEntity;
        }
	}
	
	public static HttpEntity getHttpEntity(String baseURL, Map<String, Object> paramsMap,String charset) throws IOException{
    	//有实体对象参数，表示可能有文件上传
    	if(HttpRequestUtils.isMultipart(paramsMap)){
    		
    		//对请求的表单域进行填充  
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            //以浏览器兼容模式运行，防止文件名乱码。
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //初始路径中的参数集合对象
            List<NameValuePair> nameValuePostBodies = HttpURIUtils.buildNameValuePairs(baseURL);
            for (NameValuePair nameValuePair : nameValuePostBodies) {
            	entityBuilder.addTextBody(nameValuePair.getName(), nameValuePair.getValue() , HttpRequestUtils.getTextContentType(charset));
            }
            //初始参数对象
    		if(paramsMap != null && !paramsMap.isEmpty()){
    			//组织参数
    	        Iterator<String> iterator = paramsMap.keySet().iterator();
    	        while (iterator.hasNext()) {
    	            String name = iterator.next();
    	            Object value = paramsMap.get(name);
    	            if (value instanceof File) {
    	            	//文件对象
    	            	File file = (File) value;
    	            	//对应服务端类的同名属性<File类型>
    	                entityBuilder.addBinaryBody(name, file );
    	                //用来封装上传文件的文件名
    	                entityBuilder.addTextBody(name + "FileName", file.getName());
    	                //用来封装上传文件的类型
    	                entityBuilder.addTextBody(name + "ContentType", FilemimeUtils.getFileMimeType(file));
    	            } else if (value instanceof InputStream) {
    	            	// 把输入流转换成流对象InputStreamBody
    	                entityBuilder.addBinaryBody(name, (InputStream) value);
    	            } else if (value instanceof byte[]) {
    	                byte[] byteVlue = (byte[]) value;
    	                // 把字节数组转换流对象ByteArrayBody
    	                entityBuilder.addBinaryBody(name, byteVlue);
    	            } else {
    	            	entityBuilder.addTextBody(name, value.toString());
					}
    	        }
        	}
            entityBuilder.setCharset(CharsetUtils.get(charset));
            return entityBuilder.build();
    	}else{
    		List<NameValuePair> nameValuePairs = HttpURIUtils.buildNameValuePairs(baseURL, paramsMap);
    		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nameValuePairs, charset);
    		return uefEntity;
    	}
	}
	

	public static boolean isMultipart(Map<String, Object> paramsMap){
		boolean isMultipart = false;
		if(paramsMap != null && !paramsMap.isEmpty()){
	        Iterator<String> iterator = paramsMap.keySet().iterator();
	        while (iterator.hasNext() && !isMultipart) {
	            String key = iterator.next();
	            Object value = paramsMap.get(key);
	            if (value instanceof File) {
	            	isMultipart = true;
	            } else if (value instanceof InputStream) {
	            	isMultipart = true;
	            } else if (value instanceof byte[]) {
	            	isMultipart = true;
	            }
	        }
    	}
		return isMultipart;
	}
	
	public static boolean isGzip(HttpRequestBase httpRequest){
		 //设置请求实体参数对象
        HeaderElementIterator it = new BasicHeaderElementIterator(httpRequest.headerIterator(HttpHeaders.ACCEPT_ENCODING));
	    boolean isGzip = false;
        while (it.hasNext()) {
	        HeaderElement elem = it.nextElement(); 
	        System.out.println(elem.getName() + " = " + elem.getValue());
	        if("gzip".equalsIgnoreCase(elem.getName())){
	        	isGzip = true;
	        	break;
	        }
	    }
		return isGzip;
	}
	

    public static ContentType getBinaryContentType(String charset) {
		try {
			return ContentType.create( HttpClientUtils.APPLICATION_OCTET_STREAM, charset);
		} catch (UnsupportedCharsetException e) {
			return ContentType.DEFAULT_BINARY;
		}
	}
    
    public static ContentType getTextContentType(String charset) {
		try {
			return ContentType.create( HttpClientUtils.TEXT_PLAIN, charset);
		} catch (UnsupportedCharsetException e) {
			return ContentType.DEFAULT_TEXT;
		}
	}
	
}
