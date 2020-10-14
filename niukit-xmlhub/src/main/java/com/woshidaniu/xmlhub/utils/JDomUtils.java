 package com.woshidaniu.xmlhub.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * 
 *@类名称	: JDomUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 6:56:01 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class JDomUtils  {
	
	protected static Logger LOG = LoggerFactory.getLogger(JDomUtils.class);
	//（1）使用JDOM首先要指定使用什么解析器。如： 这表示使用的是默认的解析器
	private static final SAXBuilder builder = new SAXBuilder();
	//xml输出对象
	private static XMLOutputter outputter = null;
	
	static{
		Format format = Format.getPrettyFormat();  
		//设置xml文件的字符为UTF-8，解决中文问题 
	    format.setEncoding("UTF-8");
		outputter = new XMLOutputter(format);
	}
	
	public static Document buildXML(String xmlDoc) {
        //创建一个新的字符串
        StringReader read = new StringReader(xmlDoc);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        try {
            //通过输入源构造一个Document
        	return  builder.build(source);
        } catch (Exception e) {
        	LOG.error("Couldn't read xml string ", e);
        }
        return null;
    }
	
	public static Document build(String filepath) {
		Document document = null;
		try {
			File docFile = new File(filepath);
			if(docFile.exists()){
				document = builder.build(docFile);
			}else{
				document = builder.build(getDefaultClassLoader().getResourceAsStream(filepath));
			}
		} catch (Exception e) {
			LOG.error("Couldn't read xml file from relative path 【 '"+ filepath + "'】", e);
		}
		return document;
	}
	
	public static Document build(String locale,String filepath)  {
		Document document = null;
		String finalpath = XMLLocaleUtils.getLocalePath(locale, filepath );
		try {
			File docFile = new File(finalpath);
			if(docFile.exists()){
				document = builder.build(docFile);
			}else{
				document = builder.build(getDefaultClassLoader().getResourceAsStream(finalpath));
			}
		} catch (Exception e) {
			LOG.error("Couldn't read xml file from relative path 【 '"+ finalpath + "'】", e);
			try {
				File docFile = new File(filepath);
				if(docFile.exists()){
					document = builder.build(docFile);
				}else{
					document = builder.build(getDefaultClassLoader().getResourceAsStream(filepath));
				}
			} catch (Exception e1) {
				LOG.error("Couldn't read default xml file from relative path 【 '"+ filepath + "'】", e);
			}
		}
		return document;
	}
	
	protected static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = JDomUtils.class.getClassLoader();
		}
		return cl;
	}
	
	public static Document build(File file) {
		Document document = null;
		try {
			document = builder.build(file);
		} catch (Exception e) {
			LOG.error("Couldn't read xml file from relative path 【 '"+ file.getAbsolutePath() + "'】", e);
		}
		return document;
	}
	
	public static Document build(InputStream in) {
		Document document = null;
		try {
			document = builder.build(in);
		} catch (Exception e) {
			LOG.error("Couldn't read xml from input stream !", e);
		}
		return document;
	}
	
	/**
	 * 
	 *@描述：从一个InputStream读入XML
	 *@创建人:
	 *@创建时间:2015-3-31上午09:29:09
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Document build(Reader r){
		Document document = null;
		try {
			document = builder.build(r);
		} catch (Exception e) {
			LOG.error("Couldn't read xml from input Reader !", e);
		}
		return document;
	}
	
	/**
	 * 
	 *@描述：从一个URL读取XML
	 *@创建人:kangzhidong
	 *@创建时间:2015-3-31上午10:19:18
	 *@param url
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Document build(URL url){
		Document document = null;
		try {
			document = builder.build(url);
		} catch (Exception e) {
			LOG.error("Couldn't read xml from URL "+ url.getPath(), e);
		}
		return document;
	}
	
	/**
	 * 
	 *@描述：保存Document的修改到XML文件中：
	 *@创建人:kangzhidong
	 *@创建时间:2015-3-31上午10:21:32
	 *@param doc
	 *@param filepath
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static void outputToFile(Document doc, String filepath) throws IOException  {
		outputToFile(doc, new File(filepath));
	}
	
	public static void outputToFile(Document doc, File file) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			outputToStream(doc, out);
		}finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	public static void outputToStream(Document doc, OutputStream out) throws IOException {
		try {
			outputter.output(doc,out);
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	 
	public static void outputToWriter(Document doc, Writer out) throws IOException {
		try {
			outputter.output(doc,out);
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	public static String outputToString(Document doc) throws IOException {
		return outputter.outputString(doc);
	}
	
	public static void outputToFile(DocType doctype, String filepath) throws IOException  {
		outputToFile(doctype, new File(filepath));
	}
	
	public static void outputToFile(DocType doctype, File file) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			outputToStream(doctype, out);
		}finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	public static void outputToStream(DocType doctype, OutputStream out) throws IOException {
		try {
			outputter.output(doctype,out);
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	 
	public static void outputToWriter(DocType doctype, Writer out) throws IOException {
		try {
			outputter.output(doctype,out);
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	public static String outputToString(DocType doctype) throws IOException {
		return outputter.outputString(doctype);
	}
	
	public static void outputToFile(Element element, String filepath) throws IOException  {
		outputToFile(element, new File(filepath));
	}
	
	public static void outputToFile(Element element, File file) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			outputToStream(element, out);
		}finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	public static void outputToStream(Element element, OutputStream out) throws IOException {
		try {
			outputter.output(element,out);
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	 
	public static void outputToWriter(Element element, Writer out) throws IOException {
		try {
			outputter.output(element,out);
		} finally{
			IOUtils.closeQuietly(out);
		}
	}
	
	public static String outputToString(Element element) throws IOException {
		return outputter.outputString(element);
	}
	
	/**
	 * @return the outputter
	 */
	public static XMLOutputter getOutputter() {
		return outputter;
	}
	
	/**
	 * @return the builder
	 */
	public static SAXBuilder getBuilder() {
		return builder;
	}
	
}

