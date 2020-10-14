/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.handler;

import org.docx4j.convert.out.ConversionHyperlinkHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.docx4j.openpackaging.parts.Part;

public class OutputConversionHyperlinkHandler implements ConversionHyperlinkHandler {

	@Override
	public void handleHyperlink(Model hyperlinkModel, OpcPackage opcPackage, Part currentPart) throws Docx4JException {
		//do nothing
	}

}
