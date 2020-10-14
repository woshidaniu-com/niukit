package com.woshidaniu.regexp.matcher;
/**
 * 
 *@类名称	: DateRegexpMatcher.java
 *@类描述	：使用正则表达式验证【时间相关字符串】或提取数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:36:31 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class DateRegexpMatcher extends StringRegexpMatcher {

	public DateRegexpMatcher() {
		super("date");
	}

	public DateRegexpMatcher(int mask) {
		super("date", mask);
	}

	public String getYear(String dateStr) {
		return "";
	}

	public String getMonth(String dateStr) {
		return "";
	}

	public String getDay(String dateStr) {
		return "";
	}

	public String getHours(String dateStr) {
		return "";
	}

	public String getMinutes(String dateStr) {
		return "";
	}

	public String getSeconds(String dateStr) {
		return "";
	}

	public String getMilliseconds(String dateStr) {
		return "";
	}

}
