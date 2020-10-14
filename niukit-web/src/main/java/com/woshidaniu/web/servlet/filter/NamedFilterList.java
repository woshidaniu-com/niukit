/**
 * 
 */
package com.woshidaniu.web.servlet.filter;

import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：具有名称的Filter列表
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日下午3:51:02
 */
public interface NamedFilterList extends List<Filter> {
	 
	/**
     * Returns the configuration-unique name assigned to this {@code Filter} list.
     */
    String getName();

    /**
     * Returns a new {@code FilterChain} instance that will first execute this list's {@code Filter}s (in list order)
     * and end with the execution of the given {@code filterChain} instance.
     */
    FilterChain proxy(FilterChain filterChain);
}
