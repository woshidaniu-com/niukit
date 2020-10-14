/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.ConversionHTMLScriptElementHandler;
import org.docx4j.convert.out.ConversionHTMLStyleElementHandler;
import org.docx4j.convert.out.ConversionHyperlinkHandler;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.fastdoc.docx4j.handler.OutputConversionHTMLScriptElementHandler;
import com.woshidaniu.fastdoc.docx4j.handler.OutputConversionHTMLStyleElementHandler;
import com.woshidaniu.fastdoc.docx4j.handler.OutputConversionHyperlinkHandler;
import com.woshidaniu.fastdoc.docx4j.handler.OutputDirFilterHandler;
import com.woshidaniu.fastdoc.docx4j.utils.Docx4jUtils;

/**
 *@类名称	: WordprocessingMLPackageWriter.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Dec 28, 2016 3:54:40 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class WordprocessingMLPackageWriter  {
	
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
	protected final String PDF_SUFFIX = ".pdf";
	protected final String DOCX_SUFFIX = ".docx";
	protected final ConversionHyperlinkHandler DEFAULT_HYPERLINK_HANDLER = new OutputConversionHyperlinkHandler();
	protected final ConversionHTMLStyleElementHandler DEFAULT_STYLE_ELEMENT_HANDLER = new OutputConversionHTMLStyleElementHandler();
	protected final ConversionHTMLScriptElementHandler DEFAULT_SCRIPT_ELEMENT_HANDLER = new OutputConversionHTMLScriptElementHandler();
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 docx
	 */
	public File writeToDocx(WordprocessingMLPackage wmlPackage) throws  IOException, Docx4JException{
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		File outFile = new File( Docx4jUtils.getTempPath() + DOCX_SUFFIX );
		return writeToDocx(wmlPackage, outFile);
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 docx
	 */
	public File writeToDocx(WordprocessingMLPackage wmlPackage,String outPath) throws  IOException, Docx4JException{
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(outPath, " outPath is not specified!");
		return writeToDocx(wmlPackage, new File(outPath));
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 docx
	 */
	public File writeToDocx(WordprocessingMLPackage wmlPackage,File outFile) throws IOException, Docx4JException {
		Assert.isTrue( outFile.exists() , " outFile is not founded !");
		writeToDocx(wmlPackage, new FileOutputStream(outFile));
		return outFile;
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 docx
	 */
	public void writeToDocx(WordprocessingMLPackage wmlPackage,OutputStream output) throws IOException, Docx4JException {
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(output, " output is not specified!");
        try {
        	wmlPackage.save(output , Docx4J.FLAG_SAVE_ZIP_FILE );//保存到 docx 文件
		} finally{
			IOUtils.closeQuietly(output);
        }
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 html
	 */
	public File writeToHtml(WordprocessingMLPackage wmlPackage) throws  IOException, Docx4JException{
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		File outFile = new File( Docx4jUtils.getTempPath() + PDF_SUFFIX );
		return writeToHtml(wmlPackage, outFile);
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 html
	 */
	public File writeToHtml(WordprocessingMLPackage wmlPackage,String outPath) throws  IOException, Docx4JException{
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(outPath, " outPath is not specified!");
		return writeToHtml(wmlPackage, new File(outPath));
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 html
	 */
	public File writeToHtml(WordprocessingMLPackage wmlPackage,File outFile) throws IOException, Docx4JException {
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.isTrue( outFile.exists() , " outFile is not founded !");
		OutputStream output = null;
        try {
        	String imageTargetUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_CONVERT_OUT_HTML_IMAGETARGETURI, "images");
        	File[] files = outFile.listFiles(new OutputDirFilterHandler(imageTargetUri));
        	if(files.length != 1){
        		File imageDir = new File(outFile,imageTargetUri);
        		imageDir.setWritable(true);
        		imageDir.setReadable(true);
        		imageDir.mkdir();
        	}
        	//创建文件输出流
        	output = new FileOutputStream(outFile);	
        	//创建Html输出设置
            HTMLSettings htmlSettings = Docx4J.createHTMLSettings();  
            htmlSettings.setImageDirPath(outFile.getParent());  
            htmlSettings.setImageTargetUri(imageTargetUri);  
            htmlSettings.setWmlPackage(wmlPackage);
          
            //d
            htmlSettings.setHyperlinkHandler(DEFAULT_HYPERLINK_HANDLER);
            htmlSettings.setScriptElementHandler(DEFAULT_SCRIPT_ELEMENT_HANDLER);
            htmlSettings.setStyleElementHandler(DEFAULT_STYLE_ELEMENT_HANDLER);
            
            Docx4jProperties.setProperty(Docx4jConstants.DOCX4J_PARAM_04, true);  

            //Docx4J.toHTML(settings, outputStream, flags);
            //Docx4J.toHTML(wmlPackage, imageDirPath, imageTargetUri, outputStream);
            Docx4J.toHTML(htmlSettings, output, Docx4J.FLAG_EXPORT_PREFER_XSL);  
            
		} finally{
			IOUtils.closeQuietly(output);
        }
		
		return outFile;
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 pdf
	 */
	public File writeToPDF(WordprocessingMLPackage wmlPackage) throws  IOException, Docx4JException{
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		File outFile = new File( Docx4jUtils.getTempPath() + PDF_SUFFIX );
		return writeToPDF(wmlPackage, outFile);
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 pdf
	 */
	public File writeToPDF(WordprocessingMLPackage wmlPackage,String outPath) throws  IOException, Docx4JException{
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(outPath, " outPath is not specified!");
		return writeToPDF(wmlPackage, new File(outPath));
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 pdf
	 */
	public File writeToPDF(WordprocessingMLPackage wmlPackage,File outFile) throws IOException, Docx4JException {
		Assert.isTrue( outFile.exists() , " outFile is not founded !");
		writeToPDF(wmlPackage, new FileOutputStream(outFile));
		return outFile;
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 pdf
	 */
	public void writeToPDF(WordprocessingMLPackage wmlPackage,OutputStream output) throws IOException, Docx4JException {
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(output, " output is not specified!");
        try {
			Docx4J.toPDF(wmlPackage, output); //保存到 pdf 文件
			output.flush();
		} finally{
			IOUtils.closeQuietly(output);
        }
	}
	
	/**
	 * 将 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 存为 pdf
	 */
	public void writeToPDFWhithFo(WordprocessingMLPackage wmlPackage,OutputStream output) throws IOException, Docx4JException {
		Assert.notNull(wmlPackage, " wmlPackage is not specified!");
		Assert.notNull(output, " output is not specified!");
        try {
        	
			// Font regex (optional)
			// Set regex if you want to restrict to some defined subset of fonts
			// Here we have to do this before calling createContent,
			// since that discovers fonts
			//String regex = null;
			
			// Refresh the values of DOCPROPERTY fields 
			FieldUpdater updater = new FieldUpdater(wmlPackage);
			updater.update(true);
			
			// .. example of mapping font Times New Roman which doesn't have certain Arabic glyphs
			// eg Glyph "ي" (0x64a, afii57450) not available in font "TimesNewRomanPS-ItalicMT".
			// eg Glyph "ج" (0x62c, afii57420) not available in font "TimesNewRomanPS-ItalicMT".
			// to a font which does
			PhysicalFonts.get("Arial Unicode MS"); 
	
			// FO exporter setup (required)
			// .. the FOSettings object
		    FOSettings foSettings = Docx4J.createFOSettings();
		    
			foSettings.setWmlPackage(wmlPackage);
	        foSettings.setApacheFopMime("application/pdf");
	            
			// Document format: 
			// The default implementation of the FORenderer that uses Apache Fop will output
			// a PDF document if nothing is passed via 
			// foSettings.setApacheFopMime(apacheFopMime)
			// apacheFopMime can be any of the output formats defined in org.apache.fop.apps.MimeConstants eg org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
			// FOSettings.INTERNAL_FO_MIME if you want the fo document as the result.
			//foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);
			
			// Specify whether PDF export uses XSLT or not to create the FO
			// (XSLT takes longer, but is more complete).
			
			// Don't care what type of exporter you use
			Docx4J.toFO(foSettings, output, Docx4J.FLAG_EXPORT_PREFER_XSL);
			
			// Prefer the exporter, that uses a xsl transformation
			// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);
			
			// Prefer the exporter, that doesn't use a xsl transformation (= uses a visitor)
			// faster, but not yet at feature parity
			// Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);
			   
			// Clean up, so any ObfuscatedFontPart temp files can be deleted 
			// if (wordMLPackage.getMainDocumentPart().getFontTablePart()!=null) {
			// 	wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
			// } 
			// This would also do it, via finalize() methods
			updater = null;
			foSettings = null;
			wmlPackage = null;
		} finally{
			IOUtils.closeQuietly(output);
        }
	}

}
