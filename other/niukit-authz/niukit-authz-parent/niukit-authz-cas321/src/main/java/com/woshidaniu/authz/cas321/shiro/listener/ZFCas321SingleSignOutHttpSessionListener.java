package com.woshidaniu.authz.cas321.shiro.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 1571
 */
public class ZFCas321SingleSignOutHttpSessionListener implements HttpSessionListener{
	
	protected final static Logger log = LoggerFactory.getLogger(ZFCas321SingleSignOutHttpSessionListener.class);

	private HttpSessionListener delegate = new SingleSignOutHttpSessionListener();
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.debug("sessionCreated,id="+se.getSession().getId());
		this.delegate.sessionCreated(se);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.debug("sessionDestroyed,id="+se.getSession().getId());
		this.delegate.sessionDestroyed(se);
	}
}
