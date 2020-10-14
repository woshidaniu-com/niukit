/**
 * 
 */
package com.woshidaniu.safety.csrf.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.woshidaniu.safety.csrf.CsrfGuard;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：TODO
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月5日下午6:50:42
 */
public class CsrfHttpSessionListener implements HttpSessionListener{
	
	//private static final Logger log = LoggerFactory.getLogger(CsrfHttpSessionListener.class);
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		CsrfGuard.getInstance().updateTokens(session);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		//do nothing
	}
	
}
