package com.woshidaniu.rjpj.api.impl;

import java.util.Map;

import com.woshidaniu.rjpj.api.RequestUrlParameterTool;

@Deprecated
public class RequestUrlBuilderImpl extends BaseRequestUrlBuilderImpl{

	private RequestUrlParameterTool requestUrlParameterTool = new RequestUrlParameterToolImpl();
	
	@Override
	protected String doBuild(String key, String apiUrl, Map<String, String> parameters) {
		String paramStr = requestUrlParameterTool.generateUrlParameterString(key,parameters);
		return apiUrl +"?" + paramStr;
	}
}
