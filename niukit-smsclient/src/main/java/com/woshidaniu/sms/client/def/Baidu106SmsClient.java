package com.woshidaniu.sms.client.def;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.woshidaniu.sms.client.SmsClientAdapter;
import com.woshidaniu.sms.client.SmsEnum;
import com.woshidaniu.sms.client.http.HttpConnectionUtils;
import com.woshidaniu.sms.client.provider.SmsPropertiesProvider;

/**
 * 
 *@类名称		： Baidu106SmsClient.java
 *@类描述		：106短信验证接口（http://apistore.baidu.com/apiworks/servicedetail/1018.html）
 *@创建人		：kangzhidong
 *@创建时间	：2017年5月11日 上午9:34:57
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class Baidu106SmsClient extends SmsClientAdapter {
	
	protected static Logger LOG = LoggerFactory.getLogger(Baidu106SmsClient.class);
	
	/**
	 * SMS服务商调用地址
	 */ 
	protected String smsurl = "http://apis.baidu.com/kingtto_media/106sms/106sms";
	/**
	 * SMS服务商提供的apikey
	 */ 
	protected String apikey;
	
	public Baidu106SmsClient(){
		super();
	}
	
	public Baidu106SmsClient(SmsPropertiesProvider propsProvider){
		super(propsProvider);
	}
	
	@Override
	public String name() {
		return SmsEnum.BAIDU_106_SMS.getName();
	}
	
	@Override
	protected void onPreHandle(HttpURLConnection conn) throws Exception {
		super.onPreHandle(conn);
        // 填入apikey到HTTP header
		//conn.setRequestProperty("apikey",  "您自己的apikey");
		conn.setRequestProperty("apikey",  getApikey());
		conn.setRequestMethod(SmsEnum.BAIDU_106_SMS.getMethod());
	}
	
	/**
	 *  JSON返回示例 :
	 *  -------------------------------------------------------------------------------------------------------
	 *	{
	 *		"returnstatus": "Success",---------- 返回状态值：成功返回Success 失败返回：Faild
	 *		"message": "ok",---------- 返回信息
	 *		"remainpoint": "0",---------- 运营商结算无意义，可不用解析
	 *		"taskID": "123456",---------- 返回本次任务的序列ID
	 *		"successCounts": "1"---------- 返回成功短信数     
	 *	}
	 *	
	 *	XML 返回示例：
	 *	-------------------------------------------------------------------------------------------------------
	 *	<?xml version="1.0" encoding="utf-8" ?>
	 *	<returnsms>
	 *		<returnstatus>status</returnstatus>---------- 返回状态值：成功返回Success 失败返回：Faild
	 *		<message>message</message>---------- 返回信息
	 *		<remainpoint> remainpoint</remainpoint>---------- 运营商结算无意义，可不用解析
	 *		<taskID>taskID</taskID>---------- 返回本次任务的序列ID
	 *		<successCounts>successCounts</successCounts>---------- 返回成功短信数
	 *	</returnsms>
	 *
	 */
	@Override
	public boolean send(String content, String mobile) {
		try {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("mobile", mobile);
			paramsMap.put("content", URLEncoder.encode(content, "UTF-8"));
			
			JSONObject jsonObject = HttpConnectionUtils.httpRequestWithPost(getSmsurl(), paramsMap, jsonHandler);
			if("Success".equalsIgnoreCase(jsonObject.getString("returnstatus"))){
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return false;
	}

	public String getSmsurl() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_URL_KEY) : smsurl;
	}

	public void setSmsurl(String smsurl) {
		this.smsurl = smsurl;
	}

	public String getApikey() {
		return getPropsProvider() != null ? getPropsProvider().props().getProperty(SMS_APPID_KEY) : apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	
}
