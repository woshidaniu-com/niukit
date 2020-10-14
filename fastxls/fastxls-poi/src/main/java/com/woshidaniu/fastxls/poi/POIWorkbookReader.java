package com.woshidaniu.fastxls.poi;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.woshidaniu.fastxls.poi.utils.POIWorkbookUtils;
/**
 * 
 * @className: WorkbookReader
 * @description:  实现读取Excel的服务  Excel 97-2003和2007 版本
 * @author : kangzhidong
 * @date : 下午2:05:00 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public abstract class POIWorkbookReader {
	
	
	public static Sheet[] getSheets(String filepath) throws IOException {
		Workbook wb = POIWorkbookUtils.getWorkbook(filepath);
		return getSheets(wb);
	}
	
	public static Sheet[] getSheets(File file) throws IOException {
		Workbook wb = POIWorkbookUtils.getWorkbook(file);
		return getSheets(wb);
	}
	
	public static Sheet[] getSheets(Workbook wb){
		Sheet[] sheets = null;
		if(wb!=null){
			sheets = new Sheet[wb.getNumberOfSheets()];
			for (int index = 0; index < wb.getNumberOfSheets(); index++) {
				sheets[index] = wb.getSheetAt(index);
			}
		}
		return sheets;
	}
	
	public static Sheet getSheet(String filepath, int sheetIndex) throws IOException {
		Workbook wb = POIWorkbookUtils.getWorkbook(filepath);
		return getSheet(wb, sheetIndex);
	}

	
	public static Sheet getSheet(String filepath, String sheetName) throws IOException {
		Workbook wb = POIWorkbookUtils.getWorkbook(filepath);
		return getSheet(wb, sheetName);
	}

	
	public static Sheet getSheet(Workbook wb, int sheetIndex) {
		Sheet sheet = null;
		if(wb!=null){
			sheet = wb.getSheetAt(sheetIndex);
		}
		return sheet;
	}

	
	public static Sheet getSheet(Workbook wb, String sheetName) {
		Sheet sheet = null;
		if(wb!=null){
			sheet = wb.getSheet(sheetName);
		}
		return sheet;
	}
	
	public static List<Row> getRows(Sheet sheet, int fromIndex, int toIndex) {
		fromIndex = fromIndex >= sheet.getLastRowNum() ? sheet.getLastRowNum() : fromIndex;
		toIndex = toIndex >= sheet.getLastRowNum() ? sheet.getLastRowNum() : toIndex;
		List<Row> rows = null;
		if (toIndex >= fromIndex) {
			rows = new ArrayList<Row>();
			for (int row_index = fromIndex; row_index <= toIndex; row_index++) {
				rows.add(sheet.getRow(row_index));
			}
		}
		return rows;
	}
	
	public static Row getRow(String filepath, int sheetIndex, int rowIndex) throws IOException {
		Sheet sheet = getSheet(filepath, sheetIndex);
		return getRow(sheet, rowIndex);
	}

	public static Row getRow(String filepath, String sheetName, int rowIndex) throws IOException {
		Sheet sheet = getSheet(filepath, sheetName);
		return getRow(sheet, rowIndex);
	}

	
	public static Row getRow(Sheet sheet, int rowIndex) {
		Row row = null;
		if(sheet!=null){
			row = sheet.getRow(rowIndex);
		}
		return row;
	}
	
	public static List<Cell> getColumn(Sheet sheet, int colIndex) {
		List<Cell> columns = null;
		if(sheet!=null){
			columns = new ArrayList<Cell>();
			for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
				columns.add(sheet.getRow(i).getCell(colIndex));
			}
		}
		return columns;
	}

	public static List<Cell> getCells(Row row) {
		List<Cell> cells = null;
		if(row!=null){
			cells = new ArrayList<Cell>();
			//获得第一行的单元格
			Iterator<Cell> ite = row.cellIterator();
			while (ite.hasNext()) {
				cells.add(ite.next());
			}
		}
		return cells;
	}
	
	public static Cell getCell(Sheet sheet, int rowIndex, int colIndex) {
		Row row = getRow(sheet, rowIndex);
		return getCell(row, colIndex);
	}
	
	public static Cell getCell(Row row, int colIndex) {
		Cell cell = null;
		if(row!=null){
			cell = row.getCell(colIndex);
		}
		return cell;
	}
	
	/**
	 * 
	 * @description: Excel存储日期、时间均以数值类型进行存储，读取时POI先判断是是否是数值类型，再进行判断转化
					  1、数值格式(CELL_TYPE_NUMERIC):
			
					　　1.纯数值格式：getNumericCellValue() 直接获取数据
			
					　　2.日期格式：处理yyyy-MM-dd, d/m/yyyy h:mm, HH:mm 等不含文字的日期格式
			
					　　　　1).判断是否是日期格式：HSSFDateUtil.isCellDateFormatted(cell)
			
					　　　　2).判断是日期或者时间
			
					　　　　　　cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")
			
					　　　　　　OR:cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd")
			
					　　3.自定义日期格式：处理yyyy年m月d日,h时mm分,yyyy年m月等含文字的日期格式
			
					　　　　判断cell.getCellStyle().getDataFormat()值，解析数值格式：yyyy年m月d日----->31；m月d日---->58；h时mm分--->32等
					
					万能处理方案：
					所有日期格式都可以通过getDataFormat()值来判断
					yyyy-MM-dd------14
					yyyy年m月d日-----31
					yyyy年m月--------57
					m月d日  ----------58
					HH:mm-----------20
					h时mm分  --------32
	 * @date : 2014-1-6
	 * @time : 下午4:05:16 
	 * @param cell
	 * @param cellType
	 * @return
	 */
	public static String getCellValue(Cell cell,int cellType) {
		String result = "";
		try {
			if(cell != null){
				switch (cellType) {
					// 数字类型
					case Cell.CELL_TYPE_NUMERIC:{
						
						// 此处不需要格式化数据，否则excel中的小数在此会变成四舍五入后的整数  
						
				        SimpleDateFormat sdf = null;
				        //Excel存储日期、时间均以数值类型进行存储，读取时POI先判断是是否是数值类型，再进行判断转化
						// 数字日期型
						//1.日期格式：处理yyyy-MM-dd, d/m/yyyy h:mm, HH:mm 等不含文字的日期格式
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							// 处理日期格式、时间格式
							//1).判断是否是日期格式：HSSFDateUtil.isCellDateFormatted(cell)
							//2).判断是日期或者时间
							if (HSSFDataFormat.getBuiltinFormat("h:mm") == cell.getCellStyle().getDataFormat()) {
			                    sdf = new SimpleDateFormat("HH:mm");
			                } else {// 日期
			                    sdf = new SimpleDateFormat("yyyy-MM-dd");
			                }
			                result = sdf.format(cell.getDateCellValue());
			            } else{
			            	
			            	// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
							short format = cell.getCellStyle().getDataFormat();
							if (format == 58) {
				                sdf = new SimpleDateFormat("yyyy-MM-dd");
				            } else if(format == 14){
					            sdf = new SimpleDateFormat("yyyy-MM-dd");
				            }else if (format == 20 ) {
					            sdf = new SimpleDateFormat("HH:mm");
					        }else if(format == 31){
					            sdf = new SimpleDateFormat("yyyy年m月d日");
					        }else if ( format == 32) {
					            sdf = new SimpleDateFormat("h时mm分 ");
					        }else if(format == 57){
					            sdf = new SimpleDateFormat("yyyy年m月");
					        }else if(format == 58){
					            sdf = new SimpleDateFormat("m月d日 ");
					        }
			            	
							//如果日期格式不为空，表示是日期
							if(null != sdf){
								
								Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
							    result = sdf.format(date);
							    
							} else {
								cell.setCellType(Cell.CELL_TYPE_STRING);
								/*// 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串  
								String value = cell.getStringCellValue();
						        if (value.indexOf(".") > -1) {  
						            val = String.valueOf(new Double(temp)).trim();  
						        } else {  
						            val = temp.trim();  
						        }  */
								result = new BigDecimal(cell.getStringCellValue()).toPlainString(); // 數字格式, 避免出現科學符號
				            }
			            }
						
						break;
					}
					// String类型
					case Cell.CELL_TYPE_STRING:{
						// 字串型
						result = cell.getStringCellValue();
						break;
					}
					case Cell.CELL_TYPE_FORMULA:{
					    // POI计算公式，得出结果     
						FormulaEvaluator evaluator = null;
						Workbook book = cell.getSheet().getWorkbook();
						if(book instanceof XSSFWorkbook){
							evaluator = new XSSFFormulaEvaluator((XSSFWorkbook) book);
						}else if(book instanceof HSSFWorkbook){
							evaluator = new HSSFFormulaEvaluator((HSSFWorkbook) book);
						}
				        int type = evaluator.evaluateFormulaCell(cell);
				        result = getCellValue(cell, type);
						// 公式型
						break;
					}
					case Cell.CELL_TYPE_BLANK:{
						// 空白型
						result = "";
						break;
					}
					case Cell.CELL_TYPE_BOOLEAN:{
						// 布尔型
						result= Boolean.toString(cell.getBooleanCellValue());
						break;
					}
					case Cell.CELL_TYPE_ERROR:{
						// Error
						result = "";
						break;
					}
				}
			}
		} catch (Exception err2) {
			result = "";
		}
		return result;
	}
	

}
