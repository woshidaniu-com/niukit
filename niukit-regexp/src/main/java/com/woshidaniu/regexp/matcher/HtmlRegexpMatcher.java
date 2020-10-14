package com.woshidaniu.regexp.matcher;

/**
 * 
 *@类名称	: HtmlRegexpMatcher.java
 *@类描述	：使用正则表达式验证【网页相关字符串】或提取数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:36:09 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HtmlRegexpMatcher extends StringRegexpMatcher{

	public HtmlRegexpMatcher() {
		super("html");
	}

	public HtmlRegexpMatcher(int mask) {
		super("html", mask);
	}

	public String bulidRegexp(String tagName){
		return  "/<"+tagName+".*?</"+tagName+">/";
	}
	
}
