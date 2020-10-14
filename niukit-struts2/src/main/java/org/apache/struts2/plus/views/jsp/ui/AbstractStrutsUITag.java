package org.apache.struts2.plus.views.jsp.ui;

import org.apache.struts2.plus.components.AbstractStrutsUIBean;
import org.apache.struts2.views.jsp.ui.AbstractUITag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 *@类名称	: AbstractStrutsUITag.java
 *@类描述	：Struts UI 标签基础抽象对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 8:54:55 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractStrutsUITag extends AbstractUITag {

	protected Logger LOG = LoggerFactory.getLogger(AbstractStrutsUITag.class);
	/**
	 * 功能代码
	 */
	protected String func_code;
	/**
	 * 操作的代码
	 */
	protected String opt_code;
	/**
	 * 组件初始化JSON
	 */
	protected String widget;
	/**
	 * 组件是否使用缓存
	 */
	protected String cacheable;
	/**
	 * 组件是否静态化
	 */
	protected String staticable;
	/**
	 * 参数对象值栈取值key 
	 */
	protected String paramKey;
	/**
	 * 对象值栈取值key 
	 */
	protected String stackKey;
	
	@Override
    protected void populateParams() {
        super.populateParams();
		//设置组件参数
        AbstractStrutsUIBean uibean = (AbstractStrutsUIBean)component;
		uibean.setFunc_code(getFunc_code());
		uibean.setOpt_code(getOpt_code());
		uibean.setWidget(getWidget());
		uibean.setCacheable(StringUtils.getSafeStr(getCacheable(), "0"));
		uibean.setStaticable(StringUtils.getSafeStr(getStaticable(), "0"));
		uibean.setParamKey(getParamKey());
		uibean.setStackKey(getStackKey());
    }
	
	public String getFunc_code() {
		return func_code;
	}

	public void setFunc_code(String funcCode) {
		func_code = funcCode;
	}

	public String getOpt_code() {
		return BlankUtils.isBlank(opt_code) ? "cx" : opt_code;
	}

	public void setOpt_code(String optCode) {
		opt_code = optCode;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
	}

	public String getCacheable() {
		return cacheable;
	}

	public void setCacheable(String cacheable) {
		this.cacheable = cacheable;
	}

	public String getStaticable() {
		return staticable;
	}

	public void setStaticable(String staticable) {
		this.staticable = staticable;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getStackKey() {
		return stackKey;
	}

	public void setStackKey(String stackKey) {
		this.stackKey = stackKey;
	}

}
