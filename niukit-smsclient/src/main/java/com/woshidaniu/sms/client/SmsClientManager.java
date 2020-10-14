package com.woshidaniu.sms.client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SmsClientManager {

	/**
	 * 默认：default
	 */
	public static final String DEFAULT = "default";
	
	protected ConcurrentMap<String, SmsClient> COMPLIED_SMS_CLIENT = new ConcurrentHashMap<String, SmsClient>();
	
	public SmsClientManager(){
		
	}
	
	public Set<String> names() {
		return COMPLIED_SMS_CLIENT.keySet();
	}
	
	/**
	 * 
	 *@描述		: 注册短信发送客户端
	 *@创建人		: kangzhidong
	 *@创建时间	: 2017年4月7日下午3:12:13
	 *@param client
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public void register(SmsClient client) {
		if (client != null ) {
			COMPLIED_SMS_CLIENT.putIfAbsent( client.name(), client);
		}
	}
	
	/**
	 * 
	 *@描述		: 根据客户端名称查找短信发送客户端实现对象
	 *@创建人		: kangzhidong
	 *@创建时间	: 2017年4月7日下午3:12:04
	 *@param strategy
	 *@return
	 *@修改人		: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public SmsClient getSmsClient(String strategy) {
		if (strategy != null) {
			SmsClient ret = COMPLIED_SMS_CLIENT.get(strategy);
			if (ret != null) {
				return ret;
			}
		}
		return COMPLIED_SMS_CLIENT.get(DEFAULT);
	}
	
}
