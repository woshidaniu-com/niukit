package org.springframework.enhanced.web.servlet.mvc.method.annotation;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HttpCrosHeadersReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		//仅仅支持 Rest API
		return returnType.getDeclaringClass().getAnnotation(RestController.class) != null;
	}

	@Override
	@SuppressWarnings("resource")
	public void handleReturnValue(Object returnValue, MethodParameter returnType,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

		mavContainer.setRequestHandled(true);
		
		HttpServletResponse servletResponse = webRequest.getNativeResponse(HttpServletResponse.class);
		ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(servletResponse);
		
		outputMessage.getHeaders().set("Access-Control-Allow-Origin", "*");  
		outputMessage.getHeaders().set("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");  
		outputMessage.getHeaders().set("Access-Control-Max-Age", "3600");
		outputMessage.getHeaders().set("Access-Control-Allow-Headers", "x-requested-with"); 
		
		outputMessage.getBody(); // flush headers
	}

}
