package com.woshidaniu.cache.jedis;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import com.woshidaniu.cache.core.AbstractCacheManager;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;

public class JedisClusterManager extends AbstractCacheManager {

	private JedisCluster cluster;

	public JedisClusterManager() {
		
	}

	public JedisClusterManager(String host, int port) {
		this(new JedisPoolConfig(), new HostAndPort(host, port));
	}
	
	public JedisClusterManager(JedisPoolConfig poolConfig, HostAndPort ... nodes) {
		this(new JedisPoolConfig(), new LinkedHashSet<HostAndPort>(Arrays.asList(nodes)));
	}

	public JedisClusterManager(JedisPoolConfig poolConfig, Set<HostAndPort> nodes) {
		/*// 数据库链接池配置
		JedisPoolConfig config = new JedisPoolConfig(); 
		config.setMaxTotal(100); 
		config.setMaxIdle(50); 
		config.setMinIdle(20); 
		config.setMaxWaitMillis(6 * 1000); 
		config.setTestOnBorrow(true); 
		// Redis集群的节点集合
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort("192.168.1.111", 7111));
		jedisClusterNodes.add(new HostAndPort("192.168.1.112", 7112));
		jedisClusterNodes.add(new HostAndPort("192.168.1.113", 7113));
		jedisClusterNodes.add(new HostAndPort("192.168.1.114", 7114));
		jedisClusterNodes.add(new HostAndPort("192.168.1.115", 7115));
		jedisClusterNodes.add(new HostAndPort("192.168.1.116", 7116));
		// 根据节点集创集群链接对象
		//JedisCluster jedisCluster = newJedisCluster(jedisClusterNodes);
		// 节点，超时时间，最多重定向次数，链接池
		JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes, 2000, 100,config);
		*/
		this.cluster = new JedisCluster(nodes, poolConfig);  
	}

	public JedisClusterManager(JedisCluster cluster) {
		this.cluster = cluster;
	}

	public JedisCluster getCluster() {
		return cluster;
	}

	public void setCluster(JedisCluster cluster) {
		this.cluster = cluster;
	}

	@Override
	protected <K, V> Cache<K, V> createCache(String name) throws CacheException {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("error name");
		}
		return new JedisClusterCache<K, V>(name, cluster);
	}

}
