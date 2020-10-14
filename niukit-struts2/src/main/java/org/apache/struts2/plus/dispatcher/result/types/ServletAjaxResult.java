package org.apache.struts2.plus.dispatcher.result.types;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @className: FileStreamResult
 * @description: Ajax请求处理，输出 text,html,json,xml
 * <pre>
 * 直接输出纯TEXT: 	&lt;param name="contentType"&gt;text/plain;charset=UTF-8&lt;/param&gt;
 * 直接输出纯HTML:	&lt;param name="contentType"&gt;text/html;charset=UTF-8&lt;/param&gt;
 * 直接输出纯XML:	&lt;param name="contentType"&gt;text/xml;charset=UTF-8&lt;/param&gt;
 * 直接输出纯JSON:	&lt;param name="contentType"&gt;text/x-json;charset=UTF-8&lt;/param&gt;
 * </pre>
 * <b>Example:</b>
 * <pre>
 * <!-- START SNIPPET: example -->
 * &lt;result name="success" type="file"&gt;
 *   &lt;param name="inputName"&gt;fileStream&lt;/param&gt;
 *   &lt;param name="contentType"&gt;text/html&lt;/param&gt;
 *   &lt;param name="contentCharSet"&gt;UTF-8&lt;/param&gt;
 * &lt;/result&gt;
 * 
 * <!-- END SNIPPET: example -->
	
 * </pre>
 * @author : kangzhidong
 */
@SuppressWarnings("serial")
public class ServletAjaxResult extends StrutsResultSupport {
	
	private final static Logger LOG = LoggerFactory.getLogger(ServletAjaxResult.class);

	protected String contentType = "text/html;charset=UTF-8";
	protected String contentName = "";
	protected String contentCharSet = "UTF-8";

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	
	public String getContentCharSet() {
		return contentCharSet;
	}

	public void setContentCharSet(String contentCharSet) {
		this.contentCharSet = contentCharSet;
	}

	protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
		// Override any parameters using values on the stack
		resolveParamsFromStack(invocation.getStack(), invocation);
				
		// Find the Response in context
		HttpServletResponse response = ServletActionContext.getResponse();
		
		// Set the content type
		if (contentCharSet != null && !contentCharSet.equals("")) {
			response.setContentType(conditionalParse(contentType,invocation) + ";charset=" + contentCharSet);
		} else {
			response.setContentType(conditionalParse(contentType,invocation));
		}
		
		// Copy input to output
		LOG.debug("Streaming to output +++ START +++");
		String responseText = (String) invocation.getStack().findValue(contentName);
		response.getWriter().write(responseText);
		LOG.debug("Streaming to output +++ END +++");
	}
	
	/**
	 * Tries to lookup the parameters on the stack. Will override any existing
	 * parameters
	 * 
	 * @param stack  The current value stack
	 */
	protected void resolveParamsFromStack(ValueStack stack,ActionInvocation invocation) {

		String contentType = stack.findString("contentType");
		if (contentType != null) {
			setContentType(contentType);
		}
		
		String contentName = stack.findString("contentName");
		if (contentName != null) {
			setContentName(contentName);
		}
		
		if (contentCharSet != null) {
			contentCharSet = conditionalParse(contentCharSet, invocation);
		} else {
			contentCharSet = stack.findString("contentCharSet");
		}
	}
}
