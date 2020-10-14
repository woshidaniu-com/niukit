/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j;

import java.io.File;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.docx4j.TextUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

public class WordprocessingMLPackageExtractor {

	public String extract(String inputfile) throws Exception {
		return this.extract(new File(inputfile));
	}
	
	public String extract(File inputfile) throws Exception {
		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.load(inputfile);
		StringBuilderWriter output = new StringBuilderWriter();
		try {
			this.extract(wmlPackage, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return output.toString();
	}
	
	public String extract(WordprocessingMLPackage wmlPackage) throws Exception {
		StringBuilderWriter output = new StringBuilderWriter(); 
		try {
			this.extract(wmlPackage, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
		return output.toString();
	}
	
	public void extract(WordprocessingMLPackage wmlPackage,Writer out) throws Exception {
		MainDocumentPart documentPart = wmlPackage.getMainDocumentPart();		
		org.docx4j.wml.Document wmlDocumentEl = documentPart.getContents();
		TextUtils.extractText(wmlDocumentEl, out);
		//out.flush();
		//out.close();
	}
	
}
