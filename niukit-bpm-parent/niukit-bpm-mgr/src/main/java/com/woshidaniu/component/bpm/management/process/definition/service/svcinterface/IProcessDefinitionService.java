package com.woshidaniu.component.bpm.management.process.definition.service.svcinterface;

import java.util.List;

import com.woshidaniu.component.bpm.common.BaseService;
import com.woshidaniu.component.bpm.management.process.definition.dao.entities.ProcessDefinitionModel;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程定义管理Service
 *	 <br>class：com.woshidaniu.component.bpm.processDefMgr.service.svcinterface.IProcessDefMgrService.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface IProcessDefinitionService extends BaseService<ProcessDefinitionModel>{

	
	/**
	 * 
	 * <p>方法说明：获取流程定义类别<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年11月15日下午4:35:54<p>
	 */
	List<String> findProcessDefinitionCategoryList();
	
	/**
	 * 
	 * <p>方法说明：创建并部署流程<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年11月29日下午5:15:09<p>
	 */
	boolean createAndDeployProcessDefinition(ProcessDefinitionModel processDefinationModel);
}
