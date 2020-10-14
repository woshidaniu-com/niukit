package com.woshidaniu.xmlhub.xstream;

import java.io.File;

/**
 * 
 *@类名称	: XMLStreamReader.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 5:11:28 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface XMLStreamReader {
	
	public <T> T toBean(String xmlStr, Class<T> clazz);
	
	public <T> T toBean(File xmlfile, Class<T> clazz);
	
}
