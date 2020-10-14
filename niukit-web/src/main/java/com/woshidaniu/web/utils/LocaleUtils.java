package com.woshidaniu.web.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.woshidaniu.web.context.WebContext;


/**
 * 
 *@类名称	: LocaleUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 11, 2016 4:52:28 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class LocaleUtils extends com.woshidaniu.basicutils.LocaleUtils {

	public static Locale getLocale(){
		// 获取request请求对象
		HttpServletRequest request = (HttpServletRequest) WebContext.getRequest();
		//会话作废前取出,原Locale
        Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if (locale == null) {
			locale = WebContext.getLocale();
		}
		if (locale == null) {
			return new Locale("zh","CN");
		}
		return locale;
	}
	
	public static String getLocaleKey(){
		//会话作废前取出,原Locale
        Locale locale = LocaleUtils.getLocale();
		return locale.toString();
	}
	
	public static String getLocalePath(String filepath){
		return LocaleUtils.getLocalePath(LocaleUtils.getLocaleKey(), filepath);
	}
	
}
