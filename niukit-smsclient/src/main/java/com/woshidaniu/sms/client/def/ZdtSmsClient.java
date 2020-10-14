package com.woshidaniu.sms.client.def;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.woshidaniu.sms.client.SmsClientAdapter;
import com.woshidaniu.sms.client.SmsEnum;
import com.woshidaniu.sms.client.http.HttpConnectionUtils;
import com.woshidaniu.sms.client.provider.SmsPropertiesProvider;

public class ZdtSmsClient extends SmsClientAdapter {

	public static final String SMS_APPID_KEY = "sms.appid";
	public static final String SMS_TOKEN_KEY = "sms.token";
	public static final String SMS_MSGTYPE_KEY = "sms.msgtype";
	public static final String SMS_SOURCE_KEY = "sms.source";
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String openurl = "http://open.zdt.zju.edu.cn:8080/UcOpen/api/sendSms/";
	protected String smsurl = "http://zdt-sms.zju.edu.cn:8086/smsInterface/sendsms";
	/**
	 * SMS服务商提供的appid
	 */ 
	protected String appid;
	/**
	 * SMS服务商提供的appid对应的password
	 */ 
	protected String password;
	/**
	 * SMS服务商提供的appid对应的token
	 */ 
	protected String token;
	/**
	 * 发送类型 1:普通短信 2:长短信(超过64字符) 
	 */ 
	protected String msgtype;
	/**
	 * 调用SMS接口的来源，即请求方标记
	 */ 
	protected String source;
	
	public ZdtSmsClient(){
		super();
	}
	
	public ZdtSmsClient(SmsPropertiesProvider propsProvider){
		super(propsProvider);
	}
	
	@Override
	public String name() {
		return SmsEnum.ZDT_SMS.getName();
	}

	/**
	 * @param content
	 * @param mobile  短信接收号码，多个号码（用英文逗号隔开）视为一次群发操作
	 */
	@Override
	public boolean send(String content, String mobile) {
		int ret = 99;
		try {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("username", getAppid());
			paramsMap.put("password", getPassword());
			paramsMap.put("to", mobile); // 短信接收号码，多个号码（用英文逗号隔开）视为一次群发操作
			paramsMap.put("text", URLEncoder.encode(content, "GBK"));
			paramsMap.put("msgtype", getMsgtype()); // 发送类型 1:普通短信 2:长短信(超过64字符)
			paramsMap.put("source", getSource());
			
			//String result = requestGet(getSmsurl(), paramsMap);
			String result = HttpConnectionUtils.httpRequestWithPost(getSmsurl(), paramsMap, textHandler);
			if (result != null) {
				ret = Integer.parseInt(result);
			} else {
				ret = -99;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		if (ret > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getSmsurl() {
		return smsurl == null ? getPropsProvider().props().getProperty(SMS_URL_KEY) : smsurl;
	}

	public void setSmsurl(String smsurl) {
		this.smsurl = smsurl;
	}

	public String getAppid() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_APPID_KEY) : appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPassword() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_PASSWORD_KEY) : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_TOKEN_KEY) : token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMsgtype() {
		return msgtype == null ? getPropsProvider().props().getProperty(SMS_MSGTYPE_KEY,"1") : msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getSource() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_SOURCE_KEY) : source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
