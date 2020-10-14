package com.woshidaniu.qa.annotation;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD,java.lang.annotation.ElementType.TYPE })
@Retention(RUNTIME)

/**
 * 数据库准备类
 * @author Administrator
 *
 */
public @interface DbPrepare {
	/** 测试开始前执行的sql文件 */
	String[] before() default {};

	/** 测试结束后执行的sql文件 */
	String[] after() default {};
}
