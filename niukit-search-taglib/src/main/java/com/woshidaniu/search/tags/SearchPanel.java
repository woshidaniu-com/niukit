/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.search.core.Version;
import com.woshidaniu.search.utils.TemplateUtil;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询面板
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月19日下午3:06:05
 * @since 1.3.11
 */
public class SearchPanel extends TagSupport{

	private static final long serialVersionUID = 6705543647473104233L;
	
	private String theme;
	private String id;
	
	private QueryBody inputPart;
	private List<QueryBody> childList = new ArrayList<QueryBody>();
	private List<SelectPart> defaultItem = new ArrayList<SelectPart>();
	private static final String DEFAULT_SEARCH_ID = "defaultId";
	
	private static final Map<String,String> versionPageMapping = new HashMap<String,String>();
	
	static {
		versionPageMapping.put("1.0", "panel.ftl");
		versionPageMapping.put("2.0", "panel_v2.ftl");
	}
	@Override
	public int doEndTag() throws JspException {
		String version = Version.getVersion();
		String templateName = versionPageMapping.get(version);
		JspWriter out = this.pageContext.getOut();
		try {
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("inputPart", inputPart);
			data.put("childList", childList);
			data.put("defaultItem", defaultItem);
			if (StringUtils.isNull(id)){
				data.put("id", DEFAULT_SEARCH_ID);
			} else {
				data.put("id", id);
			}
			String panelHtml = TemplateUtil.getTemplateContent(data,theme,templateName);
			out.write(panelHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}


	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	public List<QueryBody> getChildList() {
		return childList;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}


	public void setInputPart(QueryBody inputPart) {
		this.inputPart = inputPart;
	}


	public List<SelectPart> getDefaultItem() {
		return defaultItem;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

}
