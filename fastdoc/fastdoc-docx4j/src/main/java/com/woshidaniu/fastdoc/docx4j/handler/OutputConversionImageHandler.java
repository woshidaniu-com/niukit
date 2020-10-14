/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.handler;

import org.docx4j.convert.out.html.HTMLConversionImageHandler;


public class OutputConversionImageHandler extends HTMLConversionImageHandler {

	/**
	 * @param imageDirPath
	 * @param targetUri
	 * @param includeUUID
	 */
	public OutputConversionImageHandler(String imageDirPath, String targetUri, boolean includeUUID) {
		super(imageDirPath, targetUri, includeUUID);
	}

}
