/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.freemarker.loader;

import java.io.IOException;
import java.net.URL;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import freemarker.cache.URLTemplateLoader;

/**
 *@类名称	: ClasspathTemplateLoader.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：May 24, 2016 3:10:50 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ClasspathTemplateLoader extends URLTemplateLoader {
   
	//spring 资源路径匹配解析器
	//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)”
	//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	protected static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	protected URL getURL(String location) {
        try {
        	Resource resource = resolver.getResource(location);
			return resource.getURL();
		} catch (IOException e) {
			return null;
		}
    }
    
}	
