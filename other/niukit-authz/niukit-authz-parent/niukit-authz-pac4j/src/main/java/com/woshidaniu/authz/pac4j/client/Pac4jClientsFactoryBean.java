/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.woshidaniu.basicutils.CollectionUtils;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @className ： Pac4jClientsFactoryBean
 * @description ：
 *              根据clientNames属性指定的多个以逗号分隔的客户端名称，以此将Spring的已经实例化的客户端对象添加到Clients对象内，且需指定默认客户端
 *              所有客户端与其客户端配置放置于相同的spring xml配置文件内，客户端需配置lazy-init="false"，在此类创建之前创建完成。
 * @author ：康康（1571）
 * @date ： 2018年5月7日 下午5:10:19
 * @version V1.0
 */
public class Pac4jClientsFactoryBean implements FactoryBean<Clients>, ApplicationContextAware {
	
	private static final String PAC4J_CLIENT_CONFIG_FILE_FORMAT  = "classpath:shiro-authz-pac4j-$.xml";

	private static final Logger log = LoggerFactory.getLogger(Pac4jClientsFactoryBean.class);
	
	private String callbackUrl;

	private ApplicationContext applicationContext;

	// 默认客户端名称
	private String defaultClientName;

	// 客户端名称列表
	private String clientNames;

	// pac4j的客户端集合
	private Clients clients;

	// 客户端列表
	@SuppressWarnings("rawtypes")
	private List<Client> clientList = new ArrayList<Client>();

	// 默认客户端
	private Client<?, ?> defaultClient;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Clients getObject() throws Exception {
		this.clients = new Clients();

		this.loadClients();
		
		this.clients.setClients(this.clientList);
		this.clients.setDefaultClient(this.defaultClient);
		this.clients.setCallbackUrl(this.callbackUrl);
		return this.clients;
	}

	private void loadClients() {
		log.info("load clients to pac4j Clients");
		
		if (StringUtils.isEmpty(this.clientNames)) {
			throw new BeanInitializationException("没有指定任何pac4j client，请检查配置文件!!!");
		}
		
		String[] names = StringUtils.splitByWholeSeparator(this.clientNames, ",");
		if(names == null || names.length <= 0) {
			throw new BeanInitializationException("没有指定任何pac4j client，请检查配置文件!!!");
		}
		
		//避免重复
		Set<String> set = new HashSet<String>(Arrays.asList(names));
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String name = it.next();
			if (StringUtils.isNotEmpty(name)) {
				
				Client<?, ?> client = this.applicationContext.getBean(name, Client.class);
				if(client != null) {
					log.info("load client[" + name + "],client[" + client.getClass().getName() + "]");
					this.clientList.add(client);
					
					if (name.equals(this.defaultClientName)) {
						this.defaultClient = client;
						log.info("default client");
					}					
				}else {
					log.error("没有在Spring context中找到客户端["+ name +"],请核对配置文件");
				}
			}
		}
		
		if (CollectionUtils.isEmpty(this.clientList)) {
			throw new BeanInitializationException("没有加载任何client，请检查配置文件!!!");			
		}
		
		if(this.defaultClient == null) {
			throw new BeanInitializationException("没有加载默认client，请检查配置文件!!!");		
		}
	}

	@Override
	public Class<?> getObjectType() {
		return Clients.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setDefaultClientName(String defaultClientName) {
		this.defaultClientName = defaultClientName;
	}

	public void setClientNames(String clientNames) {
		this.clientNames = clientNames;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	@PostConstruct
	public void startup() {
		if(this.applicationContext instanceof ConfigurableApplicationContext) {
			String[] names = StringUtils.splitByWholeSeparator(this.clientNames, ",");
			if(names == null || names.length <= 0) {
				throw new BeanInitializationException("没有指定任何pac4j client，请检查配置文件!!!");
			}
			
			ConfigurableApplicationContext  cac = (ConfigurableApplicationContext )this.applicationContext;
			XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)cac.getBeanFactory());  
			beanDefinitionReader.setResourceLoader(cac);
			beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(cac)); 
			
			for(int i=0;i<names.length;i++) {
				String name = names[i];
				String location = PAC4J_CLIENT_CONFIG_FILE_FORMAT.replace("$",name);
				log.info("load spring config:"+location);
				try {
					int n = beanDefinitionReader.loadBeanDefinitions(cac.getResources(location));
					log.info("load bean definitions count:"+n);
				} catch (Exception e) {
					log.error("",e);
				}
			}
		}
	}
}
