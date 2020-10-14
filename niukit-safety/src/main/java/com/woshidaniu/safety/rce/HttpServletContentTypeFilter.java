/**
 * 
 */
package com.woshidaniu.safety.rce;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.servlet.filter.AbstractPathMatchFilter;

/**
 * 
 <pre>
       漏洞详情

　　漏洞编号：S2-045

　　CVE编号：CVE-2017-5638

　　漏洞类型：远程代码执行

　　漏洞级别：高危

　　漏洞风险：黑客通过利用漏洞可以实现远程命令执行。

　　影响版本：struts2.3.5– struts2.3.31 , struts2.5–struts2.5.10。

　　漏洞描述：攻击者可在上传文件时通过修改HTTP请求头中的Content-Type值来触发该漏洞，进而执行系统命令

　　漏洞分析

　　漏洞存在的文件

　　1. \core\src\main\java\org\apache\struts2\dispatcher\multipart\MultiPartRequestWrapper.java

　　2. \core\src\main\java\org\apache\struts2\dispatcher\multipart\JakartaMultiPartRequest.java

　　3. \core\src\main\java\org\apache\struts2\dispatcher\multipart\JakartaStreamMultiPartRequest.java

　　修复方法

　　if (LocalizedTextUtil.findText(this.getClass(), errorKey, defaultLocale, null, new Object[0]) == null) {

　　return LocalizedTextUtil.findText(this.getClass(), "struts.messages.error.uploading", defaultLocale, null, new

　　Object[] { e.getMessage() });

　　} else {

　　return LocalizedTextUtil.findText(this.getClass(), errorKey, defaultLocale, null, args);

　　}

　　漏洞利用条件

　　1. 基于Jakarta(Jakarta Multipart parser)插件的文件上传功能

　　2. 恶意攻击者构造Content-Type的值

　　修复建议

　　1. 修复Jakarta文件上传插件或者是存在漏洞的Struts 2版本请升级至Struts2安全版本

　　2. 通过Servlet过滤器验证Content-Type值

　　3. 添加waf规则进行拦截恶意攻击

　　参考文档

　　https://cwiki.apache.org/confluence/display/WW/S2-045

　　https://github.com/apache/struts
 </pre>
 */
public class HttpServletContentTypeFilter extends AbstractPathMatchFilter{
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpServletContentTypeFilter.class);
	protected String www_url_encode= "application/x-www-form-urlencoded";
	protected String mul_data= "multipart/form-data ";
	protected String txt_pla= "text/plain";
	
	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response) throws Exception {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		String contenType = httpRequest.getHeader("conTent-type");
		
		if(!StringUtils.isNull(contenType) 
				&& !contenType.equalsIgnoreCase(www_url_encode)
				&& !contenType.equalsIgnoreCase(mul_data)
				&& !contenType.equalsIgnoreCase(txt_pla)){
			
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("非法请求Content-Type！");
			
			if(LOG.isDebugEnabled()){
				LOG.debug("Filter:{}  Illegal Content-Type{}.",  new Object[]{getName(),contenType});
			}
			return false;
		}
		
		return true;
	}
	 
	
}
