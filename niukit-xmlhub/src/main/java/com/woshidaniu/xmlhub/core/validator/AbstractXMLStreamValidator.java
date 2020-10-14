package com.woshidaniu.xmlhub.core.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.woshidaniu.xmlhub.utils.URLUtils;

/**
 * 
 *@类名称	: AbstractXMLStreamValidator.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:30:51 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class AbstractXMLStreamValidator extends AbstractXMLValidator implements XMLStreamValidator {
	
	@Override
	public boolean validate(File xmlfile, File file) throws IOException {
		return this.validate(new FileInputStream(xmlfile), new FileInputStream(file));
	}
	
	@Override
	public boolean validate(InputStream xmlInput, String path) throws IOException {
		return this.validate(xmlInput, new File(path));
	}

	@Override
	public boolean validate(InputStream xmlInput, File file) throws IOException {
		return this.validate(xmlInput, new FileInputStream(file));
	}

	@Override
	public boolean validate(InputStream xmlInput, URL url) throws IOException {
		return this.validate(xmlInput, URLUtils.getURLAsStream(url));
	}
	
}
