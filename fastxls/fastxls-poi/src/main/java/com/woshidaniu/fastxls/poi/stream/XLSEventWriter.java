/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream;

import com.woshidaniu.fastxls.poi.exception.XLSStreamException;
import com.woshidaniu.fastxls.poi.stream.events.XLSEvent;

/**
 *@类名称	: XLSEventWriter.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 9:46:50 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public interface XLSEventWriter extends XLSEventConsumer {
	
    public void add(XLSEvent event) throws XLSStreamException;

    public void add(XLSEventReader reader) throws XLSStreamException;

    public void close() throws XLSStreamException;

    public void flush() throws XLSStreamException;

    public String getPrefix(String uri) throws XLSStreamException;

    public void setPrefix(String prefix, String uri) throws XLSStreamException;
    
}
