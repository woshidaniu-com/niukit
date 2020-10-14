package com.woshidaniu.cache.jedis.client;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
/**
 * 
 * @className	： LocalRedisClient
 * @description	：  memcached缓存客户端实现
 * @author 		： kangzhidong
 * @date		： Dec 17, 2015 4:06:09 PM
 */
public abstract class LocalRedisClient implements IJedisCacheClient{
	
	protected static Logger LOG = LoggerFactory.getLogger(LocalRedisClient.class);
	private JedisPool jedisPool;
	//默认缓存时效 永久 
	private int expiry = 0;
	private String auth = null;
   
    
    private LocalRedisClient() {
	    try {
			ResourceBundle bundle = ResourceBundle.getBundle("jedis");
			if (bundle == null) {
		    	throw new IllegalArgumentException("[jedis.properties] is not found!");
		    }
		    
			JedisPoolConfig config = new JedisPoolConfig();
			//borrowString返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；是否启用后进先出, 默认true
			config.setLifo(StringUtils.getSafeBoolean(bundle.getString("jedis.pool.lifo"), "true"));
			//连接池中最少空闲的连接数,默认为0.
			config.setMinIdle(StringUtils.getSafeInt(bundle.getString("jedis.pool.minIdle"), "0"));
			//最大能够保持idel状态的对象数
			//控制一个pool最多有多少个状态为idle的jedis实例；
			config.setMaxIdle(StringUtils.getSafeInt(bundle.getString("jedis.pool.maxIdle"), "8"));
			//链接池中最大空闲的连接数,默认为8.
			//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示 不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态就成exhausted了，在JedisPoolConfig
			config.setMaxTotal(StringUtils.getSafeInt(bundle.getString("jedis.pool.maxTotal"), "8"));
			//当池内没有返回对象时，最大等待时间获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
			//表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(StringUtils.getSafeInt(bundle.getString("jedis.pool.maxWaitMillis"), "-1"));
			//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
			config.setBlockWhenExhausted(StringUtils.getSafeBoolean(bundle.getString("jedis.pool.blockWhenExhausted"), "true"));
			//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)，达到此值后空闲连接将可能会被移除。负值(-1)表示不移除。
			//表示一个对象至少停留在idle状态的最短时间，然后才能被idle String evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
			config.setMinEvictableIdleTimeMillis(StringUtils.getSafeInt(bundle.getString("jedis.pool.minEvictableIdleTimeMillis"), "-1"));
			//连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1.
			//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
			//在minEvictableIdleTimeMillis基础上，加入了至少 minIdle个对象已经在pool里面了。
			//如果为-1，evicted不会根据idle time驱逐任何对象。
			//如果minEvictableIdleTimeMillis>0，则此项设置无意义，且只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
			config.setSoftMinEvictableIdleTimeMillis(StringUtils.getSafeInt(bundle.getString("jedis.pool.softMinEvictableIdleTimeMillis"), "-1"));
			//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 .
			//表示idle String evitor每次扫描的最多的对象数；
			config.setNumTestsPerEvictionRun(StringUtils.getSafeInt(bundle.getString("jedis.pool.numTestsPerEvictionRun"), "3"));
			//向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值.
			//在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(StringUtils.getSafeBoolean(bundle.getString("jedis.pool.testOnBorrow"), "false"));
			//向连接池“归还”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
			config.setTestOnReturn(StringUtils.getSafeBoolean(bundle.getString("jedis.pool.testOnReturn"), "false"));
			//向连接池“获取”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
			config.setTestOnCreate(StringUtils.getSafeBoolean(bundle.getString("jedis.pool.testOnCreate"), "false"));
			//向调用者输出“链接”对象时，是否检测它的空闲超时；默认为false。如果“链接”空闲超时，将会被移除。建议保持默认值.
			//如果为true，表示有一个idle String evitor线程对idle String进行扫描，如果validate失败，此String会被从pool中drop掉；这一项只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
			config.setTestWhileIdle(StringUtils.getSafeBoolean(bundle.getString("jedis.pool.testWhileIdle"), "true"));
			//“空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1.
			//表示idle String evitor两次扫描之间要sleep的毫秒数；
			config.setTimeBetweenEvictionRunsMillis(StringUtils.getSafeInt(bundle.getString("jedis.pool.timeBetweenEvictionRunsMillis"), "-1"));
			//是否启用pool的jmx管理功能, 默认true -->
			config.setJmxEnabled(StringUtils.getSafeBoolean(bundle.getString("jedis.pool.jmxEnabled"), "true"));
			///MBean StringName = new StringName("org.apache.commons.pool2:type=GenericStringPool,name=" + "pool" + i)
			// 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好. -->
			config.setJmxNamePrefix(StringUtils.getSafeStr("jedis.pool.jmxNamePrefix", "pool"));
			//当“连接池”中active数量达到阀值时，即“链接”资源耗尽时，连接池需要采取的手段
			//表示当pool中的jedis实例都被allocated完时，pool要采取的操作；
			//默认有三种
			//WHEN_EXHAUSTED_FAIL（表示无jedis实例时，直接抛出NoSuchElementException）、
			//WHEN_EXHAUSTED_BLOCK（则表示阻塞住，或者达到maxWait时抛出JedisConnectionException）、 
			//WHEN_EXHAUSTED_GROW（则表示新建一个jedis实例，也就说设置的maxActive无用）；
			//jedis.pool.whenExhaustedAction=WHEN_EXHAUSTED_GROW
			//设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
			config.setEvictionPolicyClassName(StringUtils.getSafeStr("jedis.pool.evictionPolicyClassName", "org.apache.commons.pool2.impl.DefaultEvictionPolicy"));
			
			//redis服务端口 
			int port = StringUtils.getSafeInt(bundle.getString("jedis.port"), "6379");
			//redis服务连接超时时间，默认2000，即2s 
			int timeout = StringUtils.getSafeInt(bundle.getString("jedis.timeout"), "2000");
			//初始化jedis连接池
			jedisPool = new JedisPool(config, bundle.getString("jedis.ip"),port, timeout );
			//默认过期时间
			this.setExpiry(StringUtils.getSafeInt(bundle.getString("jedis.expiry"), "0"));
			//授权用户
			this.setAuth(bundle.getString("jedis.auth"));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e); 
		}
	}
	
    private void initAuth(Jedis jedis){
		if(!BlankUtils.isBlank(this.getAuth())){
			jedis.auth(this.getAuth());
		}
	}
    
	@Override
	public boolean set(String key, Object value) {
		return this.set(key, expiry, value);
	}
	
	@Override
	public boolean set(String key,int expiry,Object value) {
		String result = null;
		Jedis jedis = null;
		boolean flag = true;
		try {
			jedis = jedisPool.getResource();
			//授权
			this.initAuth(jedis);
			LOG.info("set cache: [" + key + "]"); 
		}  catch (Exception e) { 
            LOG.error(e.getMessage(), e); 
            flag = false;
        }finally{
			if (flag){
				jedisPool.returnResource(jedis);
            }else{
            	jedisPool.returnBrokenResource(jedis);
            }
		}
		return true;
		
	}
	
	@Override
	public boolean replace(String key, Object value) {
		return this.replace(key, expiry, value);
	}
	
	@Override
	public boolean replace(String key, int expiry, Object value) {
		boolean flag  = false;
		try {
			LOG.info("replace cache: [" + key + "]");  
		} catch (Exception e) {  
            LOG.error(e.getMessage(), e);  
		} 
		return flag;
	}
	
	@Override
	public <T> T get(String key){
		T String = null;
		try {
			LOG.info("get cache: [" + key + "]");  
		} catch (Exception e) {  
            LOG.error(e.getMessage(), e);  
		} 
		return String;
	}

	@Override
	public boolean delete(String key) {
		boolean flag  = false;
		try {
			LOG.info("delete cache: [" + key + "]");  
		} catch (Exception e) {  
            LOG.error(e.getMessage(), e);  
		} 
		return flag;
	}
	
    
	@Override
	public void flushAll(){
		try {  
            LOG.info("flushAll Cache !");  
		 } catch (Exception e) {  
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
			return jedisPool.isClosed();
		} catch (Exception e) {
			return true;
		}
	}
    
    /** 
     * Shut down the Memcached Cilent. 
     */  
    @Override
    public void shutdown() {  
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
    
	public JedisPool getMemcachedClient() {
		return jedisPool;
	}

	public void setMemcachedClient(JedisPool memcachedClient) {
		this.jedisPool = memcachedClient;
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	
}
