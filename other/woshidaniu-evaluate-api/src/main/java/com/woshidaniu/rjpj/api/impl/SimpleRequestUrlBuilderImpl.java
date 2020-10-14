package com.woshidaniu.rjpj.api.impl;

import java.util.Map;

import com.woshidaniu.rjpj.api.RequestUrlParameterTool;

/**
 * 简单md5签名的请求路径构建起
 * @author 1571
 */
public class SimpleRequestUrlBuilderImpl extends BaseRequestUrlBuilderImpl{

	private RequestUrlParameterTool requestUrlParameterTool = new SimpleRequestUrlParameterToolImpl();
	
	@Override
	protected String doBuild(String key, String apiUrl, Map<String, String> parameters) {
		String paramStr = requestUrlParameterTool.generateUrlParameterString(key,parameters);
		return apiUrl +"?" + paramStr;
	}

}
