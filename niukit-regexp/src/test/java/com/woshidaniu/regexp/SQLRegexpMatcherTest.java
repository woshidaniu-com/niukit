package com.woshidaniu.regexp;

import junit.framework.TestCase;

import com.woshidaniu.regexp.matcher.SQLRegexpMatcher;

public class SQLRegexpMatcherTest extends TestCase{
	
	public void testSQLRegexpMatcher() {
		SQLRegexpMatcher r = new SQLRegexpMatcher();
		String str = "<body>sdasds</body>";
		String str1 = "select * from table";
		System.out.println(r.matches(str,"SQL"));
		System.out.println(r.matches(str1,"SQL"));
	}
}
