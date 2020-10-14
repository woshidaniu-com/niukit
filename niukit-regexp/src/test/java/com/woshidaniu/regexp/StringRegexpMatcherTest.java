package com.woshidaniu.regexp;

import junit.framework.TestCase;

import com.woshidaniu.regexp.matcher.StringRegexpMatcher;

public class StringRegexpMatcherTest extends TestCase {

	public void testStringRegexpMatcher(){
		StringRegexpMatcher regex = new StringRegexpMatcher();
		String source = "540357164@qq.com";
		System.out.println(regex.contains(source, "email"));
	}
}
