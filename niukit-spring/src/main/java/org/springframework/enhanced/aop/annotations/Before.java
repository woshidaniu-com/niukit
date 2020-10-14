package org.springframework.enhanced.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className: Before
 * @description: Aop方法前置通知注解：有此注解的方法才会继续其他的操作
 * @author : kangzhidong
 * @date : 下午06:27:47 2014-6-7
 * @modify by:
 * @modify date :
 * @modify description :
 */
@Target({ ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Before {
	
	/**
	 * bean对象中要调用的方法
	 */
	public abstract String method() default "doBefore";
	
}