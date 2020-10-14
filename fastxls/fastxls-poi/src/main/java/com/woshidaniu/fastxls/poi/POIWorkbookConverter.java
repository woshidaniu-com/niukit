package com.woshidaniu.fastxls.poi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.eventusermodel.examples.XLS2CSVmra;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.eventusermodel.XLSX2CSV;
import org.xml.sax.SAXException;
/**
 * 
 * @className: POIWorkbookConverter
 * @description:  将 :xls,xlsx转换为csv,xls转换为xlsx,xlsx转换为xls
 * @author : kangzhidong
 * @date : 下午2:25:35 2014-11-22
 * @modify by:
 * @modify date :
 * @modify description :
 */
public abstract class POIWorkbookConverter {

	private static int minColumns = -1;
	private static POIFSFileSystem fs;
	private static OPCPackage xlsxPackage;
	private static PrintStream output;
	private static String OUTPUT_CHARSET = "GBK";
	
	public static void xls2Csv(String inputFilePath, String outputFilePath){
		try {
			fs = new POIFSFileSystem(new FileInputStream(inputFilePath));
			output = new PrintStream(outputFilePath, OUTPUT_CHARSET);
			minColumns = -1;
			xls2Csv(fs, output, minColumns);
		} catch (FileNotFoundException e) {
			
		} catch (UnsupportedEncodingException e) {
			
		} catch (IOException e) {
			
		}
	}

	public static void xls2Csv(POIFSFileSystem fs, PrintStream output, int minColumns) {
		try {
			new XLS2CSVmra(fs, output, minColumns).process();
		} catch (IOException e) {
			
		}
	}
	
	public static void xlsx2Csv(OPCPackage pkg, PrintStream output, int minColumns) {
		try {
			new XLSX2CSV(pkg, output, minColumns).process();
		} catch (Exception e) {
			
		}
	}

	public static void xlsx2Csv(String inputFilePath, String outputFilePath) {
		try {
			xlsxPackage = OPCPackage.open(inputFilePath,PackageAccess.READ);
			output = new PrintStream(outputFilePath,OUTPUT_CHARSET);
			xlsx2Csv(xlsxPackage, output,minColumns);
		} catch (InvalidFormatException e) {
			
		} catch (FileNotFoundException e) {
			
		} catch (UnsupportedEncodingException e) {
			
		}
	}

}
