package com.woshidaniu.regexp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称	: Regexp.java
 *@类描述	：正则表达式注解，用于bean的字段或者参数，检查字段是否匹配正则
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:43:58 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Target({ ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Regexp {
	
	public abstract String pattern();
	
	public RegexpType type() default RegexpType.NORMAL;

}
