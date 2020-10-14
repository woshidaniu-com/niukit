package com.woshidaniu.taglibs.tags;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：自定义标签库抽象类
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2017年2月21日下午4:15:43
 */
public abstract class AbstractTagSupport extends TagSupport {

	private static final long serialVersionUID = 1248122819921969214L;

	private String id;
	private String name;
	private String cssStyle;
	private String cssClass;
	private String dataRules;
	private String defaultValue;

	/**
	 * 
	 * <p>方法说明：处理HTML参数<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2017年2月22日下午2:12:19<p>
	 * @return
	 */
	protected Map<String,String> getHtmlData(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("name", name);
		map.put("cssStyle", cssStyle);
		map.put("cssClass", cssClass);
		map.put("dataRules", dataRules);
		map.put("defaultValue", defaultValue);
		return map;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCssStyle() {
		return cssStyle;
	}
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}
	public String getCssClass() {
		return cssClass;
	}
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	public String getDataRules() {
		return dataRules;
	}
	public void setDataRules(String dataRules) {
		this.dataRules = dataRules;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	
}
