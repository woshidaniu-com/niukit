<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	http://www.springframework.org/schema/tx
	http://www.springframework.org/schema/tx/spring-tx-3.0.xsd
	http://www.springframework.org/schema/aop
	http://www.springframework.org/schema/aop/spring-aop-3.0.xsd">

	<!--基于方法的缓存切面-->
	<bean id="cacheMethodAspect" class="com.firefly.jedis.aop.JedisMethodAspectInterceptor"></bean>

	<!-- Spring AOP config -->
	<aop:config expose-proxy="true" proxy-target-class="true">  
        <!--  * *..service*..*(..))  -->
        <aop:aspect id="cacheAspect" ref="cacheMethodAspect">
        	<aop:pointcut id="cachePointcut" expression=" execution(* *..service..*.*Cached*(..)) or @annotation(com.firefly.jedis.annotations.CacheResult) "/>
        	<aop:before pointcut-ref="cachePointcut" method="before"/>
        	<aop:around pointcut-ref="cachePointcut" method="around"/>
        </aop:aspect>
        <aop:aspect id="updateCacheAspect" ref="cacheMethodAspect">
        	<aop:pointcut id="updateCachePointcut" expression=" execution(* *..service..*.*(..)) and @annotation(com.firefly.jedis.annotations.CacheExpire) "/>
            <aop:after-returning pointcut-ref="updateCachePointcut" method="afterReturning"  returning="returnValue" /> 
        </aop:aspect>
    </aop:config>  
    
</beans>