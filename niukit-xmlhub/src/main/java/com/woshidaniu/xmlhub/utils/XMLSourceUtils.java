package com.woshidaniu.xmlhub.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import com.woshidaniu.xmlhub.core.CharEncoding;

/**
 * 
 *@类名称	: XMLSourceUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:26:04 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class XMLSourceUtils {

	public static Source getInputSource(StringBuilder xmlString) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(xmlString.toString().getBytes(CharEncoding.UTF_8));
		Reader reader = new InputStreamReader(inputStream);
		return new StreamSource(reader);
	}
	
	
	
	
	
}
