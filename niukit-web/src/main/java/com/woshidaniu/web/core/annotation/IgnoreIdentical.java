package com.woshidaniu.web.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称	: IgnoreIdentical.java
 *@类描述	：标记忽略会话用户与指定参数值一致性判断注解
 *@创建人	：kangzhidong
 *@创建时间	：Mar 9, 2016 2:58:27 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface IgnoreIdentical {
	
}
