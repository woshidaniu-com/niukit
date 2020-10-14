package com.woshidaniu.web.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称	: IdenticalAccess.java
 *@类描述	：会话用户与指定参数值一致性权限注解：标注哪个请求参数需与会话中用户ID一致
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 10:10:36 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface IdenticalAccess {
	
	public abstract String value() default "sessionUserKey";
	
}
