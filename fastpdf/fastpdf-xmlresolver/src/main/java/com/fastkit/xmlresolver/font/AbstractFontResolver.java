
package com.fastkit.xmlresolver.font;

import com.fastkit.xmlresolver.xml.XMLElement;

 /**
 * @package com.fastkit.xmlresolver.fonts
 * @className: AbstractFontResolver
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-11
 * @time : 下午2:20:46 
 */

public abstract class AbstractFontResolver<F,C,E extends XMLElement> implements FontResolver<F,C,E>{

	public String family(Object family) {
		return (family != null && family.toString().length() > 0) ? "黑体":family.toString();
	}

}



