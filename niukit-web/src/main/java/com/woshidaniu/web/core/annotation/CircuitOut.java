package com.woshidaniu.web.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称	: CircuitOut.java
 *@类描述	：流程检查注解，用于action的方法，检查该方法指定的module下的功能是否已经到了指定的流程
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 10:10:24 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Target({ElementType.METHOD })
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CircuitOut {
	
	/**
	 * 
	 * 模块代码 
	 */
	public abstract String module();

	/**
	 * 
	 * 功能代码 
	 */
	public abstract String func();
	
}
