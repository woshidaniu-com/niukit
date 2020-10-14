/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import com.woshidaniu.search.utils.TemplateUtil;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：日期组件
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月30日下午3:30:32
 */
public class DateSelect extends TagSupport implements QueryBody {

	private static final long serialVersionUID = 1L;
	private static final String template_name="selectDate.ftl";
	private String name;
	private String text;
	private String format;

	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.search.tags.QueryBody#getHtml(java.lang.String)
	 */
	public String getHtml(String theme) {
		String html = TemplateUtil.getTemplateContent(getData(),theme,template_name);
		return html;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.search.tags.QueryBody#getHtml(java.lang.String, java.lang.String)
	 */
	public String getHtml(String theme,String panelId) {
		Map<String,Object> data = getData();
		data.put("panelId", panelId);
		String html = TemplateUtil.getTemplateContent(data,theme,template_name);
		return html;
	}
	
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		
		Tag tag = this.getParent();
		
		if (tag instanceof SearchPanel){
			SearchPanel panel = (SearchPanel) tag;
			panel.getChildList().add(this);
		} else if (tag instanceof ItemGroup){
			ItemGroup group = (ItemGroup) tag;
			group.getChildList().add(this.getData());
		}
		
		return EVAL_BODY_INCLUDE;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}


	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.search.tags.QueryBody#getData()
	 */
	public Map<String, Object> getData() {
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("name", name);
		data.put("text", text);
		data.put("format", format);
		return data;
	}
	
}
