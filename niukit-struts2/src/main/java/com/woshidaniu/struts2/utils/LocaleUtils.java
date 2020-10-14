/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.struts2.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

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

	/**
	 * 
	 *@描述：国际化语言切换
	 *@创建人:kangzhidong
	 *@创建时间:Jan 29, 20162:56:10 PM
	 *@param invocation
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static void interceptLocale(HttpServletRequest request,ValueStack statck){
		Locale locale = LocaleUtils.interceptLocale(request);
		statck.set("language", locale != null ? locale.getLanguage() + "_" + locale.getCountry() : "zh_CN");
	}
	
	public static void setSessionLocale(HttpSession session,ValueStack statck) {
		LocaleUtils.setSessionLocale(session);
		LocaleUtils.setStackLocale(LocaleUtils.getSessionLocale(session), statck);
	}
	
	public static void setSessionLocale(HttpSession session,Locale locale,ValueStack statck){
		if (locale == null) {
			locale = LocaleUtils.toLocale(DEFAULT_LANGUAGE);
		}
		session.setAttribute(SESSION_KEY, locale);
		LocaleUtils.setStackLocale(locale, statck);
	}
	
	public static void setStackLocale(Locale locale,ValueStack statck){
		if (locale == null) {
			String loc[] = "zh_CN".split("_");
			locale = new Locale(loc[0], loc[1]);
		}
		ActionContext.getContext().setLocale(locale);
		statck.set("language", locale.getLanguage() + "_" + locale.getCountry() );
	}
	
	public static Locale getLocale(){
		// 获取request请求对象
		HttpServletRequest request = ServletActionContext.getRequest();
		//会话作废前取出,原Locale
        Locale locale = LocaleUtils.getSessionLocale(request.getSession());
		if (locale == null) {
			locale = ActionContext.getContext().getLocale();
		}
		if (locale == null) {
			return new Locale("zh","CN");
		}
		return locale;
	}
	
	public static String getLocaleKey(){
		//会话作废前取出,原Locale
        Locale locale = LocaleUtils.getLocale();
		return locale.getLanguage() + "_" + locale.getCountry();
	}
	
	public static String getLocalePath(String filepath){
		return LocaleUtils.getLocalePath(LocaleUtils.getLocaleKey(), filepath);
	}
	
}
