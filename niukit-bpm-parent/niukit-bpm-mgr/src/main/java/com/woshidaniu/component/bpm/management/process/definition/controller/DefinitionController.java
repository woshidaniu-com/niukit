package com.woshidaniu.component.bpm.management.process.definition.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.woshidaniu.component.bpm.common.BPMQueryModel;
import com.woshidaniu.component.bpm.management.BaseBPMController;
import com.woshidaniu.component.bpm.management.process.definition.dao.entities.ProcessDefinitionModel;
import com.woshidaniu.component.bpm.management.process.definition.service.svcinterface.IProcessDefinitionService;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：流程管理Controller <br>
 * class：com.woshidaniu.component.bpm.processDefMgr.controller.ProcessDefMgrController.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Controller
@RequestMapping("/processDefinition")
public class DefinitionController extends BaseBPMController {

	@Autowired
	protected IProcessDefinitionService processDefinitionService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected RepositoryService repositoryService;

	protected List<String> findCategoryList() {
		return processDefinitionService.findProcessDefinitionCategoryList();
	}

	@RequestMapping("/list.zf")
	public String list(HttpServletRequest request, ProcessDefinitionModel model) {
		try {
			request.setAttribute("categoryList", findCategoryList());
			return "/processManagement/definition/list";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@ResponseBody
	@RequestMapping("/listData.zf")
	public Object listData(ProcessDefinitionModel model) {
		try {
//			ProcessDefinitionQuery createProcessDefinitionQuery = repositoryService.createProcessDefinitionQuery();
//			if(BPMUtils.isNoneBlank(request.getParameter("name"))){
//				createProcessDefinitionQuery.processDefinitionNameLike("%" + request.getParameter("name") + "%");
//			}
//			if(BPMUtils.isNoneBlank(request.getParameter("category"))){
//				createProcessDefinitionQuery.processDefinitionCategory(request.getParameter("name"));
//			}
//			if(BPMUtils.isNoneBlank(request.getParameter("state"))){
//				if(BPMUtils.equals("1", request.getParameter("state"))){
//					createProcessDefinitionQuery.active();
//				}
//				if(BPMUtils.equals("2", request.getParameter("state"))){
//					createProcessDefinitionQuery.suspended();
//				}
//			}
//			createProcessDefinitionQuery.orderByProcessDefinitionName().asc();
//			List<ProcessDefinition> models = createProcessDefinitionQuery.listPage(model.getCurrentResult(), model.getShowCount());
//			model.setTotalResult((int) createProcessDefinitionQuery.count());
//			model.setItems(models);
//			return model;
			
			BPMQueryModel queryModel = model.getQueryModel();
			List<ProcessDefinitionModel> pagedList = processDefinitionService.getPagedList(model);
			queryModel.setItems(pagedList);
			return queryModel;
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.SYSTEM_ERROR.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/active.zf")
	public Object active(HttpServletRequest request) {
		try {
			String processDefinitionId = request.getParameter("processDefinitionId");
			ProcessDefinition processDefinition = repositoryService.
						createProcessDefinitionQuery().processDefinitionId(processDefinitionId).
						singleResult();
			if(processDefinition != null && processDefinition.isSuspended()){
				repositoryService.activateProcessDefinitionById(processDefinitionId);
			}
			return BPMMessageKey.PROCESS_DEFINITION_ACTIVE_SUCCESS.getJson();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.PROCESS_DEFINITION_ACTIVE_FAIL.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/suspend.zf")
	public Object suspend(HttpServletRequest request) {
		try {
			String processDefinitionId = request.getParameter("processDefinitionId");
			
			ProcessDefinition processDefinition = repositoryService.
					createProcessDefinitionQuery().processDefinitionId(processDefinitionId).
					singleResult();
			if(processDefinition != null && (!processDefinition.isSuspended())){
				repositoryService.suspendProcessDefinitionById(processDefinitionId);
			}
			return BPMMessageKey.PROCESS_DEFINITION_SUSPEND_SUCCESS.getJson();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.PROCESS_DEFINITION_SUSPEND_FAIL.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/del.zf")
	public Object del(HttpServletRequest request) {
		try {
			String processDefinitionId = request.getParameter("processDefinitionId");
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId).singleResult();
			if(processDefinition != null){
				long count = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).count();
				if (count > 0) {
					return BPMMessageKey.PROCESS_DEFINITION_DEL_FAIL.getJson();
				} else {
					//delete process definition
					repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
					return BPMMessageKey.PROCESS_DEFINITION_DEL_SUCCESS.getJson();
				}
			}
			return BPMMessageKey.PROCESS_DEFINITION_NOT_FOUND.getJson();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.SYSTEM_ERROR.getJson();
		}
	}

	@RequestMapping("/listVersions.zf")
	public String listVersions(HttpServletRequest request) {
		try {
			String processDefinitionKey = request.getParameter("processDefinitionKey");
			List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey(processDefinitionKey).orderByProcessDefinitionVersion().desc().list();
			
			request.setAttribute("processDefinitionList", processDefinitionList);
			return "/processManagement/definition/list_versions";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}
	
}
