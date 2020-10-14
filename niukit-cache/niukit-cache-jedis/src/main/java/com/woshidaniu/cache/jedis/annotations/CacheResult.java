package com.woshidaniu.cache.jedis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.woshidaniu.cache.jedis.client.IJedisCacheClient;
/**
 * 
 * @className: CacheResult
 * @description: 缓存结果注解：有此注解时判断key是否不为空，不为空则作为缓存对象的key
 * @author : kangzhidong
 * @date : 下午07:11:30 2015-1-27
 * @modify by:
 * @modify date :
 * @modify description :
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CacheResult {
	
	/**
	 * 缓存的key
	 */
	public abstract String key() default "";
	/**
	 * 缓存的周期
	 */
	public abstract int expiry() default IJedisCacheClient.CACHE_EXP_WEEK;
	
}