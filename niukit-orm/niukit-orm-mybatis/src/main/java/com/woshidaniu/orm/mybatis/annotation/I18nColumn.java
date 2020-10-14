package com.woshidaniu.orm.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *@类名称		： I18nColumn.java
 *@类描述		：该注解用于方法，字段；指明字段
 *@创建人		：kangzhidong
 *@创建时间	：Feb 8, 2017 9:00:15 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v2.0.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface I18nColumn {

	public abstract String column() default "";
	
	public abstract I18nLocale[] i18n();
	
}
