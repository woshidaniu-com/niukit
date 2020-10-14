package com.woshidaniu.httputils;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethodBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *@类名称	: HttpResponeUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：2016年4月27日 下午1:31:02
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class HttpResponeUtils{
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpResponeUtils.class);

	public static String getContentType(HttpMethodBase httpMethod) {
		Header header = httpMethod.getResponseHeader(HttpHeaders.CONTENT_TYPE);
		/*HeaderElement[] elements = header.getElements();
		for (HeaderElement elem : elements) {
			LOG.debug(elem.getName() + " = " + elem.getValue());
			if ("gzip".equalsIgnoreCase(elem.getName())) {
				contentType = elem.getValue();
				break;
			}
		}*/
		return header.getValue();
	}
}
