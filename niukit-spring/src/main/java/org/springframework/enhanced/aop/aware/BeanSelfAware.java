package org.springframework.enhanced.aop.aware;

/**
 *@类名称:BeanSelfAware.java
 *@类描述：
 *@创建人：kangzhidong
 *@创建时间：2014-8-6 下午03:17:46
 *@版本号:v1.0
 */
public interface BeanSelfAware {
	 
	//实现BeanSelfAware接口 
	public void setSelf(Object proxyBean);
	
}
