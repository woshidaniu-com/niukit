package com.woshidaniu.cache.cache.oscache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.oscache.OSCache;
import com.opensymphony.oscache.OSCacheManager;
import com.woshidaniu.cache.core.Cache;
import com.woshidaniu.cache.core.CacheManager;
import com.woshidaniu.cache.core.exception.CacheException;
import com.woshidaniu.cache.core.exception.ConfigurationException;
import com.woshidaniu.io.utils.ResourceUtils;

public class OSCacheCacheManager implements CacheManager {

	/**
	 * This class's private log instance.
	 */
    private static final Logger LOG = LoggerFactory.getLogger(OSCacheCacheManager.class);
    /**
     * Indicates if the CacheManager instance was implicitly/automatically created by this instance, indicating that
     * it should be automatically cleaned up as well on shutdown.
     */
    private boolean cacheManagerImplicitlyCreated = false;

    /**
     * Classpath file location of the OSCache CacheManager config file.
     */
    private String cacheManagerConfigFile = "classpath:oscache.properties";
    
    /**
     * The OSCache cache manager used by this implementation to create caches.
     */
    protected OSCacheManager cacheManager;
    
	/**
     * Default no argument constructor
     */
    public OSCacheCacheManager() {
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
	public final <K, V> Cache<K, V> getCache(String name) throws CacheException {
		
		if (LOG.isTraceEnabled()) {
            LOG.trace("Acquiring OSCache instance named [" + name + "]");
        }

        try {
        	OSCache cache = ensureCacheManager().getCache(name);
            if (cache == null) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Cache with name '{}' does not yet exist.  Creating now.", name);
                }
                if (LOG.isInfoEnabled()) {
                    LOG.info("Added OSCache named [" + name + "]");
                }
            } else {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Using existing OSCache named [" + cache.getName() + "]");
                }
            }
            return new OSCacheCache<K, V>(cache);
        } catch (CacheException e) {
            throw new CacheException(e);
        }
	}
	
	private OSCacheManager ensureCacheManager() {
        try {
            if (this.cacheManager == null) {
                if (LOG.isDebugEnabled()) {
                	LOG.debug("cacheManager property not set.  Constructing CacheManager instance... ");
                }
                this.cacheManager = new OSCacheManager(getCacheManagerConfigFileInputStream());
                if (LOG.isTraceEnabled()) {
                	LOG.trace("instantiated OSCache CacheManager instance.");
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
     * Shuts-down the wrapped OSCache CacheManager <b>only if implicitly created</b>.
     * <p/>
     * If another component injected
     * a non-null CacheManager into this instace before calling {@link #init() init}, this instance expects that same
     * component to also destroy the CacheManager instance, and it will not attempt to do so.
     */
    public void destroy() {
        if (cacheManagerImplicitlyCreated) {
            try {
            	getCacheManager().shutdown();
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
    
	public OSCacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(OSCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public boolean isCacheManagerImplicitlyCreated() {
		return cacheManagerImplicitlyCreated;
	}

	public void setCacheManagerImplicitlyCreated(
			boolean cacheManagerImplicitlyCreated) {
		this.cacheManagerImplicitlyCreated = cacheManagerImplicitlyCreated;
	}

	public String getCacheManagerConfigFile() {
		return cacheManagerConfigFile;
	}

	public void setCacheManagerConfigFile(String cacheManagerConfigFile) {
		this.cacheManagerConfigFile = cacheManagerConfigFile;
	}
 
}
