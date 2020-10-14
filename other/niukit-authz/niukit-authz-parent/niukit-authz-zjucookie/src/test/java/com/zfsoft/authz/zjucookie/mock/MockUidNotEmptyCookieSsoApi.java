/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zjucookie.mock;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.zjucookie.utils.CookieSsoApi;

/**
 * 
 * @className	： MockCookieSsoApi
 * @description	： 模拟CookieSsoApi，用于测试登录，注销，等等
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午11:27:08
 * @version 	V1.0
 */
public class MockUidNotEmptyCookieSsoApi implements CookieSsoApi{

	private static Logger log = LoggerFactory.getLogger(MockUidNotEmptyCookieSsoApi.class);
	
	private String mockUserName;
	private String mockUid;
	private String mockDeptNo;
	
	@Override
	public boolean logout(String token)
			throws ServletException, IOException {
	
		log.info("logout success");
		return true;
	}

	@Override
	public String login(String username, String password, HttpServletResponse response) {
		return null;
	}

	@Override
	public String getUidByTokenInCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return this.mockUid;
	}

	@Override
	public String getUidByToken(String token) throws IOException {
		return this.mockUid;
	}

	@Override
	public String getNameByUid(String uid) {
		return this.mockUserName;
	}

	@Override
	public String getDepNoByUid(String uid) {
		return this.mockDeptNo;
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

}
