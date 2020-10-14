package com.woshidaniu.xmlhub.core.validator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.woshidaniu.xmlhub.utils.URLUtils;

/**
 * 
 *@类名称	: AbstractXMLValidator.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 5:07:59 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class AbstractXMLValidator implements XMLValidator {

	@Override
	public boolean validate(String xmlpath, String path) throws IOException {
		return this.validate(new File(xmlpath), new File(path));
	}

	@Override
	public boolean validate(String xmlpath, File file) throws IOException {
		return this.validate(new File(xmlpath), file);
	}

	@Override
	public boolean validate(String xmlpath, URL URL) throws IOException {
		return this.validate(new File(xmlpath), URL);
	}

	@Override
	public boolean validate(File xmlfile, String path) throws IOException {
		return this.validate(xmlfile, new File(path));
	}
	
	@Override
	public boolean validate(File xmlfile, URL URL) throws IOException {
		return this.validate(xmlfile, URLUtils.getFile(URL));
	}
	
}
