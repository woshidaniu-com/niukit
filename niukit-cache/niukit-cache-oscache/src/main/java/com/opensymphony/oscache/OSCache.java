package com.opensymphony.oscache;


import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.base.algorithm.AbstractConcurrentReadCache;
import com.woshidaniu.beanutils.reflection.ReflectionUtils;

/**
 * 
 * *******************************************************************
 * @className	： OSCache
 * @description	： 常用的方法有：
 * <pre>
 *	public Object getFromCache(String key) throws NeedsRefreshException; //从缓存中获取一个key标识的对象.
 *	public Object getFromCache(String key, int refreshPeriod) throws NeedsRefreshException; //从缓存中获取一个key标识的对象. refreshPeriod刷新周期,标识此对象在缓存中保存的时间(单位:秒)
 *	
 *	public void putInCache(String key, Object content); //存储一个由Key标识的缓存对象.
 *	public void putInCache(String key, Object content, String[] groups); //存储一个由Key标识的属于groups中所有成员的缓存对象.
 *	
 *	public void flushEntry(String key); //更新一个Key标识的缓存对象.
 *	public void flushGroup(String group); //更新一组属于groupr标识的所有缓存对象.
 *	public void flushAll(); //更新所有缓存.
 *	
 *	public void cancelUpdate(String key); //取消更新，只用于在处理捕获的NeedsRefreshException异常并尝试生成新缓存内容失效的时候.
 *	public void removeEntry(String key); //从缓存中移除一个key标识的对象
 *</pre>
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Nov 5, 2016 2:22:37 PM
 * @version 	V1.0 
 * *******************************************************************
 */
@SuppressWarnings({"serial","unchecked"})
public class OSCache extends Cache {
	
	/**
	 * 缓存名称取值key
	 */
    public final static String CACHE_NAME_KEY = "cache.name";
    /**
   	 * 缓存值前缀key
   	 */
    public final static String CACHE_KEYPREFIX_KEY = "cache.keyPrefix";
    /**
	 * 缓存值刷新周期key
	 */
    public final static String CACHE_REFRESHPERIOD_KEY = "cache.refreshPeriod";
    /**
   	 * 缓存值刷新周期key
   	 */
    public final static String CACHE_GROUP_KEY = "cache.group.id";
	/**
	 * This cache name.
	 */
	protected String name;
	
	protected ConcurrentSkipListSet<String> COMPLIED_KEYS = new ConcurrentSkipListSet<String>();
	protected String keyPrefix; //关键字前缀字符，区别属于哪个模块  
    protected int refreshPeriod; //过期时间(单位为秒);  
    
    protected static boolean useMemoryCaching = true;
    protected static boolean unlimitedDiskCache = true; 
    protected static boolean overflowPersistence = true;
    
    public OSCache(Properties p) {
    	super(useMemoryCaching, unlimitedDiskCache, overflowPersistence);
    	this.name = p.getProperty(CACHE_NAME_KEY);
    	if (this.name == null) {
			throw new IllegalArgumentException("Cache instances require a group ID");
	    }
		this.keyPrefix = p.getProperty(CACHE_KEYPREFIX_KEY);
		this.refreshPeriod = Integer.valueOf(p.getProperty(CACHE_REFRESHPERIOD_KEY)).intValue();
    }
    
    public OSCache(Properties p,String cachName,int refreshPeriod) {
    	this(p, cachName, null, refreshPeriod);
    }

	public OSCache(Properties p,String cachName,String keyPrefix, int refreshPeriod) {
		super(useMemoryCaching, unlimitedDiskCache, overflowPersistence);
		if (cachName == null) {
			throw new IllegalArgumentException("Cache instances require a group ID");
	    }
		this.name = cachName;
		this.keyPrefix = keyPrefix;
		this.refreshPeriod = refreshPeriod; 
	}
    
    public OSCache(boolean useMemoryCaching, boolean unlimitedDiskCache, boolean overflowPersistence){
    	super(useMemoryCaching, unlimitedDiskCache, overflowPersistence);
    }
    
    public OSCache(boolean useMemoryCaching, boolean unlimitedDiskCache, boolean overflowPersistence, boolean blocking, String algorithmClass, int capacity) {
    	super(useMemoryCaching, unlimitedDiskCache, overflowPersistence, blocking, algorithmClass, capacity);
    }
    
	private String getKey(String key) {
		return this.keyPrefix == null ? key : this.keyPrefix + "_" + key;
	}
	
	/**
	 * 
	 * @description	：添加被缓存的对象
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
	 * @date 		：Jan 2, 2016 11:59:28 PM
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		String key_ = getKey(key);
		this.putInCache(key_, value);
		COMPLIED_KEYS.add(key_);
	}

	/**
	 * 
	 * @description	： 删除被缓存的对象
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
	 * @date 		：Jan 2, 2016 11:59:48 PM
	 * @param key
	 */
	public void remove(String key) {
		//this.removeEntry(key);
		this.flushEntry(key);
	}
	
	public void removeEntry(String key) {
		super.removeEntry(getKey(key));
	}
	
	public void flushEntry(String key) {
		super.flushEntry(getKey(key));
	}

	/**
	 * 
	 * @description	： 删除指定日期所有被缓存的对象
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
	 * @date 		：Jan 3, 2016 12:00:04 AM
	 * @param date
	 */
	public void removeAll(Date date) {
		this.flushAll(date);
	}

	/**
	 * 
	 * @description	： 删除所有被缓存的对象
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
	 * @date 		：Jan 3, 2016 12:00:19 AM
	 */
	public void removeAll() {
		//this.flushAll();
		this.flushGroup(this.name);
	}

	/**
	 * 
	 * @description	：获取被缓存的对象
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
	 * @date 		：Jan 3, 2016 12:00:36 AM
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Object get(String key) throws Exception {
		try {
			return this.getFromCache(key ,this.refreshPeriod);
		} catch (NeedsRefreshException e) {
			this.cancelUpdate(key);
			throw e;
		}
	}
	
	 public Object getFromCache(String key) throws NeedsRefreshException {
		 return super.getFromCache(getKey(key));
	 }
	 
	 public Object getFromCache(String key, int refreshPeriod) throws NeedsRefreshException {
		 return super.getFromCache(getKey(key), refreshPeriod);
	 }
	 
	 public Object getFromCache(String key, int refreshPeriod, String cronExpression) throws NeedsRefreshException {
		 return super.getFromCache(getKey(key), refreshPeriod, cronExpression);
	 }
	 
	 public void cancelUpdate(String key) {
		 super.cancelUpdate(getKey(key));
	 }

	public String getName() {
		return name;
	}

	public Set<String> getKeys() {
		AbstractConcurrentReadCache cacheMap = (AbstractConcurrentReadCache) ReflectionUtils.getField("cacheMap", this);
		return cacheMap.keySet();
	}
	
}
