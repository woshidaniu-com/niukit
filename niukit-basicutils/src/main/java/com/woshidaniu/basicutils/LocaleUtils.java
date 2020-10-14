package com.woshidaniu.basicutils;

import java.io.File;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
 
/**
 * 
 *@类名称	: LocaleUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:35:38 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class LocaleUtils extends org.apache.commons.lang3.LocaleUtils {

	public final static String SESSION_KEY = "WW_TRANS_I18N_LOCALE";
	public final static String DEFAULT_LANGUAGE = "zh_CN";
	public final static Locale DEFAULT_LOCALE = new Locale("zh","CN");
	public final static String STATCK_KEY = "language";
	 
	public static Locale getLocale(HttpServletRequest request){
		//会话作废前取出,原Locale
        Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if (locale == null) {
			return DEFAULT_LOCALE;
		}
		return locale;
	}
	
	public static String getLocaleKey(HttpServletRequest request){
		//会话作废前取出,原Locale
        Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if (locale == null) {
			locale = LocaleUtils.getRequestLocale(request);
		}
		if (locale == null) {
			return DEFAULT_LANGUAGE;
		}
		return locale.getLanguage() + "_" + locale.getCountry();
	}
	
	public static String getLocalePath(HttpServletRequest request,String filepath){
		return LocaleUtils.getLocalePath(LocaleUtils.getLocaleKey(request), filepath);
	}
	
	public static String getLocalePath(String locale,String filepath){
		String finalpath = filepath;
		if(locale != null ){
			String fullPath = FilenameUtils.getFullPath(filepath);
			String baseName = FilenameUtils.getBaseName(filepath);
			String extension = FilenameUtils.getExtension(filepath);
			finalpath = fullPath + baseName + "_" + locale + "." + extension;
		}
		File file = new File(finalpath);
		if(!file.exists()){
			finalpath = filepath;
		}
		return finalpath;
	}
	
	public static Locale getRequestLocale(HttpServletRequest request){
		String language = request.getParameter("language");
		if (StringUtils.isEmpty(language)) {
			language = DEFAULT_LANGUAGE;
		}
		String loc[] = language.split("_");
		return new Locale(loc[0], loc[1]);
	}
	
	public static Locale getSessionLocale(HttpSession session){
		//会话作废前取出,原Locale
        return (Locale)session.getAttribute(SESSION_KEY);
	}
	
	public static Locale interceptLocale(HttpServletRequest request){
		HttpSession session = request.getSession();
		Locale locale = LocaleUtils.getSessionLocale(session);
		if(locale == null){
			locale = LocaleUtils.getRequestLocale(request);
		}
		LocaleUtils.setSessionLocale(session, locale);
		return locale;
	}
	
	public static Locale interceptLocaleWithSessionInvalid(HttpServletRequest request){
		//作废前的Session
		HttpSession session = request.getSession();
		//取出Locale
		Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if(locale == null){
			locale = LocaleUtils.getRequestLocale(request);
		}
		//作废Session
		session.invalidate();
		//设置原Locale到新的Session
		LocaleUtils.setSessionLocale(request.getSession(), locale);
		return locale;
	}
	
	public static void setSessionLocale(HttpSession session) {
		// 会话作废前取出,原Locale
		Locale locale = (Locale) session.getAttribute(SESSION_KEY);
		if (locale == null) {
			locale = LocaleUtils.toLocale(DEFAULT_LANGUAGE);
		}
		session.setAttribute(SESSION_KEY, locale);
	}
	
	public static void setSessionLocale(HttpSession session,Locale locale){
		if (locale == null) {
			locale = LocaleUtils.toLocale(DEFAULT_LANGUAGE);
		}
		session.setAttribute(SESSION_KEY, locale);
	}
	
}
