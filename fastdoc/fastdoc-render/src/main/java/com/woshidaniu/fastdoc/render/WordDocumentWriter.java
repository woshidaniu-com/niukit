package com.woshidaniu.fastdoc.render;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.fastdoc.core.utils.ExtensionUtils;
import com.woshidaniu.io.utils.FileUtils;



public abstract class WordDocumentWriter {


	/**
	 * 
	 * @description: 将HWPFDocument写入doc文件
	 * @author : kangzhidong
	 * @date 下午01:55:41 2015-3-6 
	 * @param document
	 * @param outFilePath
	 * @throws IOException
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void writeToLocal(HWPFDocument document,String outFilePath) throws  IOException{
		Assert.notNull(document, " document is not specified!");
		Assert.notNull(outFilePath, " outFilePath is not specified!");
		writeToFile(document, new File(outFilePath));
	}
	
	public static void writeToClent(HWPFDocument document,HttpServletResponse response) throws IOException{
		writeToStream(document, response.getOutputStream());
	}
	
	public static void writeToFile(HWPFDocument document,File outFile) throws IOException {
		writeToStream(document, new FileOutputStream(outFile));
	}
	
	public static void writeToStream(HWPFDocument document,OutputStream os) throws IOException {
		Assert.notNull(document, " document is not specified!");
		Assert.notNull(os, " os is not specified!");
		try {
			document.write(os);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
	
	/**
	 * 
	 * @description: 将XWPFDocument写入docx文件
	 * @author : kangzhidong
	 * @date 下午01:55:41 2015-3-6 
	 * @param document
	 * @param outFilePath
	 * @throws IOException
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void writeToLocal(XWPFDocument document,String outFilePath) throws  IOException{
		Assert.notNull(document, " document is not specified!");
		Assert.notNull(outFilePath, " outFilePath is not specified!");
		writeToFile(document, new File(outFilePath));
	}
	
	public static void writeToClent(XWPFDocument document,HttpServletResponse response) throws IOException{
		writeToStream(document, response.getOutputStream());
	}
	
	public static void writeToFile(XWPFDocument document,File outFile) throws IOException {
		writeToStream(document, new FileOutputStream(outFile));
	}
	
	public static void writeToStream(XWPFDocument document,OutputStream os) throws IOException {
		Assert.notNull(document, " document is not specified!");
		Assert.notNull(os, " os is not specified!");
		try {
			document.write(os);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}
	
	public void wordToText(WordExtractor extactor, String outFilePath) throws IOException {
		Assert.notNull(extactor, " extactor is not specified!");
		Assert.notNull(outFilePath, " outFilePath is not specified!");
		this.wordToText(extactor, new File(outFilePath));
	}
	
	public void wordToText(WordExtractor extactor, File outFile) throws IOException {
		Assert.notNull(extactor, " extactor is not specified!");
		Assert.notNull(outFile, " outFile is not specified!");
		FileWriter writer = null;
		try {
			writer = new FileWriter(outFile);
			writer.write(extactor.getText().trim());
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
	public void wordToText(XWPFWordExtractor extactor, String outFilePath) throws IOException {
		Assert.notNull(extactor, " extactor is not specified!");
		Assert.notNull(outFilePath, " outFilePath is not specified!");
		this.wordToText(extactor, new File(outFilePath));
	}
	
	public void wordToText(XWPFWordExtractor extactor, File outFile) throws IOException {
		Assert.notNull(extactor, " extactor is not specified!");
		Assert.notNull(outFile, " outFile is not specified!");
		FileWriter writer = null;
		try {
			writer = new FileWriter(outFile);
			writer.write(extactor.getText().trim());
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
	public void writePictureToLoacal(String inFilePath,String suffix) throws IOException {
		Assert.notNull(inFilePath, " inFilePath is not specified!");
		Assert.notNull(suffix, " suffix is not specified!");
		if(FileUtils.isExists(inFilePath)){
			//获取所图片
			if(ExtensionUtils.isDoc(inFilePath)){
				File inFile = new File(inFilePath);
				HWPFDocument doc = WordDocumentReader.getDocument(inFile);
				this.writePictureToLoacal(doc, inFile.getParentFile(), inFile.getName(), suffix);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void writePictureToLoacal(HWPFDocument doc,File outFileDir,String preffix,String suffix) throws IOException {
		//建立图片文件目录
		FileUtils.createDir(outFileDir);
		//得到word的数据流
	    byte[] dataStream = doc.getDataStream();
	    Range range = doc.getRange();
	    int numChar = range.numCharacterRuns();
		PicturesTable pTable = new PicturesTable(doc, dataStream, dataStream, null, null);
		for(int index = 0; index < numChar; ++index){
		    CharacterRun cRun = range.getCharacterRun(index);     
		    //判断是否有图片
		    boolean has = pTable.hasPicture(cRun);
		    if(has){
			    Picture pic =  pTable.extractPicture(cRun, true);
			    FileOutputStream fo = null;
            	try {
            		fo = new FileOutputStream(new File(outFileDir, preffix+index+suffix));
	        		pic.writeImageContent(fo);
	        	} finally {
	        		IOUtils.closeQuietly(fo);
				}
		     }
	    }
	}
	
	public void writePictureToLoacal2(String inFilePath,String suffix) throws IOException {
		if(FileUtils.isExists(inFilePath)){
			//获取所图片
			if(ExtensionUtils.isDoc(inFilePath)){
				File inFile = new File(inFilePath);
				XWPFDocument doc = WordDocumentReader.getDocumentx(inFile);
				this.writePictureToLoacal(doc, inFile.getParentFile(), inFile.getName(), suffix);
			}
		}
	}
	
	public void writePictureToLoacal(XWPFDocument doc, File dir,String preffix,String suffix) throws IOException {
		//得到word的数据流
		List<XWPFPictureData> picList = doc.getAllPictures();
        for(int index = 0; index < picList.size(); ++index){
        	FileOutputStream fo = null;
        	try {
        		fo = new FileOutputStream(new File(dir, preffix+index+suffix));
        		fo.write(picList.get(index).getData());
        	} finally {
        		IOUtils.closeQuietly(fo);
			}
		}
	}
	
}
