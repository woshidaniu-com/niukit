package com.woshidaniu.regexp.factory;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.regexp.PatternMatcher;
import com.woshidaniu.regexp.matcher.DateRegexpMatcher;
import com.woshidaniu.regexp.matcher.HtmlRegexpMatcher;
import com.woshidaniu.regexp.matcher.MathRegexpMatcher;
import com.woshidaniu.regexp.matcher.MobileRegexpMatcher;
import com.woshidaniu.regexp.matcher.NetRegexpMatcher;
import com.woshidaniu.regexp.matcher.SQLRegexpMatcher;
import com.woshidaniu.regexp.matcher.StringRegexpMatcher;

public class PatternMatcherFactory {

	protected static ConcurrentMap<String, PatternMatcher> COMPLIED_MATCHER = new ConcurrentHashMap<String, PatternMatcher>();
	private volatile static PatternMatcherFactory singleton;

	public static PatternMatcherFactory getInstance() {
		if (singleton == null) {
			synchronized (PatternMatcherFactory.class) {
				if (singleton == null) {
					singleton = new PatternMatcherFactory();
				}
			}
		}
		return singleton;
	}
	
	private PatternMatcherFactory() {
		deregister();
	}

	public void deregister() {
		COMPLIED_MATCHER.clear();
		register("normal", new StringRegexpMatcher());
		register("date", new DateRegexpMatcher());
		register("html", new HtmlRegexpMatcher());
		register("math", new MathRegexpMatcher());
		register("mobile", new MobileRegexpMatcher());
		register("net", new NetRegexpMatcher());
		register("sql", new SQLRegexpMatcher());
	}

	public void deregister(String type) {
		COMPLIED_MATCHER.remove(type);
	}

	public PatternMatcher lookup(String type) {
		return ((PatternMatcher) COMPLIED_MATCHER.get(type));
	}

	public void register(String type, PatternMatcher matcher) {
		COMPLIED_MATCHER.put(type, matcher);
	}

	public boolean matches(String source, String regexName) {
		Iterator<String> ite = COMPLIED_MATCHER.keySet().iterator();
		boolean have = false;
		while (ite.hasNext() && !have) {
			PatternMatcher matcher = lookup(ite.next());
			if (!BlankUtils.isBlank(matcher)) {
				matcher.matches(source, regexName);
				have = true;
				return true;
			}
		}
		return have;
	}

}
