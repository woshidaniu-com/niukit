package com.woshidaniu.orm.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *@类名称		： I18nSwitch.java
 *@类描述		：该注解用于方法,当Dao方法被该注解标记时，国际化拦截器（DataI18nColumnInterceptor）会对SQL语句中的特殊字符进行替代，以便灵活进行国际化数据查询切换
 *@创建人		：kangzhidong
 *@创建时间	：Feb 8, 2017 8:56:37 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v2.0.0
 *@see com.woshidaniu.i18n.plugin.DataI18nColumnInterceptor
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface I18nSwitch {
	
	public abstract I18nColumn[] value() default {};
	
}
