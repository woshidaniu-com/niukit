package com.woshidaniu.fastxls.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	
	public int index();
	
	public int width()  default 200;
	
	//此字段表示如果一行的数据是map对象,此值作为取数据的key
	public String key() default "";
	
	public String description() default "";
	
}
