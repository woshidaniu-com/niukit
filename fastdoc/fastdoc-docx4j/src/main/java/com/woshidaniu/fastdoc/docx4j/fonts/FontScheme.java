/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.fonts;

import java.io.Serializable;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;

/**
 * *******************************************************************
 * 
 * @className ： FontScheme
 * @description ： TODO(描述这个类的作用)
 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date ： Dec 30, 2016 1:24:04 PM
 * @version V1.0
 *          *******************************************************************
 */
@SuppressWarnings("serial")
public class FontScheme implements Serializable {

	protected static Logger LOG = LoggerFactory.getLogger(FontScheme.class);
	protected String fontName;
	protected String fontAlias;
	protected URL fontURL;
	
	public FontScheme(String fontName, String fontAlias, URL fontURL) {
		this.fontName = fontName;
		this.fontAlias = fontAlias;
		this.fontURL = fontURL;
	}

	public String getFontName() {
		return fontName;
	}

	public String getFontAlias() {
		return fontAlias;
	}

	public URL getFontURL() {
		return fontURL;
	}

	@Override
	public String toString() {
		return "PhysicalFont [" +  StringUtils.join(new Object[]{ fontName , fontAlias , fontURL }, "-") + " ]";
	}

}
