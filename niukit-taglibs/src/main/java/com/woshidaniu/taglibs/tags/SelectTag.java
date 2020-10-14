package com.woshidaniu.taglibs.tags;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.woshidaniu.freemarker.utils.FormatUtils;

import freemarker.template.TemplateException;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：select标签
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2017年2月21日下午4:45:45
 */
public class SelectTag extends AbstractListTagSupport{

	private static final long serialVersionUID = -5082882370055816855L;
	private boolean chosen=true;
	
	@Override
	public int doStartTag() throws JspException {
		
		JspWriter out = this.pageContext.getOut();
		try {
			String html = getSelectHtml();
			out.write(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * 
	 * <p>方法说明：标签模版数据解析<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2017年2月22日下午2:47:57<p>
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	private String getSelectHtml() throws TemplateException, IOException{
		Map<String,Object> data = new HashMap<String, Object>();
		data.putAll(super.getHtmlData());
		data.put("list", super.parseList());
		
		StringBuilder template = new StringBuilder();
		template.append("<select id='${id!}'  name='${name!}' class='${cssClass!}' <#if cssStyle??>style='${cssStyle!}'</#if> <#if dataRules??>data-rules='${dataRules!}'></#if>>");
		template.append("<option value=''>---请选择---</option>");
		template.append("<#list list as item><option value='${item.key!}' <#if defaultValue?? && item.key==defaultValue>selected='selected'</#if>>${item.value!}</option></#list>");
		template.append("</select>");
		if (chosen){
			template.append("<script type='text/javascript'>");
			template.append("	$('#${id}').trigger('chosen');");
			template.append("</script>");
		}
		
		return FormatUtils.toTextStatic(data, template.toString());
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
	
	
}
