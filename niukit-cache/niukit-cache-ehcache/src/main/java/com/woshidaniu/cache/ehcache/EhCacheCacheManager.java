package com.woshidaniu.cache.ehcache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.sf.ehcache.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.cache.core.AbstractCacheManager;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.exception.CacheException;
import com.woshidaniu.cache.core.exception.ConfigurationException;
import com.woshidaniu.io.utils.ResourceUtils;

public class EhCacheCacheManager extends AbstractCacheManager {

	 /**
     * This class's private log instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(EhCacheCacheManager.class);
    
    /**
     * The EhCache cache manager used by this implementation to create caches.
     */
    protected net.sf.ehcache.CacheManager cacheManager;
    /**
     * Indicates if the CacheManager instance was implicitly/automatically created by this instance, indicating that
     * it should be automatically cleaned up as well on shutdown.
     */
    private boolean cacheManagerImplicitlyCreated = false;

    /**
     * Classpath file location of the ehcache CacheManager config file.
     */
    private String cacheManagerConfigFile = "classpath:ehcache.xml";
    
    /**
     * Default no argument constructor
     */
    public EhCacheCacheManager() {
    	
    }

	public EhCacheCacheManager(InputStream configurationInputStream) throws CacheException {
		this.cacheManager = new net.sf.ehcache.CacheManager(configurationInputStream);
	}

	public EhCacheCacheManager(String configurationFileName) throws CacheException {
		this.cacheManager = new net.sf.ehcache.CacheManager(configurationFileName);
	}

	public EhCacheCacheManager(URL configurationURL) throws CacheException {
		this.cacheManager = new net.sf.ehcache.CacheManager(configurationURL);
	}

	public EhCacheCacheManager(Configuration configuration) throws CacheException {
		this.cacheManager = new net.sf.ehcache.CacheManager(configuration);
	}

	public EhCacheCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public String getCacheManagerConfigFile() {
        return this.cacheManagerConfigFile;
    }

    public void setCacheManagerConfigFile(String classpathLocation) {
        this.cacheManagerConfigFile = classpathLocation;
    }

	public net.sf.ehcache.CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	protected InputStream getCacheManagerConfigFileInputStream() {
        String configFile = getCacheManagerConfigFile();
        try {
            return ResourceUtils.getInputStreamForPath(configFile);
        } catch (IOException e) {
            throw new ConfigurationException("Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", e);
        }
    }

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableList(Arrays.asList(cacheManager.getCacheNames()));
	}
	
	@Override
	protected <K, V> Cache<K, V> createCache(String name) throws CacheException {
		try {
			if (LOG.isTraceEnabled()) {
	            LOG.trace("Acquiring EhCache instance named [" + name + "]");
	        }
            net.sf.ehcache.Ehcache cache = ensureCacheManager().getEhcache(name);
            if (cache == null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Cache with name '{}' does not yet exist.  Creating now.", name);
                }
                this.cacheManager.addCache(name);
                cache = cacheManager.getCache(name);
                if (LOG.isInfoEnabled()) {
                    LOG.info("Added EhCache named [" + name + "]");
                }
            } else {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Using existing EHCache named [" + cache.getName() + "]");
                }
            }
            return new EhCacheCache<K, V>(cache);
        } catch (net.sf.ehcache.CacheException e) {
            throw new CacheException(e);
        }
	}

	private net.sf.ehcache.CacheManager ensureCacheManager() {
        try {
            if (this.cacheManager == null) {
                if (LOG.isDebugEnabled()) {
                	LOG.debug("cacheManager property not set.  Constructing CacheManager instance... ");
                }
                //using the CacheManager constructor, the resulting instance is _not_ a VM singleton
                //(as would be the case by calling CacheManager.getInstance().  We do not use the getInstance here
                //because we need to know if we need to destroy the CacheManager instance - using the static call,
                //we don't know which component is responsible for shutting it down.  By using a single EhCacheManager,
                //it will always know to shut down the instance if it was responsible for creating it.
                this.cacheManager = new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream());
                if (LOG.isTraceEnabled()) {
                	LOG.trace("instantiated Ehcache CacheManager instance.");
                }
                cacheManagerImplicitlyCreated = true;
                if (LOG.isDebugEnabled()) {
                	LOG.debug("implicit cacheManager created successfully.");
                }
            }
            return this.cacheManager;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    /**
     * Shuts-down the wrapped Ehcache CacheManager <b>only if implicitly created</b>.
     * <p/>
     * If another component injected
     * a non-null CacheManager into this instace before calling {@link #init() init}, this instance expects that same
     * component to also destroy the CacheManager instance, and it will not attempt to do so.
     */
    public void destroy() {
        if (cacheManagerImplicitlyCreated) {
            try {
                net.sf.ehcache.CacheManager cacheMgr = getCacheManager();
                cacheMgr.shutdown();
            } catch (Throwable t) {
                if (LOG.isWarnEnabled()) {
                	LOG.warn("Unable to cleanly shutdown implicitly created CacheManager instance.  " +
                            "Ignoring (shutting down)...", t);
                }
            } finally {
                this.cacheManager = null;
                this.cacheManagerImplicitlyCreated = false;
            }
        }
    }

	

}
