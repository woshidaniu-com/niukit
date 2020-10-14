package com.woshidaniu.fastpdf.render.style;

import java.util.Map;

import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.helper.DimensionHelper;
import com.woshidaniu.fastpdf.render.resolver.ItextAlignmentResolver;
import com.woshidaniu.fastpdf.render.resolver.ItextBaseColorResolver;
import com.woshidaniu.fastpdf.render.resolver.ItextFontResolver;

public class PDFStyleTransformer implements StyleTransformer<ItextXMLElement> {
	
	private static PDFStyleTransformer instance = null;
	private PDFStyleTransformer(){}
	
	private static ThreadLocal<PDFStyleTransformer> threadLocal = new ThreadLocal<PDFStyleTransformer>(){
		
		protected PDFStyleTransformer initialValue() {
			if (instance == null) {
				instance = new PDFStyleTransformer();
			}
			return instance;
		};
		
	};
	
	public static PDFStyleTransformer getInstance(){
		return threadLocal.get();
	}
	
	public Object[] getTransform(ItextXMLElement element,String mapping_key) {
		if(!BlankUtils.isBlank(mapping_key)){
			//第1步：取得对象方法的映射css键
			String css_key = ItextContext.getInstance().getMethodMapping(mapping_key);
			//对于有些方法对应多个映值的处理，Map的value是 以,分割的字符
			String[] css_keys = css_key.split(",");
			//判断是否有映射的css属性
			if(!BlankUtils.isBlank(css_keys)){
				return this.getTransform(element,mapping_key, css_keys);
			}else{
				//第2步：取得对象属性的映射css键
				css_key = ItextContext.getInstance().getPropertyMapping(mapping_key);
				//对于有些方法对应多个映值的处理，Map的value是 以,分割的字符
				css_keys = css_key.split(",");
				//判断是否有映射的css属性
				if(!BlankUtils.isBlank(css_keys)){
					return this.getTransform(element,mapping_key,css_keys);
				}
			}
		}
		return new Object[]{};
	}
	
	//转换样式值
	private Object[] getTransform(ItextXMLElement element,String mapping_key,String[] css_keys){
		Map<String,Object> styles = element.styles();
		Object[] css_values = null;
		Object value = null;
		//多个样式组成一个实体对象的映射
		if(css_keys.length>1){
			//字体转换
			if(mapping_key.equalsIgnoreCase("setFont")||
			    mapping_key.equalsIgnoreCase("font")){
				css_values = new Object[]{ItextFontResolver.getInstance().getFont(element)};
			}else if(mapping_key.equalsIgnoreCase("setMargins")){
				css_values = new Object[css_keys.length];
				for (int i = 0; i < css_keys.length; i++) {
					css_values[i] = DimensionHelper.getInstance().getFixed(styles, css_keys[i]);
				}
			}else{
				
			}
		}else{
			if("noWrap".equalsIgnoreCase(mapping_key)){
				//布尔值转换
				value = WhiteSpaceUtils.isNoWrap(element);
			}else if(mapping_key.toLowerCase().indexOf("color")>-1){
				//颜色值转换
				value = ItextBaseColorResolver.getInstance().getColor(element);
			}else if(css_keys[0].toLowerCase().equalsIgnoreCase("text-align")){
				//方向值转换
				value = ItextAlignmentResolver.getInstance().getTextAlign(element);
			}else if(css_keys[0].toLowerCase().equalsIgnoreCase("vertical-align")){
				//方向值转换
				value = ItextAlignmentResolver.getInstance().getVerticalAlign(element);
			}else if(mapping_key.toLowerCase().indexOf("percentage")>-1){
				//百分百值转换
				value = DimensionHelper.getInstance().getPercent(element, css_keys[0]);
			}else{
				//普通数值转换
				value = DimensionHelper.getInstance().getFixed(element, css_keys[0]);
			}
			css_values = new Object[]{value};
		}
		return css_values;
	}
	
}
