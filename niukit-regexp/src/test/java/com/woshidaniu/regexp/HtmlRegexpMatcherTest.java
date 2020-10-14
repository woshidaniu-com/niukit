package com.woshidaniu.regexp;

import junit.framework.TestCase;

import com.woshidaniu.regexp.matcher.HtmlRegexpMatcher;

public class HtmlRegexpMatcherTest extends TestCase{

	public void testHtmlRegexpMatcher() {
		HtmlRegexpMatcher r = new HtmlRegexpMatcher();
		String str = "<body>sdasds</body>";
		System.out.println(str.matches(r.bulidRegexp("body")));
	}
	
}
