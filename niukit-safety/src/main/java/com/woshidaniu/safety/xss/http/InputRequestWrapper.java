/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.safety.xss.http;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @className	： InputRequestWrapper
 * @description	： 处理表单提交的请求包装请求,用于input,textarea这两个标签的表单内容
 * @author 		：康康（1571）
 * @date		： 2018年8月8日 上午11:28:10
 * @version 	V1.0
 */
public class InputRequestWrapper extends HttpServletRequestWrapper{

	public InputRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> request_map = super.getParameterMap();
		Iterator<Entry<String, String[]>> iterator = request_map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String[]> me = iterator.next();
			String[] values = (String[]) me.getValue();
			for (int i = 0; i < values.length; i++) {
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

	private String xssClean(String str) {
		if(str == null) {
			return null;
		}
		str = str.trim();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			String filtered = "";
			char c = str.charAt(i);
			switch (c) {
			case '<':
				filtered = "&lt;";
				builder.append(filtered);
				break;
			case '>':
				filtered = "&gt;";
				builder.append(filtered);
				break;
			case '&':
				filtered = "&amp;";
				builder.append(filtered);
			case '"':
				filtered = new String(new char[] {'"'});
				builder.append(filtered);
				break;
			case '\'':
				filtered = "'";
				builder.append(filtered);
				break;
			default:
				filtered = new String(new char[] {c});
				builder.append(filtered);
			}
		}
		return builder.toString();
	}
}
