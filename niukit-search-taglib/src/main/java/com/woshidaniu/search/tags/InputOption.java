/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import com.woshidaniu.search.core.Version;
import com.woshidaniu.search.utils.TemplateUtil;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询-模糊查询标签
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月20日下午2:07:24
 */
public class InputOption extends ListTagSupport implements QueryBody {

	private static final long serialVersionUID = -6759140206893357020L;
	
	private static final Map<String,String> versionPageMapping = new HashMap<String,String>();
	
	static {
		versionPageMapping.put("1.0", "inputOption.ftl");
		versionPageMapping.put("2.0", "inputOption_v2.ftl");
	}
	
	@Override
	public String getHtml(String theme) {
		
		String version = Version.getVersion();
		String templateName = versionPageMapping.get(version);
		
		String html = TemplateUtil.getTemplateContent(getData(),theme,templateName);
		return html;
	}
	
	@Override
	public String getHtml(String theme,String panelId) {
		
		String version = Version.getVersion();
		String templateName = versionPageMapping.get(version);
		
		Map<String,Object> data = getData();
		data.put("panelId", panelId);
		String html = TemplateUtil.getTemplateContent(data,theme,templateName);
		return html;
	}

	@Override
	public int doStartTag() throws JspException {
		SearchPanel panel = (SearchPanel) this.getParent();
		panel.setInputPart(this);
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public Map<String, Object> getData() {
		List<Map<String,Object>> list = super.parseList();
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("optionList", list);
		return data;
	}
}
