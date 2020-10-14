package com.woshidaniu.sms.client.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinamobile.openmas.client.Sms;
import com.woshidaniu.sms.client.SmsClientAdapter;
import com.woshidaniu.sms.client.provider.SmsPropertiesProvider;

public class WoshidaniuSmsClient extends SmsClientAdapter {

	protected  Logger LOG = LoggerFactory.getLogger(this.getClass());
	public static final String SMS_EXTENDCODE_KEY = "sms.extendCode";
	
	/**
	 * SMS服务商调用地址
	 */ 
	/*protected String openurl = "http://open.zdt.zju.edu.cn:8080/UcOpen/api/sendSms/";*/
	// = "http://10.71.19.50:9080/openmasservice"
	protected String smsurl;
	/**
	 * SMS服务商提供的appid
	 */ 
	protected String appid;
	/**
	 * SMS服务商提供的appid对应的password
	 */ 
	protected String password;
	/**
	 * 调用SMS接口的extendCode，即请求方标记
	 */ 
	protected String extendcode;
	
	public WoshidaniuSmsClient(){
		super();
	}
	
	public WoshidaniuSmsClient(SmsPropertiesProvider propsProvider){
		super(propsProvider);
	}
	
	@Override
	public String name() {
		return "woshidaniu";
	}
	
	@Override
	public boolean send(String content, String mobile) {
		String gateWayid = "";
		try {
			Sms sms = new Sms(getSmsurl());
			gateWayid = sms.SendMessage(mobile.split(","), content, getExtendcode(), getAppid(), getPassword());
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		if (gateWayid != null && gateWayid.length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public String getSmsurl() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_URL_KEY) : smsurl;
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

	public String getExtendcode() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_EXTENDCODE_KEY) : extendcode;
	}

	public void setExtendcode(String extendcode) {
		this.extendcode = extendcode;
	}
	
	
}
