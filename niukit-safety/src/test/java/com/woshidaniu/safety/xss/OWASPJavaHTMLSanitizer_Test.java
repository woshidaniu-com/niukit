package com.woshidaniu.safety.xss;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.owasp.html.ElementPolicy;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class OWASPJavaHTMLSanitizer_Test {
	
	String taintedHTML = null;
	@Before
	public void Before() {
		taintedHTML = "<!DOCTYPE html><html><head><meta charset=\"utf-8\">"
				+ "<title>菜鸟教程(runoob.com)</title>"
				+ "<script>function displayDate(){	document.getElementById(\"demo\").innerHTML=Date();}</script>"
				+ "</head>"
				+ "<body>"
				+ "<a href=\"sdsd\">A标记测试</a>"
				+ "<i>I标记测试</i>"
				+ "<h1>我的第一个 JavaScript 程序</h1><p id=\"demo\">这是一个段落</p>"
				+ "<button type=\"button\" onclick=\"displayDate()\">显示日期</button>"
				+ "</body></html>";
		taintedHTML = "%22()%26%25<acx><ScRiPt%20>prompt(909614)</ScRiPt>";
	}
	
	@Test
	public void test1() throws Exception {
		
		PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
		String safeHTML = policy.sanitize(taintedHTML);
		System.out.println(safeHTML);
		
		
	}
	
	@Test
	public void test2() throws Exception {
		
		PolicyFactory policy = Sanitizers.IMAGES.and(Sanitizers.LINKS);
		String safeHTML = policy.sanitize(taintedHTML);
		System.out.println(safeHTML);
		
		
	}
	
	@Test
	public void test3() throws Exception {
		
		PolicyFactory policy = new HtmlPolicyBuilder()
			    .allowElements("a")
			    .allowUrlProtocols("https")
			    .allowAttributes("href").onElements("a")
			    .requireRelNofollowOnLinks()
			    .toFactory();
		String safeHTML = policy.sanitize(taintedHTML);
		System.out.println(safeHTML);
		
		
	}
	
	@Test
	public void test4() throws Exception {
		
		PolicyFactory policy = new HtmlPolicyBuilder()
			    .allowElements("p")
			    .allowElements(
			        new ElementPolicy() {
			          public String apply(String elementName, List<String> attrs) {
			            attrs.add("class");
			            attrs.add("header-" + elementName);
			            return "div";
			          }
			        }, "h1", "h2", "h3", "h4", "h5", "h6")
			    .toFactory();
		String safeHTML = policy.sanitize(taintedHTML);
		System.out.println(safeHTML);
		
		
	}
	
	@Test
	public void test5() throws Exception {
		
		PolicyFactory policy = new HtmlPolicyBuilder().toFactory();
		String safeHTML = policy.sanitize(taintedHTML);
		System.out.println(safeHTML);
		
	}
	
	@After
	public void after() {
	}
	
}
