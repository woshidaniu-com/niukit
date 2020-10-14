package com.woshidaniu.safety.xss.http;

import java.util.Enumeration;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 
 * @className	： EscapedEnumeration
 * @description	： TODO(描述这个类的作用)
 * @author 		：大康（743）
 * @date		： 2017年9月14日 下午12:14:14
 * @version 	V1.0
 */
public class EscapedEnumeration implements Enumeration<String> {

	private Enumeration<String> headers;
	
	public EscapedEnumeration(Enumeration<String> headers){
		this.headers = headers;
	}
	
	@Override
	public boolean hasMoreElements() {
		return headers.hasMoreElements();
	}

	@Override
	public String nextElement() {
		return StringEscapeUtils.escapeHtml4(headers.nextElement());
	}

}
