package com.woshidaniu.regexp.matcher;

/**
 * 
 *@类名称	: SpecialRegexpMatcher.java
 *@类描述	：使用正则表达式验证【身份证相关字符串】或提取数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:42:45 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class SpecialRegexpMatcher extends StringRegexpMatcher {

	public SpecialRegexpMatcher() {
		super("special");
	}

	public SpecialRegexpMatcher(int mask) {
		super("special", mask);
	}
}
