package com.woshidaniu.safety.csrf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：生成csrf前端脚本
 *   class : com.woshidaniu.safety.csrf.CsrfJavascriptFilter
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月8日下午8:36:20
 */
public class CsrfJavascriptFilter extends AbstractPathMatchFilter{

	private static final Logger LOG = LoggerFactory.getLogger(CsrfJavascriptFilter.class);
	
	private static final String TOKEN_NAME_IDENTIFIER = "%TOKEN_NAME%";
	
	private static final String TOKEN_VALUE_IDENTIFIER = "%TOKEN_VALUE%";
	
	private static final String DOMAIN_ORIGIN_IDENTIFIER = "%DOMAIN_ORIGIN%";
	
	private static final String DOMAIN_STRICT_IDENTIFIER = "%DOMAIN_STRICT%";
	
	private static final String INJECT_INTO_XHR_IDENTIFIER = "%INJECT_XHR%";
	
	private static final String INJECT_INTO_FORMS_IDENTIFIER = "%INJECT_FORMS%";

	private static final String INJECT_GET_FORMS_IDENTIFIER = "%INJECT_GET_FORMS%";
	
	private static final String INJECT_FORM_ATTRIBUTES_IDENTIFIER = "%INJECT_FORM_ATTRIBUTES%";
	
	private static final String INJECT_INTO_ATTRIBUTES_IDENTIFIER = "%INJECT_ATTRIBUTES%";
	
	private static final String CONTEXT_PATH_IDENTIFIER = "%CONTEXT_PATH%";
	
	private static final String SERVLET_PATH_IDENTIFIER = "%SERVLET_PATH%";
	
	@Override
	protected boolean onPreHandle(ServletRequest request,
			ServletResponse response) throws Exception {
		boolean hasError = false;
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		HttpServletResponse httpResponse = WebUtils.toHttp(response);
		String refererHeader = httpRequest.getHeader("referer");
		
		//获取domain,校验domain 和 referer 是否匹配
		if(refererHeader != null){
			String refererDomain = WebUtils.getDomainName(refererHeader);
			String requestDomain = WebUtils.getDomainName(httpRequest);
			
			if(!refererDomain.equals(requestDomain)){
				LOG.error("CSRF Javascript 请求中的referer头部信息和请求主机信息不匹配，该请求将被拦截。");
				hasError = true;
				httpResponse.sendError(404);
			}
		}
		if(!hasError){
			writeJavaScript(httpRequest, httpResponse);
		}
		//返回Boolean.false,直接response中写入脚本流
		return false;
	}

	/**
	 * 
	 * <p>方法说明：获取js模板实例，并返回给客户端程序<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月9日上午10:25:28<p>
	 */
	private void writeJavaScript(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		response.setContentType("text/javascript");
		response.setCharacterEncoding("utf-8");
		/** build dynamic javascript **/
		String code = CsrfGuard.getInstance().getJavascriptTemplateCode();
		if(LOG.isDebugEnabled()){
			LOG.debug("CSRF SCRIPT: {}", code);
		}
		code = code.replace(TOKEN_NAME_IDENTIFIER, CsrfGuard.CSRF_TOKEN_NAME);
		code = code.replace(TOKEN_VALUE_IDENTIFIER, fetchCsrfToken(request));
		code = code.replace(INJECT_INTO_FORMS_IDENTIFIER, Boolean.toString(true));
		code = code.replace(INJECT_GET_FORMS_IDENTIFIER, Boolean.toString(true));
		code = code.replace(INJECT_FORM_ATTRIBUTES_IDENTIFIER, Boolean.toString(false));
		code = code.replace(INJECT_INTO_ATTRIBUTES_IDENTIFIER, Boolean.toString(true));
		code = code.replace(INJECT_INTO_XHR_IDENTIFIER, Boolean.toString(true));
		code = code.replace(DOMAIN_ORIGIN_IDENTIFIER, WebUtils.getDomainName(request.getRequestURL().toString()));
		code = code.replace(DOMAIN_STRICT_IDENTIFIER, Boolean.toString(true));
		code = code.replace(CONTEXT_PATH_IDENTIFIER, request.getContextPath());
		code = code.replace(SERVLET_PATH_IDENTIFIER, request.getContextPath() + request.getServletPath());
		/** write dynamic javascript **/
		OutputStream output = null;
		PrintWriter writer = null;
		try {
			output = response.getOutputStream();
			writer = new PrintWriter(output);

			writer.write(code);
			writer.flush();
		} finally {
			writer.close();
			output.close();
		}
	}
	
	/**
	 * 
	 * <p>方法说明：获取csrf session token,如果没有设置，就创建个默认的值<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月9日上午9:07:42<p>
	 */
	private String fetchCsrfToken(HttpServletRequest httpRequest){
		Object attribute = httpRequest.getSession().getAttribute(CsrfGuard.SESSION_CSRF_TOKEN_KEY);
		
		if(LOG.isDebugEnabled() && attribute != null){
			LOG.debug("获取存储在HttpSession 中的CSRF Token 值为 {}。" , attribute); 
		}
		
		return attribute == null ? null : attribute.toString();
	}
}
