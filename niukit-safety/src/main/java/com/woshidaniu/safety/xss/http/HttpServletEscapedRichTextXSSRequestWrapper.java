package com.woshidaniu.safety.xss.http;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.EntityArrays;

import com.woshidaniu.safety.utils.AntiSamyScanUtils;
import com.woshidaniu.safety.utils.XssScanUtils;
import com.woshidaniu.safety.xss.factory.AntiSamyProxy;

/**
 * @author 1571
 */
public class HttpServletEscapedRichTextXSSRequestWrapper extends HttpServletRequestWrapper{

	private AntiSamyProxy antiSamyProxy = null;
	
	public HttpServletEscapedRichTextXSSRequestWrapper(AntiSamyProxy antiSamyProxy,HttpServletRequest request) {
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
			return doXssClean(value);
		}
		return value;
	}
	
	@Override
	public Cookie[] getCookies() {
		Cookie[] existingCookies = super.getCookies();
		if (existingCookies != null) {
			for (int i = 0; i < existingCookies.length; ++i) {
				Cookie cookie = existingCookies[i];
				cookie.setValue(doXssClean(cookie.getValue()));
			}
		}
		return existingCookies;
	}

	@Override
	public String getQueryString() {
		return doXssClean(super.getQueryString());
	}
	
	public String xssClean(String taintedHTML) {
		
		boolean escaped = isEscaped(taintedHTML);
		
		if(escaped){
			String originHtml = StringEscapeUtils.unescapeHtml4(taintedHTML);
			
			String xssCleanResult = doXssClean(originHtml);
			
			String result = StringEscapeUtils.escapeHtml4(xssCleanResult);
			
			return result;
		}else{
			String xssCleanResult = doXssClean(taintedHTML);
			return xssCleanResult;
		}
	}

	private boolean isEscaped(String taintedHTML) {
		
		if(taintedHTML == null){
			return false;
		}
		
		String[][] arr1 = EntityArrays.BASIC_ESCAPE();
		if(isContains(taintedHTML, arr1)){
			return true;
		}
		
		String[][] arr2 = EntityArrays.ISO8859_1_ESCAPE();
		if(isContains(taintedHTML, arr2)){
			return true;
		}
		
		String[][] arr3 = EntityArrays.HTML40_EXTENDED_ESCAPE();
		if(isContains(taintedHTML, arr3)){
			return true;
		}
		return false;
	}
	
	private boolean isContains(String taintedHTML,String[][] arr) {
		
		for(int i = 0;i<arr.length;i++){
			String first = arr[i][0];
			String second = arr[i][1];
			if(taintedHTML.contains(second)){
				return true;
			}
		}
		return false;
	}

	public String doXssClean(String taintedHTML) {
		return AntiSamyScanUtils.xssClean(_getHttpServletRequest(), antiSamyProxy, taintedHTML);
	}
	
	protected HttpServletRequest _getHttpServletRequest() {
		 return (HttpServletRequest) super.getRequest();
    }
}
