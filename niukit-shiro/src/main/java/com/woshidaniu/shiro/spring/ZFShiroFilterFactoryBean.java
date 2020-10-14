package com.woshidaniu.shiro.spring;

import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;

import com.woshidaniu.shiro.filter.ZFShiroFilter;

public class ZFShiroFilterFactoryBean extends ShiroFilterFactoryBean {

	private static transient final Logger log = LoggerFactory.getLogger(ZFShiroFilterFactoryBean.class);

	private boolean casLogin;

	@Override
	protected AbstractShiroFilter createInstance() throws Exception {

		log.debug("Creating Shiro Filter instance.");

		SecurityManager securityManager = getSecurityManager();
		if (securityManager == null) {
			String msg = "SecurityManager property must be set.";
			throw new BeanInitializationException(msg);
		}

		if (!(securityManager instanceof WebSecurityManager)) {
			String msg = "The security manager does not implement the WebSecurityManager interface.";
			throw new BeanInitializationException(msg);
		}

		FilterChainManager manager = createFilterChainManager();

		// Expose the constructed FilterChainManager by first wrapping it in a
		// FilterChainResolver implementation. The AbstractShiroFilter
		// implementations
		// do not know about FilterChainManagers - only resolvers:
		
		//origin
		//PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
		
		//hotfix 安全问题
		PathMatchingFilterChainResolver chainResolver = new com.woshidaniu.shiro.filter.mgt.SafePathMatchingFilterChainResolver();
		chainResolver.setFilterChainManager(manager);

		// Now create a concrete ShiroFilter instance and apply the acquired
		// SecurityManager and built
		// FilterChainResolver. It doesn't matter that the instance is an
		// anonymous inner class
		// here - we're just using it because it is a concrete
		// AbstractShiroFilter instance that accepts
		// injection of the SecurityManager and FilterChainResolver:
		return new ZFSpringShiroFilter((WebSecurityManager) securityManager, chainResolver);
	}

	/**
	 * Returns
	 * <code>{@link org.apache.shiro.web.servlet.AbstractShiroFilter}.class</code>
	 *
	 * @return <code>
	 *         {@link org.apache.shiro.web.servlet.AbstractShiroFilter}.class</code>
	 */
	@Override
	public Class getObjectType() {
		return ZFSpringShiroFilter.class;
	}

	public String getCasLoginUrl() {
		return null;
	}
	
	public String getCasLoginUrl(AuthenticationFilter filter) {
		return null;
	}
	
	private void applyCasLoginUrlIfNecessary(Filter filter) {
		if ( isCasLogin() && (filter instanceof AuthenticationFilter) ) {
			AuthenticationFilter acFilter = (AuthenticationFilter) filter;
			String casloginUrl = null;
			if(StringUtils.hasText(acFilter.getSuccessUrl())){
				casloginUrl = this.getCasLoginUrl(acFilter);
			}
			else {
				casloginUrl = this.getCasLoginUrl();
            }
			acFilter.setSuccessUrl(casloginUrl);
            acFilter.setLoginUrl(casloginUrl);
		}
	}
	
	@Override
	protected FilterChainManager createFilterChainManager() {
		
		FilterChainManager manager = super.createFilterChainManager();

		Map<String, Filter> defaultFilters = manager.getFilters();
		for (Filter filter : defaultFilters.values()) {
			applyCasLoginUrlIfNecessary(filter);
		}

		return manager;
	}

	/**
	 * Inspects a bean, and if it implements the {@link Filter} interface,
	 * automatically adds that filter instance to the internal
	 * {@link #setFilters(java.util.Map) filters map} that will be referenced
	 * later during filter chain construction.
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof Filter) {
			log.debug("Found filter chain candidate filter '{}'", beanName);
			Filter filter = (Filter) bean;
			applyCasLoginUrlIfNecessary(filter);
		}
		super.postProcessBeforeInitialization(bean, beanName);
		return bean;
	}

	/**
	 * Ordinarily the {@code AbstractShiroFilter} must be subclassed to
	 * additionally perform configuration and initialization behavior. Because
	 * this {@code FactoryBean} implementation manually builds the
	 * {@link AbstractShiroFilter}'s
	 * {@link AbstractShiroFilter#setSecurityManager(org.apache.shiro.web.mgt.WebSecurityManager)
	 * securityManager} and
	 * {@link AbstractShiroFilter#setFilterChainResolver(org.apache.shiro.web.filter.mgt.FilterChainResolver)
	 * filterChainResolver} properties, the only thing left to do is set those
	 * properties explicitly. We do that in a simple concrete subclass in the
	 * constructor.
	 */
	private static final class ZFSpringShiroFilter extends ZFShiroFilter {

		protected ZFSpringShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
			super();
			if (webSecurityManager == null) {
				throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
			}
			setSecurityManager(webSecurityManager);
			if (resolver != null) {
				setFilterChainResolver(resolver);
			}
		}
	}

	public boolean isCasLogin() {
		return casLogin;
	}

	public void setCasLogin(boolean casLogin) {
		this.casLogin = casLogin;
	}

}
