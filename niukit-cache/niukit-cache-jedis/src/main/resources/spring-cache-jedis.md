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
	
	<!-- 加载fastdfs配置文件 -->
	<context:property-placeholder location="jedis.properties" ignore-unresolvable="true"/> 
	
	<!-- jedis连接池的配置  maxActive" -> "maxTotal" and "maxWait" -> "maxWaitMillis -->
	<bean name="jedisPoolConfig" class="jedis.clients.jedis.JedisPoolConfig"> 
        <!-- 是否启用后进先出, 默认true -->
        <property name="lifo" value="${jedis.pool.lifo}"></property>
        <!-- 连接池中最少空闲的连接数,默认为0. -->
        <property name="minIdle" value="${jedis.pool.minIdle}"></property>
        <!-- 链接池中最大空闲的连接数,默认为8. -->
        <property name="maxIdle" value="${jedis.pool.maxIdle}"></property>
        <!-- 链接池中最大空闲的连接数,默认为8. -->
        <property name="maxTotal" value="${jedis.pool.maxTotal}"></property>
        <!--  获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1 -->
        <property name="maxWaitMillis" value="${jedis.pool.maxWaitMillis}"></property>
        <!--  逐出连接的最小空闲时间 默认1800000毫秒(30分钟)，达到此值后空闲连接将可能会被移除。负值(-1)表示不移除。 -->
        <property name="minEvictableIdleTimeMillis" value="${jedis.pool.minEvictableIdleTimeMillis}"></property>
        <!-- 连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1. -->
        <!-- 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)-->
        <property name="softMinEvictableIdleTimeMillis" value="${jedis.pool.softMinEvictableIdleTimeMillis}"></property>
        <!-- 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 . -->
        <property name="numTestsPerEvictionRun" value="${jedis.pool.numTestsPerEvictionRun}"></property>
        <!-- 向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值. -->
        <property name="testOnBorrow" value="${jedis.pool.testOnBorrow}"></property>
        <!-- 向连接池“归还”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值. -->
        <property name="testOnReturn" value="${jedis.pool.testOnReturn}"></property>
        <!-- 向连接池“获取”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值. -->
        <property name="testOnCreate" value="${jedis.pool.testOnCreate}"></property>
        <!-- 向调用者输出“链接”对象时，是否检测它的空闲超时；默认为false。如果“链接”空闲超时，将会被移除。建议保持默认值. -->
        <property name="testWhileIdle" value="${jedis.pool.testWhileIdle}"></property>
        <!-- 空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1-->
        <property name="timeBetweenEvictionRunsMillis" value="${jedis.pool.timeBetweenEvictionRunsMillis}"></property>
        <!-- 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true -->
        <property name="blockWhenExhausted" value="${jedis.pool.blockWhenExhausted}"></property>
        <!-- 是否启用pool的jmx管理功能, 默认true -->
        <property name="jmxEnabled" value="${jedis.pool.jmxEnabled}"></property>
        <!-- /MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好. -->
        <property name="jmxNamePrefix" value="${jedis.pool.jmxNamePrefix}"></property>
        <!--
		   当“连接池”中active数量达到阀值时，即“链接”资源耗尽时，连接池需要采取的手段, 默认为1：
	     -> 0 : 抛出异常，
	     -> 1 : 阻塞，直到有可用链接资源
	     -> 2 : 强制创建新的链接资源
		
        <property name="whenExhaustedAction" value="${jedis.pool.whenExhaustedAction}"></property>-->
        <!-- 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数) -->
        <property name="evictionPolicyClassName" value="${jedis.pool.evictionPolicyClassName}"></property>
	</bean>
	
	<!-- Redis连接池配置;不是必选项：timeout/password  -->
	<bean id="jedisPool" class="redis.clients.jedis.JedisPool" destroy-method="destroy">
	    <constructor-arg index="0" ref="jedisPoolConfig" />
        <constructor-arg index="1" value="${${redis.host}}"/>
        <constructor-arg index="2" value="${${redis.port}}"/>
        <!--timeout-->
        <constructor-arg index="3" value="${redis.timeout}"/>
        <constructor-arg index="4" value="${${redis.password}}"/>
	</bean>

	<bean id="shardedJedisPool" class="redis.clients.jedis.ShardedJedisPool">
		<constructor-arg index="0" ref="jedisPoolConfig" />
		<constructor-arg index="1">
			<list>
				<bean class="redis.clients.jedis.JedisShardInfo">
					<constructor-arg index="0" value="${redis1.ip}" />
					<constructor-arg index="1" value="${redis.port}" type="int" />
				</bean>
				<bean class="redis.clients.jedis.JedisShardInfo">
					<constructor-arg index="0" value="${redis2.ip}" />
					<constructor-arg index="1" value="${redis.port}" type="int" />
				</bean>
			</list>
		</constructor-arg>
	</bean>
	
	<bean id="jedisConnectionFactory"  class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory" >  
    	<property name="hostName" value="${redis.ip}" />  
    	<property name="port"  value="${redis.port}" />  
	    <property name="poolConfig"  ref="jedisPoolConfig" />  
	</bean> 

	<bean id="redisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate">  
	    <property name="connectionFactory"  ref="jedisConnectionFactory" />  
	</bean>
	 
</beans>