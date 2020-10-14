/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.woshidaniu.basicutils.RegexUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.search.utils.TemplateUtil;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询-多选标签
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月19日下午4:32:39
 * @since 1.3.11
 */
public class MultiOption extends ListTagSupport  implements QueryBody,SelectPart{

	private static final long serialVersionUID = -4418732626282622402L;
	private static final String template_name="selectOption.ftl";
	private static final String type_name="multiOption";
	private static final String default_value_split = ",";
	private static final String default_value_patten = "(\\[){1,}+(\\s?+\\w{1,}+,*){1,}+(\\])$";
	private String defaultValue;
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		
		Tag tag = this.getParent();
		
		if (tag instanceof SearchPanel){
			SearchPanel panel = (SearchPanel) this.getParent();
			panel.getChildList().add(this);
			
			if (!StringUtils.isNull(defaultValue)) {
				panel.getDefaultItem().add(this);
			}
		} else if (tag instanceof ItemGroup){
			ItemGroup group = (ItemGroup) tag;
			group.getChildList().add(this.getData());
		}
		
		return EVAL_BODY_INCLUDE;
	}

	
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
	
	//解析默认值 
	private List<String> parseDefaultValue(){
		List<String> valueList = new ArrayList<String>();
		
		if (RegexUtils.isContentMatche(defaultValue, default_value_patten)){
			List<String> arrList = Arrays.asList(defaultValue.substring(1, defaultValue.length()-1).split(default_value_split));
			valueList.addAll(arrList);
		} else {
			valueList.add(defaultValue);
		}
		return valueList;
	}
	
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.search.tags.SelectPart#getDefaultItem()
	 */
	public List<Map<String,Object>> getDefaultItem() {
		List<Map<String,Object>> defaultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> optionList = super.parseList();
		List<String> defaultValue = parseDefaultValue();
		for (Map<String,Object> map : optionList){
			if (defaultValue.contains(map.get(KEY_NAME))){
				defaultList.add(map);
			}
		}
		return defaultList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.search.tags.QueryBody#getData()
	 */
	public Map<String, Object> getData() {
		List<Map<String,Object>> list = super.parseList();
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("optionList", list);
		data.put("name", super.getName());
		data.put("text", super.getText());
		data.put("pinyin", super.isPinyin());
		data.put("hasBlank", super.isHasBlank());
		data.put("type", type_name);
		
		if (!StringUtils.isNull(super.getRelation())){
			data.put("relation", super.parseRelation());
		}
		if (!StringUtils.isNull(defaultValue)){
			data.put("defaultValue", parseDefaultValue());
		}
		return data;
	}
}
