/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.orm.mybatis.spring.mapper;

import java.util.Set;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.orm.mybatis.cache.BeanMethodDefinition;
import com.woshidaniu.orm.mybatis.cache.BeanMethodDefinitionFactory;
import com.woshidaniu.orm.mybatis.utils.MybatisUtils;

/**
 *@类名称		: MybatisClassPathMapperScanner.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：Jun 16, 2016 6:19:47 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class MybatisClassPathMapperScanner extends ClassPathMapperScanner {

	protected Logger LOG = LoggerFactory.getLogger(MybatisClassPathMapperScanner.class);
	protected ApplicationContext applicationContext;
	
	public MybatisClassPathMapperScanner(ApplicationContext applicationContext,BeanDefinitionRegistry registry) {
		super(registry);
		this.applicationContext = applicationContext;
	}

	@Override
	public int scan(String... basePackages) {
		int count = 0;
		try {
			count = super.scan(basePackages);
		} catch (Throwable e) {
			LOG.error(e.getLocalizedMessage(),e);
		}
		return count;
	}
	
	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitions) {
			try {
				//获取Spring扫描注入对象的名称
				String beanName = beanDefinitionHolder.getBeanName();
				//获取代理对象
				Object target = MybatisUtils.getTarget(applicationContext.getBean(beanName));
				//获取对象代理接口类型
				Class<?> beanClass = ((Class<?>)ReflectionUtils.getAccessibleField(target, "mapperInterface").get(target));
				//获取接口类名称
				String className = beanClass.getName();
				//缓存对象引用
				BeanMethodDefinition definition = new BeanMethodDefinition(beanName, 
						beanDefinitionHolder.getAliases(), 
						beanDefinitionHolder.getBeanDefinition(), 
						beanClass);
				BeanMethodDefinitionFactory.setBeanMethodDefinition(className, definition );
			} catch (Exception e) {
				LOG.error(e.getLocalizedMessage(),e);
			}
		}
		
		return beanDefinitions;
		
	}
	 

	protected void postProcessBeanDefinition(AbstractBeanDefinition beanDefinition, String beanName){
		try {
			super.postProcessBeanDefinition(beanDefinition, beanName);
		} catch (Throwable e) {
			LOG.error(e.getLocalizedMessage(),e);
		}
	} 
	
	protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
		try {
			BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
		} catch (Throwable e) {
			LOG.error(e.getLocalizedMessage(),e);
		}
	}
	
}
