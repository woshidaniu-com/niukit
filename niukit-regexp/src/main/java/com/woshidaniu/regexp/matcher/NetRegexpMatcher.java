package com.woshidaniu.regexp.matcher;

/**
 * 
 *@类名称	: NetRegex.java
 *@类描述	：使用正则表达式验证【网络相关字符串】或提取数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:34:51 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class NetRegexpMatcher extends StringRegexpMatcher{

	public NetRegexpMatcher() {
		super("net");
	}

	public NetRegexpMatcher(int mask) {
		super("net", mask);
	}

}
