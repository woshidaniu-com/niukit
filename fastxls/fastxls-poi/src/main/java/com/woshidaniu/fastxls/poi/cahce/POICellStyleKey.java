package com.woshidaniu.fastxls.poi.cahce;

import java.io.Serializable;

import org.apache.poi.ss.usermodel.Font;

import com.woshidaniu.basicutils.StringUtils;

@SuppressWarnings("serial")
public class POICellStyleKey implements Serializable {
	
	//字体名称
	protected String fontName = "黑体";
	//字体粗细
	protected short boldweight = Font.BOLDWEIGHT_NORMAL;
	//字体颜色
	protected short color = Font.COLOR_NORMAL;
	//字体大小
	protected short fontHeightInPoints = (short)11;
	//是否斜体
	protected boolean italic  = false;
	//是否有删除线
	protected boolean strikeout = false;
	//设置上标，下标
	protected short typeOffset = Font.SS_NONE;
	//下划线
	protected byte underline = Font.U_NONE;
	//字符集
	protected int charSet = Font.DEFAULT_CHARSET;
	
	public POICellStyleKey() {
		
	}
	
	public POICellStyleKey(String fontName) {
		this.fontName = fontName;
	}

	public POICellStyleKey(String fontName, short boldweight, short color, short fontHeightInPoints,
			boolean italic, boolean strikeout, short typeOffset, byte underline, int charSet) {
		this.boldweight = boldweight;
		this.color = color;
		this.fontHeightInPoints = fontHeightInPoints;
		this.fontName = fontName;
		this.italic = italic;
		this.strikeout = strikeout;
		this.typeOffset = typeOffset;
		this.underline = underline;
		this.charSet = charSet;
	}


	public short getBoldweight() {
		return boldweight;
	}

	public void setBoldweight(short boldweight) {
		this.boldweight = boldweight;
	}


	public short getColor() {
		return color;
	}


	public void setColor(short color) {
		this.color = color;
	}


	public short getFontHeightInPoints() {
		return fontHeightInPoints;
	}


	public void setFontHeightInPoints(short fontHeightInPoints) {
		this.fontHeightInPoints = fontHeightInPoints;
	}


	public String getFontName() {
		return fontName;
	}


	public void setFontName(String fontName) {
		this.fontName = fontName;
	}


	public boolean isItalic() {
		return italic;
	}


	public void setItalic(boolean italic) {
		this.italic = italic;
	}


	public boolean isStrikeout() {
		return strikeout;
	}


	public void setStrikeout(boolean strikeout) {
		this.strikeout = strikeout;
	}


	public short getTypeOffset() {
		return typeOffset;
	}


	public void setTypeOffset(short typeOffset) {
		this.typeOffset = typeOffset;
	}


	public byte getUnderline() {
		return underline;
	}


	public void setUnderline(byte underline) {
		this.underline = underline;
	}


	public int getCharSet() {
		return charSet;
	}


	public void setCharSet(int charSet) {
		this.charSet = charSet;
	}
	
	@Override
	public String toString() {
		return "Font [" +  StringUtils.join(new Object[]{ fontName , boldweight , color , fontHeightInPoints
				, italic , strikeout , typeOffset , underline , charSet }, "-") + " ]" ;
	}

}
