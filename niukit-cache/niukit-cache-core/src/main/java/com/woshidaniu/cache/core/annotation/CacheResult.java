package com.woshidaniu.cache.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className	： CacheResult
 * @description	： 缓存结果注解：有此注解时判断key是否不为空，不为空则作为缓存对象的key
 * @author 		： kangzhidong
 * @date		： Dec 25, 2015 3:43:16 PM
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
	public abstract int expiry() default 0;
	
}