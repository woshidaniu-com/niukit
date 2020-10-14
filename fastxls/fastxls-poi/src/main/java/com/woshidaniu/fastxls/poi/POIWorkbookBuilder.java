package com.woshidaniu.fastxls.poi;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.woshidaniu.fastxls.core.Suffix;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.core.model.SheetModel;
import com.woshidaniu.fastxls.core.model.WorkBookModel;
import com.woshidaniu.fastxls.poi.utils.POIWorkbookUtils;

/**
 * 
 *@类名称	: POIWorkbookBuilder.java
 *@类描述	：xls文档内容构建
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 8:58:45 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class POIWorkbookBuilder {
	
	/**
	 * 
	 *@描述		：根据表头集合，创建一个只有表头的xls空文件
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 29, 20169:00:12 AM
	 *@param <T>
	 *@param suffix
	 *@param sheetName
	 *@param rowModel
	 *@param out
	 *@throws IOException
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static <T extends CellModel> void buildTemplate(Suffix suffix,String sheetName,RowModel<T> rowModel,OutputStream out) throws IOException{
		//生成workbook（POI）
		Workbook wb = POIWorkbookUtils.getWorkbook(suffix);
		//创建sheet
		Sheet sheet = wb.createSheet(sheetName);
		// 生成列标题Row
		Row row = sheet.createRow(0);
		//构建表头
		POIWorkbookFiller.fillRow(row, rowModel);
		//输出xls文档
		wb.write(out);
		out.flush();
	}
	
	public static <T extends CellModel> void buildToLocal(WorkBookModel<T> dataModel,String outFilePath) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(dataModel.getSuffix());
		POIWorkbookFiller.fillSheets(workbook,dataModel);
		POIWorkbookWriter.writeToLocal(workbook, outFilePath);
	}
	

	public static <T extends CellModel> void buildToLocal(ArgumentsModel arguments,SheetModel<T> sheetModel,String outFilePath) throws Exception{
		//构建Workbook
		Workbook workbook = POIWorkbookUtils.getWorkbook(arguments);
		POIWorkbookFiller.fillSheets(workbook,sheetModel,0);
		POIWorkbookWriter.writeToLocal(workbook, outFilePath);
	}
	
	public static <T extends CellModel> void buildToLocal(ArgumentsModel arguments,List<RowModel<T>> dataList,String outFilePath) throws Exception{
		//构建Workbook
		Workbook workbook = POIWorkbookUtils.getWorkbook(arguments);
		POIWorkbookFiller.fillRows(workbook.createSheet(arguments.getSheetName()),dataList,0);
		POIWorkbookWriter.writeToLocal(workbook, outFilePath);
	}
	
	public static <T extends CellModel> void buildToClent(WorkBookModel<T> dataModel,HttpServletResponse response) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(dataModel.getSuffix());
		POIWorkbookFiller.fillSheets(workbook,dataModel);
		POIWorkbookWriter.writeToClent(workbook, response);
	}
	

	public static <T extends CellModel> void buildToClent(ArgumentsModel arguments,SheetModel<T> sheetModel,HttpServletResponse response) throws Exception{
		//构建Workbook
		Workbook workbook = POIWorkbookUtils.getWorkbook(arguments);
		POIWorkbookFiller.fillSheets(workbook,sheetModel,0);
		POIWorkbookWriter.writeToClent(workbook, response);
	}
	
	public static <T extends CellModel> void buildToClent(ArgumentsModel arguments,List<RowModel<T>> dataList,HttpServletResponse response) throws Exception{
		//构建Workbook
		Workbook workbook = POIWorkbookUtils.getWorkbook(arguments);
		POIWorkbookFiller.fillRows(workbook.createSheet(arguments.getSheetName()),dataList,0);
		POIWorkbookWriter.writeToClent(workbook, response);
	}
	
	public static <T extends CellModel> void buildToStream(WorkBookModel<T> dataModel,OutputStream out) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(dataModel.getSuffix());
		POIWorkbookFiller.fillSheets(workbook,dataModel);
		POIWorkbookWriter.writeToStream(workbook, out);
	}
	
	public static <T extends CellModel> void buildToStream(ArgumentsModel arguments,SheetModel<T> sheetModel,OutputStream out) throws Exception{
		//构建Workbook
		Workbook workbook = POIWorkbookUtils.getWorkbook(arguments);
		POIWorkbookFiller.fillSheets(workbook,sheetModel,0);
		POIWorkbookWriter.writeToStream(workbook, out);
	}
	
	public static <T extends CellModel> void buildToStream(ArgumentsModel arguments,List<RowModel<T>> dataList,OutputStream out) throws Exception{
		//构建Workbook
		Workbook workbook = POIWorkbookUtils.getWorkbook(arguments);
		POIWorkbookFiller.fillRows(workbook.createSheet(arguments.getSheetName()),dataList,0);
		POIWorkbookWriter.writeToStream(workbook, out);
	}
	
	public static <T extends CellModel> void buildWithTempleteToLocal(WorkBookModel<T> dataModel,String templetePath,String outFilePath) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillSheets(workbook,dataModel);
		POIWorkbookWriter.writeToLocal(workbook, outFilePath);
	}
	
	public static <T extends CellModel> void buildWithTempleteToLocal(ArgumentsModel arguments,SheetModel<T> sheetModel,String templetePath,String outFilePath) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillSheets(workbook,sheetModel,0);
		POIWorkbookWriter.writeToLocal(workbook, outFilePath);
	}
	
	public static <T extends CellModel> void buildWithTempleteToLocal(ArgumentsModel arguments,List<RowModel<T>> dataList,String templetePath,String outFilePath) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillRows(workbook.createSheet(arguments.getSheetName()),dataList,0);
		POIWorkbookWriter.writeToLocal(workbook, outFilePath);
	}
	
	public static <T extends CellModel> void buildWithTempleteToClent(WorkBookModel<T> dataModel,String templetePath,HttpServletResponse response) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillSheets(workbook,dataModel);
		POIWorkbookWriter.writeToClent(workbook, response);
	}
	
	public static <T extends CellModel> void buildWithTempleteToClent(ArgumentsModel arguments,SheetModel<T> sheetModel,String templetePath,HttpServletResponse response) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillSheets(workbook,sheetModel,0);
		POIWorkbookWriter.writeToClent(workbook, response);
	}
	
	public static <T extends CellModel> void buildWithTempleteToClent(ArgumentsModel arguments,List<RowModel<T>> dataList,String templetePath,HttpServletResponse response) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillRows(workbook.createSheet(arguments.getSheetName()),dataList,0);
		POIWorkbookWriter.writeToClent(workbook, response);
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(WorkBookModel<T> dataModel,String templetePath,OutputStream out) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillSheets(workbook,dataModel);
		POIWorkbookWriter.writeToStream(workbook, out);
	}

	public static <T extends CellModel> void buildWithTempleteToStream(ArgumentsModel arguments,SheetModel<T> sheetModel,String templetePath,OutputStream out) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillSheets(workbook,sheetModel,0);
		POIWorkbookWriter.writeToStream(workbook, out);
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(ArgumentsModel arguments,List<RowModel<T>> dataList,String templetePath,OutputStream out) throws Exception{
		Workbook workbook = POIWorkbookUtils.getWorkbook(templetePath);
		POIWorkbookFiller.fillRows(workbook.createSheet(arguments.getSheetName()),dataList,0);
		POIWorkbookWriter.writeToStream(workbook, out);
	}
	
}



