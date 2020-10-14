/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.Map;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询部件
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月19日下午3:42:53
 * @since 1.3.11
 */
public interface QueryBody {

	/**
	 * 
	 * <p>方法说明：构建查询部件的HTML元素<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年9月19日下午4:22:28<p>
	 * @param theme 风格
	 * @return
	 */
	String getHtml(String theme);
	
	
	/**
	 * 
	 * <p>方法说明：构建查询部件的HTML元素<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年12月29日上午10:33:20<p>
	 * @param theme
	 * @param panelId
	 * @return
	 */
	String getHtml(String theme,String panelId);
	
	
	/**
	 * 
	 * <p>方法说明：查询部件相关数据<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年11月28日上午10:12:58<p>
	 * @return
	 */
	Map<String,Object> getData();
	
	
}
