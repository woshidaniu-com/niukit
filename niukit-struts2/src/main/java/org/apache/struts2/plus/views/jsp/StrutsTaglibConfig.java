/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package org.apache.struts2.plus.views.jsp;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

/**
 *@类名称	: 	StrutsTaglibConfig.java
 *@类描述	：	默认的struts标签库参数实现，获取容器上下文属性和请求参数作为标签库参数Map对象
 *@创建人	：	kangzhidong
 *@创建时间	：2016年4月14日 上午11:26:08
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class StrutsTaglibConfig implements TaglibConfig {

	@Override
	public Map<String, Object> getParameterMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//返回ServletContext 对象
		ServletContext context = ServletActionContext.getServletContext();
		//迭代请求的参数，将参数传递下去
		Enumeration<String> emus = context.getAttributeNames();
		while (emus.hasMoreElements()) {
			String name = (String) emus.nextElement();
			resultMap.put(name, context.getAttribute(name));
		}
		
		//返回Request对象
		HttpServletRequest request = ServletActionContext.getRequest();
		//迭代请求的参数，将参数传递下去
		Enumeration<String> emus2 = request.getParameterNames();
		while (emus2.hasMoreElements()) {
			String name = (String) emus2.nextElement();
			resultMap.put(name, request.getParameter(name));
		}
		
		return resultMap;
	}

}
