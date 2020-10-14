package com.woshidaniu.fastxls.jexcel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import jxl.JXLException;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableWorkbook;


public abstract class JXLWorkbookWriter{

	
	public static void writerToLocal(Workbook wb, String filepath) throws JXLException, IOException {
		//1.避免乱码的设置
	    WorkbookSettings setting = new WorkbookSettings();
	    java.util.Locale locale = new java.util.Locale("zh","CN");
	    setting.setLocale(locale);
	    setting.setEncoding("ISO-8859-1");
	    //2.创建可读写的副本
	    WritableWorkbook writer = Workbook.createWorkbook(new File(filepath),wb,setting);
		writer.write();
		writer.close();//一定要关闭, 否则没有保存Excel
	}

	
	public static void writerToLocal(Workbook wb, OutputStream os) throws JXLException, IOException {
		//1.避免乱码的设置
	    WorkbookSettings setting = new WorkbookSettings();
	    java.util.Locale locale = new java.util.Locale("zh","CN");
	    setting.setLocale(locale);
	    setting.setEncoding("ISO-8859-1");
	    //2.创建可读写的副本
	    WritableWorkbook writer = Workbook.createWorkbook(os,wb,setting);
		writer.write();
		writer.close();//一定要关闭, 否则没有保存Excel
		wb.close();
		os.close();
	}

	
	public static void writerToClient(Workbook wb, HttpServletResponse response) throws JXLException, IOException {
		//1.避免乱码的设置
	    WorkbookSettings setting = new WorkbookSettings();
	    java.util.Locale locale = new java.util.Locale("zh","CN");
	    setting.setLocale(locale);
	    setting.setEncoding("ISO-8859-1");
	    //2.创建可读写的副本
	    WritableWorkbook writer = Workbook.createWorkbook(response.getOutputStream(),wb,setting);
		writer.write();
		writer.close();//一定要关闭, 否则没有保存Excel
		wb.close();
	}

	
	
}
