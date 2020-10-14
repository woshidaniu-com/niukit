package com.woshidaniu.cache.jedis.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.util.Hashing;

public class ShardedJedisPool extends redis.clients.jedis.ShardedJedisPool {

	/*public ShardedJedisPool(GenericObjectPoolConfig poolConfig,String servers) {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		this(poolConfig, shards);
	}*/
	
	public ShardedJedisPool(GenericObjectPoolConfig poolConfig,List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
		super(poolConfig, shards, algo, keyTagPattern);
	}

	public ShardedJedisPool(GenericObjectPoolConfig poolConfig,List<JedisShardInfo> shards) {
		super(poolConfig, shards);
	}

	public ShardedJedisPool(GenericObjectPoolConfig poolConfig,List<JedisShardInfo> shards, Hashing algo) {
		super(poolConfig, shards, algo);
	}

	public ShardedJedisPool(GenericObjectPoolConfig poolConfig,List<JedisShardInfo> shards, Pattern keyTagPattern) {
		super(poolConfig, shards, keyTagPattern);
	}

}
