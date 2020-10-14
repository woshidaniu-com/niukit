package com.woshidaniu.cache.xmemcached.utils;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class MemCachedUtils {

	protected static Logger LOG = LoggerFactory.getLogger(MemCachedUtils.class);
	public static List<String> getKeySet(MemcachedClient memCachedClient) throws MemcachedException, InterruptedException, TimeoutException {

		List<String> keyList = new ArrayList<String>();

		//获取所有缓存服务器的缓存对象 
		Map<InetSocketAddress, Map<String, String>> items = memCachedClient.getStats();
		//以InetSocketAddress为key；遍历key；获得各个服务上的缓存对象
		Iterator<InetSocketAddress> iterator = items.keySet().iterator();
		InetSocketAddress itemKey  = null;
		Map<String, String> maps = null;
		//int items_number = 0;
		while (iterator.hasNext()) {
			itemKey  = (InetSocketAddress) iterator.next();
			maps  = items.get(itemKey);
			//迭代当前缓存服务器上的缓存对象key
			Iterator<String>  keys = maps.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = maps.get(key);
				/*if (key.toUpperCase().startsWith("items:".toUpperCase()) && key.toUpperCase().endsWith(":number".toUpperCase())) {
					items_number = Integer.parseInt(maps.get(key).trim());
				}
				*/
				if (key.endsWith("number")) {//memcached key 类型  item_str:integer:number_str
					//String[] arr = key.split(":");
					//int slabNumber = Integer.valueOf(arr[1].trim());
					int limit = Integer.valueOf(value.trim());
					Map<InetSocketAddress, Map<String, String>> dumpMaps = memCachedClient.getStats(limit);
					for (Iterator<InetSocketAddress> dumpIt = dumpMaps.keySet().iterator(); dumpIt.hasNext();) {
						InetSocketAddress dumpKey = dumpIt.next();
						Map<String, String> allMap = dumpMaps.get(dumpKey);
						for (Iterator<String> allIt = allMap.keySet().iterator(); allIt.hasNext();) {
							String allKey = allIt.next();
							try {
								keyList.add(URLDecoder.decode(allKey.trim(), "UTF-8"));
							} catch (UnsupportedEncodingException e) {
								LOG.error(e.getMessage());
							}
						}
					}
				}
			}
		}
		return keyList;
		
	}
}
