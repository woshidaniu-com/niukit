package com.woshidaniu.regexp.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *@类名称	: JakartaRegexpUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:28:06 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class JakartaRegexpUtils {

	protected static ConcurrentMap<String, Pattern> COMPLIED_PATTERN = new ConcurrentHashMap<String, Pattern>();
	
	/**
	 * 
	 *@描述		：正则表达式验证方法:匹配表达式则返回true,不匹配则返回false
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 23, 20164:04:15 PM
	 *@param regexp
	 *@param str
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static boolean matches(String regexp, String str) { 
        Pattern pattern = getPattern(regexp); 
        Matcher matcher = pattern.matcher(str); 
        return matcher.matches(); 
    }

	/**
	 * 
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 23, 20164:04:47 PM
	 *@param regexp
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static Pattern getPattern(String regexp) {
		if (StringUtils.isNotEmpty(regexp)) {
			Pattern ret = COMPLIED_PATTERN.get(regexp);
			if (ret != null) {
				return ret;
			}
			ret = Pattern.compile(regexp);
			Pattern existing = COMPLIED_PATTERN.putIfAbsent(regexp, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
    
}
