package com.woshidaniu.fastxls.poi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.model.PairModel;

/**
 * 
 *@类名称	: POIWorkbookVisitor.java
 *@类描述	：xls文档内容访问
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 9:01:30 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class POIWorkbookVisitor{
	

	/**
	 * 
	 *@描述：获取指定内容在当前行的单元格索引
	 *@创建人:kangzhidong
	 *@创建时间:2014-12-2上午08:30:56
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param row		：行对象
	 *@param content	：内容
	 *@return
	 */
	public static int indexOfColumn(Row row,String content){
		int index = 0;
		boolean finded = false;
		String cellVal = null;
		for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
			index = cellnum;
			Cell cell = row.getCell(cellnum);
			if(cell!=null){
				cellVal = POIWorkbookReader.getCellValue(cell, cell.getCellType());
				if( !BlankUtils.isBlank(cellVal) && cellVal.startsWith(content)){
					finded = true;
					break;
				}
			}
		}	
		return finded ? index : -1;
	}

	/**
	 * 
	 *@描述：获取sheet中指定列中指定内容所在行索引
	 *@创建人:kangzhidong
	 *@创建时间:2014-12-2上午08:32:05
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param sheet		：sheet对象
	 *@param colIndex	：列索引
	 *@param content	：内容
	 *@return
	 */
	public static int indexOfRow(Sheet sheet, int colIndex,String content) {
		int index = 0;
		boolean finded = false;
		String cellVal = null;
		for (int rownum = 0; rownum < sheet.getLastRowNum(); rownum++) {
			index = rownum;
			Cell cell =  sheet.getRow(rownum).getCell(colIndex);
			if(cell!=null){
				cellVal = POIWorkbookReader.getCellValue(cell, cell.getCellType());
				if( !BlankUtils.isBlank(cellVal) && cellVal.indexOf(content) > -1){
					finded = true;
					break;
				}
			}
		}
		return finded ? index : -1;
	}
	
	public static List<String> getRow(Sheet sheet, int rowIndex) {
		if(sheet!=null){
			return POIWorkbookVisitor.getRow(sheet.getRow(rowIndex));
		}
		return null;
	}
	
	public static List<String> getRow(Row row) {
		List<String> list = null;
		if(row!=null){
			list = new ArrayList<String>();
			for (int colIndex = 0; colIndex < row.getLastCellNum(); colIndex++) {
				Cell cell = row.getCell(colIndex);
				list.add(POIWorkbookReader.getCellValue(cell, cell.getCellType()));
			}
		}
		return list;
	}
	
	/**
	 * 
	 *@描述：获取sheet表中指定列数据起始行到数据结束行的单元格内容集合
	 *@创建人:kangzhidong
	 *@创建时间:2015-3-4上午08:46:29
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param sheet
	 *@param colIndex
	 *@return
	 */
	public static List<String> getColumn(Sheet sheet, int colIndex) {
		return getColumn(sheet, colIndex, sheet.getFirstRowNum());
	}
	
	/**
	 * 
	 *@描述：获取sheet表中指定列指定起始行到数据结束行的单元格内容集合
	 *@创建人:kangzhidong
	 *@创建时间:2015-3-4上午08:45:52
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param sheet
	 *@param colIndex
	 *@param startRow
	 *@return
	 */
	public static List<String> getColumn(Sheet sheet, int colIndex,int startRow) {
		List<String> columns = null;
		if(sheet!=null){
			columns = new ArrayList<String>();
			for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if(row!=null){
					Cell cell =  row.getCell(colIndex);
					if(cell!=null){
						columns.add(POIWorkbookReader.getCellValue(cell, cell.getCellType()));
					}else{
						columns.add("");
					}
				}else{
					columns.add("");
				}
			}
		}
		return columns;
	}
	
	public static List<PairModel> getColumns(String filepath) throws Exception {
		List<PairModel> result = new ArrayList<PairModel>();
		// 获得上传xls文件的表头
		Sheet sheet = POIWorkbookReader.getSheet(filepath, 0);
		//获得第一行的单元格
		Iterator<Cell> cells = sheet.getRow(0).cellIterator();
		while (cells.hasNext()) {
			Cell cell = cells.next();
			//单元格内容
			String  value = POIWorkbookReader.getCellValue(cell,cell.getCellType());
			result.add(new PairModel(cell.getColumnIndex()+"", value));
		}
		return result;
	}

	
}



