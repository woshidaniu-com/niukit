package com.woshidaniu.component.bpm.management.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.woshidaniu.component.bpm.BPMUtils;
import com.woshidaniu.component.bpm.common.BPMQueryModel;
import com.woshidaniu.component.bpm.management.BaseBPMController;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.management.user.controller.GroupController.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Controller
@RequestMapping("/processGroup")
public class GroupController extends BaseBPMController {
	@Autowired
	protected IdentityService identityServive;
	
	
	@RequestMapping("/list.zf")
	public String listUsers(){
		try {
	        return "/processManagement/processGroup/list";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@ResponseBody
	@RequestMapping("/listData.zf")
	public Object listUsersData(HttpServletRequest request, BPMQueryModel model){
		try{
			GroupQuery groupQuery = identityServive.createGroupQuery();
			if(BPMUtils.isNotBlank(request.getParameter("search"))){
				groupQuery.groupNameLike("%" + request.getParameter("search") + "%");
			}
			groupQuery.orderByGroupName().asc();
			model.setTotalResult((int) groupQuery.count());
			List<Group> listPage = groupQuery.listPage(model.getCurrentResult(), model.getShowCount());
			model.setItems(listPage);
			return model;
		} catch(Exception e){
			logException(e);
			return BPMMessageKey.SYSTEM_ERROR.getJson();
		}
	}
}
