package com.woshidaniu.cache.jedis.client;

import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.cache.core.interceptor.CacheInvocation;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
 
/**
 * 
 *@类名称	: NewJedisClient.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 24, 2016 3:43:24 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class NewJedisClient implements IJedisCacheClient{
	
	protected static Logger LOG = LoggerFactory.getLogger(NewJedisClient.class);
	
	protected ShardedJedisPool jedisPool;
	
	static {
	    ResourceBundle bundle = ResourceBundle.getBundle("redis");
	    if (bundle == null) {
	    	throw new IllegalArgumentException("[redis.properties] is not found!");
	    }
	    JedisPoolConfig config = new JedisPoolConfig();
	    config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
	    config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
	    config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
	    config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
	    config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
	    //pool = new ShardedJedisPool(config, bundle.getString("redis.ip"),Integer.valueOf(bundle.getString("redis.port")));
	}
	
	@Override
	public boolean set(String key, Object value) {
		return this.set(key, IJedisCacheClient.CACHE_EXP_FOREVER, value);
	}

	@Override
	public boolean set(String key,int expiry,Object value) {
		boolean flag  = false;
		ShardedJedis resource = null;
		try {
			resource = jedisPool.getResource();
			resource.setex(key, expiry, value.toString());
			LOG.info("set cache: [" + key + "]"); 
		}  catch (Exception e) { 
            LOG.error(e.getMessage(), e); 
        } 
		jedisPool.returnBrokenResource(resource);
		jedisPool.returnResourceObject(resource);
		jedisPool.returnBrokenResource(resource);
		return flag;
		
	}
	
	@Override
	public boolean replace(String key, Object value) {
		return this.replace(key, IJedisCacheClient.CACHE_EXP_FOREVER, value);
	}
	
	@Override
	public boolean replace(String key, int expiry, Object value) {
		boolean flag  = false;
		try {
			LOG.info("replace cache: [" + key + "]");  
		}  catch (Exception e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return flag;
	}
	
	@Override
	public <T> T get(String key){
		T object = null;
		try {
			LOG.info("get cache: [" + key + "]");  
		}  catch (Exception e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return object;
	}

	@Override
	public boolean delete(String key) {
		boolean flag  = false;
		try {
			LOG.info("delete cache: [" + key + "]");  
		}  catch (Exception e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return flag;
	}
	
	@Override
	public void flushAll(){
		try {  
            LOG.info("flushAll Cache !");  
		}  catch (Exception e) { 
            LOG.error(e.getMessage(), e); 
        } 
	}
	
    public boolean isMutex(String key) {  
        return isMutex(key, MUTEX_EXP);  
    }  
    
    public boolean isMutex(String key, int exp) {  
        boolean status = true;  
        try {  
        } catch (Exception e) {  
            LOG.error(e.getMessage(), e);  
        }  
        return status;  
    }  
    
    public boolean isNearExpiry(String key, int exp){
		int mutex_expiry = exp - 3 * IJedisCacheClient.CACHE_EXP_MINUTE;
		if( mutex_expiry > 0){
		    // 3 min timeout to avoid mutex holder crash
		    if (isMutex(key, mutex_expiry) == true) {
		    	return true;
		    }
		}
		return false;
    }
  
    @Override
	public boolean isShutdown() {
		try {
			return true;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 24, 20163:50:41 PM
	 *@param <T>
	 *@param key
	 *@param invocation
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	: 
	 */
	@Override
	public <T> T get(String key, CacheInvocation<T> invocation) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 24, 20163:50:41 PM
	 *@param <T>
	 *@param lock
	 *@param key
	 *@param invocation
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	: 
	 */
	@Override
	public <T> T get(Object lock, String key, CacheInvocation<T> invocation) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 24, 20163:50:41 PM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	: 
	 */
	@Override
	public String getClientName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 24, 20163:50:41 PM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	: 
	 */
	@Override
	public boolean isEffective() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 24, 20163:50:41 PM
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	: 
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		if (jedisPool != null) {  
            try {  
                if (!jedisPool.isClosed()) {  
                    jedisPool.close();
                    LOG.debug("Shutdown MemcachedManager...");  
                }  
            } catch (Exception e) {  
            	LOG.error(e.getMessage(), e);  
            }
        }  
	}
    

}
