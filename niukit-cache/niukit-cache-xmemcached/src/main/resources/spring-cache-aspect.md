<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:util="http://www.springframework.org/schema/util" xmlns:security="http://www.springframework.org/schema/security"
	xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:jpa="http://www.springframework.org/schema/data/jpa"
	xsi:schemalocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        			http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
        			 http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
                    http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
                    http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd
					http://www.springframework.org/schema/security  http://www.springframework.org/schema/security/spring-security.xsd
       	 			http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
       	 			http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd"
	default-autowire="byName" default-lazy-init="false">

	<!--基于方法的缓存切面-->
	<bean id="cacheMethodAspect" class="com.woshidaniu.fastcache.core.interceptor.MethodCachedAspectInterceptor">
		<property name="interceptors">
			<list>
		        <ref bean="memcachedInterceptor" />
		    </list>
		</property> 
	</bean>

	<!-- Spring AOP Config -->
	<aop:config expose-proxy="true" proxy-target-class="true">  
        <!--  * *..service*..*(..))  -->
        <aop:aspect id="cacheAspect" ref="cacheMethodAspect">
        	<aop:pointcut id="cachePointcut" expression=" execution(* *..service..*.*(..)) or @annotation(com.woshidaniu.fastcache.core.annotation.CacheResult) "/>
        	<aop:before pointcut-ref="cachePointcut" method="before"/>
        	<aop:around pointcut-ref="cachePointcut" method="around"/>
        </aop:aspect>
        <aop:aspect id="updateCacheAspect" ref="cacheMethodAspect">
        	<aop:pointcut id="updateCachePointcut" expression=" execution(* *..service..*.*(..)) and @annotation(com.woshidaniu.fastcache.core.annotation.CacheExpire) "/>
            <aop:after-returning pointcut-ref="updateCachePointcut" method="afterReturning"  returning="returnValue" /> 
        </aop:aspect>
    </aop:config>
    
</beans>