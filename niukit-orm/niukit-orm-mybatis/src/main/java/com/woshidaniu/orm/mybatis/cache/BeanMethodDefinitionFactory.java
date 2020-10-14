package com.woshidaniu.orm.mybatis.cache;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.orm.mybatis.utils.MybatisUtils;

public class BeanMethodDefinitionFactory {

	private static final Logger LOG = LoggerFactory.getLogger(BeanMethodDefinitionFactory.class);

	protected static final ConcurrentMap<String , BeanMethodDefinition> COMPLIED_BEAN_METHODS = new ConcurrentHashMap<String , BeanMethodDefinition>();
	
	public static BeanMethodDefinition getBeanMethodDefinition(String className) {
		return getBeanMethodDefinition(className, null);
	}
	
	public static BeanMethodDefinition getBeanMethodDefinition(String className,BeanMethodDefinition definition) {
		BeanMethodDefinition ret = COMPLIED_BEAN_METHODS.get(className);
		if (ret != null) {
			return ret;
		} 
		if(definition != null){
			BeanMethodDefinition existing = COMPLIED_BEAN_METHODS.putIfAbsent(className, definition);
			if (existing != null) {
				ret = existing;
			}
		}
		return ret;
	}
	
	public static BeanDefinition getBeanDefinition(String className) {
		BeanMethodDefinition definition = getBeanMethodDefinition(className);
		if(definition == null){
			return null;
		}
		return definition.getBeanDefinition();
	}
	
	public static Method getMethodDefinition(String mappedStatementId) {
		return getMethodDefinition(mappedStatementId, null);
	}
	
	/**
	 * String mappedStatementId = className + "." + method.getName();
	 * 
	 * // 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
	 *  		BoundSql boundSql = metaStatementHandler.getBoundSql();
	 * 		MetaObject metaBoundSql = MetaObjectUtils.forObject(boundSql);
	 * 		
	 * 		boundSql.getParameterObject()
	 */
	public static Method getMethodDefinition(String mappedStatementId, Class<?>[] paramTypes) {
		int index = mappedStatementId.lastIndexOf(".");
		//类名称
		String className = mappedStatementId.substring(0, index);
		//方法名称
		String methodName = mappedStatementId.substring(index + 1);
		
		BeanMethodDefinition definition = getBeanMethodDefinition(className);
		
		if(definition == null){
			return null;
		}
		return definition.getMethod(methodName, paramTypes);
	}
	
	
	public static BeanMethodDefinition setBeanMethodDefinition(String className,BeanMethodDefinition definition) {
		BeanMethodDefinition ret = definition;
		BeanMethodDefinition existing = COMPLIED_BEAN_METHODS.putIfAbsent(className, definition);
		if (existing != null) {
			ret = existing;
		}
		return ret;
	}
	
	public static void setBeanDefinitions(BeanFactory beanFactory, Set<BeanDefinitionHolder> beanDefinitions) {
		// 对扫描的Dao接口代理对象进行引用
		for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitions) {
			try {
				//获取Spring扫描注入对象的名称
				String beanName = beanDefinitionHolder.getBeanName();
				//获取代理对象
				Object target = MybatisUtils.getTarget(beanFactory.getBean(beanName));
				//获取对象代理接口类型
				Class<?> beanClass = ((Class<?>) ReflectionUtils.getAccessibleField(target, "mapperInterface").get(target));
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
	}
	
	
	
	
}
