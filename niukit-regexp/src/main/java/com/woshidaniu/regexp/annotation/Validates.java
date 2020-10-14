package com.woshidaniu.regexp.annotation;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称	: Validates.java
 *@类描述	：验证注解，用于bean的字段或者参数，对字段进行验证
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:45:43 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Target({FIELD,PARAMETER })
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Validates {
	
	boolean blank() default true;
	
	int length() default -1;
	
	int min() default Integer.MIN_VALUE;
	
	int max() default Integer.MAX_VALUE;
	
	String[] in() default {};
	
	String[] not() default {};
	
	String[] contain() default {};
	
	String equals() default "";
	
	String equalsIgnoreCase() default "";
	
	String regexp() default "";
	
	String callback() default "";
}