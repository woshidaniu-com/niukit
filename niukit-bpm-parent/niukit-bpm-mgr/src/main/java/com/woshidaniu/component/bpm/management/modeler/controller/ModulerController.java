package com.woshidaniu.component.bpm.management.modeler.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.woshidaniu.component.bpm.BPMUtils;
import com.woshidaniu.component.bpm.common.BPMQueryModel;
import com.woshidaniu.component.bpm.management.BaseBPMController;
import com.woshidaniu.component.bpm.management.modeler.ModelDataJsonConstants;
import com.woshidaniu.component.bpm.management.modeler.service.ModelService;
import com.woshidaniu.component.bpm.management.simpelWorkflow.SimpleWorkflow;
import com.woshidaniu.component.bpm.management.simpelWorkflow.SimpleWorkflowJsonConverter;
import com.woshidaniu.component.bpm.service.BPMService;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：com.woshidaniu.component.bpm.management.modeler.ModulerController.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Controller
@RequestMapping("/processManagement/moduler")
public class ModulerController extends BaseBPMController implements ModelDataJsonConstants {

	static Logger log = LoggerFactory.getLogger(ModulerController.class);

	@Autowired
	protected RepositoryService repositoryService;

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected BPMService bpmService;
	
	@RequestMapping("/list.zf")
	public String listModuler() {
		try {
			return "/processManagement/moduler/list";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@RequestMapping("/addModuler.zf")
	public String addModeluer() {
		try {
			return "/processManagement/moduler/add-moduler";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@ResponseBody
	@RequestMapping("/{modelId}/deployModulerData.zf")
	public Object deployModulerData(@PathVariable String modelId) {
		try {
			modelService.deployModuler(modelId);
			return BPMMessageKey.PROCESS_MODEL_DEPLOY_SUCCESS.getJson();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.PROCESS_MODEL_DEPLOY_FAIL.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/getSimpleModelData.zf")
	public Object getSimpleModelJSONData(HttpServletRequest request) {
		String modelId = request.getParameter("modelId");
		try {
			byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode readTree = (ObjectNode) mapper.readTree(modelEditorSource);
			ObjectNode message = mapper.createObjectNode();

			message.put("status", BPMMessageKey.DO_SUCCESS.getStatus());
			message.put("model", readTree);
			return message;
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.PROCESS_MODEL_DATA_QUERY_FAIL.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/{modelId}/saveSimpleModulerData.zf")
	public Object saveSimpleModulerData(@PathVariable String modelId, SimpleWorkflow workflow) {
		if (log.isDebugEnabled()) {
			log.debug("modelId: {}", modelId);
			log.debug("workflow : {} ", workflow);
		}
		try {
			SimpleWorkflowJsonConverter converter = new SimpleWorkflowJsonConverter(workflow);
			converter.convert();
			ObjectNode objectNode = converter.getObjectNode();
			if (log.isDebugEnabled()) {
				log.debug("objectNode: {}", objectNode.toString());
			}
			repositoryService.addModelEditorSource(modelId, objectNode.toString().getBytes("utf-8"));
			return BPMMessageKey.PROCESS_MODEL_SAVE_SUCCESS.getJson();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.PROCESS_MODEL_SAVE_FAIL.getJson();
		}
	}

	@RequestMapping("/uploadModuler.zf")
	public String uploadModuler(HttpServletRequest request) {
		try {
			return "/processManagement/moduler/upload-moduler";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@ResponseBody
	@RequestMapping("/uploadModulerData.zf")
	public Object uploadModulerData(@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request) {
		ByteArrayOutputStream outputStream = null;
		try {
			JSONObject json = new JSONObject();
			String fileName = file.getOriginalFilename();
			if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn")) {
				XMLInputFactory xif = XMLInputFactory.newInstance();
				if (xif.isPropertySupported(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)) {
					xif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
				}
				if (xif.isPropertySupported(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES)) {
					xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
				}
				if (xif.isPropertySupported(XMLInputFactory.SUPPORT_DTD)) {
					xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
				}

				outputStream = new ByteArrayOutputStream();
				InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()),
						"UTF-8");
				XMLStreamReader xtr = xif.createXMLStreamReader(in);
				BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

				if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
					json.put("status", BPMMessageKey.SAVE_FAIL);
					json.put("messsage", "上传文件无法解析，未找到流程信息");
				} else {
					if (bpmnModel.getLocationMap().isEmpty()) {
						json.put("status", BPMMessageKey.SAVE_FAIL);
						json.put("messsage", "上传文件无法解析，未找到BPMN DI元素");
					} else {

						String processName = null;
						if (BPMUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
							processName = bpmnModel.getMainProcess().getName();
						} else {
							processName = bpmnModel.getMainProcess().getId();
						}

						Model modelData = repositoryService.newModel();
						ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
						modelObjectNode.put(MODEL_NAME, processName);
						modelObjectNode.put(MODEL_REVISION, 1);
						modelData.setMetaInfo(modelObjectNode.toString());
						modelData.setName(processName);
						modelData.setCategory("advanced");
						repositoryService.saveModel(modelData);
						BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
						repositoryService.addModelEditorSource(modelData.getId(),
								jsonConverter.convertToJson(bpmnModel).toString().getBytes("utf-8"));
						json.put("status", BPMMessageKey.SAVE_SUCCESS);
						json.put("messsage", "上传成功");
					}
				}
			} else {
				json.put("status", BPMMessageKey.SAVE_FAIL);
				json.put("messsage", "上传文件无法解析，文件格式不正确");
			}
			return json;
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.SAVE_FAIL.getJson();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logException(e);
				}
			}
		}
	}

	@RequestMapping("/{modelId}/copyModuler.zf")
	public String copyModuler(@PathVariable String modelId, HttpServletRequest request) {
		try {
			Model modelData = repositoryService.getModel(modelId);
			request.setAttribute("model", modelData);
			return "/processManagement/moduler/copy-moduler";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@ResponseBody
	@RequestMapping("/{modelId}/copyModulerData.zf")
	public Object copyModulerData(@PathVariable String modelId, HttpServletRequest request) {
		try {
			Model modelData = repositoryService.getModel(modelId);
			String modelName = request.getParameter("modelName");
			String modelDesc = request.getParameter("modelDesc");

			Model newModelData = repositoryService.newModel();
			ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();

			if (BPMUtils.isNotEmpty(modelName)) {
				modelName = modelDesc.replaceAll("\\s+", " ");
			} else {
				modelName = modelData.getName() + "(拷贝)";
			}

			if (BPMUtils.isNotEmpty(modelDesc)) {
				modelDesc = modelDesc.replaceAll("\\s+", " ");
			} else {
				modelDesc = "拷贝自:" + modelData.getName();
			}
			modelObjectNode.put(MODEL_NAME, modelName);
			modelObjectNode.put(MODEL_DESCRIPTION, modelDesc);
			modelObjectNode.put(MODEL_REVISION, 1);

			newModelData.setMetaInfo(modelObjectNode.toString());
			newModelData.setName(modelName);
			newModelData.setCategory(modelData.getCategory());
			newModelData.setKey(BPMUtils.getUniqIDHash());
			repositoryService.saveModel(newModelData);
			repositoryService.addModelEditorSource(newModelData.getId(),
					repositoryService.getModelEditorSource(modelData.getId()));
			repositoryService.addModelEditorSourceExtra(newModelData.getId(),
					repositoryService.getModelEditorSourceExtra(modelData.getId()));

			return BPMMessageKey.PROCESS_MODEL_COPY_SUCCESS.getJson();
		} catch (Exception ex) {
			logException(ex);
			return BPMMessageKey.PROCESS_MODEL_COPY_FAIL.getJson();
		}
	}

	@RequestMapping("/editModuler.zf")
	public String editModuler(HttpServletRequest request) {
		try {
			String modelId = request.getParameter("modelId");
			String editor = request.getParameter("editor");
			request.setAttribute("modelId", modelId);
			if ("simple".equals(editor)) {
				Model model = repositoryService.getModel(modelId);
				request.setAttribute("model", model);
			}
			return "/processManagement/moduler/" + editor + "-moduler-edit";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@RequestMapping("/viewSimpleModuler.zf")
	public String viewSimpleModuler(HttpServletRequest request) {
		try {
			String modelId = request.getParameter("modelId");
			Model model = repositoryService.getModel(modelId);
			if("simple".equals(model.getCategory())){
				request.setAttribute("model", model);
			}else{
				return ERROR_VIEW;
			}
			return "/processManagement/moduler/simple-moduler-view";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}
	
	@ResponseBody
	@RequestMapping("/delModuler.zf")
	public Object delModuler(HttpServletRequest request) {
		String modelIds = request.getParameter("modelIds");
		try {
			String[] modelIdList = modelIds.split(",");
			for (String modelId : modelIdList) {
				repositoryService.deleteModel(modelId);
			}
			return BPMMessageKey.PROCESS_MODEL_DEL_SUCCESS.getJson();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.PROCESS_MODEL_DEL_FAIL.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/addModulerData.zf")
	public Object addModulerData(HttpServletRequest request) {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String editor = request.getParameter("editor");
		try {
			ObjectNode value = modelService.addModuler(name, description, editor);
			ObjectNode json = BPMMessageKey.PROCESS_MODEL_ADD_SUCCESS.getJson();
			json.putAll(value);
			return json;
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.PROCESS_MODEL_ADD_FAIL.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/listData.zf")
	public Object listModulerData(HttpServletRequest request, BPMQueryModel model) {
		try {
			ModelQuery createModelQuery = repositoryService.createModelQuery();
			if (BPMUtils.isNotBlank(request.getParameter("searchModelName"))) {
				createModelQuery.modelNameLike("%" + BPMUtils.trim(request.getParameter("searchModelName")) + "%");
			}
			
			if(BPMUtils.isNotBlank(request.getParameter("searchEditorType"))){
				createModelQuery.modelCategory(BPMUtils.trim(request.getParameter("searchEditorType")));
			}
			
			if(BPMUtils.isNotBlank(request.getParameter("searchDeploymentState"))){
				if(BPMUtils.equals(request.getParameter("searchDeploymentState"), "0")){
					createModelQuery.notDeployed();
				}else{
					createModelQuery.deployed();	
				}
			}
			
			createModelQuery.orderByLastUpdateTime().desc();
			model.setTotalResult((int) createModelQuery.count());
			List<Model> models = createModelQuery.listPage(model.getCurrentResult(), model.getShowCount());
			model.setItems(models);
			return model;
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.SYSTEM_ERROR.getJson();
		}
	}

	@ResponseBody
	@RequestMapping("/queryProcessDefinitionCategoryData")
	public Object queryProcessDefinitionCategoryData(HttpServletRequest request, BPMQueryModel model) {
		try {
			Set<String> processDefinitionCategories = new TreeSet<String>();
			List<ProcessDefinition> processDefinitionList = repositoryService.
					createProcessDefinitionQuery().latestVersion().
					orderByProcessDefinitionCategory().asc().list();
			for (ProcessDefinition processDefinition : processDefinitionList) {
				processDefinitionCategories.add(processDefinition.getCategory());
			}
			return processDefinitionCategories;
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.SYSTEM_ERROR.getJson();
		}
	}
}
