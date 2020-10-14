/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.woshidaniu.search.utils.TemplateUtil;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询-组合标签
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年12月1日上午11:13:07
 */
public class ItemGroup extends TagSupport implements QueryBody {

	private static final long serialVersionUID = -2655047163552811506L;
	private List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
	private static final String TEMPLATE_NAME = "itemGroup.ftl";
	private String name;
	private String text;
	
	@Override
	public int doEndTag() throws JspException {
		
		SearchPanel panel = (SearchPanel) this.getParent();
		panel.getChildList().add(this);
		
		return EVAL_PAGE;
	}


	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}


	public List<Map<String, Object>> getChildList() {
		return childList;
	}


	@Override
	public String getHtml(String theme) {
		String html = TemplateUtil.getTemplateContent(getData(),theme,TEMPLATE_NAME);
		return html;
	}

	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.search.tags.QueryBody#getHtml(java.lang.String, java.lang.String)
	 */
	public String getHtml(String theme,String panelId) {
		Map<String,Object> data = getData();
		data.put("panelId", panelId);
		String html = TemplateUtil.getTemplateContent(data,theme,TEMPLATE_NAME);
		return html;
	}

	@Override
	public Map<String, Object> getData() {
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("childList", childList);
		data.put("name", name);
		data.put("text", text);
		return data;
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
	
	
}
