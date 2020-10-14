package com.woshidaniu.authz.uaac.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iflytek.uaac.client.rest.context.UaacServiceContext;
import com.woshidaniu.authz.uaac.AuthcConstant;
import com.woshidaniu.authz.uaac.utils.UaacSsoUtils;
/*import com.iflytek.uaac.client.rest.context.UaacServiceContext;*/
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.basicutils.URLUtils;
import com.woshidaniu.shiro.filter.LogoutListener;
import com.woshidaniu.shiro.filter.ZFLogoutFilter;
import com.woshidaniu.web.utils.WebParameterUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @className ： UaacShiroSsoLogoutFilter(科大讯飞单点登录注销)
 * @description ： 实现Shiro登录注销与Cas单点登录注销的整合
 * @author ：姜杨勇（111）
 * @date ： 2017年11月22日 上午10:07:50
 * @version V1.0
 */
public class UaacShiroSsoLogoutShiroFilter extends ZFLogoutFilter {
	
	private static final Logger log = LoggerFactory.getLogger(UaacShiroSsoLogoutShiroFilter.class);
	
	private String logoutUrl = "/logout";
	 
	@Override
	public boolean isCasLogin() {
		return true;
	}
	
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		Subject subject = getSubject(request, response);
		//调用事件监听器
		if(getLogoutListeners() != null && getLogoutListeners().size() > 0){
			for (LogoutListener logoutListener : getLogoutListeners()) {
				logoutListener.beforeLogout(subject, request, response);
			}
		}
		Exception ex = null;
		boolean result = false;
		try {
			// 如果是单点登录，需要重新构造登出的重定向地址
			if(UaacSsoUtils.isCasLogin(request)){
		        String redirectUrl = getCasRedirectUrl(request, response);
		        //try/catch added for SHIRO-298:
		        try {
		            subject.logout();
		        } catch (SessionException ise) {
		            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		        }
		        // 重定向到单点登出地址
		        issueRedirect(request, response, redirectUrl);
		        result = false;
			} else {
				// do real thing
				result = uaacPreHandle(request, response);
			}
		} catch (Exception e) {
			ex = e;
		} finally {
			//调用事件监听器
			if(getLogoutListeners() != null && getLogoutListeners().size() > 0){
				for (LogoutListener logoutListener : getLogoutListeners()) {
					if(ex != null){
						logoutListener.onLogoutFail(subject, ex);
					}else{
						logoutListener.onLogoutSuccess(request, response);
					}
				}
			}
			
		}
		
		if(ex != null){
			throw ex;
		}
		return result;
	}
	
	
    protected boolean uaacPreHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        issueRedirect(request, response, getLogoutUrl());
        return false;
    }
	
	
	/**
	 * 
	 * @description ： 构造单点登出请求地址
	 * @author ：姜杨勇（111）
	 * @date ：2017年11月22日 上午9:34:08
	 * @param request
	 * @param response
	 * @return 单点登出请求地址;<br/>
	 *         格式如： <br/>
	 *         http://localhost:9080/uaac-server/logout?service=http://127.0.0.1/jwglxt/uaaclogin<br/>
	 */
	@Override
	public String getCasRedirectUrl(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String requestUrl = String.valueOf(httpRequest.getRequestURL());
		String contextPath = String.valueOf(httpRequest.getContextPath());
		//认证的退出地址
		String casServerLogoutUrl = UaacServiceContext.getUaacConstant().getCasContext() + UaacServiceContext.getUaacConstant().getCasContextLogout();
		String result = "";
		if(!BlankUtils.isBlank(casServerLogoutUrl)&&!BlankUtils.isBlank(requestUrl)&&!BlankUtils.isBlank(contextPath)){
			result = casServerLogoutUrl + "?service=" + URLUtils.escape(requestUrl.split(contextPath)[0]+contextPath+ getRedirectUrl());
		}
		return result;
	}

	public String getLogoutUrl() {
		return StringUtils.hasText(logoutUrl) ? logoutUrl : WebParameterUtils.getString(AuthcConstant.REDIRECT_URL, "/logout" );
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
}
