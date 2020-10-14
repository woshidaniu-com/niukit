package com.woshidaniu.fastxls.poi.factory;

import org.apache.commons.lang3.builder.Builder;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *@类名称		： POIFontBuilder.java
 *@类描述		：Builder Pattern(建造者模式） 下的字体创建对象
 *@创建人		：kangzhidong
 *@创建时间	：Mar 2, 2017 8:53:42 AM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class POIFontBuilder implements Builder<Font> {
    
	protected static Logger LOG = LoggerFactory.getLogger(POIFontBuilder.class);
	private Font font;
    private StringBuilder builder;
	
    public POIFontBuilder(Workbook wb) {
    	//生成一个字体对象
        this.font = wb.createFont();
        this.builder = new StringBuilder();
    }

    public POIFontBuilder fontName(String fontName) {
        //字体
        this.font.setFontName(fontName);
        this.builder.append("fontName : ").append(fontName).append(",");
        return this;
    }
    
    public POIFontBuilder bold(short boldweight) {
    	//字体粗细
        this.font.setBoldweight(boldweight);
        this.builder.append("boldweight : ").append(boldweight).append(",");
        return this;
    }
    
    public POIFontBuilder color(short color) {
    	//字体颜色
    	this.font.setColor(color);
    	this.builder.append("color : ").append(color).append(",");
        return this;
    }
    
    public POIFontBuilder fontHeightInPoints(short fontHeightInPoints) {
    	//字体大小
    	this.font.setFontHeightInPoints(fontHeightInPoints);
    	this.builder.append("fontHeightInPoints : ").append(fontHeightInPoints).append(",");
        return this;
    }
    
    public POIFontBuilder fontHeight(short fontHeight) {
    	//字体大小
    	this.font.setFontHeight(fontHeight);
    	//font.setFontHeight((short)(12 * 20));
    	this.builder.append("fontHeight : ").append(fontHeight).append(",");
        return this;
    }
    
    public POIFontBuilder italic(boolean italic) {
    	//是否斜体
    	this.font.setItalic(italic);
    	this.builder.append("italic : ").append(italic).append(",");
        return this;
    }
    
    public POIFontBuilder strikeout(boolean strikeout) {
        //是否有删除线
    	this.font.setStrikeout(strikeout);
    	this.builder.append("strikeout : ").append(strikeout).append(",");
        return this;
    }
    
    public POIFontBuilder typeOffset(short typeOffset) {
    	//设置上标，下标
    	this.font.setTypeOffset(typeOffset);
    	this.builder.append("typeOffset : ").append(typeOffset).append(",");
        return this;
    }
    
    public POIFontBuilder underline(byte underline) {
    	//下划线
    	this.font.setUnderline(underline);
    	this.builder.append("underline : ").append(underline).append(",");
        return this;
    }
    
    public POIFontBuilder charSet(int charSet) {
    	//字符集
    	this.font.setCharSet(charSet);
    	this.builder.append("charSet : ").append(charSet).append(",");
        return this;
    }
    
    // Other Font construction methods
    public Font build() {
    	if(this.builder.length() > 0){
    		LOG.debug("Create Font [" +  this.builder.substring(0, this.builder.length() - 1) + " ]" );
    		this.builder = null;
    	}
        return this.font;
    }
    
}