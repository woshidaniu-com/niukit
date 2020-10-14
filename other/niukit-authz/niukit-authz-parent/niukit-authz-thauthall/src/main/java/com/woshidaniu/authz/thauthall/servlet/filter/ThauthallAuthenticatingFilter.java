/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.thauthall.servlet.filter;

import java.util.Hashtable;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.thauthall.shiro.token.ThauthallAuthenticationToken;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.servlet.filter.AbstractAuthenticatingFilter;
import com.woshidaniu.web.utils.WebRequestUtils;

import thauth.Roam;
import thauth.ThauthConst;

/**
 * 
 * @className	： ThauthallAuthenticatingFilter
 * @description	： 华中师范大学单点登录Shiro集成
 * @author 		：大康（743）
 * @date		： 2017年8月24日 下午7:50:56
 * @version 	V1.0
 */
@SuppressWarnings("rawtypes")
@Deprecated
public class ThauthallAuthenticatingFilter extends AbstractAuthenticatingFilter {

	protected Logger LOG = LoggerFactory.getLogger(ThauthallAuthenticatingFilter.class);
	protected String appId;
	
	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
		this.appId = filterConfig.getInitParameter("appId");
	}
	
	@Override
	protected void onAccessDeniad(ServletRequest request, ServletResponse response) {
		
		try {
			
			// 第一步：获取请求中的票据信息
			
			// 票据ID
			String ticketId = request.getParameter("ticket");
			if (StringUtils.isEmpty(ticketId)) {
				return;
			}
			
			LOG.debug("Get ticketId In Request ：ticketId = " + ticketId);
			
			// 登录IP
			String host = WebRequestUtils.getRemoteAddr((HttpServletRequest) request);
			
			// 第二步： 根据票据获取用户信息
			Hashtable ht = Roam.thauthCheckTicket(ticketId, appId, host);
			// 登录状态
			int code = ((Integer) ht.get(ThauthConst.THAUTH_CODE)).intValue();
			// 单位
			String dw = (String) ht.get(ThauthConst.THAUTH_DW);
			// 邮箱
			String email = (String) ht.get(ThauthConst.THAUTH_EMAIL);
			// 密码
			String mm = (String) ht.get(ThauthConst.THAUTH_MM);
			// 秘钥
			String skey = (String) ht.get(ThauthConst.THAUTH_SKEY);
			// 票据
			String ticket = (String) ht.get(ThauthConst.THAUTH_TICKET);
			// 姓名
			String xm = (String) ht.get(ThauthConst.THAUTH_XM);
			// 用户类别
			String yhlb = (String) ht.get(ThauthConst.UUTYPE_YHLB);
			// 用户名
			String yhm = (String) ht.get(ThauthConst.THAUTH_YHM);
			// 用户状态
			String yhzt = (String) ht.get(ThauthConst.THAUTH_YHZT);
			// 职工号
			String zjh = (String) ht.get(ThauthConst.THAUTH_ZJH);
	
			// 构造Token对象
			AuthenticationToken authToken = new ThauthallAuthenticationToken(code, dw, email, mm, skey, ticket, xm, yhlb, yhm, yhzt, zjh, host);
			
			SecurityUtils.getSubject().login(authToken);
			
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
	}
	
}
