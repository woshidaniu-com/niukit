/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream;

import com.woshidaniu.fastxls.poi.exception.XLSStreamException;
import com.woshidaniu.fastxls.poi.stream.events.XLSEvent;

/**
 *@类名称	: XMLEventConsumer.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 9:45:26 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public interface XLSEventConsumer {

	 public void add(XLSEvent event) throws XLSStreamException;
	 
}
