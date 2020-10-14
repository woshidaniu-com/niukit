package org.springframework.enhanced.factory;

import org.springframework.enhanced.context.support.ModuleMessageSource;
import org.springframework.enhanced.context.support.ModuleReloadableMessageSource;

/**
 * 
 * @className	： EnhancedMessageFactory
 * @description	： TODO(描述这个类的作用)
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:09:32
 * @version 	V1.0
 */
public abstract class EnhancedMessageFactory extends EnhancedBeanFactory {

	protected ModuleMessageSource staticResource;
	protected ModuleReloadableMessageSource reloadableResource;
	
	public String getMessage(String key,String ...patams) {
		return getMessage(key, null, patams);
	}
	
	public String getMessage(String key,String defaultMessage,String ...patams) {
		String message = null;
		//优先使用动态加载的资源
		if (reloadableResource != null) {
			message = reloadableResource.getMessage(key, patams, defaultMessage);
		}
		//没有取到，再考虑今天不变的资源是否存在
		if(message == null && staticResource != null){
			message = staticResource.getMessage(key, patams, defaultMessage);
		}
		return message;
	}

	/**
	 * @return the staticResource
	 */
	public ModuleMessageSource getStaticResource() {
		return this.staticResource;
	}

	/**
	 * @param staticResource the staticResource to set
	 */
	public void setStaticResource(ModuleMessageSource staticResource) {
		this.staticResource = staticResource;
	}
	
	/**
	 * @return the reloadableResource
	 */
	public ModuleReloadableMessageSource getReloadableResource() {
		return this.reloadableResource;
	}

	/**
	 * @param reloadableResource the reloadableResource to set
	 */
	public void setReloadableResource(ModuleReloadableMessageSource reloadableResource) {
		this.reloadableResource = reloadableResource;
	}
	
}
