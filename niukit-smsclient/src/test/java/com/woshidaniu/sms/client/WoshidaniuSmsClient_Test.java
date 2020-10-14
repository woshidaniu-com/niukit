package com.woshidaniu.sms.client;

import com.woshidaniu.sms.client.def.WoshidaniuSmsClient;
import junit.framework.TestCase;

public class WoshidaniuSmsClient_Test extends TestCase {

	public boolean testSend() {
		WoshidaniuSmsClient client = new WoshidaniuSmsClient();
		client.setSmsurl("http://10.71.19.50:9080/openmasservice");
		client.setAppid("woshidaniu");
		client.setExtendcode("001");
		client.setPassword("woshidaniu_dxwg");
		return client.send("测试短,验证码:123456", "18667167083");
	}
	
}
