package com.woshidaniu.sms.client;

import com.woshidaniu.sms.client.provider.SmsPropertiesProvider;

public interface SmsClient {

	public String name();
	
	public boolean send(String content, String mobile);
	
	public SmsPropertiesProvider getPropsProvider();

	public void setPropsProvider(SmsPropertiesProvider propsProvider);
	
}
