/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 
 * @className	： URLUtils
 * @description	：URL工具类
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:57:18
 * @version 	V1.0
 */
public class URLUtils {

	public static String escape(String name, String encode) {
		String ret = "";

		try {
			ret = URLEncoder.encode(name, encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return ret;
	}

}
