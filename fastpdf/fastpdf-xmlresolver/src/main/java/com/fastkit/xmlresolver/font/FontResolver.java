package com.fastkit.xmlresolver.font;

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
public interface FontResolver<F,C,E extends XMLElement> {

	public F getFont(E element) ;

	public F getFont(Map<String, Object> styles);
	
	public F getFont(String[] css_values);

	public String family(Object style);

	public int style(Object style);
	
	public int size(Object style);

	public C color(Object style);
}
