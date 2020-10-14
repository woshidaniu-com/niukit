package com.woshidaniu.fastxls.template;

import java.io.File;
import java.io.OutputStream;

/**
 * 
 *@类名称	: XLSOutput.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 24, 2016 10:03:00 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface XLSOutput {
	
	public void output(String templatePath,OutputStream out);
	
	public void output(File templateFile,OutputStream out);
	
}
