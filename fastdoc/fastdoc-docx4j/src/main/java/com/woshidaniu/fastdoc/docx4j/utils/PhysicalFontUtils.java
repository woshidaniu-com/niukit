/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.utils;

import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;

import com.woshidaniu.fastdoc.docx4j.fonts.ChineseFont;
import com.woshidaniu.fastdoc.docx4j.fonts.PhysicalFontFactory;

public class PhysicalFontUtils {

	
	private static PhysicalFontFactory FONT_FACTORY = PhysicalFontFactory.getInstance();
 
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置中文字体;解决中文乱码问题
	 */
	public static void setWmlPackageFonts(WordprocessingMLPackage wmlPackage) throws Exception {
		//字体映射;  
		Mapper fontMapper = FONT_FACTORY.getFontMapper();;
        //设置文档字体库
		wmlPackage.setFontMapper(fontMapper, true);
    }
	
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置默认字体
	 */
	public static void setDefaultFont(WordprocessingMLPackage wmlPackage,String fontName) throws Exception {
        //设置文件默认字体
        RFonts rfonts = Context.getWmlObjectFactory().createRFonts(); 
        rfonts.setAsciiTheme(null);
        rfonts.setAscii(fontName);
        rfonts.setHAnsi(fontName);
        rfonts.setEastAsia(fontName);
        RPr rpr = wmlPackage.getMainDocumentPart().getPropertyResolver().getDocumentDefaultRPr();
        rpr.setRFonts(rfonts);
    }
	
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置中文字体
	 */
	public static void setSimSunFont(WordprocessingMLPackage wmlPackage) throws Exception {
        //设置文件默认字体
		setDefaultFont(wmlPackage,ChineseFont.SIMSUM.getFontName());
    }
	
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 增加新的字体
	 */
	public static void setPhysicalFont(WordprocessingMLPackage wmlPackage,PhysicalFont physicalFont) throws Exception {
		Mapper fontMapper = wmlPackage.getFontMapper() == null ? new IdentityPlusMapper() : wmlPackage.getFontMapper();
		//分别设置字体名和别名对应的字体库
		fontMapper.put(physicalFont.getName(), physicalFont );
		//设置文档字体库
		wmlPackage.setFontMapper(fontMapper, true);
    }
	
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 增加新的字体
	 */
	public static void setPhysicalFont(WordprocessingMLPackage wmlPackage,String fontName) throws Exception {
		//Mapper fontMapper = new BestMatchingMapper();  
		Mapper fontMapper = wmlPackage.getFontMapper() == null ? new IdentityPlusMapper() : wmlPackage.getFontMapper();
		//获取字体库
		PhysicalFont physicalFont = PhysicalFonts.get(fontName);
		//分别设置字体名和别名对应的字体库
		fontMapper.put(fontName, physicalFont );
		//设置文档字体库
		wmlPackage.setFontMapper(fontMapper, true);
    }
}
