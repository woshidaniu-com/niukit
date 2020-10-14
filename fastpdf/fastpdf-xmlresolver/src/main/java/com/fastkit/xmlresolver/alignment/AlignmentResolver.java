package com.fastkit.xmlresolver.alignment;

import com.fastkit.xmlresolver.xml.XMLElement;

 /**
 * @package com.fastkit.xmlresolver.alignment
 * @className: AlignmentResolver
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-16
 * @time : 下午2:15:00 
 */
public interface AlignmentResolver<A,E extends XMLElement> {

	public A getTextAlign(E element);
	
	public A getVerticalAlign(E element);

	public A getAlignment(E element, String cssName);
	
}



