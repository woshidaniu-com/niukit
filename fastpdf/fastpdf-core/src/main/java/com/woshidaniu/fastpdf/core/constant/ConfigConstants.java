package com.woshidaniu.fastpdf.core.constant;

public class ConfigConstants {
	
	//---------初始化---------------------------------------
	/**
	 * Key[itext.config.path] : IMexportContext 初始化properties文件路径 .  默认 ： itext-config.properties
	 */
	public static String KEY_CONFIG_FILE_PATH = "itext.config.path";
	
	/**
	 * Key[itext.config.xml.path] : IMexportContext 初始化xml文件路径 .  默认 ： itext-config.xml
	 */
	public static String KEY_CONFIG_XML_PATH = "itext.config.xml.path";
	
	/**
	 * Key[itext.config.encoding] : 编码格式 ，默认： UTF-8
	 */
	public static  String KEY_CONFIG_ENCODING = "itext.config.encoding";
	
	//---------字体---------------------------------------
	/**
	 * Key[itext.fonts.dir] : 自定义字体存储路径 ，默认： fonts
	 */
	public static  String KEY_FONTS_DIR = "itext.fonts.dir";
	
	//---------存储---------------------------------------
	/**
	 * Key[itext.store.enable] : 是否需要生成临时.pdf 文件，默认 ：false
	 */
	public static String KEY_STORE_ENABLE = "itext.store.enable";
	/**
	 * Key[itext.store.tmpDir] : pdf 临时文件存储路径 ，默认 ：itextDir
	 */
	public static String KEY_STORE_TMPDIR = "itext.store.tmpDir";
	
	/**
	 * Key[itext.store.prefix] : pdf 文件存储时，文件名后的前缀字符串 【前缀字符串-文件名.pdf】 ，默认 ：空
	 */
	public static String KEY_STORE_PREFIX = "itext.store.prefix";
	/**
	 * Key[itext.store.suffix] : pdf 文件存储时，文件名后的后缀字符串 【文件名-后缀字符串.pdf】生成方式 ，可选【Date,UUID】，默认 ：UUID
	 */
	public static String KEY_STORE_SUFFIX = "itext.store.suffix";
	
	
}