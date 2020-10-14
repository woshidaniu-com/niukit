package com.woshidaniu.safety.xss;

import javax.servlet.http.HttpServletRequest;

import com.woshidaniu.safety.xss.http.BaseHttpServletRequestWrapper;

public class TrimRequestParameterRequest extends BaseHttpServletRequestWrapper{
	
    public TrimRequestParameterRequest(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    protected String doWrapperParameter(String parameter, String originValue) {
        if(originValue == null) {
            return null;
        }
        String resultStr = originValue.replaceAll("\\s*", "");
        return resultStr;
    }
}