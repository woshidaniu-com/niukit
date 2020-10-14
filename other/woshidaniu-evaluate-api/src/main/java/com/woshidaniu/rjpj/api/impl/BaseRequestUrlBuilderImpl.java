package com.woshidaniu.rjpj.api.impl;

import java.util.Map;

import com.woshidaniu.rjpj.api.RequestUrlBuilder;

/**public**/ abstract class BaseRequestUrlBuilderImpl implements RequestUrlBuilder{

	@Override
	public String build(String key,String apiUrl, Map<String, String> parameters) {
		if(apiUrl == null){
			throw new IllegalArgumentException("apiUrl is null!");
		}
		
		String trimedApiUrl = apiUrl.trim();
		if(trimedApiUrl.equals("")){
			throw new IllegalArgumentException("apiUrl is empty!");
		}
		
		if(trimedApiUrl.startsWith("https://") || trimedApiUrl.startsWith("http://")){
		}else{
			throw new IllegalArgumentException("wrong apiUrl format!"); 
		}
		
		if(trimedApiUrl.endsWith("/")){
			trimedApiUrl = trimedApiUrl.substring(0, trimedApiUrl.length()-1);
		}
		
		if(parameters == null){
			throw new IllegalArgumentException("parameters is null!");
		}
		
		return doBuild(key, trimedApiUrl, parameters);
	}
	
	protected abstract String doBuild(String key,String apiUrl, Map<String, String> parameters);
}
