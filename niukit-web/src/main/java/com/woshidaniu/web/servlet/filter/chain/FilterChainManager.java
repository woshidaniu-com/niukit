/**
 * 
 */
package com.woshidaniu.web.servlet.filter.chain;

import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;

import com.woshidaniu.web.servlet.filter.NamedFilterList;


/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：FilterChain管理器，负责创建和维护filterchian
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日下午2:50:43
 */
public interface FilterChainManager {

	/**
	 * 
	 * <p>方法说明：获取所有Filter<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年7月19日下午3:19:02<p>
	 */
    Map<String, Filter> getFilters();

    /**
     * 
     * <p>方法说明：根据指定的chainName获取filter列表<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年7月19日下午3:19:49<p>
     */
    NamedFilterList getChain(String chainName);

    /**
     * 
     * <p>方法说明：是否有fiterChain<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年7月19日下午3:20:27<p>
     */
    boolean hasChains();

    /**
     * 
     * <p>方法说明：获取filterChain名称列表<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年7月19日下午3:20:49<p>
     */
    Set<String> getChainNames();

    /**
     * 
     * <p>方法说明：生成代理FiterChain,先执行chainName指定的filerChian,最后执行servlet容器的original<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年7月19日下午3:23:42<p>
     */
    FilterChain proxy(FilterChain original, String chainName);

   /**
    * 
    * <p>方法说明：增加filter到filter列表中<p>
    * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
    * <p>时间：2016年7月19日下午3:23:34<p>
    */
    void addFilter(String name, Filter filter);

    /**
     * 
     * <p>方法说明：增加filter到filter列表前，调用初始化方法<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年7月19日下午3:24:25<p>
     */
    void addFilter(String name, Filter filter, boolean init);

    
    /**
     * 
     * <p>方法说明：创建FilterChain<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年7月19日下午3:27:30<p>
     */
    void createChain(String chainName, String chainDefinition);

    /**
     * 
     * <p>方法说明：追加filter到指定的filterChian中<p>
     * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
     * <p>时间：2016年7月19日下午3:28:27<p>
     */
    void addToChain(String chainName, String filterName);
	
}
