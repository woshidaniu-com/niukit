package com.woshidaniu.orm.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称		： I18nMapper.java
 *@类描述		：该注解用于方法,当Dao方法被该注解标记时，国际化拦截器（DataI18nExternalInterceptor或DataI18nInternalInterceptor）会
 *			 自动查找国际化查询配置，并查询国际化数据，得到国际化数据后进行原结果的行数据映射替换
 *@创建人		：kangzhidong
 *@创建时间	：Feb 8, 2017 9:42:53 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v2.0.0
 *@see com.woshidaniu.i18n.plugin.DataI18nExternalInterceptor
 *@see com.woshidaniu.i18n.plugin.DataI18nInternalInterceptor
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface I18nMapper {
	
	public abstract I18nColumn[] value() default {};
	
}
