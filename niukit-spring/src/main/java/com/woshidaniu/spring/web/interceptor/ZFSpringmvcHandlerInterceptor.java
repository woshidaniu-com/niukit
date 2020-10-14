/**
 * <p>Copyright (R) 2014 我是大牛。<p>
 */
package com.woshidaniu.spring.web.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 我是大牛扩展扩展拦截器注解,为了不改变原有我是大牛脚手架的{@link org.springframework.web.servlet.HandlerInterceptor}的配置,
 利用此注解可将配置的Bean额外追加到 {@link org.springframework.web.servlet.HandlerExecutionChain}尾部

 例如:
 @ZFSpringmvcHandlerInterceptor(
 enable=true,
 order=1,
 includePatterns= {"/a/b/c","/d/e**"},
 excludePatterns="/a/b/c/f.zf"
 )
 @Component
 public class MyInterceptor implements org.springframework.web.servlet.HandlerInterceptor{

 @Override
 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
 //do something
 return false;
 }

 @Override
 public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
 //do something
 }

 @Override
 public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
 //do something
 }
 }

 这里的MyInterceptor最好配置成单例的.这里是实现{@link org.springframework.web.servlet.HandlerInterceptor}

  * @author 		：康康（1571）
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZFSpringmvcHandlerInterceptor {

    /**
     * @description	： 是否启用
     * @return
     */
    boolean enable() default true;

    /**
     * @description	： 追加顺序,顺序越小，越靠前
     * @return
     */
    int order() default Integer.MAX_VALUE;

    /**
     * @description	： 拦截路径模式
     * @return
     */
    String[] includePatterns ();

    /**
     * @description	： 排除在外的路径模式
     * @return
     */
    String[] excludePatterns();

}
