package com.woshidaniu.fastpdf.render.helper;

import com.woshidaniu.fastpdf.render.draw.Arrow;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;

/**
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: VerticalPositionMarkHelper
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 下午6:42:43
 */
public class ArrowMarkHelper{
	
	private static ArrowMarkHelper instance = null;
	private ArrowMarkHelper(){}
	
	public static ArrowMarkHelper getInstance(){
		instance = instance==null?new ArrowMarkHelper():instance;
		return instance;
	}
	
	public Arrow getArrow(ItextXMLElement element){
		return null;
	}
	
}
