package com.woshidaniu.sms.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.woshidaniu.sms.client.http.handler.JSONResponseHandler;
import com.woshidaniu.sms.client.http.handler.PlainTextResponseHandler;
import com.woshidaniu.sms.client.http.handler.ResponseHandler;
import com.woshidaniu.sms.client.provider.SmsPropertiesProvider;

public abstract class SmsClientAdapter implements SmsClient {

	protected ResponseHandler<String> textHandler = new PlainTextResponseHandler();
	protected ResponseHandler<JSONObject> jsonHandler = new JSONResponseHandler();
	
	protected static final String SMS_URL_KEY = "sms.url";
	public static final String SMS_APPID_KEY = "sms.appid";
	protected static final String SMS_PASSWORD_KEY = "sms.password";
	
	protected SmsPropertiesProvider propsProvider;

	public SmsClientAdapter(){}
	
	public SmsClientAdapter(SmsPropertiesProvider propsProvider){
		this.propsProvider = propsProvider;
	}
	
	@Override
	public boolean send(String content, String mobile) {
		return false;
	}
	
	protected void onPreHandle(HttpURLConnection conn) throws Exception {
		
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(3000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setRequestProperty("Charset", "UTF-8"); // 设置编码
		
	}
	
	protected String requestGet(String httpUrl, Map<String, String> httpArg) {
		if (httpUrl == null || "".equals(httpUrl.trim())) {
			return null;
		}
		BufferedReader reader = null;
		StringBuffer result = new StringBuffer();
		try {
			StringBuffer paramsBuffer = new StringBuffer();
			if (httpArg != null && httpArg.size() > 0) {
				Set<String> set = httpArg.keySet();
				Iterator<String> it = set.iterator();
				int count = 0;
				while (it.hasNext()) {
					String key = it.next();
					String value = httpArg.get(key);
					if (count > 0) {
						paramsBuffer.append("&");
					}
					//内容转码由调用者实现
					//paramsBuffer.append(key + "=" + URLEncoder.encode(value, "UTF-8"));
					paramsBuffer.append(key + "=" + value);
					count++;
				}
			}
			String params = paramsBuffer.toString();
			if (params != null && !"".equals(params.trim())) {
				if (httpUrl.indexOf("?") == -1) {
					httpUrl = httpUrl + "?" + params;
				} else if (httpUrl.indexOf("?") == httpUrl.length() - 1) {
					httpUrl = httpUrl + params;
				} else {
					httpUrl = httpUrl + "&" + params;
				}
			}
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			this.onPreHandle(conn);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String strRead = null;
				int row = 0;
				while ((strRead = reader.readLine()) != null) {
					//多行数据，且当前行读取到数据，则在上一行之后添加换行符
					if(row > 0 && strRead != null && strRead.length() > 0){
						result.append("\r\n");
					}
					result.append(strRead);
					row ++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		return result.toString();
	}

	@Override
	public SmsPropertiesProvider getPropsProvider() {
		return propsProvider;
	}

	public void setPropsProvider(SmsPropertiesProvider propsProvider) {
		this.propsProvider = propsProvider;
	}
	
}
