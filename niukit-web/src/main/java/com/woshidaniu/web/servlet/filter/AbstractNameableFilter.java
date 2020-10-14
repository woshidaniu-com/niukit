/**
 * 
 */
package com.woshidaniu.web.servlet.filter;


/**
 * <p>
 * <h3>niutal框架
 * <h3>
 * 说明：给过滤器名称
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月7日下午2:53:20
 */
public abstract class AbstractNameableFilter extends AbstractFilter implements Nameable{

	/**
	 * 过滤器名称
	 */
	protected String name;

	protected String getName() {
		if (this.name == null) {
			this.name = getFilterName();
		}
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
