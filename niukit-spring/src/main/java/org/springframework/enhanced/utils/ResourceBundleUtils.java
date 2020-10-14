package org.springframework.enhanced.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

public class ResourceBundleUtils {

	protected static Logger LOG = LoggerFactory.getLogger(ResourceBundleUtils.class);
    private static final ConcurrentMap<String, List<ResourceBundle>> bundlesMap = new ConcurrentHashMap<String, List<ResourceBundle>>();
    private static final ConcurrentMap<String, ResourceBundle> bundleMap = new ConcurrentHashMap<String, ResourceBundle>();
    // 用于保存ResourceBundle的实例对象
    private static final ConcurrentMap<String, ResourceBundle> packageBundleMap = new ConcurrentHashMap<String, ResourceBundle>();
    
    private static final Set<String> missingBundles = Collections.synchronizedSet(new HashSet<String>());
	
    private static ClassLoader getCurrentThreadContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    /**
     * Creates a key to used for lookup/storing in the bundle misses cache.
     *
     * @param prefix      the prefix for the returning String - it is supposed to be the ClassLoader hash code.
     * @param expression  the expression of the bundle .
     * @return the key to use for lookup/storing in the bundle misses cache.
     */
    private static String createMissesKey(String prefix, String expression) {
        return prefix  + "_" + expression;
    }
    
    public static String getBundleFileName(Resource resource) throws IOException {
    	// 获取资源文件名称
    	String filename = FilenameUtils.getName(resource.getFile().getAbsolutePath());
    	// 获取基本名称部分
		String baseName = FilenameUtils.getBaseName(filename);
		// 文件后缀
		String extension = FilenameUtils.getExtension(filename);
		// 获取系统默认的Locale（国家/语言环境）
		Locale currentLocale = Locale.getDefault();
		if (baseName.endsWith(currentLocale.toString())) {
			return filename;
		}
		Locale[] localeList = Locale.getAvailableLocales();
		for (Locale locale : localeList) {
			if (baseName.endsWith(locale.toString())) {
				return filename;
			}
		}
		return baseName + "_" + currentLocale.toString() + "." + extension;
	}
    
    /**
	 * 判断资源文件表达式是否符合国际化资源表达式规范
	 * @param expression : properties文件表达式 ；如："classpath*:i18n/message*_zh_CN.properties"
	 */
    public static boolean isBundleExpression(String expression) {
    	// 文件后缀
		String extension = FilenameUtils.getExtension(expression);
		String expressionPart = expression.substring(0 , expression.length() - extension.length() - 1);
    	// 获取系统默认的Locale（国家/语言环境）
		Locale currentLocale = Locale.getDefault();
		if (expressionPart.endsWith(currentLocale.toString())) {
			return true;
		}
		Locale[] localeList = Locale.getAvailableLocales();
		for (Locale locale : localeList) {
			if (expressionPart.endsWith(locale.toString())) {
				return true;
			}
		}
		return false;
	}
    
	// 得到ResourceBundle对象的实例
	public static ResourceBundle getResourceBundle(Class<?> cls,String bundleName) {
		return getResourceBundle(cls.getPackage().getName());
	}

	// 得到ResourceBundle对象的实例
	public static ResourceBundle getResourceBundle(String packageName, String bundleName) {
		// 获取配置文件的绝对路径，注意后缀为.LocalStrings，不加_EN或_CN，系统将根据本地语言自动获取相应配置文件
		String aBundleName = packageName + "." + bundleName;
		//获取缓存对象
		ResourceBundle ret = packageBundleMap.get(aBundleName);
		if (ret != null) {
			return ret;
		}
		ret = PropertyResourceBundle.getBundle(aBundleName);
		ResourceBundle existing = packageBundleMap.putIfAbsent(aBundleName, ret);
		if (existing != null) {
			ret = existing;
		}
		return ret;
	}
    
    /**
     * 获取指定国际化文件对应的ResourceBundle对象
     * @param bundleFileName : properties文件名称 ；如："message_zh_CN.properties"
     * @return java.util.ResourceBundle
     */
    public static ResourceBundle getResourceBundle(String bundleFileName) {
    	//获取缓存对象
		ResourceBundle ret = bundleMap.get(bundleFileName);
		if (ret != null) {
			return ret;
		} 
		// 获取基本名称部分
		String baseName = FilenameUtils.getBaseName(bundleFileName);
		// 文件后缀
		String extension = FilenameUtils.getExtension(bundleFileName);
		// 获取文件对应的Locale对象
		Locale bundleLocale = FilenameUtils.getBundleLocale(bundleFileName);
		// 判断是否为空
		if(bundleLocale != null){
			String aBundleName = baseName.substring(0, baseName.length() - bundleLocale.toString().length() + 1) + "." + extension;
			ret = ResourceBundle.getBundle(aBundleName, bundleLocale, getCurrentThreadContextClassLoader());
		} else {
			ret = ResourceBundle.getBundle(baseName);
		}
		ResourceBundle existing = bundleMap.putIfAbsent(bundleFileName, ret);
		if (existing != null) {
			ret = existing;
		}
		return null;
    }
    
    /**
   	 * 根据表达式加载资源文件
   	 * @param expression : properties文件表达式 ；如："classpath*:i18n/message*_zh_CN.properties"
   	 */
    public static List<ResourceBundle> getResourceBundles(String expression) {
    	return getResourceBundles(expression, true);
    }
    
    /**
     * 根据表达式加载资源文件
   	 * @param expression : properties文件表达式 ；如："classpath*:i18n/message*_zh_CN.properties"
     * @param cacheable  : 是否使用缓存
     * @return
     */
	public static List<ResourceBundle> getResourceBundles(String expression,boolean cacheable) {
		if(!isBundleExpression(expression)){
			throw new IllegalArgumentException(" expression must like 'classpath*:message*_zh_CN.properties' or 'classpath*:i18n/message*_zh_CN.properties' ");
		}
		ClassLoader classLoader = getCurrentThreadContextClassLoader();
        String key = createMissesKey(String.valueOf(classLoader.hashCode()), expression);

        if (missingBundles.contains(key)) {
            return null;
        }

        List<ResourceBundle> bundles = null;
        try {
            if (bundlesMap.containsKey(key)) {
                bundles = bundlesMap.get(key);
            } else {
            	
            	bundles = new ArrayList<ResourceBundle>();
            	//解析表达式
    			Resource[] resources = SpringResourceUtils.getResources(expression);
    			//遍历资源
    			for (Resource resource : resources) {
    				InputStream input = null;
    				try {
    					//国际化资源名称
    					String bundleFileName = getBundleFileName(resource);
    					//获取缓存对象
    					ResourceBundle ret = bundleMap.get(bundleFileName);
    					if (ret != null && true == cacheable) {
    						//无缓存对象且允许缓存时忽略
    					} else {
    						
    						//资源文件缓存不存或不允许缓存，从新加载资源
        					
        					if(ResourceUtils.isJarURL(resource.getURL())){
        						input = new InputStreamResource(resource.getInputStream()).getInputStream();
        			        }else{
        			        	input = resource.getInputStream();
        			        }
        					
        					ret = new PropertyResourceBundle(input);
        					ResourceBundle existing = cacheable ? bundleMap.putIfAbsent(bundleFileName, ret) : bundleMap.put(bundleFileName, ret);
        					if (existing != null) {
        						ret = existing;
        					}
        					
						}
    					bundles.add(ret);
    				} finally {
    					IOUtils.closeQuietly(input);
    				}
    			}
    			
                bundlesMap.putIfAbsent(key, bundles);
            }
        } catch (Exception ex) {
            LOG.debug("Missing resource bundle with expression [#0]!", expression);
            LOG.debug(ex.getMessage());
            missingBundles.add(key);
		}
		
		return bundles;
	}
	
}
