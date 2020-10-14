package com.woshidaniu.authz.ticket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.woshidaniu.authz.ticket.utils.TicketTokenUtils;
import com.woshidaniu.httpclient.utils.HttpURLConnectionUtils;

/**
 * 
 * @className	： TicketLoginTest
 * @description	： 测试ticket认证
 * @author 		：康康（1571）
 * @date		： 2018年4月9日 下午3:17:50
 * @version 	V1.0
 */
public class TicketLoginTest{
	
	//连接超时 单位毫秒
	public static int timeout = 30000;
	
	private final String charset = "UTF-8";
	
	private HttpClientBuilder builder;
	
	//请求地址信息
	private final String ip = "10.71.33.158";
	private final String port = "8080";
	private final String contextPath = "/niutal-demo-v5-web";
	private final String getTokenSuffix = "/ticketToken";
	private final String loginUrlSuffix = "/ticketLogin";
	
	//首页地址后缀
	private final String indexUrlSuffix = "/xtgl/index/initMenu.zf";
	//token地址
	private final String TokenUrl = "http://"+ip+":"+port+contextPath + getTokenSuffix;
	
	//登录地址
	private final String LoginUrl = "http://"+ip+":"+port+contextPath + loginUrlSuffix;
	
	//首页地址
	private final String indexUrl = "http://"+ip+":"+port+contextPath + indexUrlSuffix;
	
	//参数信息
	private final String param_xxdm = "10427";//10427,00000
	private final String param_userid = "admin";
	private final String param_roleid = "admin";
	
	private TicketTokenUtils ticketTokenUtils = new TicketTokenUtils();
	
	@Before
	public void init() throws Exception{
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(5000).build();//设置请求和传输超时时间
        this.builder = HttpClientBuilder.create();
        this.builder.setDefaultRequestConfig(requestConfig);
        
        this.ticketTokenUtils.setXxdm("10427");
        this.ticketTokenUtils.setTokenPeriod("2m");
        this.ticketTokenUtils.setTimeStampPeriod("2m");
	}
	
	private String getToken(String xxdm) throws IOException {
		
		System.out.println("获取token...");
		System.out.println("xxdm:"+param_xxdm);
		
		String appid = ticketTokenUtils.genAppid(xxdm);
		String secret = ticketTokenUtils.genSecret(xxdm);
		System.out.println("appid:"+appid);
		System.out.println("secret:"+secret);
		
		//参数
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("appid", appid);
		paramsMap.put("secret", secret);
		
		//结果
		String resultString = getByPost(TokenUrl, paramsMap, charset);
		JSONObject jsonObject = JSON.parseObject(resultString);
		System.out.println("返回json:"+jsonObject);
		String token = jsonObject.getString("token");
		System.out.println("获得token:"+token);
		return token;
	}
	
	/**
	 * 
	 * @description	： 测试获取token正常
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月17日 上午10:34:53
	 */
	@Test
	public void testGetTokenRight() {
		try {
			this.getToken(param_xxdm);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @description	： 测试获取token异常
	 * @author 		： 康康（1571）
	 * @date 		：2018年5月17日 上午11:45:57
	 */
	@Test
	public void testTokenWrong() {
		try {
			this.getToken("000000");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description	：测试通过正确的token登录
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月10日 下午3:30:30
	 * @throws Exception
	 */
	@Test
	public void testLoginByRightToken() throws Exception{
		
		String token = this.getToken(param_xxdm);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = df.format(new Date());
		System.out.println("timestamp:"+timestamp);
		//TimeUnit.MILLISECONDS.sleep(1000);
		String verify = ticketTokenUtils.genVerify( param_userid , param_roleid, timestamp, token );
		this.login(param_userid, param_roleid, token, verify, timestamp);
	}
	
	/**
	 * 
	 * @description	：测试错误的token登录
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月10日 下午3:30:39
	 * @throws Exception
	 */
	@Test
	public void test_login_wrong_token() throws Exception{
		
		String token = "xxx";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = df.format(new Date());
		TimeUnit.MILLISECONDS.sleep(1000);
		String verify = ticketTokenUtils.genVerify( param_userid , param_roleid, timestamp, token );
		this.login(param_userid, param_roleid, token, verify, timestamp);
	}
	
	/**
	 * 
	 * @description	： 测试登录-时间戳未过期
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月10日 下午3:41:23
	 * @throws Exception
	 */
	@Test
	public void test_login_inner_timestamp() throws Exception{
		
		String token = this.getToken(param_xxdm);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = df.format(new Date());
		String verify = ticketTokenUtils.genVerify( param_userid , param_roleid, timestamp, token );
		this.login(param_userid, param_roleid, token, verify, timestamp);
	}
	
	/**
	 * 
	 * @description	： 测试登录-时间戳过期
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月10日 下午3:41:23
	 * @throws Exception
	 */
	@Test
	public void test_login_out_timestamp() throws Exception{
		
		String token = this.getToken(param_xxdm);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = df.format(new Date());
		String verify = ticketTokenUtils.genVerify( param_userid , param_roleid, timestamp, token );
		sleep_while_loop(60*2+2);
		this.login(param_userid, param_roleid, token, verify, timestamp);
	}
	
	/**
	 * 
	 * @description	： 测试过滤器，没有登录
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月10日 下午3:58:03
	 */
	@Test
	public void test_pass_filter_no_login() throws Exception{
		
		String token = this.getToken(param_xxdm);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = df.format(new Date());
		String verify = ticketTokenUtils.genVerify( param_userid , param_roleid, timestamp, token );
		this.sleep_while_loop(5);
		this.pass_filter(param_userid, param_roleid, token, verify, timestamp);
		
	}

	/**
	 * 
	 * @description	： 测试过滤器，登录成功之后
	 * @author 		： 康康（1571）
	 * @date 		：2018年4月10日 下午3:46:03
	 */
	@Test
	public void test_filter_after_login_success() {
	}
	
	
	private void pass_filter(String userid,String roleid,String token,String verify,String timestamp) throws Exception{
		
		System.out.println("穿过过滤器...");
		//参数
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", userid);
		paramsMap.put("roleid", roleid);
		paramsMap.put("token", token);
		paramsMap.put("verify", verify);
		paramsMap.put("time", timestamp);
		System.out.println("参数:"+paramsMap);
		
		String resultString = this.get_request(indexUrl, paramsMap, charset);
		System.out.println("过滤器返回:");
		System.out.println(resultString);
		if(resultString.contains("忘记密码")) {
			System.out.println("包含忘记密码");
		}else {
			System.out.println("不包含忘记密码");
		}
	}
	
	private void login(String userid,String roleid,String token,String verify,String timestamp) throws IOException {
		System.out.println("登录...");
		//参数
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userid", userid);
		paramsMap.put("roleid", roleid);
		paramsMap.put("token", token);
		paramsMap.put("verify", verify);
		paramsMap.put("time", timestamp);
		System.out.println("参数:"+paramsMap);
		
		String resultString = HttpURLConnectionUtils.getResultWithGet(LoginUrl, paramsMap, charset);
		System.out.println("登录返回:");
		System.out.println(resultString);
		System.out.println(resultString.contains("登录"));
		
	}
	
    
    @Test
    public void test_get_get_request_url()throws Exception {
    	String token = this.getToken(param_xxdm);
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String timestamp = df.format(new Date());
		String verify = ticketTokenUtils.genVerify( param_userid , param_roleid, timestamp, token );
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("userid", param_userid);
		paramsMap.put("roleid", param_roleid);
		paramsMap.put("token", token);
		paramsMap.put("verify", verify);
		paramsMap.put("time", timestamp);
		
		String url = this.get_get_request_url(indexUrl, paramsMap, charset);
		System.out.println("测试url:"+url);
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
     * @description	： 获取请求
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
        CloseableHttpClient httpClient =  builder.build();
        
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
    
    private String getByPost(String url, Map<String, String> paramsHashMap,String encoding) {
        //创建httpClient连接
        CloseableHttpClient httpClient =  builder.build();

        String result = null;
        List<NameValuePair> nameValuePairArrayList = new ArrayList<NameValuePair>();
        // 将传过来的参数添加到List<NameValuePair>中
        if (paramsHashMap != null && !paramsHashMap.isEmpty()) {
            //遍历map
            for (Map.Entry<String, String> entry : paramsHashMap.entrySet()) {
                nameValuePairArrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        try {
            // 利用List<NameValuePair>生成Post请求的实体数据
            // UrlEncodedFormEntity 把输入数据编码成合适的内容
        	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairArrayList, encoding);
            HttpPost httpPost = new HttpPost(url);
            // 为HttpPost设置实体数据
            httpPost.setEntity(entity);
            // HttpClient 发送Post请求
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // CloseableHttpResponse
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                	String str = EntityUtils.toString(httpEntity);
                	return str;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private void sleep_while_loop(int seconds) {
    	System.out.println("start sleep...");
    	for(int i=0;i<seconds;i++) {
    		try {
				TimeUnit.SECONDS.sleep(1);
				System.out.print("  "+i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	System.out.println("...");
    	System.out.println("finish sleep loop...");
    }
}
