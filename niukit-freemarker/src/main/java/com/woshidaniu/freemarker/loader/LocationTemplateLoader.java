/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.freemarker.loader;

import java.net.URL;

import com.woshidaniu.beanutils.ClassLoaderWrapper;

import freemarker.cache.URLTemplateLoader;

/**
 *@类名称	: LocationTemplateLoader.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：May 24, 2016 3:10:50 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class LocationTemplateLoader extends URLTemplateLoader {
   
	private final ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();
	
	protected URL getURL(String name) {
        return classLoaderWrapper.getResourceAsURL(name);
    }
    
}	
