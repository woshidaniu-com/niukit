/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idstar.servlet.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.idstar.shiro.token.IDstarAuthenticationToken;
import com.woshidaniu.authz.idstar.utils.IDstarSsoUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.filter.AbstractAuthenticatingFilter;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @className	： IDstarAuthenticatingFilter
 * @description	： 整合金智的单点认证登录
 * @author 		：大康（743）
 * @date		： 2017年8月24日 下午7:51:14
 * @version 	V1.0
 */
@Deprecated
public class IDstarAuthenticatingFilter extends AbstractAuthenticatingFilter {
 
	protected Logger LOG = LoggerFactory.getLogger(IDstarAuthenticatingFilter.class);
	protected String configPath = "/WEB-INF/properties/client.properties";

	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
		this.configPath = filterConfig.getInitParameter("configPath");
	}
	 
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) {
		
		try {
			
			// 第一步：获取Cookie中的Token信息
			String token = IDstarSsoUtils.getTokenInCookie(WebUtils.toHttp(request), WebUtils.toHttp(response));
			if (StringUtils.isEmpty(token)) {
				return;
			}
			
			LOG.debug("Get Token In Cookie ：token = " + token);
			
			// 客户端配置
			String is_config = request.getServletContext().getRealPath(configPath);
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request );
			// 登录账号ID
			String uid = IDstarSsoUtils.getUidByToken(is_config, token);
			// 构造Token对象
			AuthenticationToken authToken = new IDstarAuthenticationToken(token, uid, host);
			
			SecurityUtils.getSubject().login(authToken);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
	}
	
}
