package com.woshidaniu.sms.client;

public enum SmsEnum {

	ZDT_SMS("zdt", "GET", "http"),
	
	BAIDU_106_SMS("baidu-106", "GET", "http"),
	
	GreenTown("GreenTown", "POST", "http");
	
	protected String name;
	/**•GET •POST •HEAD •OPTIONS •PUT •DELETE •TRACE */
	protected String method;
	protected String protocol;
	
	private SmsEnum(String name,String method,String protocol){
		this.name = name;
		this.method = method;
		this.protocol = protocol;
	}

	public String getName() {
		return name;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getProtocol() {
		return protocol;
	}
	
}
