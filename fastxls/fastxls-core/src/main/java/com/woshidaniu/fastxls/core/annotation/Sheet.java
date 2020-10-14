package com.woshidaniu.fastxls.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sheet {
	
	public String name() default "";

	//前缀（prefix）
	public String prefix() default "";
	
	//词根（stem）
	public String stem() default "";
	
	//后缀（suffix）
	public String suffix() default "";
	
	public String description() default "";
	
}
