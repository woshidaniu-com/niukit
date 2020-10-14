/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.jexcel.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;

/**
 *@类名称	: CellFormatCacheManager.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 4:22:16 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class JXLCellFormatCacheManager {

	protected static Logger LOG = LoggerFactory.getLogger(JXLCellFormatCacheManager.class);
	protected static JXLFontCacheManager fontCacheManager = JXLFontCacheManager.getInstance();
	protected static ConcurrentMap<String, JXLCellFormatKey> COMPLIED_CELLFORMATKEY = new ConcurrentHashMap<String, JXLCellFormatKey>();
	protected static ConcurrentMap<JXLCellFormatKey,WritableCellFormat> COMPLIED_CELLFORMAT = new ConcurrentHashMap<JXLCellFormatKey,WritableCellFormat>();
	
	private volatile static JXLCellFormatCacheManager singleton;

	public static JXLCellFormatCacheManager getInstance() {
		if (singleton == null) {
			synchronized (JXLCellFormatCacheManager.class) {
				if (singleton == null) {
					singleton = new JXLCellFormatCacheManager();
				}
			}
		}
		return singleton;
	}
	
	private JXLCellFormatCacheManager(){
		
	}
	
	public JXLCellFormatKey getCellFormatKey(String name, int size, boolean bold,
			Alignment alignment, VerticalAlignment valignment,
			Colour background, Border border, BorderLineStyle borderLine) {
		if (StringUtils.isNotEmpty(name)) {
			String format =  StringUtils.join(new Object[]{ name , size , bold , alignment.getDescription() , 
					valignment.getDescription() , background.getDescription(), borderLine.getDescription()}, "-");
			JXLCellFormatKey ret = COMPLIED_CELLFORMATKEY.get(format);
			if (ret != null) {
				return ret;
			}
			ret = new JXLCellFormatKey(name, size, bold, alignment, valignment, background, border, borderLine);
			JXLCellFormatKey existing = COMPLIED_CELLFORMATKEY.putIfAbsent(format, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	 
	
	public WritableCellFormat getCellFormat(String name, int size, boolean bold,
			Alignment alignment, VerticalAlignment valignment,
			Colour background, Border border, BorderLineStyle borderLine){
		JXLCellFormatKey formatKey = getCellFormatKey(name, size, bold,alignment, valignment, background, border, borderLine);
		WritableCellFormat ret = COMPLIED_CELLFORMAT.get(formatKey);
		if (ret != null) {
			return ret;
		}
		try {
			//获取字体
			WritableFont font = fontCacheManager.getFont(formatKey.getName(), formatKey.getSize(), formatKey.isBold());
			//创建CellFormat
			ret = new WritableCellFormat(font);
			ret.setAlignment(alignment);
			ret.setVerticalAlignment(valignment);
			ret.setBackground(background);
			ret.setBorder(border, borderLine);
			WritableCellFormat existing = COMPLIED_CELLFORMAT.putIfAbsent(formatKey, ret);
			if (existing != null) {
				ret = existing;
			}
			//输出日志
			LOG.debug("Create WritableCellFormat [  FontName: {0}, Size: {1}, Bold: {2}, Alignment: {3}, valignment: {4}, background: {5}, border: {6} - {7} ] " , 
					new Object[]{ formatKey.getName(), formatKey.getSize(), formatKey.isBold() , 
					alignment.getDescription() , valignment.getDescription() , 
					background.getDescription(), border.getDescription() , borderLine.getDescription()});
		} catch (WriteException e) {
			LOG.error("Create WritableCellFormat Error:", e);
			return null;
		}
		return ret;
	}
	
}
