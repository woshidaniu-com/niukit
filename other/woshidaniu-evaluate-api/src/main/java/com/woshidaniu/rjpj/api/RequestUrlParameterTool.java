package com.woshidaniu.rjpj.api;

import java.util.Map;

public interface RequestUrlParameterTool {

	String generateUrlParameterString(String privateKey,Map<String,String> parameters);
	
	boolean verifyUrlParameter(String publicKey,Map<String,String> parameters);
	
	Map<String,String> getOriginParameter(Map<String,String> parameters);
}
