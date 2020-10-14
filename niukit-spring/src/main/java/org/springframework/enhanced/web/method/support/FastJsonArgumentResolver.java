package org.springframework.enhanced.web.method.support;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 *@类名称		： FastJsonArgumentResolver.java
 *@类描述		： http://zjumty.iteye.com/blog/1927890
 *@创建人		：kangzhidong
 *@创建时间	：2017年5月15日 下午3:40:47
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class FastJsonArgumentResolver implements HandlerMethodArgumentResolver {  
    @Override  
    public boolean supportsParameter(MethodParameter parameter) {  
        return parameter.getParameterAnnotation(FastJson.class) != null;  
    }  
  
    @Override  
    public Object resolveArgument(MethodParameter parameter,  
                                  ModelAndViewContainer mavContainer,  
                                  NativeWebRequest webRequest,  
                                  WebDataBinderFactory binderFactory) throws Exception {  
  
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);  
        // content-type不是json的不处理  
        if (!request.getContentType().contains("application/json")) {  
            return null;  
        }  
  
        // 把reqeust的body读取到StringBuilder  
        BufferedReader reader = request.getReader();  
        StringBuilder sb = new StringBuilder();  
  
        char[] buf = new char[1024];  
        int rd;  
        while((rd = reader.read(buf)) != -1){  
            sb.append(buf, 0, rd);  
        }  
  
        // 利用fastjson转换为对应的类型  
        if(JSONObjectWrapper.class.isAssignableFrom(parameter.getParameterType())){  
            return new JSONObjectWrapper(JSONObject.parseObject(sb.toString()));  
        } else {  
            return JSONObject.parseObject(sb.toString(), parameter.getParameterType());  
        }  
    }  
}  