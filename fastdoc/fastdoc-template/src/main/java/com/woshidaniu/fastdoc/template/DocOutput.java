package com.woshidaniu.fastdoc.template;

import java.io.File;
import java.io.OutputStream;

/**
 * 
 *@类名称	: DocOutput.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 11:43:43 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface DocOutput {
	
	public void output(String templatePath,OutputStream out);
	
	public void output(File templateFile,OutputStream out);
	
}
