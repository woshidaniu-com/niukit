/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zfca.shiro.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.shiro.SubjectUtils;
import com.woshidaniu.niuca.tp.cas.client.filter.ZFSSOFilter;

/**
 * 
 * @className ： ZFShiroCasSSOFilter
 * @description ： 我是大牛 cas认证登录Filter,配置在Shiro的Filter链中
 * @author ：康康（1571）
 * @date ： 2018年5月2日 下午3:12:47
 * @version V1.0
 */
public class ZFCasSsoShiroFilter extends AdviceFilter {

	private static Logger log = LoggerFactory.getLogger(ZFCasSsoShiroFilter.class);

	private ZFSSOFilter zfssoFilter;
	
	private String casAuthorizedProxyParam;

	private String casServiceUrlParam;

	private String casRenewParam;

	private String setsessionclassParam;

	private String notCheckURLListParam;

	private boolean wrapRequestParam;

	public void setCasAuthorizedProxyParam(String casAuthorizedProxyParam) {
		this.casAuthorizedProxyParam = casAuthorizedProxyParam;
	}

	public void setCasServiceUrlParam(String casServiceUrlParam) {
		this.casServiceUrlParam = casServiceUrlParam;
	}

	public void setCasRenewParam(String casRenewParam) {
		this.casRenewParam = casRenewParam;
	}

	public void setSetsessionclassParam(String setsessionclassParam) {
		this.setsessionclassParam = setsessionclassParam;
	}

	public void setNotCheckURLListParam(String notCheckURLListParam) {
		this.notCheckURLListParam = notCheckURLListParam;
	}

	public boolean isWrapRequestParam() {
		return wrapRequestParam;
	}

	public void setWrapRequestParam(boolean wrapRequestParam) {
		this.wrapRequestParam = wrapRequestParam;
	}

	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Exception exception = null;

		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			if (log.isDebugEnabled()) {
				log.debug("filter url:" + httpServletRequest.getRequestURI());
			}
			
			if(this.zfssoFilter == null) {
				this.zfssoFilter = new ZFSSOFilter();
				ProxyFilterConfig proxyFilterConfig = new ProxyFilterConfig(this.getFilterConfig(), this,request.getServletContext());
				this.zfssoFilter.init(proxyFilterConfig);
				log.info("create delegate and init:{}",this.zfssoFilter.getClass().getName());
			}

			Subject subject = SubjectUtils.getSubject();
			if (!subject.isAuthenticated()) {
				if (log.isDebugEnabled()) {
					log.debug("not auth,doFilter");
				}
				this.zfssoFilter.doFilter(request, response, chain);
				if (log.isDebugEnabled()) {
					Subject subjectAfterDoFilter = ThreadContext.getSubject();
					boolean isAuth = subjectAfterDoFilter.isAuthenticated();
					log.debug("subject is auth:{}", isAuth);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("authed , skip this doFilter,do next filter");
				}
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			exception = e;
		} finally {
			cleanup(request, response, exception);
		}
	}

	private class ProxyFilterConfig implements FilterConfig {

		private FilterConfig delegate;
		private ZFCasSsoShiroFilter zfCasSSOFilter;
		private ServletContext servletContext;

		public ProxyFilterConfig(FilterConfig delegate, ZFCasSsoShiroFilter zfCasSSOFilter,ServletContext servletContext) {
			super();
			this.delegate = delegate;
			this.zfCasSSOFilter = zfCasSSOFilter;
			this.servletContext = servletContext;
		}

		@Override
		public String getFilterName() {
			return this.delegate.getFilterName();
		}

		@Override
		public ServletContext getServletContext() {
			return this.servletContext;
		}

		@Override
		public String getInitParameter(String name) {
			String value = null;
			if ("setsessionclass".equals(name)) {
				value = this.zfCasSSOFilter.setsessionclassParam;
			} else if ("notCheckURLList".equals(name)) {
				value = this.zfCasSSOFilter.notCheckURLListParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.serviceUrl".equals(name)) {
				value = this.zfCasSSOFilter.casServiceUrlParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.authorizedProxy".equals(name)) {
				value = this.zfCasSSOFilter.casAuthorizedProxyParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.renew".equals(name)) {
				value = this.zfCasSSOFilter.casRenewParam;
			} else if ("com.woshidaniu.niuca.tp.cas.client.filter.wrapRequest".equals(name)) {
				value = Boolean.valueOf(this.zfCasSSOFilter.wrapRequestParam).toString();
			} else {
				value = this.delegate.getInitParameter(name);
			}
			if (log.isDebugEnabled()) {
				log.debug("do call getInitParameter, {} : {}", name, value);
			}
			return value;
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return this.delegate.getInitParameterNames();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		if(this.zfssoFilter != null) {
			log.info("destory delegate :{}",this.zfssoFilter.getClass().getName());
			this.zfssoFilter.destroy();
		}
	}
}
