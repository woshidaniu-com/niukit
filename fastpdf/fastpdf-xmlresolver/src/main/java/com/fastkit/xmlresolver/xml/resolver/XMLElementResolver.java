package com.fastkit.xmlresolver.xml.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.fastkit.xmlresolver.context.XMLElementContext;
import com.fastkit.xmlresolver.css.XMLCSSResolver2;
import com.fastkit.xmlresolver.xml.XMLElement;
/**
 * 
 * @package com.fastkit.xmlresolver.resolver
 * @className: XMLElementResolver
 * @description: XML配置文件解析器
 * @author : kangzhidong
 * @date : 2014-1-9
 * @time : 下午1:40:21
 */
public class XMLElementResolver {

	private static XMLElementResolver instance = null;
	private XMLElementResolver(){}
	
	private static ThreadLocal<XMLElementResolver> threadLocal = new ThreadLocal<XMLElementResolver>(){
		
		protected XMLElementResolver initialValue() {
			if (instance == null) {
				instance= new XMLElementResolver();
			}
			return instance;
		};
		
	};
	
	public static XMLElementResolver getInstance(){
		return threadLocal.get();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, XMLElement> resolver(String xmlRelativePath)throws JDOMException, IOException {
		List<String> paths = XMLPathResolver.getInstance().resolverAll(xmlRelativePath);
		for (String xmlPath : paths) {
			// 解析XML
			Document doc = null;
			SAXBuilder sb = new SAXBuilder();
			sb.setEntityResolver(new NoElementResolver());
			doc = sb.build(new FileInputStream(new File(xmlPath)));
			Element root = doc.getRootElement(); 
			XMLElement element = null;
			
			List<Element> list = root.getChildren();
			if(null!=list&&list.size()>0){
				Iterator<Element> link_itr = list.iterator();
				while (link_itr.hasNext()) {
					element = (XMLElement)link_itr.next();
					if("link".equalsIgnoreCase(element.getName())){
						//引入的css文件
						XMLElementContext.addLink(element.attr("href"));
						String path = element.attr("href");
						XMLCSSResolver2.getInstance().resolveURI(path);		
					}else if("style".equalsIgnoreCase(element.getName())){
						//直接写的css样式
						String styleText = element.text();
						System.out.println(styleText);
						XMLCSSResolver2.getInstance().resolveText(styleText);
					}else{
						//其他二级子元素
						String key = XMLElementContext.getInstance().getKey();
						XMLElementContext.addElement(element.attr(key), element);
					}
				}
			}
		}
		return XMLElementContext.getElements();
	}
	
	public XMLElement resolver(String xmlRelativePath,String key) throws JDOMException, IOException  {
		this.resolver(xmlRelativePath);
		return XMLElementContext.getElement(key);
	}
	
}
