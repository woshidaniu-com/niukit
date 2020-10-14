package com.woshidaniu.safety.struts;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.utils.WebUtils;



public class SecurityFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = 1L;
	
	
	public final String www_url_encode= "application/x-www-form-urlencoded";
	public final String mul_data= "multipart/form-data";
	public final String txt_pla= "text/plain";
	public final String xml_pla= "text/xml";
	
	public void destroy() {

	}

	public void doFilter(final ServletRequest arg0, final ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = WebUtils.toHttp(arg0);
		HttpServletResponse response = WebUtils.toHttp(arg1);
	    
		String contenType=request.getContentType();
		if( StringUtils.isNotEmpty(contenType) 
				&& !contenType.startsWith(www_url_encode) 
				&& !contenType.startsWith(mul_data)
				&& !contenType.startsWith(txt_pla) 
				&& !contenType.startsWith(xml_pla) 
				){
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("非法请求Content-Type！");
			return;
		}
		
		if (!StringUtils.isNull(contenType) && contenType.startsWith(mul_data)){
			chain.doFilter(new Struts2RequestWrap(request), response);    
		} else {
			chain.doFilter(request, response);    
		}
        
	}

	

	public void init(FilterConfig arg0) throws ServletException {

	}
	

}

class Struts2RequestWrap extends HttpServletRequestWrapper{

	private final HttpServletRequest request;
	private final byte[] body;
	
	public Struts2RequestWrap(HttpServletRequest request) throws IOException {
		super(request);
		this.request = request;
		this.body = IOUtils.toByteArray(request.getInputStream());
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream is = new ByteArrayInputStream(body);
		
		byte[] bytes =IOUtils.toByteArray(is);
		String bodyData = new String(bytes, "UTF-8");
		
		if (bodyData.contains("@ognl.OgnlContext@")){
			throw new RuntimeException("非法请求");
		}
		
		final ByteArrayInputStream bis = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bis.read();
			}
		};
	}

	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return request.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	@Override
	public ServletRequest getRequest() {
		return super.getRequest();
	}
	
	
	
}

