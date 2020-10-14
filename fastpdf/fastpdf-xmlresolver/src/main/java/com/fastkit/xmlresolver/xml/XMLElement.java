package com.fastkit.xmlresolver.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Element;

import com.fastkit.xmlresolver.context.XMLElementContext;
import com.fastkit.xmlresolver.css.XMLStyleResolver;
import com.fastkit.xmlresolver.utils.XMLEscaper;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.format.utils.PatternFormatUtils;

@SuppressWarnings({"serial"})
public abstract class XMLElement extends Element{
	
	private Map<String,Object> styles = new HashMap<String, Object>();
	
	public boolean isElement(String tag) {
		return this.getName().equalsIgnoreCase(tag);
	}
	
	public boolean isElementOf(String[] tags) {
		if(tags!=null){
			for (String tag : tags) {
				if(this.getName().equalsIgnoreCase(tag)){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	public boolean hasAttr() {
		return this.getAttributes().size() > 0;
	}

	public boolean hasAttr(String tag) {
		return this.getAttribute(tag) != null;
	}

	public boolean hasChild() {
		return this.getChildren().size() > 0;
	}

	public boolean hasChild(String tag) {
		return this.getChildren(tag) != null;
	}
	
	public boolean assertAttrEquals(String tag,String equal) {
		return equal.equalsIgnoreCase(this.attr(tag));
	}

	public String attr(String atrr) {
		String vl = this.getAttributeValue(atrr);
		return (vl != null && vl.length() > 0) ? vl : "";
	}

	public String attr(String atrr,String... arguments) {
		String vl = this.getAttributeValue(atrr);
		return PatternFormatUtils.format(vl,arguments);
	}
	
	public Map<String, String> attrs() {
		Map<String, String> attrs = new HashMap<String, String>();
		if (this.getAttributes().isEmpty()) {
			return attrs;
		}
		Iterator<?> itr = this.getAttributes().iterator();
		while (itr.hasNext()) {
			Attribute a = (Attribute) itr.next();
			attrs.put(a.getName(), a.getValue());
		}
		return attrs;
	}

	public String wrapString(Object atrr) {
		return (atrr != null && atrr.getClass().equals(String.class)) ? String.valueOf(atrr) : "";
	}

	/**
	 * 
	 * @description: 解析所有的样式为键值对形式，先解释class对应的，接着是style，后者同名属性会覆盖前者
	 * @author : kangzhidong
	 * @date 下午4:45:30 2013-8-15 
	 * @param styles 如:"line-height:20;font-size: 12;font-weight:bold ; "
	 * @return
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public Map<String,Object> styles(){
		try {
			if(styles.isEmpty()){
				if(!BlankUtils.isBlank(this.attr("class"))){
					String className = this.attr("class");
					if(!BlankUtils.isBlank(className)){
						String[] classes = className.split(" ");
						for (String tmp : classes) {
							if(!BlankUtils.isBlank(tmp)){
								System.out.println(className);
								styles.putAll(XMLElementContext.getStyle(tmp));
							}
						}
					}
				}
				if(!BlankUtils.isBlank(this.attr("style"))){
					String style = this.attr("style");
					styles.putAll(XMLStyleResolver.getInstance().resolver(style));
				}
			}
			return styles;
		} catch (Exception e) {
		}
		return styles;
	}
	
	public String text() {
		return BlankUtils.isBlank(this.getText()) ? "" : XMLEscaper.revert(this.getText());
	}
	
	/**
	 * 
	 * @description: 取得横向方向
	 * @author : kangzhidong
	 * @date : 2014-1-16
	 * @time : 下午7:48:31 
	 * @return
	 */
	public abstract int getAlignment();
	
	/**
	 * 
	 * @description: 取得纵向方向
	 * @author : kangzhidong
	 * @date : 2014-1-16
	 * @time : 下午7:48:31 
	 * @return
	 */
	public abstract int getVerticalAlignment();
	
	/**
	 * 
	 * @description: 获得格式化后的内容 ，可格式化如：{0}学年{1}学期{2}统计报表
	 * @author : kangzhidong
	 * @date : 2014-1-10
	 * @time : 上午9:15:37 
	 * @param arguments 按照顺序对应的格式化参数
	 * @return
	 */
	public String text(String... arguments) {
		return PatternFormatUtils.format(this.text(),arguments);
	}
	
	public <T> String text(T argument){
		return PatternFormatUtils.format(this.text(), argument);
	}
	
	public String childText(String tag) {
		return BlankUtils.isBlank(this.getChildText(tag)) ? "" : XMLEscaper.revert(this.getChildText(tag));
	}
	
	public String childText(String tag,String... arguments) {
		return PatternFormatUtils.format(this.childText(tag),arguments);
	}
	
	public <T> String childText(String tag,T argument){
		return PatternFormatUtils.format(this.childText(tag), argument);
	}

}
