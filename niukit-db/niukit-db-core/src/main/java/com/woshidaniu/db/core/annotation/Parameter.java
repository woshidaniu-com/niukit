package com.woshidaniu.db.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

	public String key() default "";
	
}