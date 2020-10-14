/**
 * 
 */
package com.woshidaniu.spring.web.filter.integration;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.FactoryBean;

import com.woshidaniu.basicutils.CollectionUtils;
import com.woshidaniu.cache.core.CacheManager;
import com.woshidaniu.web.core.config.Ini;
import com.woshidaniu.web.servlet.filter.AbstractRouteableFilter;
import com.woshidaniu.web.servlet.filter.Nameable;
import com.woshidaniu.web.servlet.filter.cache.CacheSupport;
import com.woshidaniu.web.servlet.filter.chain.FilterChainManager;
import com.woshidaniu.web.servlet.filter.chain.FilterChainResolver;
import com.woshidaniu.web.servlet.filter.chain.impl.DefaultFilterChainManager;
import com.woshidaniu.web.servlet.filter.chain.impl.PathMatchingFilterChainResolver;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日上午11:15:33
 */
public class ZFFilterFactoryBean implements FactoryBean<Filter> {
	/**
	 * 过滤器定义
	 */
	private Map<String, Filter> filters;

	/**
	 * 过滤器链定义
	 */
    private Map<String, String> filterChainDefinitionMap;

    private AbstractRouteableFilter instance;
    
    private CacheManager cacheManager;
    
	public ZFFilterFactoryBean() {
		filters = new LinkedHashMap<String, Filter>();
		filterChainDefinitionMap = new LinkedHashMap<String, String>();
	}

	@Override
	public Filter getObject() throws Exception {
		if(instance == null){
			instance = createInstance();
		}
		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		return SpringwoshidaniuFilter.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Map<String, Filter> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Filter> filters) {
		this.filters = filters;
	}

	public Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(
			Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setFilterChainDefinitions(String definitions) {
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setFilterChainDefinitionMap(section);
    }
	
	protected FilterChainManager createFilterChainManager() {
        FilterChainManager manager = new DefaultFilterChainManager();
        Map<String, Filter> filters = getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }
                
                //try to set cachemanager if possiable
                if(filter instanceof CacheSupport){
                	((CacheSupport)filter).setCacheManager(getCacheManager());
                }
                
                manager.addFilter(name, filter, false);
            }
        }

        Map<String, String> chains = getFilterChainDefinitionMap();
        if (!CollectionUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                manager.createChain(url, chainDefinition);
            }
        }

        return manager;
    }
	
	protected AbstractRouteableFilter createInstance() throws Exception {
        FilterChainManager manager = createFilterChainManager();
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);
        return new SpringwoshidaniuFilter(chainResolver);
    }
	
	/**
	 * <p>
	 *   <h3>niutal框架<h3>
	 *   说明：静态内部类
	 * <p>
	 * @author <a href="#">Zhangxiaobin[1036]<a>
	 * @version 2016年7月19日上午11:22:15
	 */
	private static class SpringwoshidaniuFilter extends AbstractRouteableFilter{

		public SpringwoshidaniuFilter(FilterChainResolver filterChainResolver) {
			super(filterChainResolver);
		}
	}
	
}
