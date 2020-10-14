/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idstar.utils;


import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wiscom.is.IdentityFactory;
import com.wiscom.is.IdentityManager;

/**
 * 
 * @className	： IDstarSsoUtils
 * @description	： 金智单点工具
 * @author 		：大康（743）
 * @date		： 2017年8月24日 下午1:59:47
 * @version 	V1.0
 */
public class IDstarSsoUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(IDstarSsoUtils.class);
	protected static String TOKEN_KEY = "iPlanetDirectoryPro";
	
	public static String getTokenInCookie(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String token = null;
		Cookie[] all_cookies = request.getCookies();
		if (all_cookies == null) {
			return null;
		}
		for (int i = 0; i < all_cookies.length; i++) {
			if (TOKEN_KEY.equals(all_cookies[i].getName())) {
				token = URLDecoder.decode(all_cookies[i].getValue(),"GB2312");
				break;
			}
		}
		if ((token == null) || ("".equals(token.trim()))) {
			return null;
		}
		return token;
	}

	public static String getUidByToken(String is_config, String token) {
		String curUser = "";
		if ((token == null) || ("".equals(token.trim()))) {
			return null;
		}
		try {
			
			IdentityFactory factory = IdentityFactory.createFactory( is_config );
			IdentityManager im = factory.getIdentityManager();
			
			if (token != null ) {
		    	curUser = im.getCurrentUser( token );
		    }
			LOG.info("Get curUser By Token Success !");
			LOG.info("curUser:" + curUser);
		} catch (Exception e) {
			LOG.error("Get Uid By Token Error ." , e);
		}
		return curUser;
	}
	
}
