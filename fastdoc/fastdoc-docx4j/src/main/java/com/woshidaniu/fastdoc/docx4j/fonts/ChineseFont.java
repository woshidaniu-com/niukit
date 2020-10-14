/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.fonts;


import java.net.URL;

public enum ChineseFont {
	
	//仿宋体
	SIMFANG("仿宋体","SimFang","SIMFANG.ttf"),
	//黑体
	SIMHEI("黑体","SimHei","SIMHEI.ttf"),
	//楷体
	SIMKAI("楷体","SimKai","SIMKAI.ttf"),
	//宋体&新宋体
	SIMSUM("宋体","SimSun","simsun.ttc"),
	//华文仿宋
	STFANGSO("华文仿宋","StFangSo","STFANGSO.ttf");
	
	private String fontName;
	private String fontAlias;
	private String fontFileName;
	
	public URL getFontURL() {
		return ChineseFont.class.getResource(this.fontFileName);
	}
	public String getFontName() {
		return fontName;
	}
	
	public String getFontAlias() {
		return fontAlias;
	}
	 
	private ChineseFont(String fontName,String fontAlias,String fontFileName) {
		this.fontName = fontName;
		this.fontAlias = fontAlias;
		this.fontFileName = fontFileName;
    }

}
