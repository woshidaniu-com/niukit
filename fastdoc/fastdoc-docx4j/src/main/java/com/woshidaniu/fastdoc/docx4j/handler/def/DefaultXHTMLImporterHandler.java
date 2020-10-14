/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.handler.def;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.docx4j.Docx4jProperties;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

import com.woshidaniu.fastdoc.docx4j.Docx4jConstants;
import com.woshidaniu.fastdoc.docx4j.handler.XHTMLImporterHandlerAdapter;
import com.woshidaniu.fastdoc.docx4j.DataMap;

/**
 * *******************************************************************
 * @className	： DefaultXHTMLImporterHandler
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Dec 30, 2016 9:17:40 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class DefaultXHTMLImporterHandler extends XHTMLImporterHandlerAdapter {

	/**
	 * <p>Jsoup.parse(File in, String charsetName) : 它使用文件的路径做为 baseUri。 这个方法适用于如果被解析文件位于网站的本地文件系统，且相关链接也指向该文件系统</p>
	 * <p>Jsoup.parse(File in, String charsetName, String baseUri) : 这个方法用来加载和解析一个HTML文件。如在加载文件的时候发生错误，将抛出IOException，应作适当处理。
	 *	baseUri 参数用于解决文件中URLs是相对路径的问题。如果不需要可以传入一个空的字符串。</p>
	 */
	@Override
	public void handle(WordprocessingMLPackage wmlPackage, File htmlFile,boolean fragment) throws IOException, Docx4JException {
		//获取Jsoup参数
		String charsetName = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_CHARSETNAME, Docx4jConstants.DEFAULT_CHARSETNAME );
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI);
		//使用Jsoup将html转换成Document对象
		Document doc = Jsoup.parse(htmlFile, charsetName, baseUri );
		//调用Document处理函数
		this.handle(wmlPackage, doc, fragment);
	}

	/**
	 * Jsoup.parse(html)
	 * Jsoup.parse(html, baseUri)
	 * Jsoup.parseBodyFragment(bodyHtml)
	 * Jsoup.parseBodyFragment(bodyHtml, baseUri)
	 */
	@Override
	public void handle(WordprocessingMLPackage wmlPackage, String html,boolean fragment) throws IOException, Docx4JException{
		//获取Jsoup参数
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI);
		//使用Jsoup将html转换成Document对象
		Document doc = fragment ? Jsoup.parseBodyFragment( html, baseUri) : Jsoup.parse( html,baseUri);
		//调用Document处理函数
		this.handle(wmlPackage, doc, fragment);
	}
	
	/**
	 * Jsoup.parse(String url, int timeoutMillis)
	 * Jsoup.connect(String url) 方法创建一个新的 Connection, 和  post() 取得和解析一个HTML文件。如果从该URL获取HTML时发生错误，便会抛出 IOException，应适当处理。
	 * 这两个方法只支持Web URLs (http和https 协议); 
	 */
	@Override
	public void handle(WordprocessingMLPackage wmlPackage,URL url,boolean fragment) throws IOException, Docx4JException{
		//获取Jsoup参数
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI);
		int timeout = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_TIMEOUTMILLIS, Docx4jConstants.DEFAULT_TIMEOUTMILLIS);
		//fetch the specified URL and parse to a HTML DOM
		Document doc = Jsoup.parse(url,timeout);
		doc.setBaseUri(baseUri);
		//调用Document处理函数
		this.handle(wmlPackage, doc, fragment);
	}
	
	/**
	 * Jsoup.parse(String url, int timeoutMillis)
	 * Jsoup.connect(String url) 方法创建一个新的 Connection, 和  post() 取得和解析一个HTML文件。如果从该URL获取HTML时发生错误，便会抛出 IOException，应适当处理。
	 * 这两个方法只支持Web URLs (http和https 协议); 
	 */
	@Override
	public void handle(WordprocessingMLPackage wmlPackage,String url, DataMap dataMap,boolean fragment) throws IOException, Docx4JException{
		//获取Jsoup参数
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI);
		String userAgent = "Mozilla/5.0 (jsoup)";
		int timeout = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_TIMEOUTMILLIS, Docx4jConstants.DEFAULT_TIMEOUTMILLIS);
		//fetch the specified URL and parse to a HTML DOM
		Document doc = Jsoup.connect(url)
				  .data(dataMap.getData1())
				  .data(dataMap.getData2())
				  .userAgent(userAgent)
				  .cookies(dataMap.getCookies())
				  .timeout(timeout)
				  .post();
		doc.setBaseUri(baseUri);
		//调用Document处理函数
		this.handle(wmlPackage, doc, fragment);
	}
	
	/**
	 * Jsoup.parse(in, charsetName, baseUri)
	 */
	@Override
	public void handle(WordprocessingMLPackage wmlPackage, InputStream input,boolean fragment) throws IOException, Docx4JException{
		//获取Jsoup参数
		String charsetName = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_CHARSETNAME, Docx4jConstants.DEFAULT_CHARSETNAME );
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI);
		//使用Jsoup将html转换成Document对象
		Document doc = Jsoup.parse(input, charsetName, baseUri);
		//调用Document处理函数
		this.handle(wmlPackage, doc, fragment);
	}

	@Override
	public void handle(WordprocessingMLPackage wmlPackage, Document doc,boolean fragment) throws IOException, Docx4JException {
		//设置转换模式
		doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);  //转为 xhtml 格式
		//创建html导入对象
		XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wmlPackage);
		//将xhtml转换为wmlPackage可用的对象
		List<Object> list = xhtmlImporter.convert((fragment ? doc.body().html() : doc.html()), doc.baseUri());
		//导入转换后的内容对象
		wmlPackage.getMainDocumentPart().getContent().addAll(list);
	}

}
