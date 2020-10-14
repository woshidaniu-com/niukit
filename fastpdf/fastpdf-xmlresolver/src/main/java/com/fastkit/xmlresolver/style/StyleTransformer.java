package com.fastkit.xmlresolver.style;

import com.fastkit.xmlresolver.xml.XMLElement;


 /**
 * @package com.fastkit.xmlresolver.style
 * @className: StyleTransformer
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-21
 * @time : 下午6:26:17 
 */
public interface StyleTransformer<E extends XMLElement> {

	public Object[] getTransform(E element,String mapping_key);
	
}



