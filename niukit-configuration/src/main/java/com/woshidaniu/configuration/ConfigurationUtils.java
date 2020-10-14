package com.woshidaniu.configuration;

import java.util.List;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLPropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;

/**
 * 
 *@类名称	: ConfigurationUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 4:55:40 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ConfigurationUtils {
	
	private static final String XML_FILE_EXTENSION = ".xml";
	
	public static Configuration loadConfiguration(String resourceName) throws ConfigurationException {
		AbstractConfiguration.setDefaultListDelimiter('/'); 
		Configuration configuration = null;
		String filename = FilenameUtils.getName(resourceName);
		if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
			configuration =  new XMLPropertiesConfiguration(resourceName);
		}else {
			configuration =  new PropertiesConfiguration(resourceName);
		}
		return configuration;
	}

	public void getConfiguration() throws ConfigurationException{
	   
	    Configuration config1 = ConfigurationUtils.loadConfiguration("te/Vasp.properties");  
	    String[] keys=config1.getStringArray("keys");  
	    List key2=config1.getList("keys");  
	    
		//注意路径默认指向的是classpath的根目录  
	    Configuration config = ConfigurationUtils.loadConfiguration("test.properties");  
	    String ip=config.getString("ip");  
	    int port=config.getInt("port");  
	    String title=config.getString("application.title");  
	    //再举个Configuration的比较实用的方法吧,在读取配置文件的时候有可能这个键值对应的值为空，那么在下面这个方法中  
	    //你就可以为它设置默认值。比如下面这个例子就会在test.properties这个文件中找id的值，如果找不到就会给id设置值为123  
	    //这样就保证了java的包装类不会返回空值。虽然功能很简单，但是很方便很实用。  
	    Integer id=config.getInteger("id", new Integer(123));  
	    //如果在properties 文件中有如下属性keys=cn,com,org,uk,edu,jp,hk  
	    //可以实用下面的方式读取：  
	    String[] keys1=config.getStringArray("keys");  
	        List keys2=config.getList("keys");  
	}
	
	
}
