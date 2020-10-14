package com.woshidaniu.web.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 *@类名称	: TimeOut.java
 *@类描述	：操作开关检查注解，用于action的方法，检查该方法指定的module下的功能是否已经到了开放时间和允许范围
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 10:11:38 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Target({ElementType.METHOD })
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeOut {
	
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
