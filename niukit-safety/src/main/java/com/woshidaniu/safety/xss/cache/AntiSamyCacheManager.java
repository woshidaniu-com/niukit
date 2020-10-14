 package com.woshidaniu.safety.xss.cache;


import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.LocationUtils;
import com.woshidaniu.safety.SafetyParameter;

/**
 * 
 *@类名称	: AntiSamyCacheManager.java
 *@类描述	：AntiSamy对象缓存管理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 30, 2016 2:29:19 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class AntiSamyCacheManager {
	
	protected static Logger LOG = LoggerFactory.getLogger(AntiSamyCacheManager.class);
	protected static PolicyCacheManager policyCacheManager = PolicyCacheManager.getInstance();
	protected static ConcurrentMap<Policy, AntiSamy> COMPLIED_ANTISAMY = new ConcurrentHashMap<Policy, AntiSamy>();
	private volatile static AntiSamyCacheManager singleton;

	public static AntiSamyCacheManager getInstance() {
		if (singleton == null) {
			synchronized (AntiSamyCacheManager.class) {
				if (singleton == null) {
					singleton = new AntiSamyCacheManager();
				}
			}
		}
		return singleton;
	}
	
	private AntiSamyCacheManager(){
		
	}
	
	public Policy getXssPolicy(String relativePath){
		/*String path = this.getClass().getClassLoader().getResource(relativePath).getFile();
  	  	if (path.startsWith("file")) {
  	  		path = path.substring(6);
  	    }*/
		Policy xssPolicy = null;
		try {
			URL policy = LocationUtils.getExtendResourceAsURL(relativePath);
			xssPolicy = policyCacheManager.getXssPolicy(policy);
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
			try {
				xssPolicy = Policy.getInstance(SafetyParameter.SAFETY_XSS_DEFAULT_POLICY.getDefault());
			} catch (PolicyException e1) {
				LOG.error(e.getLocalizedMessage());
			}
		}
		return xssPolicy;
	}
	
	public AntiSamy getXssAntiSamy(String relativePath){
		Policy xssPolicy = getXssPolicy(relativePath);
		return getXssAntiSamy(xssPolicy);
	}
	
	public AntiSamy getXssAntiSamy(Policy xssPolicy){
		if(!BlankUtils.isBlank(xssPolicy)){
			AntiSamy ret = COMPLIED_ANTISAMY.get(xssPolicy);
			if (ret != null) {
				return ret;
			}
			ret = new AntiSamy(xssPolicy);
			AntiSamy existing = COMPLIED_ANTISAMY.putIfAbsent(xssPolicy, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	
	public void destroy() {
		synchronized (COMPLIED_ANTISAMY) {
			policyCacheManager.destroy();
			COMPLIED_ANTISAMY.clear();
		}
	}
}

