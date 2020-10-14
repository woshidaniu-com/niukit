package com.woshidaniu.safety.xss.http;

import java.util.Enumeration;

import org.owasp.html.PolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @className	： PolicyEnumeration
 * @description	： TODO(描述这个类的作用)
 * @author 		：大康（743）
 * @date		： 2017年9月14日 下午5:27:47
 * @version 	V1.0
 */
public class PolicyEnumeration implements Enumeration<String> {

	private Logger LOG = LoggerFactory.getLogger(PolicyEnumeration.class);
	/**原始Header*/
	private Enumeration<String> headers;
	/**Xss检查策略工厂*/
	private PolicyFactory policy = null;
	
	public PolicyEnumeration( Enumeration<String> headers, PolicyFactory policy){
		this.headers = headers;
		this.policy = policy;
	}
	
	@Override
	public boolean hasMoreElements() {
		return headers.hasMoreElements();
	}

	@Override
	public String nextElement() {
		String taintedHeader = headers.nextElement();
		LOG.debug("Tainted Header :" + taintedHeader);
		String cleanHeader = policy.sanitize(taintedHeader);
		LOG.debug("XSS Clean Header :" + cleanHeader);
		return cleanHeader;
	}

}