/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.configuration;

import org.pac4j.core.client.Client;

/**
 * 
 * @className	： CreateClientSupport
 * @description	： 支持创建Pac4j客户端接口
 * 
 * 配置子类需满足如下： 
 * 
 * 1. 携带@Configuration注解
 * 
 * 2. 实现此接口，在createClient方法内创建相应配置对象和相应的客户端对象
 * 
 * 3.实现此接口的createClient方法需添加@Bean注解，注解的value就是那个客户端的名称，也与相应的xml配置文件相对应
 *  
 * 若客户端名称为demo,则xml配置文件必须为 shiro-authz-pac4j-demo.xml,配置对象内部，定义<bean id="demoConfiguration" class="...">
 * 
 * @author 		：康康（1571）
 * @date		： 2018年5月8日 下午2:17:13
 * @version 	V1.0
 */
public interface CreateClientSupport {

	@SuppressWarnings("rawtypes")
	public Client createClient();
}
