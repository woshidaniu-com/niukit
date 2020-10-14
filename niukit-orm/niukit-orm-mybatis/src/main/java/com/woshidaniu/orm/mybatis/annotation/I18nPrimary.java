package com.woshidaniu.orm.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *@类名称		： I18nPrimary.java
 *@类描述		：该注解用于方法，字段；指明主键字段
 *@创建人		：kangzhidong
 *@创建时间	：Feb 8, 2017 9:34:32 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v2.0.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface I18nPrimary {

	public abstract String value() default "";
	
}
