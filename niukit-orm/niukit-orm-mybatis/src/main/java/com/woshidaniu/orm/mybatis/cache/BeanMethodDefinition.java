package com.woshidaniu.orm.mybatis.cache;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.BeanDefinition;

import com.woshidaniu.beanutils.reflection.ReflectionUtils;

public class BeanMethodDefinition {

	protected BeanDefinition beanDefinition;
	protected String beanName;
	protected String[] aliases;
	protected Class<?> beanClass;
	protected String beanClassName;
	protected static final ConcurrentMap<String , Method> COMPLIED_METHODS = new ConcurrentHashMap<String , Method>();
	
	public BeanMethodDefinition(String beanName, String[] aliases, BeanDefinition beanDefinition, Class<?> beanClass) {
		this.beanDefinition = beanDefinition;
		this.beanName = beanName;
		this.aliases = aliases;
		this.beanClass = beanClass;
		this.beanClassName = beanClass.getName();
	}

	public BeanDefinition getBeanDefinition() {
		return beanDefinition;
	}

	public String getBeanName() {
		return beanName;
	}

	public String[] getAliases() {
		return aliases;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public Method getMethod(String methodName, Class<?>[] paramTypes) {
		StringBuilder builder = new StringBuilder(getBeanClassName()).append(".").append(methodName);
		if(!ArrayUtils.isEmpty(paramTypes)){
			builder.append("[");
			for (Class<?> paramType : paramTypes) {
				builder.append(".").append(paramType.getName());
			}
			builder.append("]");
		}
		String uid = DigestUtils.md2Hex(builder.toString());
		Method ret = COMPLIED_METHODS.get(uid);
		if (ret != null) {
			return ret;
		}
		synchronized (beanClass) {
			
			// 查找对应参数类型方法
			Class<?> searchType = beanClass;
			while (searchType != null) {
				Method[] methods = (searchType.isInterface() ? searchType.getMethods() : ReflectionUtils.getAllDeclaredMethods(searchType));
				for (Method method : methods) {
					if (methodName.equals(method.getName()) && ( ArrayUtils.isEmpty(paramTypes) || equals(paramTypes, method.getParameterTypes()))) {
						ret = method;
						Method existing = COMPLIED_METHODS.putIfAbsent(uid, ret);
						if (existing != null) {
							ret = existing;
						}
						return ret;
					}
				}
				searchType = searchType.getSuperclass();
			}
			// 没有找到参数匹配的方法：则取出第一个参数名匹配的对象，在实际应用中，我们的项目参数名在同一个对象中基本唯一
			searchType = beanClass;
			while (searchType != null) {
				Method[] methods = (searchType.isInterface() ? searchType.getMethods() : ReflectionUtils.getAllDeclaredMethods(searchType));
				for (Method method : methods) {
					if (methodName.equals(method.getName())) {
						ret = method;
						Method existing = COMPLIED_METHODS.putIfAbsent(uid, ret);
						if (existing != null) {
							ret = existing;
						}
						return ret;
					}
				}
				searchType = searchType.getSuperclass();
			}
			
		}
		return ret;
	}
	
	public boolean equals(Class<?>[] a, Class<?>[] a2) {
        if (a==a2)
            return true;
        if (a==null || a2==null)
            return false;

        int length = a.length;
        if (a2.length != length)
            return false;

        for (int i=0; i<length; i++) {
        	Class<?> class1 = a[i];
        	Class<?> class2 = a2[i];
        	
            if (!(class1==null ? class2==null : (class1.equals(class2) || class2.isAssignableFrom(class1)))){
            	 return false;
            }
               
        }

        return true;
    }
	
	

}
