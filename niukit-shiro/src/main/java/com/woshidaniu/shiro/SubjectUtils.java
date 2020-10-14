/**
 * 
 */
package com.woshidaniu.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;

import com.woshidaniu.web.context.WebContext;

/**
 * @author zhangxb
 * @desc 获取session principals等工具类
 *
 */
public abstract class SubjectUtils{

	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
   /**
     * Creates a {@link WebSubject} instance to associate with the incoming request/response pair which will be used
     * throughout the request/response execution.
     *
     * @param request  the incoming {@code ServletRequest}
     * @param response the outgoing {@code ServletResponse}
     * @return the {@code WebSubject} instance to associate with the request/response execution
     * @since 1.0
     */
	public static Subject getWebSubject(){
		Subject subject = ThreadContext.getSubject();
        if (subject == null) {
        	subject = new WebSubject.Builder( WebContext.getRequest(), WebContext.getResponse()).buildWebSubject();
            ThreadContext.bind(subject);
        }
		return SecurityUtils.getSubject();
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getPrincipal(Class<T> clazz){
		Object principal = getSubject().getPrincipal();
		if(principal.getClass().isAssignableFrom(clazz) ){
			return (T)principal;
		}
		return null;
	}
	
	public static Object getPrincipal(){
		return getSubject().getPrincipal();
	}
	
	public static boolean isAuthenticated(){
		return getSubject().isAuthenticated();
	}
	
	public static Session getSession(){
		return getSubject().getSession();
	}
	
	public static Session getSession(boolean create){
		return getSubject().getSession(create);
	}
}
