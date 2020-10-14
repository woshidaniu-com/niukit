package com.woshidaniu.component.bpm.management.process.instance.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.management.process.instance.dao.IProcessInstanceDao.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Repository
public interface IProcessInstanceDao {
	List<Map<String,String>> getProcessInstanceComments(@Param("processInstanceId")String processInstanceId);
}
