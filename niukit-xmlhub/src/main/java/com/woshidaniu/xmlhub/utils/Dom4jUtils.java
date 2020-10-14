package com.woshidaniu.xmlhub.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.xmlhub.core.CharEncoding;

/**
 * 
 *@类名称:Dom4jUtils.java
 *@类描述：XML解析实用工具类
 *@创建人：
 *@创建时间：2015-3-31 上午09:39:43
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public abstract class Dom4jUtils{
	
	protected static Logger LOG = LoggerFactory.getLogger(Dom4jUtils.class);
	private static final SAXReader reader = new SAXReader();

	/**
	 * 
	 * @description: 把传进来的Xml字符串转换成dom4j的Document对象
	 * @author : kangzhidong
	 * @date 2015-3-31 
	 * @time 上午09:45:14
	 * @param xmlString
	 * @return
	 * @throws DocumentException
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public static Document parse(String xmlString) throws DocumentException {
		return DocumentHelper.parseText(xmlString);
	}

	/**
	 * 
	 * @description: 从文件读取XML，输入文件名，返回XML文档
	 * @author : kangzhidong
	 * @date 2015-3-31 
	 * @time 上午09:45:28
	 * @param file
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public static synchronized Document read(File file)  {
		Document document = null;
		try {
			document = reader.read(file);
		} catch (DocumentException e) {
			LOG.error("couldn't read xml file from relative path 【 '"+ file.getAbsolutePath() + "'】", e);
		}
		return document;
	}
	
	/**
	 * 
	 * @description: 从文件路径读取XML，输入文件名，返回XML文档
	 * @author : kangzhidong
	 * @date 2015-3-31 
	 * @time 上午09:45:35
	 * @param filepath
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public static synchronized Document read(String filepath)  {
		Document document = null;
		try {
			File docFile = new File(filepath);
			if(docFile.exists()){
				document = reader.read(docFile);
			}else{
				document = reader.read(getDefaultClassLoader().getResourceAsStream(filepath));
			}
		} catch (Exception e) {
			LOG.error("couldn't read xml file from relative path 【 '"+ filepath + "'】", e);
		}
		return document;
	}

	public static synchronized Document read(String locale,String filepath)  {
		Document document = null;
		String finalpath = XMLLocaleUtils.getLocalePath(locale, filepath );
		try {
			File docFile = new File(finalpath);
			if(docFile.exists()){
				document = reader.read(docFile);
			}else{
				document = reader.read(getDefaultClassLoader().getResourceAsStream(finalpath));
			}
		} catch (Exception e) {
			LOG.error("Couldn't read xml file from relative path 【 '"+ finalpath + "'】", e);
			try {
				File docFile = new File(filepath);
				if(docFile.exists()){
					document = reader.read(docFile);
				}else{
					document = reader.read(getDefaultClassLoader().getResourceAsStream(filepath));
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
			cl = Dom4jUtils.class.getClassLoader();
		}
		return cl;
	}
	
	/**
	 * 
	 * @description:从一个URL读取XML
	 * @author : kangzhidong
	 * @date 2015-3-31 
	 * @time 上午09:45:51
	 * @param url
	 * @return
	 * @modify by:
	 * @modify date :
	 * @modify description :
	 */
	public static synchronized Document read(URL url){
		Document document = null;
		try {
			document = reader.read(url);
		} catch (DocumentException e) {
			LOG.error("Couldn't read xml from URL "+ url.getPath() , e);
		}
		return document;
	}

	/**
	 * 
	 *@描述：从一个InputStream读入XML
	 *@创建人:
	 *@创建时间:2015-3-31上午09:29:16
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static synchronized Document read(InputStream is){
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
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
	public static synchronized Document read(Reader r){
		Document document = null;
		try {
			document = reader.read(r);
		} catch (DocumentException e) {
			LOG.error("Couldn't read xml from input Reader !", e);
		}
		return document;
	}

	/**
	 * 
	 *@描述：获得一个XML文档的Root Element
	 *@创建人:
	 *@创建时间:2015-3-31上午09:34:42
	 *@param doc ： Document文档
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	/**
	 * 
	 *@描述：获得一个XML文档的所有Element
	 *@创建人:
	 *@创建时间:2015-3-31上午09:35:09
	 *@param doc ： Document文档
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Iterator getAllElement(Document doc) {
		return doc.getRootElement().elementIterator();
	}

	/**
	 * 
	 *@描述：获得一个XML文档所有指定Element Name的Element  
	 *@创建人:
	 *@创建时间:2015-3-31上午09:35:28
	 *@param doc:Document文档
	 *@param elementName:要获得的Element Name
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Iterator getNamedElement(Document doc, String elementName) {
		return doc.getRootElement().elementIterator(elementName);
	}

	/**
	 * 
	 *@描述：获得一个XML文档的所有属性
	 *@创建人:
	 *@创建时间:2015-3-31上午09:35:59
	 *@param doc ：  Document文档
	 *@return Iterator
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Iterator getAllAttribute(Document doc) {
		return doc.getRootElement().attributeIterator();
	}

	/**
	 * 
	 *@描述： 获得一个XPath表达式的所有Node
	 *@创建人:
	 *@创建时间:2015-3-31上午09:36:17
	 *@param doc：Document文档
	 *@param xpathString： XPath表达式
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	
	public static List getNodes(Document doc, String xpathString) {
		return doc.selectNodes(xpathString);

	}

	/**
	 * 
	 *@描述：通过XPath表达式只获得一个Node
	 *@创建人:
	 *@创建时间:2015-3-31上午09:36:40
	 *@param doc：Document文档
	 *@param xpathString： XPath表达式
	 *@return:Node
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Node getNode(Document doc, String xpathString) {
		return doc.selectSingleNode(xpathString);
	}

	/**
	 * 
	 *@描述：通过XPath表达式获得一个Node的值
	 *@创建人:
	 *@创建时间:2015-3-31上午09:37:10
	 *@param node
	 *@param xpathString
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static String getNodeValue(Node node, String xpathString) {
		return node.valueOf(xpathString);
	}

	/**
	 * 
	 *@描述：通过XPath表达式获得一个值
	 *@创建人:
	 *@创建时间:2015-3-31上午09:37:18
	 *@param doc
	 *@param xpathString
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static String getValue(Document doc, String xpathString) {
		return doc.valueOf(xpathString);
	}

	/**
	 * 
	 *@描述：计算某一个Element在XML文档的个数
	 *@创建人:
	 *@创建时间:2015-3-31上午09:37:30
	 *@param doc:Document文档
	 *@param elementName:Element名字
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static int count(Document doc, String elementName) {
		return Integer.parseInt(doc.valueOf("count(//" + elementName + ")"));
	}

	/**
	 * 
	 *@描述：将XML文件的内容转化为String
	 *@创建人:
	 *@创建时间:2015-3-31上午09:37:51
	 *@param doc
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static String doc2String(Document doc) {
		String str = "";
		// 使用输出流来进行转化
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// 使用GBK编码
		OutputFormat format = new OutputFormat("  ", true, "GBK");
		XMLWriter writer;
		try {
			writer = new XMLWriter(out, format);
			writer.write(doc);
			str = out.toString("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 
	 *@描述：格式化XML文档,并解决中文问题
	 *@创建人:
	 *@创建时间:2015-3-31上午09:38:44
	 *@param doc
	 *@param filename
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static int doc2XMLFile(Document doc, String filename) {
		int returnValue = 0;
		try {
			/** 格式化输出,类型IE浏览一样 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 指定XML编码 */
			format.setEncoding("GBK");
			XMLWriter writer = new XMLWriter(new FileWriter(new File(filename)), format);
			
			writer.write(doc);
			writer.close();
			
			/** 执行成功,需返回1 */
			returnValue = 1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnValue;
	}
	
	// Convert a Document to a String with pretty format.
    public static String createPrettyFormat(org.w3c.dom.Document document) throws Exception {
        OutputFormat format = OutputFormat.createPrettyPrint();
        return createXmlString(document, format);
    }

    // Convert a Document to a String with compact format.
    public static String createCompactFormat(org.w3c.dom.Document document) throws Exception {
        OutputFormat format = OutputFormat.createCompactFormat();
        return createXmlString(document, format);
    }

    private static String createXmlString(org.w3c.dom.Document document, OutputFormat format) throws Exception {
        format.setEncoding(CharEncoding.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter(outputStream, format);

        DOMReader reader = new DOMReader();
        writer.write(reader.read(document));

        return outputStream.toString(CharEncoding.UTF_8);
    }
}