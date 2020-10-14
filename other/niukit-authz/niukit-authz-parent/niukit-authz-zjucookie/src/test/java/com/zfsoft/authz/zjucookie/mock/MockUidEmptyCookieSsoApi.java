/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.mock;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.authz.zjucookie.utils.CookieSsoApi;

public class MockUidEmptyCookieSsoApi implements CookieSsoApi{
	
	private String mockUserName;
	private String mockUid;
	private String mockDeptNo;
	
	private String usernameParam;
	private String password;

	@Override
	public boolean logout(String token) throws ServletException, IOException {
		return false;
	}

	@Override
	public String login(String username, String password, HttpServletResponse response) {
		if(this.usernameParam.equals(username) && this.password.equals(password)) {
			return "no_empty_token";			
		}else {
			return null;
		}
	}

	@Override
	public String getUidByTokenInCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return null;
	}

	@Override
	public String getUidByToken(String token) throws IOException {
		if("no_empty_token".equals(token)) {
			return this.mockUid;
		}else {
			return null;			
		}
	}

	@Override
	public String getNameByUid(String uid) {
		if(this.mockUid.equals(uid)) {
			return this.mockUserName;			
		}else {
			return null;
		}
	}

	@Override
	public String getDepNoByUid(String uid) {
		if(this.mockUid.equals(uid)) {
			return this.mockDeptNo;			
		}else {
			return null;
		}
	}

	public void setMockUserName(String mockUserName) {
		this.mockUserName = mockUserName;
	}

	public void setMockUid(String mockUid) {
		this.mockUid = mockUid;
	}

	public void setMockDeptNo(String mockDeptNo) {
		this.mockDeptNo = mockDeptNo;
	}

	public void setUsernameParam(String usernameParam) {
		this.usernameParam = usernameParam;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
