package com.woshidaniu.cache.xmemcached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;

/**
 *@类名称		： XmemcachedCache.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：Feb 23, 2017 5:16:43 PM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 *@param <K>
 *@param <V>
 *@see http://www.jb51.net/article/38494.htm
 *@see http://blog.163.com/sbg_wg@126/blog/static/1220467132011102244426760/
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class XmemcachedCache<K, V> implements Cache<K, V> {
	
	protected static final Logger LOG = LoggerFactory.getLogger(XmemcachedCache.class);
	protected final MemcachedClient cache;
	protected String keyPrefix; //关键字前缀字符，区别属于哪个模块  
	
	public XmemcachedCache(MemcachedClient client) {
		this(client, null);
	}
	
	public XmemcachedCache(MemcachedClient client,String keyPrefix) {
		if (client == null) {
			throw new CacheException("error Memcached ");
		}
		this.cache = client;
		this.keyPrefix = keyPrefix;
	}
	
	protected String getKey(K k) {
		return this.keyPrefix == null ? String.valueOf(k) : this.keyPrefix + "_" + String.valueOf(k);
	}

	@Override
	public String getName() throws CacheException {
		try {
			return this.cache.getName();
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V get(K k) throws CacheException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.trace("Get data in cache [" + cache.getName() + "] for key [" + k + "]");
			}
			if(k == null){
				return null;
			}
			V element = cache.<V>get(getKey(k));
			if(element == null){
				if (LOG.isDebugEnabled()) {
					LOG.debug("Data from KEY:{} is null.", k);
				}
				return null;
			}
			return element;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@Override
	public V put(K k, V v) throws CacheException {
		if (LOG.isDebugEnabled()) {
			LOG.trace("Put data in cache [" + cache.getName() + "] for key [" + k + "]");
        }
        try {
            V previous = get(k);
            cache.set(getKey(k), CACHE_EXP_FOREVER, v);
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public V remove(K k) throws CacheException {
		if (LOG.isDebugEnabled()) {
			LOG.trace("Remove data from cache [" + cache.getName() + "] for key [" + k + "]");
        }
        try {
            V previous = get(k);
            cache.delete(getKey(k));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public void clear() throws CacheException {
		if (LOG.isDebugEnabled()) {
			LOG.trace("Clear all data from cache [" + cache.getName() + "]");
        }
        try {
        	cache.flushAll();;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public long size() throws CacheException {
		try {
			long count = 0;
			//获取所有缓存节点状态信息
			Map<InetSocketAddress,Map<String,String>>  statsMaps =  cache.getStats();
			//遍历所有缓存节点状态信息
			for (InetSocketAddress address : statsMaps.keySet()) {
				 Map<String, String> status = cache.stats(address);
				 for (String key : status.keySet()) {
					if(Stats.STAT_CURR_ITEMS.getName().equalsIgnoreCase(key)){
						 String status_val = status.get(key);  
						 count += Long.parseLong(status_val);
					}
				 }
			}
            return count;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}
	
	@Override
	public Set<K> keys() throws CacheException {
		 try {
		    Set<K> keys = new LinkedHashSet<K>();  
		    Map<InetSocketAddress, Map<String, String>> items = cache.getStats();  
		    for (Iterator<InetSocketAddress> itemIt = items.keySet().iterator(); itemIt.hasNext();) {  
		    	InetSocketAddress address = itemIt.next();  
		    	KeyIterator keyIterator = cache.getKeyIterator(address);
		    	//遍历key
		    	for (String key = keyIterator.next(); keyIterator.hasNext();) {  
		    		keys.add((K) key.trim()); 
		    	}
			}
            if (keys != null && keys.size() > 0) {
                return Collections.unmodifiableSet(keys);
            } else {
                return Collections.emptySet();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	@Override
	public Collection<V> values() throws CacheException {
		try {
			Set<K> keys = this.keys();
			if (keys != null && keys.size() > 0) {
				List<V> values = new ArrayList<V>(keys.size());
				for (K key : keys) {
					V value = get(key);
					if (value != null) {
						values.add(value);
					}
				}
				return Collections.unmodifiableList(values);
			} else {
				return Collections.emptyList();
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public Map<String, Map<String, String>> stats() throws CacheException {
		try {
			Map<String, Map<String, String>> statsMaps_r = new HashMap<String, Map<String,String>>();
			//获取所有缓存节点状态信息
			Map<InetSocketAddress,Map<String,String>>  statsMaps =  cache.getStats();
			//遍历所有缓存节点状态信息
			for (InetSocketAddress address : statsMaps.keySet()) {
				statsMaps_r.put(address.toString(), statsMaps.get(address));
			}
			return statsMaps_r;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}
	
	@Override
	public Object origin() throws CacheException {
		return cache;
	}
	

}
