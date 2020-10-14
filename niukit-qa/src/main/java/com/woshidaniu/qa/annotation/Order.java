package com.woshidaniu.qa.annotation; 
/** 
* @author shouquan
* @version 创建时间：2017年5月15日 上午10:40:09 
* 类说明 :方法排序注解 
* */
@java.lang.annotation.Target(value = { java.lang.annotation.ElementType.METHOD })
@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Order {
	public abstract int index() default 1;
}
 