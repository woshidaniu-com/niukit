package com.woshidaniu.component.bpm.management.process.instance.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woshidaniu.component.bpm.management.process.instance.dao.IProcessInstanceDao;

@Service
/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.management.process.instance.service.InstanceService.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class PorcessInstanceService {

	@Autowired
	protected IProcessInstanceDao dao;
	@Autowired
	protected ProcessEngineConfiguration processEnginConfiguration;
	
	/**
	 * 
	 * <p>方法说明：获取跟踪流程信息<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月12日下午4:37:58<p>
	 */
	public List<Map<String,String>> traceProcessInstance(String processInstanceId){
		return dao.getProcessInstanceComments(processInstanceId);
	}
	
}
