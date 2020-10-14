package com.woshidaniu.authz.zjucookie.test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Test;

/**
 * 
 * @className	： TicketLoginTest
 * @description	： 测试ticket认证
 * @author 		：康康（1571）
 * @date		： 2018年4月9日 下午3:17:50
 * @version 	V1.0
 */
public class ZjuLoginTest{
	
	//连接超时 单位毫秒
	public static int timeout = 30000;
	
	private HttpClientBuilder builder;
	
	//请求地址信息
	private static final String ip = "10.71.33.158";
	private static final String port = "8082";
	private static final String contextPath = "/niutal-demo-v5-web-outh2";
	private static final String loginUrlSuffix = "/zjucookieLogin";
	
	private static final String domin ="http://"+ip+":"+port+contextPath;
	
	//登录地址
	private static final String LoginUrl = "http://"+ip+":"+port+contextPath + loginUrlSuffix;
	
	private static final String TOKEN_KEY = "iPlanetDirectoryPro";
	
	@org.junit.Before
	public void init() throws Exception{
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(5000).build();//设置请求和传输超时时间
        this.builder = HttpClientBuilder.create();
        this.builder.setDefaultRequestConfig(requestConfig);
	}
	
	private String get_get_request_url(String url, Map<String, String> paramsHashMap, String encoding)throws Exception {
        StringBuilder params = new StringBuilder();
		if(paramsHashMap != null && !paramsHashMap.isEmpty()){
			params.append("?");
			boolean isfirstParamPair = true;
			for (String key : paramsHashMap.keySet()) {
				String value = paramsHashMap.get(key);
				if(isfirstParamPair) {
					params.append(key).append("=").append(URLEncoder.encode(value, encoding));					
					isfirstParamPair = false;
				}else {
					params.append("&").append(key).append("=").append(URLEncoder.encode(value, encoding));
				}
			}
		}
		String requestUrl = url + params.toString();
		return requestUrl;
	}
	/**
     * 
     * @description	： 请求
     * @author 		： 康康（1571）
     * @date 		：2018年4月10日 下午7:33:46
     * @param paramsHashMap
     * @param url
     * @param encoding
     * @return
     * @throws EncodingException 
     */
    private String get_request(String url,Map<String, String> paramsHashMap, String encoding) throws Exception {
        //创建httpClient连接
        CookieStore cookieStore = new BasicCookieStore(); 
        BasicClientCookie cookie = new BasicClientCookie(TOKEN_KEY, "admin");   
        cookie.setVersion(0);    
        cookie.setDomain(domin);   //设置范围  
        cookie.setPath("/"); 
        cookieStore.addCookie(cookie);  
        
        CloseableHttpClient httpClient = builder.setDefaultCookieStore(cookieStore).build();
		String requestUrl = this.get_get_request_url(url, paramsHashMap,encoding);
		System.out.println("请求地址:"+requestUrl);
        String result = null;
        try {
            // 利用List<NameValuePair>生成Post请求的实体数据
            // UrlEncodedFormEntity 把输入数据编码成合适的内容
            HttpGet httpGet = new HttpGet(requestUrl);
            // HttpClient 发送get请求
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int stateCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("返回码:"+stateCode);
            if (stateCode == 200) {
                // CloseableHttpResponse
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), encoding), 10 * 1024);
                        StringBuilder strBuilder = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            strBuilder.append(line);
                        }
                        result = strBuilder.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                //关闭流
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	@Test
	public void test_login_by_cookie() throws Exception{
		Map<String,String> paramsHashMap  = new HashMap<String,String>();
		this.get_request(LoginUrl, paramsHashMap, "UTF-8");
	}
	

	@Test
	public void test_login_by_param() throws Exception{
		

	}
	
}