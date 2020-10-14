/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.token;

import java.util.Map;

import com.woshidaniu.shiro.token.ZfSsoToken;

/**
 * 
 * @className	： ZFCasToken
 * @description	： ZFCasToken
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:57:02
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class ZFCasToken extends ZfSsoToken {
	
	/** The service ticket returned by the CAS server */
    private String ticket = null;
	/** 其他参数 */
	private Map<String, Object> attrs;

	public ZFCasToken(String host) {
		this.host = host;
	}
	
	@Override
	public Object getCredentials() {
		return ticket;
	}
	
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}
	
}
