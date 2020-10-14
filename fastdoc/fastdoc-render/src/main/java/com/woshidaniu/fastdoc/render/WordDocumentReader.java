package com.woshidaniu.fastdoc.render;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.woshidaniu.basicutils.Assert;


/**
 * 
 *@类名称:WordDocumentReader.java
 *@类描述：word文档读取对象
 *@创建人：kangzhidong
 *@创建时间：2015-3-6 上午11:00:35
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public abstract class WordDocumentReader  {

	/**
	 * 
	 * @description: 加载 *.doc word文档对象
	 * @author : kangzhidong
	 * @date 上午10:39:57 2015-3-6 
	 * @param inFilePath
	 * @return
	 * @throws IOException
	 * @return  HWPFDocument 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static HWPFDocument getDocument(String filePath) throws IOException {
		Assert.notNull(filePath, " filePath is not specified!");
		return getDocument(new File(filePath));
	}
	
	/**
	 * 
	 * @description:  加载 *.doc word文档对象
	 * @author : kangzhidong
	 * @date 上午10:40:27 2015-3-6 
	 * @param docFile
	 * @return
	 * @throws IOException
	 * @return  HWPFDocument 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static HWPFDocument getDocument(File docFile) throws IOException {
		Assert.notNull(docFile, " docFile is not specified!");
		Assert.isTrue(docFile.exists(), " docFile is not found !");
		Assert.isTrue(docFile.isFile(), " docFile is not a file !");
		InputStream inputStream = null;
		try {
			 //载入文档 
			inputStream = new FileInputStream(docFile);
			return new HWPFDocument(inputStream);   
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * 
	 * @description: 加载 *.doc word文档对象
	 * @author : kangzhidong
	 * @date 上午10:52:13 2015-3-6 
	 * @param docBytes
	 * @return
	 * @throws IOException
	 * @return  XWPFDocument 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static HWPFDocument getDocument(byte[] docBytes) throws IOException {
		Assert.notNull(docBytes, " docBytes is not specified!");
		InputStream inputStream = null;
		try {
			 //载入文档 
			inputStream =  new ByteArrayInputStream(docBytes);
			return new HWPFDocument(inputStream);   
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * 
	 * @description:  加载 *.docx word文档对象
	 * @author : kangzhidong
	 * @date 上午10:40:41 2015-3-6 
	 * @param inFilePath
	 * @return
	 * @throws IOException
	 * @return  XWPFDocument 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static XWPFDocument getDocumentx(String inFilePath) throws IOException {
		return getDocumentx(new File(inFilePath));
	}
	
	/**
	 * 
	 * @description: 加载 *.docx word文档对象
	 * @author : kangzhidong
	 * @date 上午10:40:52 2015-3-6 
	 * @param docxFile
	 * @return
	 * @throws IOException
	 * @return  XWPFDocument 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static XWPFDocument getDocumentx(File docxFile) throws IOException {
		Assert.notNull(docxFile, " docxFile is not specified!");
		Assert.isTrue(docxFile.exists(), " docxFile is not found !");
		Assert.isTrue(docxFile.isFile(), " docxFile is not a file !");
		InputStream inputStream = null;
		try {
			 //载入文档 
			inputStream = new FileInputStream(docxFile);
			return new XWPFDocument(inputStream);   
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * 
	 * @description: 加载 *.docx word文档对象
	 * @author : kangzhidong
	 * @date 上午10:52:13 2015-3-6 
	 * @param docBytes
	 * @return
	 * @throws IOException
	 * @return  XWPFDocument 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static XWPFDocument getDocumentx(byte[] docBytes) throws IOException {
		Assert.notNull(docBytes, " docBytes is not specified!");
		InputStream inputStream = null;
		try {
			 //载入文档 
			inputStream =  new ByteArrayInputStream(docBytes);
			return new XWPFDocument(inputStream);   
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	
	/**
	 * 
	 * @description:得到 *.doc word文档内容提取对象
	 * @author : kangzhidong
	 * @date 上午10:41:06 2015-3-6 
	 * @param doc
	 * @return
	 * @return  WordExtractor 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	
	public static WordExtractor getDocumentExtractor(String filePath) throws IOException{
		return new WordExtractor(getDocument(filePath));
	}
	
	public static WordExtractor getDocumentExtractor(File docFile) throws IOException{
		return new WordExtractor(getDocument(docFile));
	}
	
	public static WordExtractor getDocumentExtractor(byte[] docBytes) throws IOException{
		return new WordExtractor(getDocument(docBytes));
	}

	public static WordExtractor getDocumentExtractor(HWPFDocument doc){
		return new WordExtractor(doc);
	}
	
	/**
	 * 
	 * @description: 得到 *.docx word文档内容提取对象
	 * @author : kangzhidong
	 * @date 上午10:41:34 2015-3-6 
	 * @param doc
	 * @return
	 * @return  XWPFWordExtractor 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	
	public static XWPFWordExtractor getDocumentxExtractor(String filePath) throws IOException{
		return new XWPFWordExtractor(getDocumentx(filePath));
	}
	
	public static XWPFWordExtractor getDocumentxExtractor(File docFile) throws IOException{
		return new XWPFWordExtractor(getDocumentx(docFile));
	}
	
	public static XWPFWordExtractor getDocumentxExtractor(byte[] docBytes) throws IOException{
		return new XWPFWordExtractor(getDocumentx(docBytes));
	}
	
	public static XWPFWordExtractor getDocumentxExtractor(XWPFDocument doc) {
		return new XWPFWordExtractor(doc);
	}
	public static void main(String[] args) {
		/*try {
			HWPFDocument document = WordDocumentReader.getDocument(new File("d:/1111.doc"));
			
			document.getRange().insertAfter("<p>开发网站的过程中，我们经常遇到某些耗时很长的javascript操作。其中，既有异步的操作（比如ajax读取服务器数据），也有同步的操作（比如遍历一个大型数组），它们都不是立即能得到结果的。</p>");
			document.getRange().insertAfter("<p>开发网站的过程中，我们经常遇到某些耗时很长的javascript操作。其中，既有异步的操作（比如ajax读取服务器数据），也有同步的操作（比如遍历一个大型数组），它们都不是立即能得到结果的。</p>");
			document.getRange().insertAfter("<p>开发网站的过程中，我们经常遇到某些耗时很长的javascript操作。其中，既有异步的操作（比如ajax读取服务器数据），也有同步的操作（比如遍历一个大型数组），它们都不是立即能得到结果的。</p>");
			document.getRange().insertAfter("<p>开发网站的过程中，我们经常遇到某些耗时很长的javascript操作。其中，既有异步的操作（比如ajax读取服务器数据），也有同步的操作（比如遍历一个大型数组），它们都不是立即能得到结果的。</p>");
			
			WordDocumentWriter.writeToFile(document, new File("d:/1112.doc"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
}
