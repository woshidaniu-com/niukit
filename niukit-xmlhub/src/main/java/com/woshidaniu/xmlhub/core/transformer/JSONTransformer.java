package com.woshidaniu.xmlhub.core.transformer;

import java.io.File;
import java.io.InputStream;

/**
 * 
 *@类名称	: JSONTransformer.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:49:57 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface JSONTransformer {

	public String toJSONString(InputStream input);
	
	public String toJSONString(File xmlfile);
	
	public String toJSONString(String xmlpath);
	
	public String toJSONString(StringBuilder xmlStr);
	
}
