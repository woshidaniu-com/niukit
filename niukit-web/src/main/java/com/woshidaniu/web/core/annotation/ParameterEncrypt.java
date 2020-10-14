package com.woshidaniu.web.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *@类名称	: ParameterEncrypt.java
 *@类描述	：参数加密注解：
 *	1、将此参数标注与model或者action的字段之上，在请求视图返回之前会对值栈中带此注解的参数对象进行加密处理
 *	2、在请求到达方法之前，拦截器会根据此注解对请求过来的参数进行解密处理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 10:10:55 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterEncrypt {

}
