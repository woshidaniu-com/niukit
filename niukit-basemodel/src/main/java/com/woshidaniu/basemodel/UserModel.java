package com.woshidaniu.basemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;


import org.apache.commons.lang.StringUtils;

/**
 * 
 *@类名称	: UserModel.java
 *@类描述	： 用户信息对象顶层模型对象；建议所有的业务系统中的User对象集成此对象，以便进行通用业务逻辑实现:
 * <pre>
 * 1、实现了HttpSessionBindingListener接口的JavaBean对象可以感知自己被绑定到Session中和 Session中删除的事件
　*　	   当对象被绑定到HttpSession对象中时，web服务器调用该对象的void valueBound(HttpSessionBindingEvent event)方法
　*　	   当对象从HttpSession对象中解除绑定时，web服务器调用该对象的void valueUnbound(HttpSessionBindingEvent event)方法
 *
 * 2、实现了HttpSessionActivationListener接口的JavaBean对象可以感知自己被活化(反序列化)和钝化(序列化)的事件
　*　     当绑定到HttpSession对象中的javabean对象将要随HttpSession对象被钝化(序列化)之前，web服务器调用该javabean对象的void sessionWillPassivate(HttpSessionEvent event) 方法。
 *	   这样javabean对象就可以知道自己将要和HttpSession对象一起被序列化(钝化)到硬盘中.
　*　     当绑定到HttpSession对象中的javabean对象将要随HttpSession对象被活化(反序列化)之后，web服务器调用该javabean对象的void sessionDidActive(HttpSessionEvent event)方法。
 *	  这样javabean对象就可以知道自己将要和 HttpSession对象一起被反序列化(活化)回到内存中
 * </pre>
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:32:01 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings( { "serial" })
public class UserModel implements Cloneable, Serializable, HttpSessionBindingListener, HttpSessionActivationListener {

	/**
	 * 用户ID（用户来源表Id）
	 */
	protected String userID;
	/**
	 * 用户Key
	 */
	protected String userKey;
	/**
	 * 用户名称
	 */
	protected String userName;
	/**
	 * 用户密码
	 */
	protected String userPassword;
	/**
	 * 用户类型;1:超级管理员，2：普通管理员，等（根据系统业务自定义）
	 */
	private String usertype;
	/**
	 * 用户类型名称
	 */
	private String usertypeName;
	/**
	 * 是否启用
	 */
	private boolean usable;
	/**
	 * 用户状态：0：禁用，1：可用，2：锁定
	 */
	private int status;
	/**
	 * 用户当前登录角色信息
	 */
	private PairModel currentRole;
	/**
	 * 拥有角色数
	 */
	private int roleCount;
	/**
	 * 用户拥有角色信息
	 */
	private List<PairModel> roleList;
	/**
	 * 角色功能代码集合 以,连接的字符
	 */
	private String func_codes; 
	/**
	 * 固定联系电话
	 */
	private String telephone;
	/**
	 * 移动电话
	 */
	private String mobile;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 腾讯QQ
	 */
	private String qq;
	/**
	 * 个人主页地址
	 */
	private String homepage;

	public String getUserID() {

		return userID;
	}

	public void setUserID(String userID) {

		this.userID = userID;
	}

	public String getUserKey() {

		return userKey;
	}

	public void setUserKey(String userKey) {

		this.userKey = userKey;
	}

	public String getUserName() {

		return userName;
	}

	public void setUserName(String userName) {

		this.userName = userName;
	}

	public String getUserPassword() {

		return userPassword;
	}

	public void setUserPassword(String userPassword) {

		this.userPassword = userPassword;
	}

	public String getUsertype() {

		return usertype;
	}

	public void setUsertype(String usertype) {

		this.usertype = usertype;
	}

	public String getUsertypeName() {

		return usertypeName;
	}

	public void setUsertypeName(String usertypeName) {

		this.usertypeName = usertypeName;
	}

	public boolean isUsable() {

		return usable;
	}

	public void setUsable(boolean usable) {

		this.usable = usable;
	}

	public int getStatus() {

		return status;
	}

	public void setStatus(int status) {

		this.status = status;
	}

	public PairModel getCurrentRole() {

		return currentRole;
	}

	public void setCurrentRole(PairModel currentRole) {

		this.currentRole = currentRole;
	}

	public int getRoleCount() {

		return roleCount;
	}

	public void setRoleCount(int roleCount) {

		this.roleCount = roleCount;
	}

	public List<PairModel> getRoleList() {

		return roleList;
	}

	public void setRoleList(List<PairModel> roleList) {

		this.roleList = roleList;
	}
	
	public String getRoleKeys() {
		List<String> roleKeys = new ArrayList<String>();
		if(getRoleList() != null){
			for (PairModel  role: getRoleList()) {
				roleKeys.add(role.getKey());
			}
		}
		return StringUtils.join(roleKeys,",");
	}
	
	public String getRoleValues() {
		List<String> roleValues = new ArrayList<String>();
		if(getRoleList() != null){
			for (PairModel  role: getRoleList()) {
				roleValues.add(role.getValue());
			}
		}
		return StringUtils.join(roleValues,",");
	}
	
	public boolean isMonitor() {
		return this.getCurrentRole() != null ? "admin".equals(this.getCurrentRole().getKey()) : false;
	}

	public String getFunc_codes() {
		return func_codes;
	}

	public void setFunc_codes(String funcCodes) {
		func_codes = funcCodes;
	}
	
	public String getTelephone() {

		return telephone;
	}

	public void setTelephone(String telephone) {

		this.telephone = telephone;
	}

	public String getMobile() {

		return mobile;
	}

	public void setMobile(String mobile) {

		this.mobile = mobile;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getQq() {

		return qq;
	}

	public void setQq(String qq) {

		this.qq = qq;
	}

	public String getHomepage() {

		return homepage;
	}

	public void setHomepage(String homepage) {

		this.homepage = homepage;
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Feb 2, 2016 3:30:56 PM
	 * @param event
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		System.out.println(userName+"被加到session中了");
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Feb 2, 2016 3:30:56 PM
	 * @param event
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		 System.out.println(userName+"被session踢出来了");
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Feb 2, 2016 3:30:56 PM
	 * @param se
	 */
	@Override
	public void sessionDidActivate(HttpSessionEvent event) {
		System.out.println(userName+"和session一起从硬盘反序列化(活化)回到内存了，session的id是："+event.getSession().getId());
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Feb 2, 2016 3:30:56 PM
	 * @param se
	 */
	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		System.out.println(userName+"和session一起被序列化(钝化)到硬盘了，session的id是："+se.getSession().getId());
	}

}
