package com.woshidaniu.cache.xmemcached.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.cache.core.interceptor.CacheInvocation;
import com.woshidaniu.cache.core.utils.CacheKeyUtils;
/**
 * 
 *@类名称	: XmemcachedDefaultClient
 *@类描述	： xmemcached缓存客户端通用调用接口实现
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 10:21:37 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XmemcachedDefaultClient implements IMemcachedCacheClient{
	
	protected static Logger LOG = LoggerFactory.getLogger(XmemcachedDefaultClient.class);
	public MemcachedClient memcachedClient;
	//默认缓存时效 永久 
	public int expiry = CACHE_EXP_FOREVER;
	
	@Override
	public boolean set(String key, Object value) {
		return this.set(key, expiry, value);
	}

	@Override
	public boolean set(String key,int expiry,Object value) {
		boolean flag  = false;
		try {
			flag =  memcachedClient.set(key,expiry, value);
			LOG.info("set cache: [" + key + "]"); 
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return flag;
		
	}
	
	@Override
	public boolean append(String key, Object value) {
		boolean flag = false;
		try {
			flag = memcachedClient.append(key, value);
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
        return flag;
	}
	
	@Override
	public void appendWithNoReply(String key, Object value) {
		try {
			memcachedClient.appendWithNoReply(key, value);
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
	}
	
	@Override
	public boolean prepend(String key, Object value) {
		boolean flag = false;
		try {
			flag = memcachedClient.prepend(key, value);
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
        return flag;
	}
	
	@Override
	public void prependWithNoReply(String key, Object value) {
		try {
			memcachedClient.prependWithNoReply(key, value);
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
	}
	
	@Override
	public boolean add(String key, Object value) {
		return this.add(key, expiry, value);
	}
	
	@Override
	public boolean add(String key, int expiry, Object value) {
		boolean flag  = false;
		try {
			flag =  memcachedClient.add(key, expiry, value);
			LOG.info("add cache: [" + key + "]"); 
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return flag;
	}
	
	@Override
	public void addWithNoReply(String key, Object value) {
		try {
			memcachedClient.addWithNoReply(key, expiry, value);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e); 
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e); 
		}
	}
	
	@Override
	public void addWithNoReply(String key,int expiry, Object value) {
		try {
			memcachedClient.addWithNoReply(key, expiry, value);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e); 
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e); 
		}
	}

	@Override
	public boolean replace(String key, Object value) {
		return this.replace(key, expiry, value);
	}
	
	@Override
	public boolean replace(String key, int expiry, Object value) {
		boolean flag  = false;
		try {
			flag =  memcachedClient.replace(key, expiry, value);
			LOG.info("replace cache: [" + key + "]");  
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return flag;
	}
	
	@Override
	public void replaceWithNoReply(String key, Object value) {
		this.replaceWithNoReply(key, expiry, value);
	}
	
	@Override
	public void replaceWithNoReply(String key, int expiry, Object value) {
		try {
			memcachedClient.replaceWithNoReply(key, expiry, value);
			LOG.info("replace cache: [" + key + "]");  
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
	}

	@Override
	public boolean delete(String key) {
		boolean flag  = false;
		try {
			flag = memcachedClient.delete(key);
			LOG.info("delete cache: [" + key + "]");  
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return flag;
	}

	@Override
	public void deleteWithNoReply(String key){
		try {
			memcachedClient.deleteWithNoReply(key);
			LOG.info("Flush Cache: [" + key + "]");  
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
	}
	
	@Override
	public <T> T get(String key){
		T object = null;
		try {
			object = memcachedClient.<T>get(key); 
			LOG.info("get cache: [" + key + "]");  
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return object;
	}
	
	@Override
	public <T> T get(String key,CacheInvocation<T> invocation){
		return this.get(key, key,invocation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object lock,String key,CacheInvocation<T> invocation){
		T cached = null;
		//缓存服务器开启
		if(isShutdown()){
			synchronized (lock) {
				Object object = this.get(key);
				if(!BlankUtils.isBlank(object)){
					cached =  (T) object;
				}else{
					//缓存过期重新查询
					cached = invocation.getOriginal();
					//永久缓存，实际上最大只有一个月
					this.set(key, invocation.getExpiry(), cached);
				}
			}
		}else{
			cached = invocation.getOriginal();
		}
		return cached;
	}
	
	@Override
	public <T> GetsResponse<T> gets(String key) {
		GetsResponse<T> object = null;
		try {
			object = memcachedClient.gets(key);
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
		
		return object;
	}

	@Override
	public boolean cas(String key, int exp, Object value, long cas){
		boolean flag  = false;
		try {
			flag = memcachedClient.cas(key, exp, value, cas);
			LOG.info("delete cache: [" + key + "]");  
		}  catch (TimeoutException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } 
		return flag;
	}
	
	@Override
	public Map<InetSocketAddress, Map<String, String>> getStats(){
		try {
			return memcachedClient.getStats();
		} catch (MemcachedException e) {
			LOG.error(e.getMessage(), e); 
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e); 
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e); 
		}
		return null;
	}
	

	@Override
	public void flushAll(){
		try {  
			memcachedClient.flushAll(); 
            LOG.info("Flush All Cache !");  
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (TimeoutException e) {
        	LOG.error(e.getMessage(), e); 
		} 
	}
    
	@Override
	public void flushAllWithNoReply(){
		try {  
			memcachedClient.flushAllWithNoReply(); 
            LOG.info("Flush All Cache !");  
        } catch (InterruptedException e) { 
            LOG.error(e.getMessage(), e); 
        } catch (MemcachedException e) { 
            LOG.error(e.getMessage(), e); 
		} 
	}
	
	@Override
    public boolean isMutex(String key) {  
        return isMutex(key, MUTEX_EXP);  
    }  
    
	@Override
    public boolean isMutex(String key, int exp) {  
        boolean status = true;  
        try {  
            if (memcachedClient.add(CacheKeyUtils.genMutexKey(key), exp, "true")) {  
                status = false;  
            }  
        } catch (Exception e) {  
            LOG.error(e.getMessage(), e);  
        }  
        return status;  
    }  
    
    public boolean isNearExpiry(String key, int exp){
		int mutex_expiry = exp - 3 * IMemcachedCacheClient.CACHE_EXP_MINUTE;
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
			return memcachedClient.isShutdown();
		} catch (Exception e) {
			return true;
		}
	}
    
    @Override
	public boolean isEffective() {
		try {
			return isShutdown() || memcachedClient.getStats().isEmpty();
		} catch (Exception e) {
			return false;
		}
	}
    
    /** 
     * Shut down the Memcached Cilent. 
     */  
    @Override
    public void shutdown() {  
        if (memcachedClient != null) {  
            try {  
                if (!isShutdown()) {  
                    memcachedClient.shutdown();  
                    LOG.debug("Shutdown MemcachedManager...");  
                }  
            } catch (Exception e) {  
            	LOG.error(e.getMessage(), e);  
            }
        }  
    }  
    
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	@Override
	public String getClientName() {
		return "memcache";
	}

}
