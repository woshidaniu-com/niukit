 package com.woshidaniu.qa.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* @author 作者 E-mail: 
* @version 创建时间：2017年5月12日 下午3:16:28 
* 类说明:全局配置文件 注解
*/
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {
	/**
	 * 
	 * @return
	 */
	String path() default "";

	/**
	 * 
	 * @return
	 */
	String sheetName() default "";

	/**
	 * 
	 * @return
	 */
	int sheetIndex() default 0;
}
 