package com.woshidaniu.fastxls.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubColumns {
	
	public abstract int firstCol();
	
	public abstract int lastCol();
	
	public String description() default "";
	
}
