/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.handler;
import org.docx4j.convert.out.ConversionHTMLScriptElementHandler;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *@类名称	: OutputConversionHTMLScriptElementHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Dec 28, 2016 4:48:33 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class OutputConversionHTMLScriptElementHandler implements ConversionHTMLScriptElementHandler {

	@Override
	public Element createScriptElement(OpcPackage opcPackage, Document document, String scriptDefinition) {
		Element ret = null;
		if ((scriptDefinition != null) && (scriptDefinition.length() > 0)) {
			ret = document.createElement("script");
			ret.setAttribute("type", "text/javascript");
			ret.appendChild(document.createComment(scriptDefinition));
		}
		return ret;
	}

}
