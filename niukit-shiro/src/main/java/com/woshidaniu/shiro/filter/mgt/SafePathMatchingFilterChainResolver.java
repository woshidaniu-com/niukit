package com.woshidaniu.shiro.filter.mgt;

import javax.servlet.ServletRequest;

import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.util.WebUtils;

/**
 * 
 * hotfix 安全问题
 * 
 * @author 1571
 */
public class SafePathMatchingFilterChainResolver extends PathMatchingFilterChainResolver{

	@Override
	protected String getPathWithinApplication(ServletRequest request) {
		return SafeInnerWebUtils.getPathWithinApplication(WebUtils.toHttp(request));
	}
}
