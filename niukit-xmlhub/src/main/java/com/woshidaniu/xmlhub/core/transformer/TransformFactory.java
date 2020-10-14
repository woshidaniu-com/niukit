/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.xmlhub.core.transformer;

import org.xml.sax.InputSource;

/**
 *@类名称	: TransformFactory.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 17, 2016 10:29:37 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface TransformFactory {

	public String toJSONString(InputSource inputSource);
	
}
