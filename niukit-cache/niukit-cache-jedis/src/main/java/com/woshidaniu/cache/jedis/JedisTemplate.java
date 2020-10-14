package com.woshidaniu.cache.jedis;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 对Jedis进行一些常用的模板封装。<br/>
 * 对springside中的JedisTemplate进行了一些改造。<br/>
 * 还有未封装的方法可以直接通过getJedis获得Jedis再来调用。
 * 
 * @author guanzhenxing
 */
public class JedisTemplate {
	protected static Logger LOG = LoggerFactory.getLogger(JedisTemplate.class);
	private JedisPool jedisPool;

	public JedisTemplate(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 获得Jedis
	 * 
	 * @return
	 */
	public Jedis getJedis() {
		return jedisPool.getResource();
	}

	/**
	 * 执行有返回结果的action。
	 */
	public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			return jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			LOG.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 执行无返回结果的action。
	 */
	public void execute(JedisActionNoResult jedisAction) throws JedisException {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			LOG.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource。
	 */
	protected void closeResource(Jedis jedis, boolean connectionBroken) {
		if (jedis != null) {
			try {
				if (connectionBroken) {
					jedisPool.returnBrokenResource(jedis);
				} else {
					jedisPool.returnResource(jedis);
				}
			} catch (Exception e) {
				LOG.error(
						"Error happen when return jedis to pool, try to close it directly.",
						e);
				closeJedis(jedis);
			}
		}
	}

	/**
	 * 退出然后关闭Jedis连接。
	 */
	private void closeJedis(Jedis jedis) {
		if (jedis.isConnected()) {
			try {
				try {
					jedis.quit();
				} catch (Exception e) {
				}
				jedis.disconnect();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 获取内部的pool做进一步的动作。
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	/**
	 * 有返回结果的回调接口定义。
	 */
	public interface JedisAction<T> {
		T action(Jedis jedis);
	}

	/**
	 * 无返回结果的回调接口定义。
	 */
	public interface JedisActionNoResult {
		void action(Jedis jedis);
	}

	// ////////////// 常用方法的封装 ///////////////////////// //

	// ////////////// 公共 ///////////////////////////

	public void flushDB() {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.flushDB();
			}
		});
	}

	// ////////////// 关于Key ///////////////////////////

	/**
	 * key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return execute(new JedisAction<Boolean>() {

			public Boolean action(Jedis jedis) {
				return jedis.exists(key);
			}

		});
	}

	/**
	 * key的类型
	 * 
	 * @param key
	 * @return none (key不存在) string (字符串) list (列表) set (集合) zset (有序集) hash
	 *         (哈希表)
	 */
	public String type(final String key) {
		return execute(new JedisAction<String>() {

			public String action(Jedis jedis) {
				return jedis.type(key);
			}

		});
	}

	/**
	 * key的有效时间
	 * 
	 * @param key
	 * @return -2: key不存在, -1:无过期时间
	 */
	public long ttl(final String key) {
		return execute(new JedisAction<Long>() {

			public Long action(Jedis jedis) {
				return jedis.ttl(key);
			}

		});
	}

	/**
	 * 删除key, 如果key存在返回true, 否则返回false。
	 */
	public boolean del(final String... key) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.del(key) == 1 ? true : false;
			}
		});
	}

	/**
	 * 模糊查询keys
	 * 
	 * @type keyword
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(final String pattern) {
		return execute(new JedisAction<Set<String>>() {

			public Set<String> action(Jedis jedis) {
				return jedis.keys(pattern);
			}

		});
	}

	/**
	 * key有效期
	 * 
	 * @param key
	 * @param expire
	 *            seconds
	 * @return 1 如果设置了过期时间 0 如果没有设置过期时间，或者不能设置过期时间
	 */
	public long expire(final String key, final int expire) {
		return execute(new JedisAction<Long>() {

			public Long action(Jedis jedis) {
				return jedis.expire(key, expire);
			}

		});
	}

	/**
	 * key有效期 unixTime时间
	 * 
	 * @param key
	 * @param unixTime
	 *            (截止时间戳)
	 * @return
	 */
	public long expireAt(final String key, final long unixTime) {
		return execute(new JedisAction<Long>() {

			public Long action(Jedis jedis) {
				return jedis.expireAt(key, unixTime);
			}

		});
	}

	// ////////////// 关于String ///////////////////////////
	/**
	 * 如果key不存在, 返回null.
	 */
	public String get(final String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}

	/**
	 * 如果key不存在, 返回0.
	 */
	public Long getAsLong(final String key) {
		String result = get(key);
		return result != null ? Long.valueOf(result) : 0;
	}

	/**
	 * 如果key不存在, 返回0.
	 */
	public Integer getAsInt(final String key) {
		String result = get(key);
		return result != null ? Integer.valueOf(result) : 0;
	}

	public void set(final String key, final String value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.set(key, value);
			}
		});
	}

	public void setex(final String key, final String value, final int seconds) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.setex(key, seconds, value);
			}
		});
	}

	/**
	 * 如果key还不存在则进行设置，返回true，否则返回false.
	 */
	public boolean setnx(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.setnx(key, value) == 1 ? true : false;
			}
		});
	}

	public long incr(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}

	public long decr(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.decr(key);
			}
		});
	}

	// ////////////// 关于List ///////////////////////////
	public void lpush(final String key, final String value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.lpush(key, value);
			}
		});
	}

	/**
	 * 返回List长度, key不存在时返回0，key类型不是list时抛出异常.
	 */
	public long llen(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.llen(key);
			}
		});
	}

	/**
	 * 删除List中的第一个等于value的元素，value不存在或key不存在时返回0.
	 */
	public boolean lremOne(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				Long count = jedis.lrem(key, 1, value);
				return (count == 1);
			}
		});
	}

	/**
	 * 删除List中的所有等于value的元素，value不存在或key不存在时返回0.
	 */
	public boolean lremAll(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				Long count = jedis.lrem(key, 0, value);
				return (count > 0);
			}
		});
	}

	// ////////////// 关于Sorted Set ///////////////////////////
	/**
	 * 加入Sorted set, 如果member在Set里已存在，只更新score并返回false,否则返回true.
	 */
	public boolean zadd(final String key, final String member,
			final double score) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.zadd(key, score, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 删除sorted set中的元素，成功删除返回true，key或member不存在返回false。
	 */
	public boolean zrem(final String key, final String member) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.zrem(key, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 返回List长度, key不存在时返回0，key类型不是sorted set时抛出异常.
	 */
	public long zcard(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.zcard(key);
			}
		});
	}
}
