package com.woshidaniu.regexp.matcher;

/**
 * 
 *@类名称	: MobileRegexpMatcher.java
 *@类描述	：使用正则表达式验证【通讯号码相关字符串】或提取数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:35:17 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class MobileRegexpMatcher extends StringRegexpMatcher{

	public MobileRegexpMatcher() {
		super("mobile");
	}

	public MobileRegexpMatcher(int mask) {
		super("mobile", mask);
	}

}
