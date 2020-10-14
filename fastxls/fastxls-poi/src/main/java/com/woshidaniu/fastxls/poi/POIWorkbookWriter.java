package com.woshidaniu.fastxls.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.io.utils.IOUtils;
/**
 * 
 *@类名称	: POIWorkbookWriter.java
 *@类描述	：Workbook写操作方法对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 9:01:02 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class POIWorkbookWriter {


	/**
	 * 
	 *@描述：将Workbook写入Excel文件
	 *@创建人:kangzhidong
	 *@创建时间:2015-3-5上午09:07:31
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param workbook
	 *@param outFilePath
	 *@throws IOException
	 */
	public static void writeToLocal(Workbook workbook,String outFilePath) throws  IOException{
		Assert.notNull(workbook, " workbook is not specified!");
		Assert.notNull(outFilePath, " outFilePath is not specified!");
		writeToFile(workbook, new File(outFilePath));
	}
	
	public static void writeToClent(Workbook workbook,HttpServletResponse response) throws IOException{
		writeToStream(workbook, response.getOutputStream());
	}
	
	public static void writeToFile(Workbook workbook,File outFile) throws IOException {
		writeToStream(workbook, new FileOutputStream(outFile));
	}
	
	public static void writeToStream(Workbook workbook,OutputStream os) throws IOException {
		Assert.notNull(workbook, " workbook is not specified!");
		Assert.notNull(os, " os is not specified!");
		try {
			workbook.write(os);
		} finally {
			IOUtils.closeQuietly(os);
		}
	}

}
