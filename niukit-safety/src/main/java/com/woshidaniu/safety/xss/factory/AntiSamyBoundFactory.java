/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.safety.xss.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.springframework.util.CollectionUtils;

import com.woshidaniu.safety.SafetyParameter;
import com.woshidaniu.safety.SafetyParameters;
import com.woshidaniu.safety.utils.Ini;
import com.woshidaniu.safety.xss.cache.AntiSamyCacheManager;
import com.woshidaniu.web.core.AntPathMatcher;
import com.woshidaniu.web.core.PathMatcher;
import com.woshidaniu.web.core.UrlPathHelper;

/**
 * 
 *@类名称		： AntiSamyBoundFactory.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：2017年8月7日 下午1:42:32
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class AntiSamyBoundFactory {

	/**AntiSamy对象缓存管理*/
	protected static AntiSamyCacheManager antiSamyCacheManager = AntiSamyCacheManager.getInstance();
	/** 扫描器类型，0：DOM类型扫描器,1:SAX类型扫描器；两者的区别如同XML解析中DOM解析与Sax解析区别相同，实际上就是对两种解析方式的实现*/
	protected int scanType = 1;
	/** 请求路径的正则匹配表达式，匹配的路径会被检测XSS*/
	protected String[] includePatterns = null;
	/** 不进行过滤请求路径的正则匹配表达式，匹配的路径不会被检测XSS*/
	protected String[] excludePatterns = null;
	/**防XSS攻击的模块对应的规则配置*/
	protected Map<String,String> policyMappings = new HashMap<String,String>();
	/**需要进行Xss检查的Header*/
	protected String[] policyHeaders = null;
	/**默认的防XSS攻击的规则配置*/
	protected String defaultPolicy = SafetyParameter.SAFETY_XSS_DEFAULT_POLICY.getDefault();
	/**路径解析工具*/
	protected UrlPathHelper urlPathHelper = new UrlPathHelper();
	/**路径规则匹配工具*/
	protected PathMatcher pathMatcher = new AntPathMatcher();
	
	private volatile static AntiSamyBoundFactory singleton;
	public static AntiSamyBoundFactory getInstance() {
		if (singleton == null) {
			synchronized (AntiSamyBoundFactory.class) {
				if (singleton == null) {
					singleton = new AntiSamyBoundFactory();
				}
			}
		}
		return singleton;
	}
	
	private AntiSamyBoundFactory(){
		
	}

	public UrlPathHelper getUrlPathHelper() {
		return urlPathHelper;
	}

	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		this.urlPathHelper = urlPathHelper;
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	/**
	 * 
	 *@描述		：初始化AntiSamy工厂
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 31, 201611:39:33 AM
	 *@param filterConfig
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public void initFactory(FilterConfig filterConfig){
		
		//解析扫描器类型，0：DOM类型扫描器,1:SAX类型扫描器；两者的区别如同XML解析中DOM解析与Sax解析区别相同，实际上就是对两种解析方式的实现 
		this.scanType = SafetyParameters.getInt(filterConfig.getFilterName() , SafetyParameter.SAFETY_XSS_SCANTYPE);
		//解析需要过滤的请求路径的正则匹配表达式
		this.includePatterns = SafetyParameters.getStringArray(filterConfig.getFilterName() ,SafetyParameter.SAFETY_XSS_INCLUDE_PATTERNS);
		//解析不需要过滤的请求路径的正则匹配表达式
		this.excludePatterns = SafetyParameters.getStringArray(filterConfig.getFilterName() ,SafetyParameter.SAFETY_XSS_EXCLUDE_PATTERNS);
		//解析模块化的XSS配置
		this.policyMappings = SafetyParameters.getStringMap(filterConfig.getFilterName() ,SafetyParameter.SAFETY_XSS_POLICY_MAPPINGS);
		//解析默认的防XSS攻击的规则配置
		this.defaultPolicy = SafetyParameters.getString(filterConfig.getFilterName() ,SafetyParameter.SAFETY_XSS_DEFAULT_POLICY);
		
	}
	
	public boolean matches(HttpServletRequest request) {
		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		return this.matches(lookupPath, this.pathMatcher);
	}
	
	/**
	 * Returns {@code true} if the interceptor applies to the given request path.
	 * @param lookupPath the current request path
	 * @param pathMatcher a path matcher for path pattern matching
	 */
	public boolean matches(String lookupPath, PathMatcher pathMatcher) {
		PathMatcher pathMatcherToUse = pathMatcher == null ? this.pathMatcher : pathMatcher;
		if (this.excludePatterns != null) {
			for (String pattern : this.excludePatterns) {
				if (pathMatcherToUse.match(pattern, lookupPath)) {
					return false;
				}
			}
		}
		if (this.includePatterns == null) {
			return true;
		}
		else {
			for (String pattern : this.includePatterns) {
				if (pathMatcherToUse.match(pattern, lookupPath)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public AntiSamyProxy getAntiSamyForRequest(HttpServletRequest request) {
		//解析请求路径
		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		for (String pattern : this.policyMappings.keySet()) {
			if (pathMatcher.match(pattern, lookupPath)) {
				String policy = this.policyMappings.get(pattern);
				Policy xssPolicy = antiSamyCacheManager.getXssPolicy(policy);
				AntiSamy antiSamy = antiSamyCacheManager.getXssAntiSamy(xssPolicy);
				return new AntiSamyProxy(antiSamy, xssPolicy, this.scanType, policyHeaders);
			}
		}
		return getDefaultAntiSamy();
	}
	
	public AntiSamyProxy getDefaultAntiSamy() {
		Policy xssPolicy = antiSamyCacheManager.getXssPolicy(this.defaultPolicy);
		AntiSamy antiSamy = antiSamyCacheManager.getXssAntiSamy(xssPolicy);
		return new AntiSamyProxy(antiSamy, xssPolicy , this.scanType, policyHeaders);
	}
	
	public void destroy() {
		antiSamyCacheManager.destroy();
	}

	public int getScanType() {
		return scanType;
	}

	public void setScanType(int scanType) {
		this.scanType = scanType;
	}

	public String[] getIncludePatterns() {
		return includePatterns;
	}

	public void setIncludePatterns(String[] includePatterns) {
		this.includePatterns = includePatterns;
	}

	public String[] getExcludePatterns() {
		return excludePatterns;
	}

	public void setExcludePatterns(String[] excludePatterns) {
		this.excludePatterns = excludePatterns;
	}

	public Map<String, String> getPolicyMappings() {
		return policyMappings;
	}

	public void setPolicyMappings(Map<String, String> policyMappings) {
		this.policyMappings = policyMappings;
	}

	public void setPolicyDefinitions(String policyDefinitions) {
		try {
			Ini ini = new Ini();
			ini.load(policyDefinitions);
			Ini.Section section = ini.getSection("urls");
			if (CollectionUtils.isEmpty(section)) {
			    section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
			}
			setPolicyMappings(section);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getPolicyHeaders() {
		return policyHeaders;
	}

	public void setPolicyHeaders(String[] policyHeaders) {
		this.policyHeaders = policyHeaders;
	}

	public String getDefaultPolicy() {
		return defaultPolicy;
	}

	public void setDefaultPolicy(String defaultPolicy) {
		this.defaultPolicy = defaultPolicy;
	}
	 
}
