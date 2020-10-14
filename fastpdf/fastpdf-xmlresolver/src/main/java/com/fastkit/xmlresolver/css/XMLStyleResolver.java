package com.fastkit.xmlresolver.css;

import java.util.HashMap;
import java.util.Map;

import com.fastkit.xmlresolver.xml.XMLElement;
import com.woshidaniu.basicutils.BlankUtils;

/**
 * 
 * @className: XMLCSSResolver
 * @description: 样式解析器
 * @author : kangzhidong
 * @date : 下午5:10:14 2013-8-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class XMLStyleResolver {
/*
	// 用于定义正规表达式对象模板类型
	protected PatternCompiler compiler = new Perl5Compiler();
	// 正规表达式比较匹配对象
	protected PatternMatcher matcher = new Perl5Matcher();*/
	protected String cssPattern = "[\\.\\#]([\\w\\-]+){([\\S]*)}";

	private static XMLStyleResolver instance = null;

	private XMLStyleResolver() {
	}

	public static XMLStyleResolver getInstance() {
		instance = instance == null ? new XMLStyleResolver() : instance;
		return instance;
	}
/*
	
	 * System.out.println(matchResult.begin(0)); // 0分组索引 , 匹配串开始值 ,如匹配串xxxx
	 * xxx, 总是0. System.out.println(matchResult.end(0)); // 0分组索引, 匹配串结束值 ,
	 * 如xxxxxxx , 则相应值为 4 3. System.out.println(matchResult.beginOffset(0)); //
	 * 0分组索引,匹配串在源串开始索引 System.out.println(matchResult.endOffset(0)); //
	 * 0分组索引,匹配串在源串结束索引 System.out.println(matchResult.groups()); // 分组数量
	 * System.out.println(matchResult.length()); // 匹配串长度
	 * System.out.println(matchResult.toString()); // 匹配串 for (int index = 0;
	 * index < matchResult.groups(); index++) {
	 * System.out.println(index+":"+matchResult.group(index).replaceAll(" ",
	 * "")); }
	 
	public Map<String, String> parserCss(String styles) {
		Map<String, String> style = null;
		if (!BlankUtil.isBlank(styles)) {
			style = new HashMap<String, String>();
			String[] splits = styles.toString().split("\\n");
			for (int i = 0; i < splits.length; i++) {
				String trim = splits[i].replaceAll("\\s", "");
				System.out.println("trim:" + trim);
				MatchResult matchResult = null;
				try {
					// 实例大小大小不敏感的正规表达式模板
					Pattern hardPattern = compiler.compile(cssPattern,Perl5Compiler.DEFAULT_MASK);
					// 返回匹配结果
					System.out.println(matcher.matches(trim, hardPattern));
					if (matcher.matches(trim, hardPattern)) {
						matchResult = matcher.getMatch();
						style.put(matchResult.group(1), matchResult.group(2));
					}
				} catch (MalformedPatternException e) {
					e.printStackTrace();
				}
			}
		}
		return style;
	}
*/
	public Map<String, String> resolver(XMLElement element) {
		if (!BlankUtils.isBlank(element.attr("style"))) {
			return this.resolver(element.attr("style"));
		}
		return new HashMap<String, String>();
	}

	public Map<String, String> resolver(String style) {
		Map<String, String> styles = new HashMap<String, String>();
		String[] splits = style.replaceAll("\\s", "").split(";");
		for (int i = 0; i < splits.length; i++) {
			String trim = splits[i].replaceAll("\\s", "");
			String[] css = trim.split(":");
			styles.put(css[0], css[1]);
		}
		return styles;
	}

}
