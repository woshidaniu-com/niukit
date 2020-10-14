package com.opensymphony.oscache;


import java.util.Date;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class KeyPrefixCacheManager extends GeneralCacheAdministrator {
	
	private static final long serialVersionUID = -4397192926052141162L;  
    private String keyPrefix; //关键字前缀字符，区别属于哪个模块  
    private int refreshPeriod; //过期时间(单位为秒);  

	public KeyPrefixCacheManager(String keyPrefix, int refreshPeriod) {
		super();
		this.keyPrefix = keyPrefix;
		this.refreshPeriod = refreshPeriod;
	}

	/**
	 * 
	 * @description	：添加被缓存的对象
	 * @author 		： kangzhidong
	 * @date 		：Jan 2, 2016 11:59:28 PM
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		this.putInCache(this.keyPrefix + "_" + key, value);
	}

	/**
	 * 
	 * @description	： 删除被缓存的对象
	 * @author 		： kangzhidong
	 * @date 		：Jan 2, 2016 11:59:48 PM
	 * @param key
	 */
	public void remove(String key) {
		this.flushEntry(this.keyPrefix + "_" + key);
	}

	/**
	 * 
	 * @description	： 删除指定日期所有被缓存的对象
	 * @author 		： kangzhidong
	 * @date 		：Jan 3, 2016 12:00:04 AM
	 * @param date
	 */
	public void removeAll(Date date) {
		this.flushAll(date);
	}

	/**
	 * 
	 * @description	： 删除所有被缓存的对象
	 * @author 		： kangzhidong
	 * @date 		：Jan 3, 2016 12:00:19 AM
	 */
	public void removeAll() {
		this.flushAll();
	}

	/**
	 * 
	 * @description	：获取被缓存的对象
	 * @author 		： kangzhidong
	 * @date 		：Jan 3, 2016 12:00:36 AM
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Object get(String key) throws Exception {
		try {
			return this.getFromCache(this.keyPrefix + "_" + key,this.refreshPeriod);
		} catch (NeedsRefreshException e) {
			this.cancelUpdate(this.keyPrefix + "_" + key);
			throw e;
		}

	}
}
