/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.safety.xss.http;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public abstract class BaseHttpServletRequestWrapper extends HttpServletRequestWrapper{

	public BaseHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> request_map = super.getParameterMap();
		Iterator<Entry<String, String[]>> iterator = request_map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String[]> me = iterator.next();
			String name = me.getKey();
			String[] values = (String[]) me.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = doWrapperParameter(name,values[i]);
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
			cleanedValues[i] = doWrapperParameter(name,rawValues[i]);
		}
		return cleanedValues;
	}

	@Override
	public String getParameter(String name) {
		String str = super.getParameter(name);
		if (str == null){
			return null;
		}
		return doWrapperParameter(name,str);
	}
	
	protected abstract String doWrapperParameter(String parameter,String originValue);
}
