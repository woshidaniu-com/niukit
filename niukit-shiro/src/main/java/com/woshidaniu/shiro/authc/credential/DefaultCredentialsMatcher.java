package com.woshidaniu.shiro.authc.credential;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.CodecSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc MD5加密对比, 密码重试限制, 默认是5次
 */
public class DefaultCredentialsMatcher extends CodecSupport implements CredentialsMatcher {

	private static final Logger log = LoggerFactory.getLogger(DefaultCredentialsMatcher.class);
	
	private static final String DEFAULT_CREDENTIALS_RETRY_CACHE_NAME = "niutal_SHIRO_CREDENTIALS_RETRY_CACHE";

	private static final int DEFALUE_CREDENTIALS_RETRY_LIMIT = 5;

	protected int retryLimit = DEFALUE_CREDENTIALS_RETRY_LIMIT;

	protected String credentialsRetryCacheName = DEFAULT_CREDENTIALS_RETRY_CACHE_NAME;

	protected CacheManager cacheManager;

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		// check retry
		//checkCredentialsRetry(token);
		// credentials match
		//boolean matches = credentialsMatch(token, info);
		//if (matches) {
		//	cacheManager.getCache(getCredentialsRetryCacheName()).remove(token.getPrincipal());
		//}
		return credentialsMatch(token, info);
	}

	protected boolean credentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		Object tokenCredentials = token.getCredentials();
		Object accountCredentials = info.getCredentials();
		return equals(tokenCredentials, accountCredentials);
	}

	protected boolean equals(Object tokenCredentials, Object accountCredentials) {
		if (log.isDebugEnabled()) {
			log.debug("Performing credentials equality check for tokenCredentials of type ["
					+ tokenCredentials.getClass().getName() + " and accountCredentials of type ["
					+ accountCredentials.getClass().getName() + "]");
		}
		if (isByteSource(tokenCredentials) && isByteSource(accountCredentials)) {
			if (log.isDebugEnabled()) {
				log.debug("Both credentials arguments can be easily converted to byte arrays.  Performing "
						+ "array equals comparison");
			}
			byte[] tokenBytes = toBytes(tokenCredentials);
			byte[] accountBytes = toBytes(accountCredentials);
			return Arrays.equals(tokenBytes, accountBytes);
		} else {
			return accountCredentials.equals(tokenCredentials);
		}
	}

	protected void checkCredentialsRetry(AuthenticationToken token) {
		Cache<Object, AtomicInteger> credentialsRetryCache = cacheManager.getCache(getCredentialsRetryCacheName());
		AtomicInteger retryCount = credentialsRetryCache.get(token.getPrincipal());
		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
			credentialsRetryCache.put(token.getPrincipal(), retryCount);
		}
		if (retryCount.incrementAndGet() > getRetryLimit()) {
			throw new ExcessiveAttemptsException();
		}
	}

	public int getRetryLimit() {
		return retryLimit;
	}

	public void setRetryLimit(int retryLimit) {
		this.retryLimit = retryLimit;
	}

	public String getCredentialsRetryCacheName() {
		return credentialsRetryCacheName;
	}

	public void setCredentialsRetryCacheName(String credentialsRetryCacheName) {
		this.credentialsRetryCacheName = credentialsRetryCacheName;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
