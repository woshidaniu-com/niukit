package com.woshidaniu.regexp.matcher;

/**
 * 
 *@类名称	: SQLRegex.java
 *@类描述	：使用正则表达式验证【SQL语句相关字符串】或提取数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:34:07 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class SQLRegexpMatcher extends StringRegexpMatcher{

	public SQLRegexpMatcher() {
		super("sql");
	}

	public SQLRegexpMatcher(int mask) {
		super("sql", mask);
	}
}
