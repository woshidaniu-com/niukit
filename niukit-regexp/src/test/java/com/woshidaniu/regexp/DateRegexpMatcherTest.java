package com.woshidaniu.regexp;

import junit.framework.TestCase;

import com.woshidaniu.regexp.matcher.DateRegexpMatcher;

public class DateRegexpMatcherTest extends TestCase{
	
	public void testDateRegex() {
		DateRegexpMatcher r = new DateRegexpMatcher();
		String str = "<body>sdasds</body>";
		String str1 = "select * from table";
		System.out.println(r.matches(str,"SQL"));
		System.out.println(r.matches(str1,"SQL"));
	}
}
