package org.springframework.enhanced.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className: Validation
 * @description: 验证注解，有此字段会进行字段规则验证
 * @author : kangzhidong
 * @date : 下午5:53:00 2014-10-22
 * @modify by:
 * @modify date :
 * @modify description :
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Validation {
	
	/**
	 * spring 上下文中的Name
	 */
	public abstract String name() default "commonValidationAspect";
	
	/**
	 * 验证指向的验证规则配置信息的ID
	 */
	public abstract String id() ;
	

}