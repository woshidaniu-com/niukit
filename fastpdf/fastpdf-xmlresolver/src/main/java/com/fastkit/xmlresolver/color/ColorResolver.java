package com.fastkit.xmlresolver.color;

import java.io.IOException;
import java.util.Map;

import com.fastkit.xmlresolver.xml.XMLElement;

/**
 * @package com.fastkit.xmlresolver.fonts
 * @className: FontParser
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-11
 * @time : 下午2:18:15
 */
public interface ColorResolver<F,E extends XMLElement> {

	public F getColor(E element) throws IOException;

	public F getColor(Map<String, Object> styles, String[] cssNames) throws IOException;

}
