/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.ConversionHTMLStyleElementHandler;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.woshidaniu.fastdoc.docx4j.Docx4jConstants;

public class OutputConversionHTMLStyleElementHandler implements ConversionHTMLStyleElementHandler {

	@Override
	public Element createStyleElement(OpcPackage opcPackage, Document document, String styleDefinition) {
		
		// See XsltHTMLFunctions, which typically generates the String styleDefinition.
		// In practice, the styles are coupled to the document content, so you're
		// less likely to override their content; just whether they are linked or inline.
		
		Element ret = null;
		if ((styleDefinition != null) && (styleDefinition.length() > 0)) {
			ret = document.createElement("style");
			ret.setAttribute("type", "text/css");
			ret.appendChild(document.createComment(styleDefinition));
		}
		
		/**Key = docx4j.Convert.Out.HTML.CssIncludeUri*/
		String cssIncludeUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_CONVERT_OUT_HTML_CSSINCLUDEURI);
		if ((cssIncludeUri != null) && (cssIncludeUri.length() > 0)) {
			try {
				ret = document.createElement("style");
				ret.setAttribute("type", "text/css");
				ret.appendChild(document.createComment(IOUtils.toString(new URI(cssIncludeUri), Charset.defaultCharset())));
			} catch (IOException e) {
				//do nothing
			} catch (URISyntaxException e) {
				//do nothing
			}
		}
		/**Key = docx4j.Convert.Out.HTML.CssIncludePath*/
		String cssIncludePath = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_CONVERT_OUT_HTML_CSSINCLUDEPATH);
		if ((cssIncludePath != null) && (cssIncludePath.length() > 0)) {
			InputStream input = null;
			try {
				input = new FileInputStream(cssIncludePath);
				ret = document.createElement("style");
				ret.setAttribute("type", "text/css");
				ret.appendChild(document.createComment(IOUtils.toString(input, Charset.defaultCharset())));
			} catch (IOException e) {
				//do nothing
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		
		return ret;
	}

}
