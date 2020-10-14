package com.fastkit.xmlresolver.utils;

import java.util.Map;

import com.fastkit.xmlresolver.xml.XMLElement;

 /**
 * @package com.fastkit.xmlresolver.text
 * @className: WhiteSpace
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-22
 * @time : 下午4:02:11 
 */
public class WhiteSpaceUtils  {

	//nowrap inherit normal pre
	public static <E extends XMLElement> boolean isNoWrap(E element) {
		Map<String,Object> styles = element.styles();
		if(null!=styles&&!styles.isEmpty()){
			Object alignment = styles.get("white-space");
			if(alignment!=null&&alignment instanceof String){
				if("inherit".equalsIgnoreCase(alignment.toString())){
					return WhiteSpaceUtils.isNoWrap((XMLElement) element.getParentElement());
				}else if("pre".equalsIgnoreCase(alignment.toString())){
					return true;
				}else if("nowrap".equalsIgnoreCase(alignment.toString())){
					return false;
				}else if("normal".equalsIgnoreCase(alignment.toString())){
					return false;
				}
			}
		}
		return false;
	}
	
}



