/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.servlet.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookie;
import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookieAuthcToken;
import com.woshidaniu.authz.zjucookie.utils.CookieSsoUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.filter.AbstractAuthenticatingFilter;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @className ： ZjuCookieAuthenticatingFilter
 * @description ： 兼容浙大基于Cookie的单点认证登录
 * @author ：大康（743）
 * @date ： 2017年8月24日 下午7:50:39
 * @version V1.0
 */
@Deprecated
public class ZjuCookieAuthenticatingFilter extends AbstractAuthenticatingFilter {

	protected Logger LOG = LoggerFactory.getLogger(ZjuCookieAuthenticatingFilter.class);
	private String sessionURL;
	private String userURL;
	private String appUid;
	private String appPwd;

	@Override
	public void setFilterConfig(FilterConfig filterConfig) {
		super.setFilterConfig(filterConfig);

		this.sessionURL = filterConfig.getInitParameter("zjdxsso.sessionURL");
		this.userURL = filterConfig.getInitParameter("zjdxsso.userURL");
		this.appUid = filterConfig.getInitParameter("zjdxsso.appUid");
		this.appPwd = filterConfig.getInitParameter("zjdxsso.appPwd");

	}

	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) {

		try {

			// 第一步：获取cookie中的账号信息
			String uid = CookieSsoUtils.getUidByTokenInCookie(sessionURL, appUid, appPwd, WebUtils.toHttp(request),
					WebUtils.toHttp(response));
			if (StringUtils.isNotEmpty(uid)) {
				LOG.info("Get Uid By Token In Cookie ：uid = " + uid);
				// 登录IP
				String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
				// 构造Token对象
				AuthenticationToken authToken = new ZjuCookieAuthcToken(new ZjuCookie(uid, host));

				SecurityUtils.getSubject().login(authToken);

			}

			// 第二步：如果没有Cookie登录信息则用参数登录

			LOG.info("Cookie SSO Login .");
			String yhm = request.getParameter("yhm");
			String mm = request.getParameter("mm");
			// 验证用户名和密码是否已填
			if (StringUtils.isEmpty(yhm) || StringUtils.isEmpty(mm)) {
				LOG.info("Cookie SSO Login [yhm or mm is empty ].");
				return;
			}
			// SSO登录
			String token = CookieSsoUtils.login(sessionURL, appUid, appPwd, yhm, mm, WebUtils.toHttp(response));
			if (StringUtils.isNotEmpty(token)) {
				// 登录成功，从服务器获取uid
				uid = CookieSsoUtils.getUidByToken(sessionURL, appUid, appPwd, token);
				if (StringUtils.isNotEmpty(uid)) {
					LOG.info("Cookie SSO UID get Success.");
					// 登录IP
					String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
					// 构造Token对象
					AuthenticationToken authToken = new ZjuCookieAuthcToken(new ZjuCookie(uid, host));

					SecurityUtils.getSubject().login(authToken);

				}
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

	}

	public String getSessionURL() {
		return sessionURL;
	}

	public void setSessionURL(String sessionURL) {
		this.sessionURL = sessionURL;
	}

	public String getUserURL() {
		return userURL;
	}

	public void setUserURL(String userURL) {
		this.userURL = userURL;
	}

	public String getAppUid() {
		return appUid;
	}

	public void setAppUid(String appUid) {
		this.appUid = appUid;
	}

	public String getAppPwd() {
		return appPwd;
	}

	public void setAppPwd(String appPwd) {
		this.appPwd = appPwd;
	}

}
