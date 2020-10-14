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
	
	<!-- <bean id="flyway" class="org.flywaydb.core.Flyway" depends-on="dataSource_flyway" lazy-init="false">
	    <property name="dataSource" ref="dataSource_flyway"/>
	</bean>
	
	<bean id="jFlyway" class="com.woshidaniu.fastdb.flyway.FlywayWithoutClean" lazy-init="false" depends-on="flyway">
	    <property name="flyway" ref="flyway"/>
	</bean> -->
	
</beans>