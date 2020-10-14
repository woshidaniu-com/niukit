package com.woshidaniu.xmlhub.core.validator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 
 *@类名称	: XMLValidator.java
 *@类描述	：使用*.xsd,*.dtd校验xml
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:29:59 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface XMLValidator {

	public boolean validate(String xmlpath, String path) throws IOException;
	
	public boolean validate(String xmlpath, File file) throws IOException;
	
	public boolean validate(String xmlpath, URL url) throws IOException;

	public boolean validate(File xmlfile, String path) throws IOException;

	public boolean validate(File xmlfile, File file) throws IOException;
	
	public boolean validate(File xmlfile, URL url) throws IOException;
	
}
