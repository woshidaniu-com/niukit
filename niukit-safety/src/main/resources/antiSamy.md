
# web应用 配置 

> 依赖包

<dependency>
	<groupId>org.owasp.antisamy</groupId>
	<artifactId>antisamy</artifactId>
	<version>1.5.3</version>
</dependency>
<dependency>
    <groupId>com.googlecode.owasp-java-html-sanitizer</groupId>
    <artifactId>owasp-java-html-sanitizer</artifactId>
    <version>20170515.1</version>
</dependency>

> web.xml 配置

<!-- 基于 AntiSamy组件的XSS(Cross Site Scripting)，即跨站脚本攻击请求过滤器（主要用于富文本） -->
<filter>
	<filter-name>httpRichtextXssFilter</filter-name>
	<filter-class>com.fastkit.servlet.filter.HttpServletRequestXssFilter</filter-class>
	<!-- 扫描器类型，0：DOM类型扫描器,1:SAX类型扫描器；两者的区别如同XML解析中DOM解析与Sax解析区别相同，实际上就是对两种解析方式的实现 -->
    <init-param>
    	<param-name>safety.xss.scanType</param-name>
    	<param-value>1</param-value>
    </init-param>
	<!-- 请求路径的正则匹配表达式，匹配的路径会被检测XSS;多个表达式可以用",; \t\n"中任意字符分割  -->
    <init-param>
    	<param-name>safety.xss.include-patterns</param-name>
    	<param-value>*.do</param-value>
    </init-param>
    <!-- 不进行过滤请求路径的正则匹配表达式，匹配的路径不会被检测XSS;多个表达式可以用",; \t\n"中任意字符分割 -->
    <init-param>
    	<param-name>safety.xss.exclude-patterns</param-name>
    	<param-value>/a/*.do,/b/*.do</param-value>
    </init-param>
    <!-- 默认的防XSS攻击的规则配置-->
    <init-param>
    	<param-name>safety.xss.default-policy</param-name>
    	<param-value>antisamy-default.xml</param-value>
    </init-param>
    <!-- 防XSS攻击的模块对应的规则配置；每个模块表达式与规则文件使用"|"分割；多个配置可以用",; \t\n"中任意字符分割 -->
    <init-param>
    	<param-name>safety.xss.policy-mappings</param-name>
    	<param-value>/manager/*|manager-antixss-policy.xml,/guest/*|guest-antixss-policy.xml</param-value>
    </init-param>
</filter>
<filter-mapping>
	<filter-name>httpRichtextXssFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>


<!-- 基于owasp-java-html-sanitizer的XSS(Cross Site Scripting)，即跨站脚本攻击请求过滤器 -->
<filter>
	<filter-name>httpXssFilter</filter-name>
	<filter-class>com.woshidaniu.safety.xss.HttpServletRequestXssFilter</filter-class>	
</filter>
<filter-mapping>
	<filter-name>httpXssFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>



