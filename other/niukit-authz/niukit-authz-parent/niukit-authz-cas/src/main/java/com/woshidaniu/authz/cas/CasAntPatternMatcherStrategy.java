/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.shiro.util.AntPathMatcher;
import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @className	： CasAntPatternMatcherStrategy
 * @description	： 
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:57:22
 * @version 	V1.0
 */
public class CasAntPatternMatcherStrategy implements UrlPatternMatcherStrategy {

	private AntPathMatcher matcher = new AntPathMatcher();
	private String[] patterns;

	@Override
	public boolean matches(String url) {
		try {
			System.out.println(new URL(url).getHost());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		for (String pattern : patterns) {
			if (matcher.match(pattern, url)) {
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
