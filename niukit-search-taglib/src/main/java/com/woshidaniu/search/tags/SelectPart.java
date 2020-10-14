/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.List;
import java.util.Map;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询-选择部件
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月21日上午10:59:10
 */
public interface SelectPart {

	/**
	 * 
	 * <p>方法说明：默认选项<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年9月21日上午10:59:25<p>
	 * @return
	 */
	List<Map<String,Object>> getDefaultItem();
}
