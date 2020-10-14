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
	
	<!-- 加载memcached配置文件 -->
	<context:property-placeholder location="memcached.properties" /> 
	
	<bean name="memcachedConfiguration" class="com.google.code.yanf4j.config.Configuration">
	    <property name="writeThreadCount" value="20"/>
	    <property name="readThreadCount" value="20"/>
	   
	    <!-- socket链接 空闲检测心跳时间，默认5s，设置为10秒-->
	    <property name="sessionIdleTimeout" value="10000"/>
	    <property name="checkSessionTimeoutInterval" value="8000"/>
	    <property name="statisticsInterval" value="500000"/>
	    <property name="handleReadWriteConcurrently" value="8000"/>
	    
	    
	    <!-- socket 读取缓冲区，默认16k，设置为10秒-->
	    <property name="sessionReadBufferSize" value="8000"/>
	    <!-- socket等待应答时间，默认5s，设置为10秒-->
	    <property name="soTimeOut" value="70000"/>
	    <property name="dispatchMessageThreadCount" value="500000"/>   
	</bean> 
	
	<!-- SASL验证信息(xmemcached 1.2.5支持)
	<bean name="server1" class="java.net.InetSocketAddress">
		<constructor-arg>
		    <value>host1</value>
		</constructor-arg>
		<constructor-arg>
		   	<value>port1</value>
		</constructor-arg>
	</bean>-->
	
  	<bean name="memcachedClientBuilder" class="com.woshidaniu.fastcache.xmemcached.NewXMemcachedClientBuilder">  
	  	<!-- 如果缓存转储或网络错误导致连接关闭，xmemcached 是否尝试再次连接 ；默认true  -->
	  	<property name="enableHealSession" value="${memcached.enableHealSession}"/>  
  		<!-- 尝试再次连接会话间隔；单位毫秒；默认2000   --> 
  		<property name="healSessionInterval" value="${memcached.healSessionInterval}"/> 
	  	<!-- 请求响应超时时间；单位毫秒 ；默认5000  -->
	  	<property name="opTimeout" value="${memcached.opTimeout}"/>
	  	<!-- 是否宕机报警   -->
		<property name="failureMode" value="${memcached.failureMode}"></property>
		<!-- NIO连接池大小，即客户端个数  -->
		<property name="connectionPoolSize" value="${memcached.connectionPoolSize}"></property>
		<!-- 链接超时时间 -->
        <property name="connectTimeout" value="${memcached.connectTimeout}"></property>
  		<!-- socket初始化参数   -->
  		<property name="configuration" href="memcachedConfiguration"/>
	  	<!-- 以 ",; \t\n"分割的  IP:PORT:WEIGHT 字符数组. -->  
        <constructor-arg>
        	<value>${memcached.servers}</value>  
        </constructor-arg>
        <!-- 授权验证信息，仅在xmemcached 1.2.5及以上版本有效  
        <property name="authInfoMap">
             <map>
                 <entry key-ref="server1">
                      <bean class="net.rubyeye.xmemcached.auth.AuthInfo" factory-method="typical">
                              <constructor-arg index="0">
                                  <value>cacheuser</value>
                              </constructor-arg>
                              <constructor-arg index="1">
                                  <value>123456</value>
                              </constructor-arg>
                      </bean>
                 </entry>
             </map>
        </property>-->
		<!-- 使用二进制文件协议，默认TextCommandFactory   -->
		<property name="commandFactory">
			<bean class="net.rubyeye.xmemcached.command.BinaryCommandFactory"></bean>
		</property>
		<!-- 使用一致性哈希算法（Consistent Hash Strategy）    -->
		<property name="sessionLocator">
			<bean class="net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator"></bean>
		</property>
		<!-- 序列化转换器  -->
		<property name="transcoder">
			<bean class="net.rubyeye.xmemcached.transcoders.SerializingTranscoder">
				<!-- 进行数据压缩，大于1KB时进行压缩   -->
				<property name="compressionThreshold" value="${memcached.compressionThreshold}"></property>
			</bean> 
		</property>
		<!-- IoBuffer分配器 ;-->
        <property name="bufferAllocator">
            <bean class="net.rubyeye.xmemcached.buffer.SimpleBufferAllocator"></bean>
        </property>
	</bean>         
	
	
	<!-- Use factory bean to build memcached client -->
	<bean name="memcachedClient" factory-bean="memcachedClientBuilder" factory-method="build" destroy-method="shutdown">
	</bean>
	
	<!-- 通过注解方式注入到BaseAction,BaseServiceImpl -->
	<bean name="newMemcachedClient" class="com.woshidaniu.fastcache.xmemcached.client.XmemcachedDefaultClient">
	    <property name="memcachedClient"  ref="memcachedClient"/>
		<!-- 永久不过期 -->
		<property name="expiry" value="0"/>
	</bean>
	
	<!-- Memcached 缓存切面切口实现 -->
	<bean id="memcachedInterceptor" class="com.woshidaniu.fastcache.xmemcached.interceptor.MemcachedAspectInterceptor"  >
		<property name="cacheClient" ref="newMemcachedClient"/>
	</bean>
	
	
</beans>