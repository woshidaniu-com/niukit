<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:util="http://www.springframework.org/schema/util" xmlns:security="http://www.springframework.org/schema/security"
	xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:jpa="http://www.springframework.org/schema/data/jpa"
	xmlns:cache="http://www.springframework.org/schema/cache" 
	xsi:schemalocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        			http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
        			 http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
                    http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
                    http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd
					http://www.springframework.org/schema/security  http://www.springframework.org/schema/security/spring-security.xsd
       	 			http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd
       	 			http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd
       	 			http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd"
	default-autowire="byName" default-lazy-init="false">
	
	<!-- 加载memcached配置文件 -->
	<context:property-placeholder location="ehcahce.properties" /> 
	
	<!-- 配置eh缓存管理器 -->
	<bean id="ehcacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
		<property name="configLocation" value="classpath:ehcache.xml"/>
		<!-- ehcache缓存管理器名称  -->
		<property name="cacheManagerName" value="${ehcache.cacheManagerName}"/>
		<!-- 是否检查指定的缓存管理器名称已经存在，默认false  -->
		<property name="acceptExisting" value="${ehcache.acceptExisting}"/>
		<!-- 
			是否共享Ehcache缓存管理器（基于单利模式的类加载器）或独立的（通常是本地应用程序内）。默认是“false”，创建一个独立的本地实例。
  		-->
		<property name="shared" value="${ehcache.shared}"/>
	</bean>	
	
	<bean id="ehcacheFactory" class="org.springframework.cache.ehcache.EhCacheFactoryBean">
		<!-- 缓存管理器  -->
  		<property name="cacheManager" ref="ehcacheManager" />
  		<!-- 默认缓存名称  -->
  		<property name="cacheName" value="${ehcache.cacheName}" />
  		<!-- 对象在失效前的允许闲置时间（单位：秒）  -->
  		<property name="timeToLive" value="${ehcache.timeToLive}" />
  		<!-- 对象在失效前允许存活时间（单位：秒）  -->
  		<property name="timeToIdle" value="${ehcache.timeToIdle}" />
  		<!-- DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区  -->
  		<property name="diskSpoolBufferSize" value="${ehcache.diskSpoolBufferSize}" />
  		<!-- 是否开启统计功能  -->
  		<property name="statisticsEnabled" value="${ehcache.statisticsEnabled}" />
  		<!-- ehcache事件监听  -->
  		<property name="cacheEventListeners">
  			<list>  
                <bean class="com.firefly.ehcache.event.EhcacheEventListener"></bean>  
            </list>  
        </property>
 	</bean>           
	
	<!-- 通过注解方式注入到BaseAction,BaseServiceImpl -->
	<bean name="newMemcachedClient" class="com.firefly.memcached.client.XmemcachedDefaultClient">
	    <property name="memcachedClient"  ref="memcachedClient"/>
		<!-- 永久不过期 -->
		<property name="expiry" value="0"/>
	</bean>
	
	<!-- Memcached 缓存切面切口实现 -->
	<bean id="memcachedInterceptor" class="com.firefly.memcached.interceptor.MemcachedAspectInterceptor"  >
		<property name="cacheClient" ref="newMemcachedClient"/>
	</bean>
	
	
</beans>