/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas;

import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @className	： CasContainsPatternsUrlPatternMatcherStrategy
 * @description	： 
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:57:26
 * @version 	V1.0
 */
public class CasContainsPatternsUrlPatternMatcherStrategy implements UrlPatternMatcherStrategy {

    private String[] patterns;

	@Override
	public boolean matches(String url) {
		for (String pattern : patterns) {
			if (url.contains(pattern)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void setPattern(String pattern) {
		this.patterns = StringUtils.tokenizeToStringArray(pattern);
	}
}