package com.woshidaniu.fastxls.jexcel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.JXLException;
import jxl.format.CellFormat;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.core.model.SheetModel;
import com.woshidaniu.fastxls.core.model.WorkBookModel;
import com.woshidaniu.fastxls.jexcel.utils.JXLCellFormatUtils;
import com.woshidaniu.fastxls.jexcel.utils.WorkbookUtils;
/**
 * 
 * @className: JXLWritableWorkbookFiller
 * @description: 构建xls文件内容
 * @author : kangzhidong
 * @date : 下午5:32:25 2014-11-22
 * @modify by:
 * @modify date :
 * @modify description :
 */
public abstract class JXLWorkbookFiller {

	//日期格式对象
 	private static SimpleDateFormat date_format = null;
 	//数字格式对象
 	private static DecimalFormat numberFormat = null;
 	
 	protected static Logger LOG = LoggerFactory.getLogger(JXLWorkbookFiller.class);
 	
 	/**
	 * 
	 * @description: 根据数据填充当前WritableWorkbook 的每个sheet内容
	 * @author : kangzhidong
	 * @date 下午6:05:56 2014-11-22 
	 * @param workbook
	 * @param dataModel
	 * @throws Exception
	 * @return  void 返回类型
	 * @throws  
	 */
	public static <T extends CellModel> void fillSheets(WritableWorkbook workbook,WorkBookModel<T> dataModel)  {
		fillSheets(workbook,dataModel, 0);
	}
	
	/**
	 * 
	 * @description: 根据数据从指定数字作为起始行填充当前WritableWorkbook 的每个sheet内容
	 * @author : kangzhidong
	 * @date 下午6:06:10 2014-11-22 
	 * @param workbook
	 * @param model
	 * @param startRow
	 * @return  void 返回类型
	 * @throws  
	 */
	public static <T extends CellModel> void fillSheets(WritableWorkbook workbook,WorkBookModel<T> model,int startRow) {
		Iterator<Map.Entry<String, SheetModel<T>>>  ite =  model.getWorkBook().entrySet().iterator();
		while (ite.hasNext()) {
			 Entry<String, SheetModel<T>> entry = ite.next();
			 WritableSheet sheet = workbook.createSheet(entry.getKey(),workbook.getNumberOfSheets());
			 fillSheet(sheet,null,entry.getValue(),startRow);
		}
	}
	
	public static <T extends CellModel> void fillSheets(WritableWorkbook workbook,SheetModel<T> sheetModel,int startRow) {
		WritableSheet sheet = workbook.createSheet(sheetModel.getSheetName(),workbook.getNumberOfSheets());
		fillSheets(sheet, sheetModel, startRow);
	}
	
	public static <T extends CellModel> void fillSheets(WritableSheet sheet,SheetModel<T> sheetModel,int startRow) {
		fillSheet(sheet,null,sheetModel,startRow);
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
	public static <T extends CellModel> void fillSheet(WritableSheet sheet,String title,SheetModel<T> sheetModel,int startRow) {
		//设置默认参数
		//处理行数据
		List<RowModel<T>> rowModels = sheetModel.getRows();
		if(title!=null&&title.length()>0){
			int lastCol = WorkbookUtils.getLastNumOfRow(rowModels);
			try {
				//合並單元格,下標從0開始
				//int firstCol, int firstRow, int lastCol, int lastRow
				sheet.mergeCells(0, startRow, lastCol, startRow);
				sheet.addCell(new Label(0, startRow, title, JXLCellFormatUtils.getTitleCellFormat()));
			} catch (RowsExceededException e) {
				LOG.error(e.getMessage(), e.getCause());
			} catch (WriteException e) {
				LOG.error(e.getMessage(), e.getCause());
			}
		}
		fillRows(sheet,rowModels,  startRow+1);
	}
	
	public static <T extends CellModel> void fillRows(WritableSheet sheet,List<RowModel<T>> rowModels,int startRow) {
		Iterator<RowModel<T>>  ite =  rowModels.iterator();
		while (ite.hasNext()) {
			RowModel<T> rowModel = ite.next();
			fillRow(sheet, rowModel);
		}
	}
	
	public static <T extends CellModel> void fillRow(WritableSheet sheet, RowModel<T> rowModel) {
		if(!rowModel.isEmpty()){
			Iterator<String> iterator = rowModel.keySet().iterator();
			while (iterator.hasNext()) {
				T cellModel = rowModel.get(iterator.next());
				fillCellWithStyle(sheet, cellModel);
			}
		}
	}
	
	public void fillMergeCell(WritableSheet sheet,int columnIndex,int rowIndex,int columnNum,int rowNum,String content,CellFormat format) throws JXLException{
		sheet.addCell(new Label(columnIndex, rowIndex,content, format));
		sheet.mergeCells(columnIndex, rowIndex, columnIndex+columnNum, rowIndex+rowNum);
	}
	
	public static <T extends CellModel> void fillCellWithStyle(WritableSheet sheet,T cellModel) {
		try {
			if(!BlankUtils.isBlank(cellModel)&&!BlankUtils.isBlank(cellModel.getContent())){
				//渲染内容
				Object content = cellModel.getContent();
				WritableCellFormat style = null;
				//判断是否是标题
				if(cellModel.isTitle()){
					
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
						//特殊字段[如：必填，唯一]标题单元格格式（红色，黑体，加粗，15磅字）
						style = JXLCellFormatUtils.getRequiredCellFormat();
					}else{
						 //大标题单元格格式（黑色，黑体，加粗，23磅字）
						style = JXLCellFormatUtils.getTitleCellFormat();
					}
				}else{
					//普通字段标题单元格格式（黑色，黑体，加粗，15磅字）
					style = JXLCellFormatUtils.getNormalCellFormat();
				}
				fillCell(sheet,cellModel,style);
			}else{
				fillCell(sheet,null,null);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.getCause());
		}
	}
	
	public static <T extends CellModel> void fillCell(WritableSheet sheet,T cellModel,WritableCellFormat style){
		try {
			if(!BlankUtils.isBlank(cellModel)&&!BlankUtils.isBlank(cellModel.getContent())){
				sheet.addCell(new Blank(cellModel.getColumnIndex(), cellModel.getRowIndex()));
			} else {
				//设置列宽，如果当前单元格的宽度小于已经设置的列宽则以最大值为准
				int width = Math.max(sheet.getColumnView(cellModel.getColumnIndex()).getSize(), cellModel.getWidth());
				sheet.setColumnView(cellModel.getColumnIndex(), width);
				
				//渲染内容
				Object content = cellModel.getContent();
				int col = cellModel.getColumnIndex();
				int rol = cellModel.getRowIndex();
				if(content instanceof String){
					sheet.addCell(new Label(col,rol,(String)content,style));
				}else if (content instanceof Number) {
					String format = cellModel.getFormat();
					if((content instanceof Integer)||(content instanceof Long)){
						sheet.addCell(new jxl.write.Number(col,rol,Double.valueOf((String)content),style));
					}else if((content instanceof Double)||(content instanceof Float)){
						//针对带小数点的数据的处理
					    if(!BlankUtils.isBlank(format)){
					    	try {
					    		style = JXLCellFormatUtils.getNumberCellFormat(format);
					    		sheet.addCell(new jxl.write.Number(col,rol,Double.valueOf((String)content),style));
							} catch (Exception e) {
								//出现异常表示给出的格式为自定义格式
								numberFormat = new DecimalFormat(format);
								sheet.addCell(new Label(col,rol,numberFormat.format(content),style));
							}
						}else{
							sheet.addCell(new jxl.write.Number(col,rol,Double.valueOf((String)content),style));
						}
					}else{
						String number = new BigDecimal(((Number)content).doubleValue()).toPlainString();
						sheet.addCell(new Label(col,rol,number,style));
					}
				} else if (content instanceof Date) {
					//针对Date格式
					String format = cellModel.getFormat();
					if(!BlankUtils.isBlank(format)){
						try {
							style = JXLCellFormatUtils.getNumberCellFormat(format);
							sheet.addCell(new jxl.write.DateTime(col,rol,(Date)content,style));
						} catch (Exception e) {
							//出现异常表示给出的格式为自定义格式
							date_format = new SimpleDateFormat(format);
							sheet.addCell(new Label(col,rol,date_format.format(content),style));
						}
					}else{
						sheet.addCell(new jxl.write.DateTime(col,rol,(Date)content,style));
					}
				} else if(content instanceof Boolean){
					sheet.addCell(new jxl.write.Boolean(col,rol,(Boolean)content,style));
				}else{
					sheet.addCell(new Label(col,rol,String.valueOf(content),style));
				}
			}
		} catch (RowsExceededException e) {
			LOG.error(e.getMessage(), e.getCause());
		} catch (NumberFormatException e) {
			LOG.error(e.getMessage(), e.getCause());
		} catch (WriteException e) {
			LOG.error(e.getMessage(), e.getCause());
		}
	}
	
}
