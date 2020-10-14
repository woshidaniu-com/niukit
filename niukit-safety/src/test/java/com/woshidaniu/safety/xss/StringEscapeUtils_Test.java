package com.woshidaniu.safety.xss;


import org.apache.commons.lang.StringEscapeUtils;  

public class StringEscapeUtils_Test {  
  
	/*
		StringEscapeUtils.escapeCsv(str)
		StringEscapeUtils.escapeHtml(writer, string);
	    StringEscapeUtils.escapeJava(str)
	    StringEscapeUtils.escapeJavaScript(str)
	    StringEscapeUtils.escapeSql(str)
	    StringEscapeUtils.escapeXml(str)
	 */
		
	public static void main(String args[]) {

		String escapeHtml = StringEscapeUtils.escapeHtml("	\"\"()	[sassd]<font>chen   xing</font>");
		System.out.println("转义HTML,注意汉字:" + escapeHtml); // 转义HTML,注意汉字
		System.out.println("反转义HTML:" + StringEscapeUtils.unescapeHtml(escapeHtml)); // 反转义HTML

		String escapeJava = StringEscapeUtils.escapeJava("中文测试");
		System.out.println("转成Unicode编码：" + escapeJava); // 转义成Unicode编码
		System.out.println("反转义Unicode编码：" + StringEscapeUtils.unescapeJava(escapeJava));

		String escapeJavaScript = StringEscapeUtils.escapeJavaScript("	function (index){alert(333);}");
		System.out.println("转义JavaScript：" + escapeJavaScript); // 转义成Unicode编码
		System.out.println("反转义JavaScript：" + StringEscapeUtils.unescapeJavaScript(escapeJavaScript));

		String sql = "1' or '1'='1' ";
		System.out.println("防SQL注入:" + StringEscapeUtils.escapeSql(sql)); // 防SQL注入

		String escapeXml = StringEscapeUtils.escapeXml("<name>中文</name>");
		System.out.println("转义XML：" + escapeXml); // 转义xml
		System.out.println("反转义XML：" + StringEscapeUtils.unescapeXml(escapeXml)); // 转义xml

	}  
}  