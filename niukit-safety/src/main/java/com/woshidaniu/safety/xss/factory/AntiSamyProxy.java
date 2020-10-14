package com.woshidaniu.safety.xss.factory;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;

public class AntiSamyProxy {

	/**AntiSamy对象*/
	protected AntiSamy antiSamy;
	/**Policy策略对象*/
	protected Policy policy;
	/** 扫描器类型，0：DOM类型扫描器,1:SAX类型扫描器；两者的区别如同XML解析中DOM解析与Sax解析区别相同，实际上就是对两种解析方式的实现*/
	protected int scanType = 1;
	/**需要进行Xss检查的Header*/
	protected String[] policyHeaders;
	
	public AntiSamyProxy(AntiSamy antiSamy,Policy policy, int scanType, String[] policyHeaders) {
		this.antiSamy = antiSamy;
		this.policy = policy;
		this.scanType = scanType;
		this.policyHeaders = policyHeaders;
	}

	public AntiSamy getAntiSamy() {
		return antiSamy;
	}

	public void setAntiSamy(AntiSamy antiSamy) {
		this.antiSamy = antiSamy;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public int getScanType() {
		return scanType;
	}

	public void setScanType(int scanType) {
		this.scanType = scanType;
	}

	public String[] getPolicyHeaders() {
		return policyHeaders;
	}

	public void setPolicyHeaders(String[] policyHeaders) {
		this.policyHeaders = policyHeaders;
	}
	
}
