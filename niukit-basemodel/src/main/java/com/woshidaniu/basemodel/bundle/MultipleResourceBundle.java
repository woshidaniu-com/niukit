package com.woshidaniu.basemodel.bundle;

import java.util.Enumeration;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 *@类名称		： MultipleResourceBundle.java
 *@类描述		：多重国际化资源对象;实现多个国际化资源的聚合
 *@创建人		：kangzhidong
 *@创建时间	：Feb 21, 2017 9:03:19 AM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class MultipleResourceBundle extends ResourceBundle {

	protected ResourceBundle[] bundles;
	protected static Logger LOG = LoggerFactory.getLogger(MultipleResourceBundle.class);
	public MultipleResourceBundle() {
    }
	
	public MultipleResourceBundle(ResourceBundle ...bundles){
		this.bundles = bundles;
	}
	
	@Override
	public Enumeration<String> getKeys() {
        return new ResourceBundleEnumeration( this.parent, this.bundles);
	}
	
	@Override
	protected Object handleGetObject(String key) {
		if (key == null) {
            throw new NullPointerException("key is null ");
        }
		for (ResourceBundle bundle : bundles) {
			try {
				Object value = bundle.getObject(key);
				if(value != null){
					return value;
				}
			} catch (Exception e) {
				// ingrone e
				LOG.warn(e.getMessage());
			}
		}
		return null;
	}
	
};
