/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastxls.poi.stream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.stream.events.XMLEvent;

import com.woshidaniu.fastxls.poi.exception.XLSStreamException;
import com.woshidaniu.fastxls.poi.stream.events.XLSEvent;

/**
 *@类名称	: XLSEventReader.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 9:41:40 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface XLSEventReader extends Iterator<XLSEvent> {

	/**
	   * Get the next XMLEvent 
	   * @see XMLEvent
	   * @throws XLSStreamException if there is an error with the underlying XML.
	   * @throws NoSuchElementException iteration has no more elements.
	   */
	  public XLSEvent nextEvent() throws XLSStreamException;

	  /**
	   * Check if there are more events.  
	   * Returns true if there are more events and false otherwise.
	   * @return true if the event reader has more events, false otherwise
	   */
	  public boolean hasNext();

	  /**
	   * Check the next XMLEvent without reading it from the stream.
	   * Returns null if the stream is at EOF or has no more XMLEvents.
	   * A call to peek() will be equal to the next return of next().
	   * @see XMLEvent
	   * @throws XLSStreamException
	   */
	  public XLSEvent peek() throws XLSStreamException;

	  /**
	   * Reads the content of a text-only element. Precondition:
	   * the current event is START_ELEMENT. Postcondition:
	   * The current event is the corresponding END_ELEMENT.
	   * @throws XLSStreamException if the current event is not a START_ELEMENT
	   * or if a non text element is encountered
	   */
	  public String getElementText() throws XLSStreamException;

	  /**
	   * Skips any insignificant space events until a START_ELEMENT or
	   * END_ELEMENT is reached. If anything other than space characters are
	   * encountered, an exception is thrown. This method should
	   * be used when processing element-only content because
	   * the parser is not able to recognize ignorable whitespace if
	   * the DTD is missing or not interpreted.
	   * @throws XLSStreamException if anything other than space characters are encountered
	   */
	  public XLSEvent nextTag() throws XLSStreamException;

	  /**
	   * Get the value of a feature/property from the underlying implementation
	   * @param name The name of the property
	   * @return The value of the property
	   * @throws IllegalArgumentException if the property is not supported
	   */
	  public Object getProperty(java.lang.String name) throws java.lang.IllegalArgumentException;  

	  /**
	   * Frees any resources associated with this Reader.  This method does not close the
	   * underlying input source.
	   * @throws XLSStreamException if there are errors freeing associated resources
	   */
	  public void close() throws XLSStreamException;
	
}
