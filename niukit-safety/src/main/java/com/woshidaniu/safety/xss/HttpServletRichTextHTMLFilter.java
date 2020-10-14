/**
 * 
 */
package com.woshidaniu.safety.xss;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：富文本字符过滤器,例如文本编辑器提交的数据，需要做严格过滤
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月4日上午11:50:01
 */
public class HttpServletRichTextHTMLFilter extends AbstractPathMatchFilter {
	
	protected static final Logger log = LoggerFactory.getLogger(HttpServletRichTextHTMLFilter.class);
	/**
	 * HTML扫描工具
	 */
	protected AntiSamy antiSamy;
	
	//antisamy bug============
	//会把“&nbsp;”转换成乱码，把双引号转换成"&quot;"
	protected String $nbsp;
	//antisamy bug============
	
	/**
	 * 
	 * <p>方法说明：初始化参数<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月4日下午4:16:39<p>
	 */
	public void init(){
		if(this.antiSamy != null){
			try {
				$nbsp = antiSamy.scan("&nbsp").getCleanHTML();
			} catch (Exception e) {
				log.error("", e);
				e.printStackTrace();
			} 
		}
	}
	
	@Override
	protected boolean onPreHandle(ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		doCleanParameterMap(httpRequest);
		return true;
	}
	
	
	public AntiSamy getAntiSamy() {
		return antiSamy;
	}

	public void setAntiSamy(AntiSamy antiSamy) {
		this.antiSamy = antiSamy;
	}

	protected void doCleanParameterMap(HttpServletRequest httpRequest) {
		Map<String, String[]> request_map = httpRequest.getParameterMap();
		Iterator<Entry<String, String[]>> iterator = request_map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String[]> me = iterator.next();
			String[] values = me.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = cleanParam(values[i]);
			}
		}
	}

	protected String cleanParam(String taintedHTML) {
		if(antiSamy != null){
			try {
				CleanResults cr = antiSamy.scan(taintedHTML);
				String cleanHTML = cr.getCleanHTML();
				//处理一些特殊异常Bug
				String escapeHtml = StringEscapeUtils.unescapeHtml4(cleanHTML);
	            String replaceAll = escapeHtml.replaceAll($nbsp, "&nbsp;");
	           //处理一些特殊异常Bug
				if(log.isDebugEnabled()){
					log.debug("处理前：{}, 处理后：{}", new Object[]{taintedHTML, replaceAll});
				}
				return replaceAll;
			} catch (ScanException e) {
				e.printStackTrace();
			} catch (PolicyException e) {
				e.printStackTrace();
			}
		}
		
		return taintedHTML;
	}


}
