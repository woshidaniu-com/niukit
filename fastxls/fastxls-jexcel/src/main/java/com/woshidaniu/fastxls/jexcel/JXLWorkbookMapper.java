package com.woshidaniu.fastxls.jexcel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;

public abstract class JXLWorkbookMapper{

	
	public static Map<String,List<Map<String, String>>> mapper(String filepath) throws JXLException,IOException {
		return mapper(filepath, 0);
	}
	
	public static Map<String,List<Map<String, String>>> mapper(String filepath,int startRow) throws JXLException,IOException {
		Workbook wb = JXLWorkbookReader.getWorkbook(filepath);
		Map<String, List<Map<String, String>>> dataMap = null;
		if(wb.getNumberOfSheets()>0){
			dataMap = new HashMap<String, List<Map<String,String>>>(wb.getNumberOfSheets());
			for (String name : wb.getSheetNames()) {
				dataMap.put(name, mapper(wb.getSheet(name),startRow));
			}
		}
		return dataMap;
	}
	
	public static Map<String,List<Map<String, String>>> mapper(Workbook wb) throws JXLException,IOException {
		return mapper(wb, 0);
	}
	
	public static Map<String,List<Map<String, String>>> mapper(Workbook wb,int startRow) throws JXLException,IOException {
		Map<String, List<Map<String, String>>> dataMap = null;
		if(wb.getNumberOfSheets()>0){
			dataMap = new HashMap<String, List<Map<String,String>>>(wb.getNumberOfSheets());
			for (String name : wb.getSheetNames()) {
				dataMap.put(name, mapper(wb.getSheet(name),startRow));
			}
		}
		return dataMap;
	}
	
	public static List<Map<String, String>> mapper(Sheet sheet){
		return mapper(sheet,0);
	}
	
	public static List<Map<String, String>> mapper(Sheet sheet,int startRowNum){
		List<Map<String, String>> resultSheet = null;
		if(sheet!=null&&sheet.getRows()>0&&(startRowNum<sheet.getRows()&&startRowNum>-1)){
			resultSheet = new ArrayList<Map<String,String>>(sheet.getRows());
			for (int i = startRowNum; i < sheet.getRows(); i++) {
				resultSheet.add(mapper(sheet.getRow(i)));
			}
		}
		return resultSheet;
	}
	
	public static Map<String, String> mapper(Cell[] row){
		Map<String, String> resultRow = null;
		if(row!=null&&row.length>0){
			resultRow = new HashMap<String, String>(row.length);
			for (Cell cell : row) {
				String content = "";
				if (cell == null){
					content = "";
				} else {
					content = cell.getContents();
				}
				resultRow.put(cell.getColumn()+"", content);
			}
		}
		return resultRow;
	}

}
