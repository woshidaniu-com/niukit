package com.woshidaniu.web.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称	: MethodAccess.java
 *@类描述	：方法访问权限注解：标注访问权限名称
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 10:10:44 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Documented
@Inherited
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAccess {
	
	public abstract String value();

	
}
