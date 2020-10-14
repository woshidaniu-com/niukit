 package com.woshidaniu.safety.xss.cache;


import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;

/**
 * 
 *@类名称	: PolicyCacheManager.java
 *@类描述	：Policy对象缓存管理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 30, 2016 2:10:16 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class PolicyCacheManager {
	
	protected static Logger LOG = LoggerFactory.getLogger(PolicyCacheManager.class);
	protected static ConcurrentMap<String, Policy> COMPLIED_POLICY = new ConcurrentHashMap<String, Policy>();
	private volatile static PolicyCacheManager singleton;

	public static PolicyCacheManager getInstance() {
		if (singleton == null) {
			synchronized (PolicyCacheManager.class) {
				if (singleton == null) {
					singleton = new PolicyCacheManager();
				}
			}
		}
		return singleton;
	}
	
	private PolicyCacheManager(){
		
	}
	
	public Policy getXssPolicy(String path) throws PolicyException{
		if(!BlankUtils.isBlank(path)){
			Policy ret = COMPLIED_POLICY.get(path);
			if (ret != null) {
				return ret;
			}
			ret = Policy.getInstance(path);
			Policy existing = COMPLIED_POLICY.putIfAbsent(path, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	
	public Policy getXssPolicy(URL url) throws PolicyException{
		if(!BlankUtils.isBlank(url)){
			Policy ret = COMPLIED_POLICY.get(url);
			if (ret != null) {
				return ret;
			}
			ret = Policy.getInstance(url);
			Policy existing = COMPLIED_POLICY.putIfAbsent(url.getPath(), ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	
	
	public Policy getXssPolicy(File policy) throws PolicyException{
		if(!BlankUtils.isBlank(policy) && policy.exists()){
			Policy ret = COMPLIED_POLICY.get(policy.getName());
			if (ret != null) {
				return ret;
			}
			ret = Policy.getInstance(policy);
			Policy existing = COMPLIED_POLICY.putIfAbsent(policy.getName(), ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	
	public void destroy() {
		synchronized (COMPLIED_POLICY) {
			COMPLIED_POLICY.clear();
		}
	}
	
}

