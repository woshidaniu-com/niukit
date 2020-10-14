package com.woshidaniu.component.bpm.management.process.definition.dao.daointerface;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.woshidaniu.component.bpm.common.BaseDao;
import com.woshidaniu.component.bpm.management.process.definition.dao.entities.ProcessDefinitionModel;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程定义管理DAO
 *	 <br>class：com.woshidaniu.component.bpm.management.process.defination.dao.daointerface.IProcessDefinationDao.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Repository("processDefinationDao")
public interface IProcessDefinitionDao extends BaseDao<ProcessDefinitionModel> {

	List<String> getCategoryList();
	
}
