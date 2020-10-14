package com.fastkit.xmlresolver.css;

import java.io.FileNotFoundException;

 /**
 * @package com.fastkit.xmlresolver.css
 * @className: CSSResolver
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-11
 * @time : 下午1:41:42 
 */
public interface CSSResolver {

	public void resolveText(String styleText);
	
	public void resolveURI(String path) throws FileNotFoundException;
	
}



