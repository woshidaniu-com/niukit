package org.springframework.enhanced.aop.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.enhanced.aop.Operation;
/**
 * 
 * @className: Comment
 * @description: 日志记录注解：有此注解的方法才会记录日志
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
public @interface Comment {
	
	/**
	 * 日志记录组：标识着某几次记录为一组
	 */
	public abstract String group() default "";
	
	/**
	 * 功能模块代码
	 */
	public abstract String model() default "";
	
	/**
	 * 业务模块代码
	 */
	public abstract String business() default "";
	
	/**
	 * 调用的SQL的命名空间：默认是当前service接口的泛型参数第二个；dao接口路径；
	 * 如果指定，表示操作的SQL 不在当前service的Mapper.xml下
	 */
	public abstract String namespace() default "";
	
	/**
	 * 调用的SQL ID;当前调用方法的名称,在ID与方法名称不相同时需要指定
	 */
	public abstract String statement() default "";
	
	/**
	 * 是否记录操作描述:默认true
	 */
	public abstract boolean recordDesc() default true;
	
	/**
	 * 是否记录SQL;默认false
	 */
	public abstract boolean recordSQL() default false;
	
	/**
	 * 是否一组日志记录中的第一个；一組SQl记录是此参数必要
	 */
	public abstract boolean first() default false;
	
	/**
	 * 操作类型
	 */
	public abstract Operation opt() default Operation.OP_INSERT;

}