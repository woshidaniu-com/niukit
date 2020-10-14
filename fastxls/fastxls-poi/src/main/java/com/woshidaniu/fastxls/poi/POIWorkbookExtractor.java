package com.woshidaniu.fastxls.poi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.woshidaniu.fastxls.core.Suffix;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.core.model.SheetModel;
import com.woshidaniu.fastxls.core.model.WorkBookModel;
import com.woshidaniu.fastxls.poi.utils.POIWorkbookUtils;
/**
 * 
 * @className: WorkbookExtractor
 * @description: 
 * Excel 数据提取工具：将Excel 97~2003和2007数据转换成java对象
 * 1.ss=xssf + hssf 
 * 2.poi的jar文件必须高于3.5版本才支持。 
 * 3.jdk的版本必须高于等于1.5 
 *  
 * HSSF is the POI Project's pure Java implementation of the Excel '97(-2007) file format. XSSF is the POI Project's pure Java implementation of the Excel 2007 OOXML (.xlsx) file format. 
 * HSSF and XSSF provides ways to read spreadsheets create, modify, read and write XLS spreadsheets. They provide: 
 *    low level structures for those with special needs 
 *    an eventmodel api for efficient read-only access 
 *    a full usermodel api for creating, reading and modifying XLS files 
 *    
 * @author : kangzhidong
 * @date : 下午2:34:26 2014-11-21
 * @param <T>
 * @modify by:
 * @modify date :
 * @modify description :
 */
@SuppressWarnings("unchecked")
public abstract class POIWorkbookExtractor {

	public static <T extends CellModel> WorkBookModel<T> extract(String filePath) throws IOException, InvalidFormatException {
		return extract(POIWorkbookUtils.getWorkbook(filePath));
	}
	
	public static <T extends CellModel> WorkBookModel<T> extract(String filePath,int startRow) throws IOException, InvalidFormatException {
		return extract(POIWorkbookUtils.getWorkbook(filePath),startRow);
	}
	
	public static <T extends CellModel> WorkBookModel<T> extract(Workbook workbook) throws IOException {
		return extract(workbook,0);
	}
	
	public static <T extends CellModel> WorkBookModel<T> extract(Workbook workbook,int startRow) throws IOException {
		if(null!=workbook){
			//实体化WorkBook的Model对象
			WorkBookModel<T> model = null;
			if(workbook instanceof HSSFWorkbook){
				model = new WorkBookModel<T>(Suffix.XLS);
			}else if(workbook instanceof XSSFWorkbook){
				model = new WorkBookModel<T>(Suffix.XLSX);
			}
			//解析Workbook
			Sheet[] sheets = POIWorkbookReader.getSheets(workbook);
			for (Sheet sheet : sheets) {
				model.setSheet(sheet.getSheetName(),(SheetModel<T>) extract(sheet));
			}
			return model;
		}else{
			return null;
		}
	}
	
	public static <T extends CellModel> SheetModel<T> extract(Sheet sheet) throws IOException{
		return extract(sheet,0);
	}
	
	public static <T extends CellModel> SheetModel<T> extract(Sheet sheet,int startRowIndex) throws IOException{
		if(null!=sheet){
			return extract(sheet, startRowIndex, sheet.getLastRowNum());
		}else{
			return null;
		}
	}
	
	public static <T extends CellModel> SheetModel<T> extract(Sheet sheet,int startRowIndex,int endRowIndex) throws IOException{
		if(null!=sheet){
			//实体化WorkBook的SheetModel对象
			SheetModel<T> sheetModel = new SheetModel<T>();
			//提取基本属性参数
			sheetModel.setSheetIndex(sheet.getWorkbook().getSheetIndex(sheet));
			sheetModel.setSheetName(sheet.getSheetName());
			sheetModel.setSelected(sheet.isSelected());
			sheetModel.setDefaultColumnWidth(sheet.getDefaultColumnWidth());
			sheetModel.setDefaultRowHeight(sheet.getDefaultRowHeight());
			List<RowModel<T>> rows = null;
			if(sheet.getLastRowNum()>0){
				rows = new ArrayList<RowModel<T>>();
				startRowIndex 	= startRowIndex>sheet.getFirstRowNum() ? startRowIndex : sheet.getFirstRowNum();
				endRowIndex		= endRowIndex >  sheet.getLastRowNum() ? sheet.getLastRowNum() : endRowIndex;
				for (int index = startRowIndex; index < endRowIndex; index++) {
					rows.add(((RowModel<T>) extract(sheet.getRow(index))));
				}
			}
			sheetModel.setRows(rows);
			return sheetModel;
		}else{
			return null;
		}
	}
	
	public static <T extends CellModel> RowModel<T> extract(Row row) throws IOException{
		if(null!=row){
			//实体化WorkBook的RowModel对象
			RowModel<T> rowModel =  new RowModel<T>(row.getRowNum(),row.getHeight());
			if(row.getLastCellNum()>0){
				for (int index = row.getFirstCellNum(); index < row.getLastCellNum(); index++) {
					rowModel.setCell(index, (T) extract(row.getCell(index)));
				}
			}
			return rowModel;
		}else{
			return null;
		}
	}
	
	public static <T extends CellModel> T extract(Cell cell){
		if(null!=cell){
			//实体化WorkBook的RowModel对象
			T cellModel = null;
			try {
				cellModel = (T) new CellModel(cell.getRowIndex(),cell.getColumnIndex());
				//提取参数
				cellModel.setRowIndex(cell.getRowIndex());
				cellModel.setColumnIndex(cell.getColumnIndex());
				cellModel.setFormula(cell.getCellFormula());
				cellModel.setWidth(cell.getSheet().getColumnWidth(cell.getColumnIndex()));
				//获取备注信息
				Comment comment = cell.getSheet().getCellComment(cell.getRowIndex(), cell.getColumnIndex());
				if(comment!=null){
					cellModel.setComments(comment.getString().getString());
				}
				//获取超链接
				Hyperlink hyperlink = cell.getHyperlink();
				if(hyperlink!=null){
					cellModel.setHyperlink(hyperlink.getAddress());
				}
				//单元格内容
				cellModel.setContent(POIWorkbookReader.getCellValue( cell, cell.getCellType()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return (T)cellModel;
		}else{
			return null;
		}
	}

}
