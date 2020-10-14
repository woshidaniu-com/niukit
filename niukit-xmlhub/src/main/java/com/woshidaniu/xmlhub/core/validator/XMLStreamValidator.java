package com.woshidaniu.xmlhub.core.validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 
 *@类名称	: XMLStreamValidator.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:30:07 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface XMLStreamValidator extends XMLValidator {

	public abstract boolean validate(InputStream xmlInput, String path) throws IOException;

	public abstract boolean validate(InputStream xmlInput, File file) throws IOException;
	
	public abstract boolean validate(InputStream xmlInput, URL url) throws IOException;
	
	public abstract boolean validate(InputStream xmlInput, InputStream input) throws IOException;

}
