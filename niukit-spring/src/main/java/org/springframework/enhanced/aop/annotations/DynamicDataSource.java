package org.springframework.enhanced.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @className: DynamicDataSource
 * @description: 动态数据源切换注解：有此注解的dao方法会自动切换数据源
 * @author : kangzhidong
 * @date : 下午06:27:47 2014-6-7
 * @modify by:
 * @modify date :
 * @modify description :
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DynamicDataSource {

	/**
	 * 数据源的Name
	 */
	public abstract String dataSource() default "";
	
}
