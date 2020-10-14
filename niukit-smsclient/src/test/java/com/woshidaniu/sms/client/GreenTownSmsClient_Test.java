package com.woshidaniu.sms.client;

import com.woshidaniu.sms.client.def.GreenTownSmsClient;

import junit.framework.TestCase;

public class GreenTownSmsClient_Test extends TestCase {

	public boolean testSend() {
		GreenTownSmsClient client = new  GreenTownSmsClient();
		client.setAppid("woshidaniu");
		client.setPassword("woshidaniu_dxwg");
		return client.send("测试短,验证码:123456", "18667167083");
	}
	
}