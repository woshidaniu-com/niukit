/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.configuration;

import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @className	： ShiroPac4jCasConfiguration
 * @description	： pac4j的Cas配置，如需自定义其配置，在shiro-authz-pac4j-CasClient.xml文件内设置其属性，利用properties引入配置
 * @author 		：康康（1571）
 * @date		： 2018年5月8日 下午2:25:52
 * @version 	V1.0
 */
@Configuration
public class ShiroPac4jCasConfiguration extends CasConfiguration implements CreateClientSupport{
	
	private static final Logger log = LoggerFactory.getLogger(ShiroPac4jCasConfiguration.class);
	
	@Bean("CasClient")
	@Override
	@SuppressWarnings("rawtypes")
	public Client createClient() {
		
		if(log.isDebugEnabled()) {
			log.debug("create CasClient by configuration:"+this);
		}
		//创建客户端对象
		CasClient client = new CasClient(this);
		client.setName("CasClient");
		return client;
	}
}
