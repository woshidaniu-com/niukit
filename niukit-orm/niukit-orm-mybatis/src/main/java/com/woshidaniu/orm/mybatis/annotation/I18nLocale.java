package com.woshidaniu.orm.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface I18nLocale {

	public abstract LocaleEnum locale() default LocaleEnum.zh_CN;
	
	public abstract String column();
	
	public abstract String alias() default "";
	
}
