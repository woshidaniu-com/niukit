package com.woshidaniu.authz.idsplus.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.idsplus.AuthcConstant;
import com.woshidaniu.authz.idsplus.utils.IdsPlusUtils;
import com.woshidaniu.authz.idsplus.utils.XmlConverUtil;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.basicutils.URLUtils;
import com.woshidaniu.shiro.filter.LogoutListener;
import com.woshidaniu.shiro.filter.ZFLogoutFilter;
import com.woshidaniu.web.utils.WebParameterUtils;
import com.woshidaniu.web.utils.WebUtils;

/**
 * 
 * @className ： IdsPlusShiroSsoLogoutFilter(南京苏迪单点登录注销)
 * @description ： 实现Shiro登录注销与Cas单点登录注销的整合
 * @author ：姜杨勇（111）
 * @date ： 2017年11月15日 上午10:07:50
 * @version V1.0
 */
public class IdsPlusShiroSsoLogoutShiroFilter extends ZFLogoutFilter {
	
	private static final Logger log = LoggerFactory.getLogger(IdsPlusShiroSsoLogoutShiroFilter.class);
	
	private String logoutUrl = "";
	
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
			if(IdsPlusUtils.isCasLogin(request)){
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
				result = idsPlusPreHandle(request, response);
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
	
	
    protected boolean idsPlusPreHandle(ServletRequest request, ServletResponse response) throws Exception {
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
	 * @date ：2017年11月16日 上午9:34:08
	 * @param request
	 * @param response
	 * @return 单点登出请求地址;<br/>
	 *         格式如： <br/>
	 *         http://cas.zyufl.edu.cn/cas/logout?service=http://127.0.0.1/jwglxt/idsPluslogin<br/>
	 */
	@Override
	public String getCasRedirectUrl(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String configPath = httpRequest.getServletContext().getRealPath("/")+ AuthcConstant.IDSPLUS_CAS_CONFIG_PATH;
		String casServerLogoutUrl = XmlConverUtil.getXmlElement(configPath, AuthcConstant.IDSPLUS_CAS_SERVER_LOGOUT_URL);
		String serverName = XmlConverUtil.getXmlElement(configPath, AuthcConstant.IDSPLUS_SERVER_NAME);
		String result = "";
		if(!BlankUtils.isBlank(casServerLogoutUrl)&&!BlankUtils.isBlank(serverName)){
			result = casServerLogoutUrl + "?service=" + 
						URLUtils.escape(
							serverName + (request.getServletContext().getContextPath()).replaceAll("/", "") + getRedirectUrl()
						);
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
