package com.woshidaniu.fastxls.jexcel.cache;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.jexcel.utils.JXLConfigUtils;

/**
 * 
 *@类名称	: JXLFontNameCacheManager.java
 *@类描述	：字体名称对象缓存管理
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 1:57:30 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class JXLFontNameCacheManager {
	
	protected static Logger LOG = LoggerFactory.getLogger(JXLFontNameCacheManager.class);
	protected static Properties config = JXLConfigUtils.getDefaultConfig();
	protected static ConcurrentMap<String, FontName> COMPLIED_FONTNAME = new ConcurrentHashMap<String, FontName>();
 
	private volatile static JXLFontNameCacheManager singleton;

	public static JXLFontNameCacheManager getInstance() {
		if (singleton == null) {
			synchronized (JXLFontNameCacheManager.class) {
				if (singleton == null) {
					singleton = new JXLFontNameCacheManager();
				}
			}
		}
		return singleton;
	}
	
	private JXLFontNameCacheManager(){
		String initFontNames = config.getProperty(ConfigConstants.XLS_FONT_NAMES);
		if(!BlankUtils.isBlank(initFontNames)){
			String[] fontNames = StringUtils.tokenizeToStringArray(initFontNames);
			for (String fontName : fontNames) {
				COMPLIED_FONTNAME.putIfAbsent(fontName, WritableFont.createFont(fontName));
				LOG.debug("Create FontName [ {0} ] " , fontName);
			}
		}
	}
	
	public FontName getFontName(String fontName){
		if (StringUtils.isNotEmpty(fontName)) {
			FontName ret = COMPLIED_FONTNAME.get(fontName);
 			if (ret != null) {
 				return ret;
 			} 
 			ret = WritableFont.createFont(fontName);
 			LOG.debug("Create FontName [ {0} ] " , fontName);
 			FontName existing = COMPLIED_FONTNAME.putIfAbsent(fontName, ret);
 			if (existing != null) {
 				ret = existing;
 			}
 			return ret;
 		}
 		return null;
	}
	
	public void destroy(String fontName) {
		if(fontName != null){
			//清除fontName对应的字体缓存
			COMPLIED_FONTNAME.remove(fontName);
		}
	}
	
	public static void main(String[] args) {
		
		JXLFontNameCacheManager.getInstance().getFontName("宋体");
		
	}

}

