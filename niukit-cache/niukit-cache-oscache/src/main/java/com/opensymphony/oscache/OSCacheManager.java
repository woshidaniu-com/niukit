/*
 * Copyright (c) 2010-2020, kangzhidong (hnxyhcwdl1003@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.opensymphony.oscache;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.cache.core.exception.CacheException;
import com.woshidaniu.io.utils.ResourceUtils;

/**
 * *******************************************************************
 * @className	： CacheManager
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Nov 7, 2016 9:16:44 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class OSCacheManager {
	
	/**
	 * This class's private log instance.
	 */
    private static final Logger LOG = LoggerFactory.getLogger(OSCacheManager.class);
    
	/**
     * Default name if not specified in the configuration/
     */
    public static final String DEFAULT_NAME = "__DEFAULT__";
	/**
	 * 缓存名称取值key
	 */
    public final static String CACHE_NAME_KEY = "cache.name";
	 /**
     * OScaches managed by this manager.
     */
    private final ConcurrentMap<String, OSCache> oscaches = new ConcurrentHashMap<String, OSCache>();
    
    private String encoding = "UTF-8";
    
    public OSCacheManager(String configurationFileName) throws IOException {
		this(ResourceUtils.getInputStreamForPath(configurationFileName));
	}
    
    public OSCacheManager(URL configurationURL) throws IOException {
		this(new FileInputStream(ResourceUtils.getResourceAsFile(configurationURL)));
	}
    
    public OSCacheManager(InputStream configurationInputStream) throws CacheException {
    	
    	Properties properties = this.getNewProperties(configurationInputStream);
    	
    	String name = properties.getProperty(CACHE_NAME_KEY, DEFAULT_NAME );
    	OSCache ret = oscaches.get(name);
		if (ret != null) {
			return;
		}
		//创建相应的缓存对象
		OSCache cache =	new OSCache(this.getNewProperties(configurationInputStream));
    	oscaches.putIfAbsent(name, cache);
    	
	}
	
    public OSCache addCache(OSCache cache){
    	OSCache ret = oscaches.get(cache.getName());
		if (ret != null) {
			return ret;
		} 
    	OSCache existing = oscaches.putIfAbsent(cache.getName(), cache);
		if (existing != null) {
			ret = existing;
		}
		return ret;
    }

    public OSCache getCache(String name){
    	if (StringUtils.isNotEmpty(name)) {
    		OSCache ret = oscaches.get(name);
 			if (ret != null) {
 				return ret;
 			} 
 		}
 		return null;
    }
   
    public String[] getCacheNames() throws IllegalStateException {
        String[] list = new String[oscaches.size()];
        return oscaches.keySet().toArray(list);
    }
    
    public void shutdown() {
    	
	}
    
    protected Properties getNewProperties(InputStream stream){
		Properties ret = new Properties();
		synchronized (this) {
 			try {
				ret.load(new InputStreamReader(stream, getEncoding()));
 			} catch (IOException e) {
 				LOG.error(e.getMessage());
			} finally {
 				IOUtils.closeQuietly(stream);
 			}
		}
		return ret;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
    
    
}
