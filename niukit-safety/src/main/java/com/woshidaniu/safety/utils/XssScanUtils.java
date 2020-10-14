package com.woshidaniu.safety.utils;


import org.apache.commons.lang3.ArrayUtils;

public class XssScanUtils {
	
	public static boolean isXssHeader(String[] policyHeaders, String name) {
		if(policyHeaders != null && policyHeaders.length > 0){
			return ArrayUtils.contains(policyHeaders, name);
		}
		return false;
	}
	
}
