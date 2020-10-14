package com.woshidaniu.sms.client.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.sms.client.SmsClient;
import com.woshidaniu.sms.client.provider.SmsPropertiesProvider;

public class NoneSmsClient implements SmsClient {

	protected static final Logger LOG = LoggerFactory.getLogger(NoneSmsClient.class);
	
	@Override
	public String name() {
		return null;
	}
	
	@Override
	public boolean send(String content, String mobile) {
		LOG.debug("短信接收号码：" + mobile + " ,内容 ： " + content);
		return true;
	}

	@Override
	public SmsPropertiesProvider getPropsProvider() {
		return null;
	}

	@Override
	public void setPropsProvider(SmsPropertiesProvider propsProvider) {

	}

}
