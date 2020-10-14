package com.woshidaniu.regexp.matcher;

/**
 * 
 *@类名称	: MathRegexpMatcher.java
 *@类描述	：使用正则表达式验证【数字字符串】或提取数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:35:39 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class MathRegexpMatcher extends StringRegexpMatcher{

	public MathRegexpMatcher() {
		super("math");
	}

	public MathRegexpMatcher(int mask) {
		super("math", mask);
	}
}
