package com.woshidaniu.fastdoc.render;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToFoConverter;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToTextConverter;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.fastdoc.core.utils.ExtensionUtils;


/**
 * 
 *@类名称:WordDocumentConverter.java
 *@类描述：word文档转换
 *@创建人：kangzhidong
 *@创建时间：2015-3-6 上午11:40:46
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public abstract class WordDocumentConverter  {


	public static String wordToText(String filePath) throws IOException {
		Assert.isTrue(ExtensionUtils.isDoc(filePath), " file is not *.doc document !");
		return wordToText(WordDocumentReader.getDocument(filePath));
	}
	
	public static String wordToText(File docFile) throws IOException {
		Assert.isTrue(ExtensionUtils.isDoc(docFile), " file is not *.doc document !");
		return wordToText(WordDocumentReader.getDocument(docFile));
	}
	
	public static String wordToText(byte[] docBytes) throws IOException {
		return wordToText(WordDocumentReader.getDocument(docBytes));
	}

	public static String wordToText(HWPFDocument doc) throws IOException {
		try {
			StringWriter writer = new StringWriter();
			WordToTextConverter converter = new WordToTextConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());  
			// 对HWPFDocument进行转换
			converter.processDocument(doc);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			// 是否添加空格
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "text");
			transformer.transform(new DOMSource(converter.getDocument() ), new StreamResult( writer ) );  
			return writer.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			return null;
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String wordToHtml(String filePath) throws IOException {
		Assert.isTrue(ExtensionUtils.isDoc(filePath), " file is not *.doc document !");
		return wordToHtml(WordDocumentReader.getDocument(filePath));
	}
	
	public static String wordToHtml(File docFile) throws IOException {
		Assert.isTrue(ExtensionUtils.isDoc(docFile), " file is not *.doc document !");
		return wordToHtml(WordDocumentReader.getDocument(docFile));
	}
	
	public static String wordToHtml(byte[] docBytes) throws IOException {
		return wordToHtml(WordDocumentReader.getDocument(docBytes));
	}

	public static String wordToHtml(HWPFDocument doc) throws IOException {
		try {
			StringWriter writer = new StringWriter();
			WordToHtmlConverter converter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());  
			// 对HWPFDocument进行转换
			converter.processDocument(doc);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			// 是否添加空格
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			transformer.transform(new DOMSource(converter.getDocument() ), new StreamResult( writer ) );  
			return writer.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			return null;
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String wordToFoxml(String filePath) throws IOException {
		Assert.isTrue(ExtensionUtils.isDoc(filePath), " file is not *.doc document !");
		return wordToFoxml(WordDocumentReader.getDocument(filePath));
	}
	
	public static String wordToFoxml(File docFile) throws IOException {
		Assert.isTrue(ExtensionUtils.isDoc(docFile), " file is not *.doc document !");
		return wordToFoxml(WordDocumentReader.getDocument(docFile));
	}
	
	public static String wordToFoxml(byte[] docBytes) throws IOException {
		return wordToFoxml(WordDocumentReader.getDocument(docBytes));
	}

	public static String wordToFoxml(HWPFDocument doc) throws IOException {
		try {
			StringWriter writer = new StringWriter();
			WordToFoConverter  converter = new WordToFoConverter (DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());  
			// 对HWPFDocument进行转换
			converter.processDocument(doc);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			// 是否添加空格
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.transform(new DOMSource(converter.getDocument() ), new StreamResult( writer ) );  
			return writer.getBuffer().toString();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
			return null;
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
