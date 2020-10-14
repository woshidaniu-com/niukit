package com.woshidaniu.safety.xss;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

import com.woshidaniu.safety.utils.AntiSamyScanUtils;
import com.woshidaniu.safety.xss.factory.AntiSamyProxy;

public class AntiSamyScanUtils_Test {
	
	AntiSamyProxy proxy = null;
	String taintedHTML = null;
	@Before
	public void Before() {
		try {
			Policy policy = Policy.getInstance("src/main/resources/antisamy.xml");
			proxy = new AntiSamyProxy(new AntiSamy(policy), policy, AntiSamy.SAX, null);
		} catch (PolicyException e) {
			e.printStackTrace();
		}
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
		//taintedHTML = "你好， &nbsp;测= &nbsp;测 &nbsp;试挖 &nbsp;的 &nbsp;";
	}
	
	@Test
	public void test() throws Exception {
		
		AntiSamyScanUtils.xssClean(proxy, taintedHTML);
		
	}
	
	@After
	public void after() {
	}
	
}
