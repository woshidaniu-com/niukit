/**
 * 我是大牛软件股份有限公司
 */
package com.woshidaniu.shiro.filter.session;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.URLUtils;

/**
 * @类名 SessionControlFilter.java
 * @工号 [1036]
 * @姓名 zhangxiaobin
 * @创建时间 2016 2016年4月19日 下午4:37:02
 * @功能描述 Session Control Filter, 用户session控制
 * 			<br/>只允许用户在一个地方登录
 * 
 */
public abstract class SessionControlFilter extends AuthenticationFilter {

	private static final String DEFAULT_SESSION_CONTROL_CACHE_NAME = "niutal_SHIRO_SESSION_CONTROL_CACHE";
	
	private static final Logger log = LoggerFactory.getLogger(SessionControlFilter.class);
	
	/**
	 * 用户sessionControl的缓存
	 */
	protected Cache<String, Map<Serializable, SessionControl>> sessionControlCache;

	protected CacheManager cacheManager;
	
	protected String sessionControlCacheName = DEFAULT_SESSION_CONTROL_CACHE_NAME;
	//忽略多地同时登陆踢出的用户列表
	protected List<String> ignoreKickoutUsers = new ArrayList<String>(1);
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response, Object mappedValue) {
		//没有登录的情况下直接pass该过滤器
		return !getSubject(request, response).isAuthenticated();
	}
	
	@Override
	protected boolean onAccessDenied(ServletRequest request,ServletResponse response) throws IOException {
		
		if(cacheManager == null){
			throw new AuthenticationException("cacheManage must be set for this filter");
		}
		if(this.sessionControlCache == null){
			sessionControlCache = cacheManager.getCache(getSessionControlCacheName());
		}
		Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        Serializable sessionId = session.getId();
        SessionControl sessionControl = new SessionControl(sessionId,SessionControl.STATE_VALID);
        
        //用户名
        String principal = getSessionControlCacheKey(subject.getPrincipal());
        if (ignoreKickoutUsers.contains(principal)) {
        	log.warn("忽略用户:{}的多地同时登陆处理,会话id:{}", principal, subject.getSession().getId());
        	return true;
        }
        
        Map<Serializable, SessionControl> sessionControlMap = sessionControlCache.get(principal);
        //new Hashtable<Serializable, SessionControl>();
        /**如果缓存为空，则说明该用户首次登录或则是只有它登录*/
        if(sessionControlMap == null || sessionControlMap.isEmpty()) {
        	sessionControlMap = new Hashtable<Serializable, SessionControl>();
        	sessionControlMap.put(sessionId, sessionControl);
        	sessionControlCache.put(principal, sessionControlMap);
        	return true;
        }
        /**如果缓存不为空，则说明有该用户之前登录，需要判断当前session是否存在缓存中
         * 如果存在则说明就是缓存中的用户，无需操作，如果不存在，则说明是当前用户新的登录状态，
         * 需要将当前session状态信息放入缓存中，并将之前登录的session状态修改该INVALIDA*/
        if(!sessionControlMap.containsKey(sessionId)) {
        	Set<Serializable> keySet = sessionControlMap.keySet();
            Iterator<Serializable> iterator = keySet.iterator();
        	//如果存在其他已登录会话，则踢出：设置状态为：INVALID
        	while(iterator.hasNext()){
        		SessionControl logged = sessionControlMap.get(iterator.next());
        		logged.setState(SessionControl.STATE_INVALID);
        	}
        	//压入新的会话控制
        	sessionControlMap.put(sessionId, sessionControl);
        	return true;
        }
        
        /**判断当前session是否不合法，不合法就强制登出*/
        sessionControl = sessionControlMap.get(sessionId);
        if (sessionControl==null || SessionControl.STATE_INVALID.equals(sessionControl.getState())) {
        	try {
        		sessionControlMap.remove(sessionId);
        		log.warn("用户:{}存在多地同时登陆，注销原会话,原会话id:{}", principal, subject.getSession().getId());
        		subject.logout();
        	} catch (Exception e) {
        		log.error("用户登出异常", e);
        	}
            
            saveRequest(request);
            // 检查是否相对目录
            boolean contextRelative = true;
            if(getLoginUrl().contains(URLUtils.escape(request.getScheme() + "://" + request.getServerName() ))){
            	contextRelative = false;
            }
            WebUtils.issueRedirect(request, response, getSuccessUrl(), null, contextRelative);
            return false;
        }
        
        return true;
	}

	protected abstract String getSessionControlCacheKey(Object principal);

	/**
	 * 设置cache
	 * @param cacheManager
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
    }
	 
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public String getSessionControlCacheName() {
		return sessionControlCacheName;
	}

	public void setSessionControlCacheName(String sessionControlCacheName) {
		this.sessionControlCacheName = sessionControlCacheName;
	}

	public List<String> getIgnoreKickoutUsers() {
		return ignoreKickoutUsers;
	}

	public void setIgnoreKickoutUsers(List<String> ignoreKickoutUsers) {
		this.ignoreKickoutUsers = ignoreKickoutUsers;
	}
}
