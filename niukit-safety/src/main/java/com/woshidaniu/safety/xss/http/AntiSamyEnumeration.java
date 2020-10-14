package com.woshidaniu.safety.xss.http;

import java.util.Enumeration;

import com.woshidaniu.safety.utils.AntiSamyScanUtils;
import com.woshidaniu.safety.xss.factory.AntiSamyProxy;

/**
 * 
 *@类名称	: AntiSamyEnumeration.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 21, 2016 5:08:11 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class AntiSamyEnumeration implements Enumeration<String> {
	
	/**AntiSamyProxy对象*/
	private AntiSamyProxy antiSamyProxy = null;
	/**原始Header*/
	private Enumeration<String> headers;
	
	public AntiSamyEnumeration(Enumeration<String> headers, AntiSamyProxy antiSamyProxy){
		this.antiSamyProxy = antiSamyProxy;
		this.headers = headers;
	}
	
	@Override
	public boolean hasMoreElements() {
		return headers.hasMoreElements();
	}

	@Override
	public String nextElement() {
		return AntiSamyScanUtils.xssClean( antiSamyProxy, headers.nextElement());
	}

}
