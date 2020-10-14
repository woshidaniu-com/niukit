package com.woshidaniu.cache.jedis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className: CacheExpire
 * @description: 缓存过期注解：有此注解的方法取药设置过期的缓存对象
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
public @interface CacheExpire {
	

	public abstract String key() default "";

	public abstract String[] keys() default "";
	

}