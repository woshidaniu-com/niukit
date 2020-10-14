package com.woshidaniu.fastxls.poi;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.MergedRegionModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.core.model.SheetModel;
import com.woshidaniu.fastxls.core.model.ValidateModel;
import com.woshidaniu.fastxls.core.model.WorkBookModel;
import com.woshidaniu.fastxls.poi.utils.POICellStyleUtils;
import com.woshidaniu.fastxls.poi.utils.POIFontUtils;
import com.woshidaniu.fastxls.poi.utils.ValidateUtils;
/**
 * 
 *@类名称	: POIWorkbookFiller.java
 *@类描述	：Excel 文件内容渲染
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 8:57:43 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class POIWorkbookFiller{
	
	//日期格式对象
 	private static SimpleDateFormat date_format = null;
 	//数字格式对象
 	private static NumberFormat numberFormat = null;
	
 	protected static Logger LOG = LoggerFactory.getLogger(POIWorkbookFiller.class);
	/**
	 * 
	 * @description: 根据数据填充当前Workbook 的每个sheet内容
	 * @author : kangzhidong
	 * @date 下午6:05:56 2014-11-22 
	 * @param workbook
	 * @param dataModel
	 * @throws Exception
	 * @return  void 返回类型
	 * @throws  
	 */
	public static <T extends CellModel> void fillSheets(Workbook workbook,WorkBookModel<T> dataModel) throws Exception {
		fillSheets(workbook,dataModel, 0);
		POICellStyleUtils.destroy(workbook);
	}
	
	/**
	 * 
	 * @description: 根据数据从指定数字作为起始行填充当前Workbook 的每个sheet内容
	 * @author : kangzhidong
	 * @date 下午6:06:10 2014-11-22 
	 * @param workbook
	 * @param model
	 * @param startRow
	 * @return  void 返回类型
	 * @throws  
	 */
	public static <T extends CellModel> void fillSheets(Workbook workbook,WorkBookModel<T> model,int startRow) {
		Iterator<Map.Entry<String, SheetModel<T>>>  ite =  model.getWorkBook().entrySet().iterator();
		while (ite.hasNext()) {
			 Entry<String, SheetModel<T>> entry = ite.next();
			 Sheet sheet = workbook.createSheet(entry.getKey());
			 fillSheet(sheet,entry.getValue(),startRow);
		}
		POICellStyleUtils.destroy(workbook);
	}
	
	public static <T extends CellModel> void fillSheets(Workbook workbook,SheetModel<T> sheetModel,int startRow) {
		Sheet sheet = workbook.createSheet(sheetModel.getSheetName());
		fillSheets(sheet, sheetModel, startRow);
		POICellStyleUtils.destroy(workbook);
	}
	
	public static <T extends CellModel> void fillSheets(Sheet sheet,SheetModel<T> sheetModel,int startRow) {
		fillSheet(sheet,sheetModel,startRow);
		POICellStyleUtils.destroy(sheet.getWorkbook());
	}
	
	/**
	 * 
	 * @description: 从第startRow行根据数据开始填充当前sheet并返回，第一行以最大行数合并作为大标题
	 * @author : kangzhidong
	 * @date ： 2013 - 2013-11-15 - 下午2:47:43
	 * @param sheet 
	 * @param title 大标题
	 * @param data 
	 */
	public static <T extends CellModel> void fillSheet(Sheet sheet,SheetModel<T> sheetModel,int startRow) {
		//设置默认参数
		sheet.setSelected(sheetModel.isSelected());
		//设置默认宽度、高度值	
		sheet.setDefaultColumnWidth(sheetModel.getDefaultColumnWidth());
		sheet.setDefaultRowHeightInPoints(sheetModel.getDefaultRowHeight());
		//处理行数据
		List<RowModel<T>> rowModels = sheetModel.getRows();
		//填充行数据
		fillRows(sheet,rowModels,  startRow);
		//设置数据校验规则
		ValidateModel[] validateModels = sheetModel.getValidates();
		if(validateModels != null ){
			for (int i = 0; i < validateModels.length; i++) {
				sheet.addValidationData(ValidateUtils.getValidation(sheet, validateModels[i]));
			}
		}
		POICellStyleUtils.destroy(sheet.getWorkbook());
	}
	
	public static <T extends CellModel> void fillMerged(Sheet sheet,RowModel<T> rowModel) {
		MergedRegionModel cellRangeAddress = rowModel.getCellRangeAddress();
		if(cellRangeAddress != null){
			//合並單元格,下標從0開始
			//int firstRow, int lastRow, int firstCol, int lastCol
		    sheet.addMergedRegion(new CellRangeAddress(cellRangeAddress.getFirstRow(),cellRangeAddress.getLastRow(),cellRangeAddress.getFirstCol(),cellRangeAddress.getLastCol()));
		    Row row = sheet.createRow(cellRangeAddress.getFirstRow());
		    //heightInPoints 设置的值永远是height属性值的20倍
			row.setHeightInPoints(rowModel.getHeight());
		    Cell cell = row.createCell(0);
			T cellModel = rowModel.getCell(0);
			cellModel.setTitle(true);
			fillCellWithStyle(cell, cellModel);
		}
		POICellStyleUtils.destroy(sheet.getWorkbook());
	}
	
	public static <T extends CellModel> void fillRows(Sheet sheet,List<RowModel<T>> rowModels,int startRow) {
		Iterator<RowModel<T>>  ite =  rowModels.iterator();
		MergedRegionModel cellRangeAddress = null;
		while (ite.hasNext()) {
			RowModel<T> rowModel = ite.next();
			cellRangeAddress = rowModel.getCellRangeAddress();
			if(cellRangeAddress != null){
				fillMerged(sheet, rowModel);
			}else{
				Row row = sheet.createRow(rowModel.getRowNum());
				fillRow(row, rowModel);
			}
		}
		POICellStyleUtils.destroy(sheet.getWorkbook());
	}
	
	public static <T extends CellModel> void fillRow(Row row, RowModel<T> rowModel) {
		//heightInPoints 设置的值永远是height属性值的20倍
		//row.setHeightInPoints(rowModel.getHeight());
		// Set the row's height or set to ff (-1) for undefined/default-height.
		// Set the height in "twips" or
		// 1/20th of a point.
		row.setHeight((short) (rowModel.getHeight() * 20));
		
		Iterator<String> iterator = rowModel.keySet().iterator();
		Cell cell = null;
		while (iterator.hasNext()) {
			T cellModel = rowModel.get(iterator.next());
			cell = row.createCell(cellModel.getColumnIndex());
			fillCellWithStyle(cell, cellModel);
		}
	}
	
	public static <T extends CellModel> void fillCellWithStyle(Cell cell,T cellModel) {
		try {
			
			Workbook workbook = cell.getSheet().getWorkbook();
			
			//这里字体使用静态对象，且只有一个实例，因为字体太多生成的Excel会提示错误
			
			//普通字段标题单元格格式（黑色，黑体，15磅字）
			CellStyle normalStyle = POICellStyleUtils.getNormalStyle(workbook);
			//设置此单元格是否锁定   
			normalStyle.setLocked(cellModel.isLocked());   
			//设置内容单元格内的位置
			normalStyle.setAlignment(cellModel.getAlignment());
			normalStyle.setVerticalAlignment(cellModel.getVerticalAlignment());
			
			if(!BlankUtils.isBlank(cellModel)&&!BlankUtils.isBlank(cellModel.getContent())){
				//渲染内容
				Object content = cellModel.getContent();
				
				//判断是否是标题
				if(cellModel.isTitle()){
					 //大标题单元格格式（黑色，黑体，加粗，23磅字）
					CellStyle titleStyle = POICellStyleUtils.getTitleStyle(workbook);
					//设置此单元格是否锁定   
					titleStyle.setLocked(cellModel.isLocked()); 
					//设置内容单元格内的位置
					titleStyle.setAlignment(cellModel.getAlignment());
					titleStyle.setVerticalAlignment(cellModel.getVerticalAlignment());
					//特殊字段[如：必填，唯一]标题单元格格式（红色，黑体，加粗，15磅字）
					CellStyle requiredStyle = POICellStyleUtils.getRequiredStyle(workbook);
					//设置内容单元格内的位置
					requiredStyle.setAlignment(cellModel.getAlignment());
					requiredStyle.setVerticalAlignment(cellModel.getVerticalAlignment());
					//设置此单元格是否锁定   
					requiredStyle.setLocked(cellModel.isLocked()); 
					boolean requisite = cellModel.isRequisite();
					boolean unique = cellModel.isUnique();
					if(requisite||unique){
						if(requisite&&unique){
							content = content.toString() + "(必填,唯一)";
						}else if(requisite&&unique==false){
							content = content.toString() + "(必填)";
						}else if(unique&&requisite==false){
							content = content.toString() + "(唯一)";
						}
						cellModel.setContent(content);
						cell.setCellStyle(requiredStyle);
					}else{
						cell.setCellStyle(titleStyle);
					}
				}else if(cellModel.isTip()){
					CellStyle tipStyle = POICellStyleUtils.getCommentStyle(workbook);
					cell.setCellStyle(tipStyle);
				}else{
					cell.setCellStyle(normalStyle);
				}
				fillCell(cell,cellModel);
			}else{
				cell.setCellStyle(normalStyle);
				fillCell(cell,null);
			}
			//fontStyle = null;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e.getCause());
		}
	}

	/**
	 * 
	 * @description: 填充单元格内容，并设置相应格式
	 * @author : kangzhidong
	 * @date 下午12:28:03 2014-11-22 
	 * @param cell
	 * @param cellModel
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static <T extends CellModel> void fillCell(Cell cell,T cellModel){
		Workbook workbook = cell.getSheet().getWorkbook();
		CreationHelper helper = workbook.getCreationHelper();
		if(BlankUtils.isBlank(cellModel)){
			cell.setCellType(Cell.CELL_TYPE_BLANK);
			cell.setCellValue(" ");
		} else {
			if(!BlankUtils.isBlank(cellModel.getContent())){
				//渲染内容
				Object content = cellModel.getContent();
				String cellValue = "";
				if(content instanceof String){
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellValue = (String)content;
					cell.setCellValue(helper.createRichTextString(cellValue));
				}else if (content instanceof Number) {
					String format = cellModel.getFormat();
					if((content instanceof Integer)||(content instanceof Long)){
						cellValue = String.valueOf(content);
						cell.setCellValue(helper.createRichTextString(cellValue));
					}else if((content instanceof Double)||(content instanceof Float)){
						cellValue = String.valueOf(content);
						//针对带小数点的数据的处理
					    cell.setCellValue(Double.valueOf(String.valueOf(content)));
					    if(!BlankUtils.isBlank(format)){
					    	try {
					    		CellStyle style = workbook.createCellStyle();
						    	style.setDataFormat(workbook.createDataFormat().getFormat(format));
						    	//设定样式
							    cell.setCellStyle(style);
							} catch (Exception e) {
								//出现异常表示给出的格式为自定义格式
								cell.setCellType(Cell.CELL_TYPE_STRING);
								numberFormat = new DecimalFormat(format);
								
								cellValue = numberFormat.format(content);
								
								cell.setCellValue(helper.createRichTextString(cellValue));
							}
						}else{
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
					}else{
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cellValue = new BigDecimal(((Number)content).doubleValue()).toPlainString();
						cell.setCellValue(helper.createRichTextString(cellValue));
					}
				} else if (content instanceof Date) {
					//针对Date格式
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String format = cellModel.getFormat();
					cell.setCellValue((Date)content);
					if(!BlankUtils.isBlank(format)){
						try {
							CellStyle style = workbook.createCellStyle();
							style.setDataFormat(workbook.createDataFormat().getFormat(format));
							//设定样式
							cell.setCellStyle(style);
							
						} catch (Exception e) {
							//出现异常表示给出的格式为自定义格式
							cell.setCellType(Cell.CELL_TYPE_STRING);
							date_format = new SimpleDateFormat(format);
							
							cellValue = date_format.format(content);
							
							cell.setCellValue(helper.createRichTextString(cellValue));
						}
					}
				} else if(content instanceof Boolean){
					cellValue = String.valueOf(content);
					cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
					cell.setCellValue((Boolean)content);
				}else{
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellValue = String.valueOf(content);
					cell.setCellValue(helper.createRichTextString(String.valueOf(content)));
				}
				//设置列宽，如果当前单元格的宽度小于已经设置的列宽则以最大值为准
				// api 段信息 Set the width (in units of 1/256th of a character width)
				int width = Math.max(Math.min(255, Math.max(cell.getSheet().getColumnWidth(cellModel.getColumnIndex())/256, Math.max(cellModel.getWidth() , cellValue.length()))),20);
				// api 段信息 Set the width (in units of 1/256th of a character width)
				cell.getSheet().setColumnWidth(cellModel.getColumnIndex(), width * 256);
				//校验规则
				ValidateModel validateModel = cellModel.getValidate();
				if(validateModel != null){
					 DataValidation dataValidation = ValidateUtils.getValidation(cell, validateModel);
					 if(dataValidation != null){
						 cell.getSheet().addValidationData(dataValidation);
					 }
				}
			}
			//添加批注
			fillComment(cell, cellModel);
		}
	}
	
	public static <T extends CellModel> void fillComment(Cell cell,T cellModel){
		if(!BlankUtils.isBlank(cellModel.getComments())){
			fillComment(cell, cellModel.getComments());
		}
	}
	
	public static <T extends CellModel> void fillComment(Cell cell,String comments){
		if(!BlankUtils.isBlank(comments)){
			//创建绘图对象 
			Drawing  drawing  = cell.getSheet().createDrawingPatriarch();
			// 添加单元格注释
			// 定义注释的大小和位置,详见文档
			ClientAnchor anchor = null;
			RichTextString text = null;
			if(cell instanceof HSSFCell){
				//获取批注对象 
				//(int dx1, int dy1, int dx2, int dy2, short col1, int row1, short col2, int row2) 
				//前四个参数是坐标点,后四个参数是编辑和显示批注时的大小.
				anchor = new HSSFClientAnchor(0, 0, 0, 0,(short)3,3,(short)5,6 );
				text = new HSSFRichTextString(comments);
			}else if(cell instanceof XSSFCell || cell instanceof SXSSFCell){
				anchor = new XSSFClientAnchor(0, 0, 0, 0,(short)3,3,(short)5,6);
				text = new XSSFRichTextString(comments);
				Font commentFormatter = POIFontUtils.getCommentFont(cell.getSheet().getWorkbook());
				text.applyFont(commentFormatter);
			}
			if(anchor != null){
				
				Comment comment = drawing.createCellComment(anchor);
				 //输入批注信息 
				comment.setString(text);
				cell.setCellComment(comment);
			}
		}
	}
	
	
}



