package com.woshidaniu.basemodel.bundle;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
/**
 * 
 *@类名称		： ResourceBundleEnumeration.java
 *@类描述		：国际化资源对象遍历实现
 *@创建人		：kangzhidong
 *@创建时间	：Feb 21, 2017 9:09:46 AM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class ResourceBundleEnumeration implements Enumeration<String> {

	private Iterator<String> ite;
	
	public ResourceBundleEnumeration(ResourceBundle ...bundles){
		this(null, bundles);
	}
	
	public ResourceBundleEnumeration(ResourceBundle parent,ResourceBundle ...bundles) {
		Set<String> keys = new HashSet<String>();
		if(parent != null){
			keys.addAll(parent.keySet());
		}
		for (ResourceBundle bundle : bundles) {
			if(bundle == null){
				continue;
			}
			keys.addAll(bundle.keySet());
		}
		this.ite = keys.iterator();
	}

	@Override
	public boolean hasMoreElements() {
		return ite.hasNext();
	}

	@Override
	public String nextElement() {
		return ite.next();
	}

}