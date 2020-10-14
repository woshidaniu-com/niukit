/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.nodes.Document;

import com.woshidaniu.fastdoc.docx4j.DataMap;

/**
 * *******************************************************************
 * @className	： XHTMLImporterHandler
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Dec 30, 2016 9:03:30 AM
 * @version 	V1.0 
 * *******************************************************************
 */

public interface XHTMLImporterHandler {

	void handle(WordprocessingMLPackage wmlPackage,File htmlFile,boolean fragment) throws IOException, Docx4JException;
	
	void handle(WordprocessingMLPackage wmlPackage,String html,boolean fragment) throws IOException, Docx4JException;
	
	void handle(WordprocessingMLPackage wmlPackage,URL url,boolean fragment) throws IOException, Docx4JException;
	
	void handle(WordprocessingMLPackage wmlPackage, String url, DataMap dataMap,boolean fragment) throws IOException, Docx4JException;
	
	void handle(WordprocessingMLPackage wmlPackage, InputStream input,boolean fragment) throws IOException, Docx4JException;
	
	void handle(WordprocessingMLPackage wmlPackage,Document doc,boolean fragment) throws IOException, Docx4JException;
	
	
}
