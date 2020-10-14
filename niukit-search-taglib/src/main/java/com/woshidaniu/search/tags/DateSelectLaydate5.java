/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.woshidaniu.search.utils.TemplateUtil;

/**
 * @className	： DateSelectLaydate5
 * @description	： 日期组件，对应日期控件版本laydate 5
 * @author 		：康康（1571）
 * @date		： 2018年8月28日 下午6:37:16
 * @version 	V1.0
 */
public class DateSelectLaydate5 extends DateSelect {
	
	private static final long serialVersionUID = 1L;

	private static final String template_name="selectDate_laydate5.ftl";
	private String name;
	private String text;
	private String type;
	
	private static final Map<String,String> TypeToFormatMapper = new HashMap<String,String>();
	
	private static String AcceptType = "";
	
	static {
		TypeToFormatMapper.put("date", "YYYY-MM-DD");
		TypeToFormatMapper.put("datetime", "YYYY-MM-DD hh:mm:ss");
		TypeToFormatMapper.put("month", "YYYY-MM");
		TypeToFormatMapper.put("year", "YYYY");
		
		Iterator<Entry<String, String>> it = TypeToFormatMapper.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> e = it.next();
			String type = e.getKey();
			String format = e.getValue();
			AcceptType = AcceptType +type+"-->"+format+",";
		}
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.search.tags.QueryBody#getData()
	 */
	public Map<String, Object> getData() {
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("name", name);
		data.put("text", text);
		data.put("type", type);
		
		String format = TypeToFormatMapper.get(type);
		if(format == null) {
			String message = "未支持的type:"+type+",仅支持 :" + AcceptType.toString();
			throw new RuntimeException(message);
		}else {
			data.put("format", format);
		}
		return data;
	}
}
