package org.springframework.enhanced.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

/**
 * 
 * @className	： SpringPropertiesUtils
 * @description	： Properties缓存管理
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:03:46
 * @version 	V1.0
 */
public abstract class SpringPropertiesUtils {
	
	/**
	 * Any number of these characters are considered delimiters between
	 * multiple context config paths in a single String value.
	 * @see org.springframework.enhanced.context.support.AbstractXmlApplicationContext#setConfigLocation
	 * @see org.springframework.enhanced.web.context.ContextLoader#CONFIG_LOCATION_PARAM
	 * @see org.springframework.enhanced.web.servlet.FrameworkServlet#setContextConfigLocation
	 */
	protected static String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

	protected static Logger LOG = LoggerFactory.getLogger(SpringPropertiesUtils.class);
	//初始化配置文件：资源池
	protected static Properties cachedProperties = new Properties();
	
	/**
	 * 
	 * @description	：读取配置文件到缓存区
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
	 * @date 		：Jan 28, 2016 1:45:38 PM
	 * @param location
	 * @param encoding
	 */
	public final static void initProperties(String location,String encoding) {
		Assert.notNull(location, " location is null!");
		try {
			LOG.info("Start loading properties file from [" + location + "]");
			//即多个资源文件路径之间用” ,; /t/n”分隔，解析成数组形式
			String[] locations = StringUtils.tokenizeToStringArray(location, CONFIG_LOCATION_DELIMITERS);
			for (int i = 0; i < locations.length; i++) {
				if(locations[i] != null && locations[i].length() > 0){
					for (String location1 : locations) {
						//处理多个资源文件字符串数组  
						Resource[] resources = SpringResourceUtils.getResources(location1);
						for (Resource resource : resources) {
							try {
								LOG.info("Loading properties file from URL [" + resource.getFile().getPath() + "]");
								//加载Properties对象
								PropertiesLoaderUtils.fillProperties(cachedProperties, new EncodedResource(resource, encoding));
							}catch (IOException ex) {
								LOG.error("Could not load properties file from URL [" + resource.getFile().getPath() + "] Caused by:  " + ex.getMessage());
							}
						}
					}
				}
			}
			LOG.info("Properties file loaded successfully !");
		} catch (Exception ex) {
			LOG.error("Properties file loaded failed . Caused by:  " + ex.getMessage());
		}
	}
	
	//---------------------------------------------------------------------
	// Get/Set methods for java.util.Properties
	//---------------------------------------------------------------------

	public final static synchronized Properties getCachedProperties(){
		return cachedProperties;
	}
	
	public final static String getProperty(String key){
		return SpringPropertiesUtils.getProperty(key,"");
	}
	
	public final static String getProperty(String key, String defaultValue) {
		Assert.notNull(key, " key is null!");
		//取值
		return getCachedProperties().getProperty(key,defaultValue);
	}

	public final static void setProperty(String key,String value){
		getCachedProperties().put(key, value);
	}

	//---------------------------------------------------------------------
	// Cache methods for java.util.Properties
	//---------------------------------------------------------------------

	
	public final static Properties getProperties(InputStream inStream) {
		Properties properties = new Properties();
		try {
			properties.load(inStream);
		} catch (IOException e) {
			LOG.error("couldn't load properties ！", e);
		}
		return properties;
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
	public final static Properties getProperties(String location) {
		Assert.notNull(location, " location is null!");
		Properties properties = new Properties();
		try {
			Resource[] resources = SpringResourceUtils.getResources(location);
			for (Resource resource : resources) {
				//加载Properties对象
				properties.putAll(SpringPropertiesUtils.getProperties(resource));
			}
		} catch (Exception e) {
			LOG.error("couldn't load properties ！", e);
		}
		return properties;
	}
	
	
	public final static Properties getProperties(Resource resource) {
		return SpringPropertiesUtils.getProperties(resource, "UTF-8");
	}
	
	public final static Properties getProperties(Resource resource,String encoding) {
		Properties properties = new Properties();
		try {
			PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(resource, encoding));
		}catch (IOException ex) {
			LOG.warn("Could not load properties from " + resource.getFilename() + ": " + ex.getMessage());
		}
		return properties;
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
	public final static Properties getRootProperties(){
		return SpringPropertiesUtils.getProperties("classpath*:*.properties");
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
	public final static Properties getAllProperties(){
		return SpringPropertiesUtils.getProperties("classpath*:**/*.properties");
	}
	
}



