/**
 * @title: CSSVisiter.java
 * @package com.fastkit.css.parser
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-8
 * @time : 下午7:02:58 
 */

package com.fastkit.xmlresolver.css;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.css.CSSCharsetRule;
import org.w3c.dom.css.CSSFontFaceRule;
import org.w3c.dom.css.CSSImportRule;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSUnknownRule;
import org.w3c.dom.stylesheets.MediaList;

import com.woshidaniu.basicutils.BlankUtils;
/**
 * @package com.fastkit.css.parser
 * @className: CSSVisiter
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-8
 * @time : 下午7:02:58
 */

public class XMLCSSVisitor2 {

	private static XMLCSSVisitor2 instance = null;
	private XMLCSSVisitor2(){}
	
	public static XMLCSSVisitor2 getInstance(){
		if (instance == null) {
			instance= new XMLCSSVisitor2();
		}
		return instance;
	}

	
	public void visitSheet(CSSStyleSheet css) throws FileNotFoundException {
		Map<String,Map<String,Object>> styles = new HashMap<String, Map<String,Object>>();
		if(!BlankUtils.isBlank(css)){
			System.out.println("==========================================================");
			CSSRuleList cssrules = css.getCssRules(); 
			Map<String,Object> style_map = null;
	        for (int i = 0; i < cssrules.getLength(); i++){ 
	        	CSSRule rule = cssrules.item(i);   
	        	style_map = new HashMap<String, Object>();
	        	/*@font-face是CSS3中的一个模块，他主要是把自己定义的Web字体嵌入到你的网页中，随着@font-face模块的出现，我们在Web的开发中使用字体不怕只能使用Web安全字体*/
	        	if (rule instanceof CSSFontFaceRule){
	        		CSSFontFaceRule cssrule = (CSSFontFaceRule) rule;
	        		CSSStyleDeclaration style = cssrule.getStyle();
	        		for(int j=0,n=style.getLength();j<n;j++){
	            		String propertyName = style.item(j);
	            		String value = style.getPropertyValue(propertyName);
	            		System.out.println(propertyName+":"+value);
	            	}
	        	}else if (rule instanceof CSSPageRule){
	        		CSSPageRule cssrule = (CSSPageRule) rule;
	        		CSSStyleDeclaration style = cssrule.getStyle();
	        		for(int j=0,n=style.getLength();j<n;j++){
	            		String propertyName = style.item(j);
	            		String value = style.getPropertyValue(propertyName);
	            		System.out.println(propertyName+":"+value);
	            	}
	        		
	        	}else if (rule instanceof CSSMediaRule){
	        		CSSMediaRule cssrule = (CSSMediaRule) rule;
	        		MediaList mediaList = cssrule.getMedia();
	        		for (int j = 0; j < mediaList.getLength(); j++) {
	        			String propertyName = mediaList.item(j);
	            		String value = mediaList.getMediaText();
	            		System.out.println(propertyName+":"+value);
					}
	        	}else if (rule instanceof CSSCharsetRule){
	        		CSSCharsetRule cssrule = (CSSCharsetRule) rule;
	        		System.out.println(cssrule.getEncoding());
	        		
	        	}else if (rule instanceof CSSStyleRule){
	            	CSSStyleRule cssrule = (CSSStyleRule) rule;
	            	System.out.println("==========================================================");
	            	System.out.println("css内容:"+cssrule.getCssText());
	            	System.out.println("css选择:"+cssrule.getSelectorText());
	            	System.out.println("css明细:");
	            	CSSStyleDeclaration style = cssrule.getStyle();
	            	for(int j=0,n=style.getLength();j<n;j++){
	            		String propertyName = style.item(j);
	            		String value = style.getPropertyValue(propertyName);
	            		style_map.put(propertyName, value);
	            	
	            		System.out.println(propertyName+":"+value);
	            	}
	            	styles.put(cssrule.getSelectorText(), style_map);
	            	System.out.println("==========================================================");
	            }else if (rule instanceof CSSImportRule){
	            	 CSSImportRule cssrule = (CSSImportRule) rule;  
	            	 System.out.println(cssrule.getHref());
	            	 System.out.println("================解析@IMPORT "+cssrule.getHref()+"==========================================");
	            	 XMLCSSResolver2.getInstance().resolveURI(cssrule.getHref());
	    		}else if (rule instanceof CSSUnknownRule){
	        	}
	        }
		}
	}
	

}
