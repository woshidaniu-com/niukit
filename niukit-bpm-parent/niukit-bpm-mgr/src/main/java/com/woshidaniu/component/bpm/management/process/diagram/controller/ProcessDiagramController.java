package com.woshidaniu.component.bpm.management.process.diagram.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.woshidaniu.component.bpm.management.BaseBPMController;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程图查看controller
 *	 <br>class：com.woshidaniu.component.bpm.management.process.diagram.controller.ProcessDiagramController.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Controller
@RequestMapping("/diagram-viewer")
public class ProcessDiagramController extends BaseBPMController {

	@RequestMapping("/index.zf")
	public String index(HttpServletRequest request) {
		try {
			request.setAttribute("processDefinitionId", request.getParameter("processDefinitionId"));
			request.setAttribute("processInstanceId", request.getParameter("processInstanceId"));
			request.setAttribute("processDefinitionKey", request.getParameter("processDefinitionKey"));
	        return "processManagement/diagram/view";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}
	
}
