package com.woshidaniu.db.core.utils;

/**
 * 
 * *******************************************************************
 * @className	： SQLInjectionUtils
 * @description	： SQL注入检测
 * @author 		： kangzhidong
 * @date		： Feb 24, 2016 9:52:02 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public final class SQLInjectionUtils {

	private static String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";

	/**
	 * 
	 * @description: 判断是否SQL注入
	 * @author : kangzhidong
	 * @date 2015-4-13 
	 * @time 下午04:06:57
	 * @param inj_str
	 * @param str
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public static boolean hasSQLInjection(String inj_str, String str) {
		SQLInjectionUtils.inj_str = inj_str;
		return SQLInjectionUtils.hasSQLInjection(str);
	}

	public static boolean hasSQLInjection(String str) {
		String[] inj_stra = inj_str.split("\\|");
		for (int i = 0; i < inj_stra.length; i++) {
			if (str.indexOf(" " + inj_stra[i] + " ") >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @description: 采用正则表达式将包含有 单引号(')，分号(;) 和 注释符号(--)的语句给替换掉来防止SQL注入
	 * @author : kangzhidong
	 * @date 2015-4-13 
	 * @time 下午04:07:07
	 * @param str
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public static String transactSQLInjection(String str) {
		return str.replaceAll(".*([';]+|(--)+).*", " ");
	}

}
