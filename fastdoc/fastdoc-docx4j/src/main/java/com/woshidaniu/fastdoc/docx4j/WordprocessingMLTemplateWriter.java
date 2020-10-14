/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.docx4j.Docx4jProperties;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import com.woshidaniu.basicutils.Assert;

public class WordprocessingMLTemplateWriter {

	public String writeToString(String docFile) throws Exception {
		return this.writeToString(new File(docFile));
	}
	
	public String writeToString(File docFile) throws Exception {
		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.load(docFile);
		StringBuilderWriter output = new StringBuilderWriter();
		try {
			this.writeToWriter(wmlPackage, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return output.toString();
	}
	
	public String writeToString(WordprocessingMLPackage wmlPackage) throws Exception {
		MainDocumentPart documentPart = wmlPackage.getMainDocumentPart();		
		return XmlUtils.marshaltoString(documentPart);
	}
	
	public static void writeToFile(WordprocessingMLPackage wmlPackage,File outFile) throws IOException {
		writeToStream(wmlPackage, new FileOutputStream(outFile));
	}
	
	public static void writeToStream(WordprocessingMLPackage wmlPackage,OutputStream output) throws IOException {
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(output, " output is not specified!");
		InputStream input = null;
		try {
			MainDocumentPart documentPart = wmlPackage.getMainDocumentPart();		
			input = XmlUtils.marshaltoInputStream(documentPart, true, Context.jc);
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	public void writeToWriter(WordprocessingMLPackage wmlPackage,Writer output) throws Exception {
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(output, " output is not specified!");
		InputStream input = null;
		try {
			MainDocumentPart documentPart = wmlPackage.getMainDocumentPart();		
			input = XmlUtils.marshaltoInputStream(documentPart, true, Context.jc);
			//获取模板输出编码格式
			String charsetName = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_CONVERT_OUT_WMLTEMPLATE_CHARSETNAME, Docx4jConstants.DEFAULT_CHARSETNAME );
			IOUtils.copy(input, output, Charset.forName(charsetName));
		} finally {
			IOUtils.closeQuietly(input);
		}
	}
	
}
