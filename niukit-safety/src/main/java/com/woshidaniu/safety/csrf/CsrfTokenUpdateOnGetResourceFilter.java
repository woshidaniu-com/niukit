/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.safety.csrf;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;
import com.woshidaniu.web.utils.WebUtils;

/**
 * @description ： 当是GET方法请求资源页面时更新CsrfToken的过滤器,和com.woshidaniu.safety.csrf.CsrfFilter配对进行使用
 * @author ：康康（1571）
 */
public class CsrfTokenUpdateOnGetResourceFilter extends AbstractPathMatchFilter {
	
	private static final String SKIP_CSRF_TOKEN_VALIDATE_KEY = "SKIP_CSRF_TOKEN_VALIDATE_KEY";

	protected boolean onPreHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		String method = httpServletRequest.getMethod();
		if ("GET".equalsIgnoreCase(method)) {
			HttpSession httpSession = httpServletRequest.getSession();
			if (httpSession != null) {
				// 更新CSRF Token
				CsrfGuard.getInstance().updateTokens(httpSession);
				
				//如果获取资源和提交表单的地址是相同的，那么设置一个标志位，使其跳过后续可能存在的CSRF TOKEN验证
				request.setAttribute(SKIP_CSRF_TOKEN_VALIDATE_KEY, Boolean.TRUE.toString());
			}
		}
		return true;
	}

	public void setAppliedPaths(List<String> appliedPaths) {
		this.appliedPaths = appliedPaths;
	}
}
