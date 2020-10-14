package com.woshidaniu.safety.xss.http;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.woshidaniu.safety.utils.AntiSamyScanUtils;
import com.woshidaniu.safety.utils.XssScanUtils;
import com.woshidaniu.safety.xss.factory.AntiSamyProxy;

/**
 * 
 * @className	： HttpServletRichTextXSSRequestWrapper
 * @description	： RichText XSS(Cross Site Scripting)，即跨站脚本攻击请求过滤;
 * @author 		：大康（743）
 * @date		： 2017年9月14日 下午5:21:08
 * @version 	V1.0
 */
public class HttpServletRichTextXSSRequestWrapper extends HttpServletRequestWrapper {

	private AntiSamyProxy antiSamyProxy = null;
	
	public HttpServletRichTextXSSRequestWrapper(AntiSamyProxy antiSamyProxy,HttpServletRequest request) {
		super(request);
		this.antiSamyProxy = antiSamyProxy;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> request_map = super.getParameterMap();
		Iterator<Entry<String, String[]>> iterator = request_map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String[]> me = iterator.next();
			String[] values = (String[]) me.getValue();
			for (int i = 0; i < values.length; i++) {
				// /System.out.println(values[i]);
				values[i] = xssClean(values[i]);
				
			}
		}
		return request_map;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] rawValues = super.getParameterValues(name);
		if (rawValues == null){
			return null;
		}
		String[] cleanedValues = new String[rawValues.length];
		for (int i = 0; i < rawValues.length; i++) {
			cleanedValues[i] = xssClean(rawValues[i]);
		}
		return cleanedValues;
	}

	@Override
	public String getParameter(String name) {
		String str = super.getParameter(name);
		if (str == null){
			return null;
		}
		return xssClean(str);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		if(XssScanUtils.isXssHeader(antiSamyProxy.getPolicyHeaders(), name)){
			return new AntiSamyEnumeration( super.getHeaders(name), antiSamyProxy);
		}
        return super.getHeaders(name);
    }
	
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null){
			return null;
		}
		if(XssScanUtils.isXssHeader(antiSamyProxy.getPolicyHeaders(), name)){
			return xssClean(value);
		}
		return value;
	}
	
	@Override
	public Cookie[] getCookies() {
		Cookie[] existingCookies = super.getCookies();
		if (existingCookies != null) {
			for (int i = 0; i < existingCookies.length; ++i) {
				Cookie cookie = existingCookies[i];
				cookie.setValue(xssClean(cookie.getValue()));
			}
		}
		return existingCookies;
	}

	@Override
	public String getQueryString() {
		return xssClean(super.getQueryString());
	}

	public String xssClean(String taintedHTML) {
		return AntiSamyScanUtils.xssClean(_getHttpServletRequest(), antiSamyProxy, taintedHTML);
	}
	
	protected HttpServletRequest _getHttpServletRequest() {
		 return (HttpServletRequest) super.getRequest();
    }

}
