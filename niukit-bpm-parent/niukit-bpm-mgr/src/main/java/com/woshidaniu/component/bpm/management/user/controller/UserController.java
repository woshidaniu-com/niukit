package com.woshidaniu.component.bpm.management.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.woshidaniu.component.bpm.BPMUtils;
import com.woshidaniu.component.bpm.common.BPMQueryModel;
import com.woshidaniu.component.bpm.management.BaseBPMController;

@Controller
@RequestMapping("/processUser")
public class UserController extends BaseBPMController{
	
	@Autowired
	protected IdentityService identityServive;
	
	
	@RequestMapping("/list.zf")
	public String listUsers(){
		try {
	        return "/processManagement/processUser/list";
		} catch (Exception e) {
			logException(e);
			return ERROR_VIEW;
		}
	}

	@ResponseBody
	@RequestMapping("/listData.zf")
	public Object listUsersData(HttpServletRequest request, BPMQueryModel model){
		try{
			UserQuery createUserQuery = identityServive.createUserQuery();
			if(BPMUtils.isNotBlank(request.getParameter("search"))){
				createUserQuery.userFirstNameLike("%" + request.getParameter("search") +"%");
			}
			createUserQuery.orderByUserFirstName().asc();
			model.setTotalResult((int) createUserQuery.count());
			List<User> listPage = createUserQuery.listPage(model.getCurrentResult(), model.getShowCount());
			model.setItems(listPage);
			return model;
		} catch(Exception e){
			logException(e);
			return BPMMessageKey.SYSTEM_ERROR.getJson();
		}
	}
	
	
	
}
