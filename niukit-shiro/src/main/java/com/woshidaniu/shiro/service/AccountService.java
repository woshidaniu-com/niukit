/**
 * 
 */
package com.woshidaniu.shiro.service;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;

import com.woshidaniu.shiro.authc.DelegateAuthenticationInfo;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 
 * 系统认证服务接口
 *
 */
public interface AccountService {

	/**
	 * @desc 获取用户信息
	 * @param token
	 * @return
	 */
	DelegateAuthenticationInfo getAuthenticationInfo(DelegateAuthenticationToken token) throws AuthenticationException;
	
	/**
	 * @desc 获取用户权限列表
	 * @param principal
	 * @return
	 */
	Set<String> getPermissionsInfo(Object principal) throws AuthenticationException;
	
	/**
	 * @desc 获取用户权限列表【多realms认证的情况下使用】
	 * @param principal
	 * @return
	 */
	Set<String> getPermissionsInfo(Set<Object> principals) throws AuthenticationException;
	
	/**
	 * @desc 获取用户角色列表
	 * @param principal
	 * @return
	 */
	Set<String> getRolesInfo(Object principal) throws AuthenticationException;
	
	/**
	 * @desc 获取用户角色列表【多realms认证的情况下使用】
	 * @param principal
	 * @return
	 */
	Set<String> getRolesInfo(Set<Object> principals) throws AuthenticationException;
}
