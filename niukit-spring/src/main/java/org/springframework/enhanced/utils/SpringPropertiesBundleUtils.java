package org.springframework.enhanced.utils;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

/**
 * 
 * @className	： SpringPropertiesBundleUtils
 * @description	： 多Properties缓存管理；每个Properties文件路径对应一个Properties对象
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:04:11
 * @version 	V1.0
 */
public abstract class SpringPropertiesBundleUtils {

	protected static Logger LOG = LoggerFactory.getLogger(SpringPropertiesBundleUtils.class);
	protected static ConcurrentMap<String, Properties> cachedProperties = new ConcurrentHashMap<String, Properties>();
	protected static String encoding = "UTF-8";
	 
	
	/**
	 * 
	 * @description: 根据location路径 获取 Properties；
	 * <p>注意：location 采用 spring 资源路径匹配解析器<ul>
	 * <li>1、“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	 * <li>2、“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
	 * <li>3、或单一路径，如："file:C:/test.dat"、"classpath:test.dat"、"WEB-INF/test.dat"
	 * </ul>
	 * </p>
	 * @author : kangzhidong
	 * @date 下午04:13:29 2015-3-19 
	 * @param location
	 * @return
	 * @return  Properties 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void initProperties(String location,String encoding) {
		Assert.notNull(location, " location is null!");
		try {
			SpringPropertiesBundleUtils.encoding = encoding;
			Resource[] resources = SpringResourceUtils.getResources(location);
			for (Resource resource : resources) {
				String path = resource.getFile().getPath();
				cachedProperties.put(path, getProperties(resource,encoding));
			}
		} catch (Exception e) {
			LOG.error("couldn't load properties ！", e);
		}
	}
	
	//---------------------------------------------------------------------
	// Get methods for java.util.Properties
	//---------------------------------------------------------------------

	public static ConcurrentMap<String,Properties> getProperties(){
		return cachedProperties;
	}

	
	/**
	 * 
	 * @description: 根据location路径 获取 Properties；
	 * <p>注意：location 采用 spring 资源路径匹配解析器<ul>
	 * <li>1、“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	 * <li>2、“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
	 * <li>3、或单一路径，如："file:C:/test.dat"、"classpath:test.dat"、"WEB-INF/test.dat"
	 * </ul>
	 * </p>
	 * @author : kangzhidong
	 * @date 下午04:13:29 2015-3-19 
	 * @param location
	 * @return
	 * @return  Properties 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static Properties getProperties(String location) {
		Assert.notNull(location, " location is null!");
		Properties mergedProperties = new Properties();
		try {
			Resource[] resources = SpringResourceUtils.getResources(location);
			for (Resource resource : resources) {
				mergedProperties.putAll(getCached(resource));
			}
		} catch (Exception e) {
			LOG.error("couldn't load properties ！", e);
		}
		return mergedProperties;
	}
	
	/**
	 * 
	 * @description: 加载 所有 匹配classpath*:**\\/*.properties的配置文件
	 * @author : kangzhidong
	 * @date 下午04:28:02 2015-3-19 
	 * @return
	 * @return  Properties 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static Properties getRootProperties(){
		Properties rootProperties = new Properties();
		try {
			//classpath*:*.properties
			Resource[] resources = SpringResourceUtils.getRootProperties();
			for (Resource resource : resources) {
				rootProperties.putAll(getCached(resource));
			}
		} catch (Exception e) {
			LOG.error("couldn't load properties ！", e);
		}
		return rootProperties;
	}

	/**
	 * 
	 * @description: 加载 所有 匹配classpath*:**\\/*.properties 配置文件
	 * @author : kangzhidong
	 * @date 下午04:27:07 2015-3-19 
	 * @return
	 * @return  Properties 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static Properties getAllProperties(){
		Properties properties = new Properties();
		try {
			//classpath*:**/*.properties
			Resource[] resources = SpringResourceUtils.getProperties();
			for (Resource resource : resources) {
				properties.putAll(getCached(resource));
			}
		} catch (Exception e) {
			LOG.error("couldn't load properties ！", e);
		}
		return properties;
	}
	
	private static Properties getProperties(Resource resource,String encoding){
		Properties properties = null;
		try {
			properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource,encoding));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static String getProperty(String key){
		return getProperty(key, null);
	}
	
	public static String getProperty(String key, String defaultValue) {
		Assert.notNull(key, " key is null!");
		//临时变量
		Properties properties = null;
		Object value = null;
		//循环缓存key
		for (String filepath : cachedProperties.keySet()) {
			properties = cachedProperties.get(filepath);
			if(properties != null && !properties.isEmpty()){
				value = properties.getProperty(key,defaultValue);
				break;
			}
		}
		//取值
		return value == null ? null : value.toString();
	}
	
	
	//---------------------------------------------------------------------
	// Reload methods for java.util.Properties
	//---------------------------------------------------------------------
	
	
	public static void reloadProperties(String location) {
		Assert.notNull(location, " location is null!");
		try {
			Resource[] resources = SpringResourceUtils.getResources(location);
			for (Resource resource : resources) {
				setCache(resource,encoding);
			}
		} catch (Exception e) {
			LOG.error("couldn't reload properties ！", e);
		}
	}
	
	public static void reloadRootProperties(){
		try {
			//classpath*:*.properties
			Resource[] resources = SpringResourceUtils.getRootProperties();
			for (Resource resource : resources) {
				setCache(resource,encoding);
			}
		} catch (Exception e) {
			LOG.error("couldn't reload properties ！", e);
		}
	}

	public static void reloadAllProperties(){
		try {
			//classpath*:**/*.properties
			Resource[] resources = SpringResourceUtils.getProperties();
			for (Resource resource : resources) {
				setCache(resource,encoding);
			}
		} catch (Exception e) {
			LOG.error("couldn't reload properties ！", e);
		}
	}

	
	//---------------------------------------------------------------------
	// set/remove methods for java.util.Properties
	//---------------------------------------------------------------------
	
	
	/**
	 * 
	 * @description: 对存在的属性文件中添加键值对并保存
	 * @author : kangzhidong
	 * @date 下午01:49:28 2015-3-18 
	 * @param location		：属性文件的路径(包括类路径及文件系统路径)
	 * @param keyValues		：键值对Hashtable
	 * @return
	 * @return  boolean 返回类型
	 * @throws  
	 */
	public static boolean setProperties(String location,Hashtable<Object,Object> keyValues){
		Assert.notNull(location, " location is null!");
		Assert.notNull(keyValues, " keyValues is null!");
		try {
			Resource[] resources = SpringResourceUtils.getResources(location);
			for (Resource resource : resources) {
				//从缓存中取Properties对象
				Properties ppts = getCached(resource);
				if(ppts == null){
					ppts = new Properties();
				}
				//添加新值
				ppts.putAll(keyValues);
				//更新缓存
				cachedProperties.put(resource.getFile().getPath(),ppts);
				break;
			}
		} catch (Exception e) {
			LOG.error("couldn't set properties ！", e);
		}
		return true;
	}
	
	public static boolean setProperty(String location, String key,String newValue) {
		Assert.notNull(location, " location is null!");
		Assert.notNull(key, " key is null!");
		Assert.notNull(newValue, " newValue is null!");
		try {
			Resource[] resources = SpringResourceUtils.getResources(location);
			for (Resource resource : resources) {
				//从缓存中取Properties对象
				Properties ppts = getCached(resource);
				if(ppts == null){
					ppts = new Properties();
				}
				//添加新值
				ppts.remove(key);
				ppts.put(key, newValue);
				//更新缓存
				cachedProperties.put(resource.getFile().getPath(),ppts);
				break;
			}
		} catch (Exception e) {
			LOG.error("couldn't set properties ！", e);
		}
		return true;
	}
	
	//---------------------------------------------------------------------
	// Delete methods for java.util.Properties
	//---------------------------------------------------------------------
	
	/**
	 * 
	 * @description: 删除缓存中 匹配 location 路径 的 配置文件 
	 * @author : kangzhidong
	 * @date 下午04:51:16 2015-3-19 
	 * @param location
	 * @return
	 * @return  boolean 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static boolean removeCache(String location){
		Assert.notNull(location, " location is null!");
		try {
			Resource[] resources = SpringResourceUtils.getResources(location);
			for (Resource resource : resources) {
				//从缓存删除Properties对象
				removeCache(resource);
			}
			return true;
		} catch (IOException e) {
			LOG.error("couldn't remove cached properties ！", e);
			return false;
		}
	}
	

	private static boolean hasCached(Resource resource){
		try {
			String path = resource.getFile().getPath();
			return cachedProperties.containsKey(path);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	private static Properties setCache(Resource resource,String encoding){
		Properties properties = null;
		if(removeCache(resource)){
			try {
				String path = resource.getFile().getPath();
				cachedProperties.remove(path);
				properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource,encoding));
				cachedProperties.put(path, properties);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	private static Properties getCached(Resource resource){
		try {
			String path = resource.getFile().getPath();
			Properties properties = null;
			if( hasCached(resource) ){
				properties = cachedProperties.get(path);
			}else{
				properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource,encoding));
				cachedProperties.put(path, properties);
			}
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static boolean removeCache(Resource resource){
		try {
			cachedProperties.remove(resource.getFile().getPath());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}



