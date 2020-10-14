
# web应用 配置 

> 依赖包

<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-pool2</artifactId>
</dependency>
<!--
	一个用于操作Internet基础协议（Finger，Whois，TFTP，Telnet，POP3，FTP，NNTP，以及SMTP）的底层API。
	Net包不仅支持对各种低层次协议的访问，而且还提供了一个高层的抽象。 它使得开发者不再需要直接面对各种协议的Socket级的低层命令。
-->
<dependency>
	<groupId>commons-net</groupId>
	<artifactId>commons-net</artifactId>
</dependency>
<dependency>
	<groupId>${project.groupId}</groupId>
	<artifactId>fastkit-basicutils</artifactId>
	<version>${project.version}</version>
</dependency>
<dependency>
	<groupId>${project.groupId}</groupId>
	<artifactId>fastkit-io</artifactId>
	<version>${project.version}</version>
</dependency>

> web.xml 配置

<!--FTP文件请求过滤器 -->
<filter>    
	<filter-name>ftpResource</filter-name>    
	<filter-class>com.woshidaniu.fastkit.ftpclient.web.FTPResourceRequestFilter</filter-class>
	<!-- 文件本地存储路径 -->
    <init-param>
    	<param-name>ftp.tmpdir</param-name>
    	<param-value>tmpdir</param-value>
    </init-param>
    <!-- 是否缓存FTP文件到本地存储路径 -->
    <init-param>
    	<param-name>ftp.cacheLocal</param-name>
    	<param-value>false</param-value>
    </init-param>
    <!-- FTP文件在本地缓存的时间;默认10分钟-->
    <init-param>
    	<param-name>ftp.cacheExpiry</param-name>
    	<param-value>600000</param-value>
    </init-param>
    <!-- 请求过滤前缀;默认 /ftp/ -->
    <init-param>
    	<param-name>ftp.requestPrefix</param-name>
    	<param-value>/ftp/</param-value>
    </init-param> 
    <!-- 异常信息重定向路径 -->
    <init-param>
    	<param-name>ftp.redirectURL</param-name>
    	<param-value></param-value>
    </init-param> 
</filter>    
<!-- 请求路径的正则匹配表达式，被匹配的路径将会被拦截器拦截处理  -->
<filter-mapping>    
	<filter-name>ftpResource</filter-name>    
	<url-pattern>/ftp/*</url-pattern>    
</filter-mapping>

  
# 相关资料

https://www.baidu.com/s?ie=utf-8&f=3&rsv_bp=1&tn=95853428_hao_pg&wd=ftpclient%E6%96%AD%E7%82%B9%E7%BB%AD%E4%BC%A0&oq=FTP%26lt%3Blient%20duan&rsv_pq=d83da70d000ee043&rsv_t=21f7zM09QWjuhyohimWWRDhBADcTSAi6YDSBnA%2FzDh0jZryIxSjvGKcbAsOENJOcuca9p7sD&rsv_enter=0&rsv_sug3=15&rsv_sug1=11&rsv_sug2=1&prefixsug=FTP%3Client%20duan&rsp=0&rsv_sug7=100&rsv_sug4=1034

http://blog.sina.com.cn/s/blog_6f6a259c01019xid.html
http://blog.csdn.net/ygzk123/article/details/7800934
http://blog.csdn.net/voyage_mh1987/article/details/7354014
