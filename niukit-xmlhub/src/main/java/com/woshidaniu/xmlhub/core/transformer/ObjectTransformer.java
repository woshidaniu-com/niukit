/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub.core.transformer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 *@类名称	: ObjectTransformer.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:52:28 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface ObjectTransformer<T> {

	public T toObject(InputStream input) throws IOException;
	
	public T toObject(File xmlfile) throws IOException;
	
	public T toObject(String xmlpath) throws IOException;
	
	public T toObject(StringBuilder xmlStr) throws IOException;
	
}
