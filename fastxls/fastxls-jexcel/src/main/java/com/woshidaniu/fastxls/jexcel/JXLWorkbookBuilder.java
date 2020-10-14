package com.woshidaniu.fastxls.jexcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellView;
import jxl.JXLException;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.JxlWriteException;

import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.core.model.SheetModel;
import com.woshidaniu.fastxls.core.model.WorkBookModel;

/**
 * 
 *@类名称	: JXLWorkbookBuilder.java
 *@类描述	： 
 *@创建人	：kangzhidong
 *@创建时间	：Mar 28, 2016 11:00:09 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class JXLWorkbookBuilder{
	
	public static WritableWorkbook buildWorkbook(String path) throws IOException{
		return Workbook.createWorkbook(new File(path));
	}
	
	public void resizeRowHeight(WritableSheet sheet,int startRow,int rowheight) throws JxlWriteException {
		for (int i = startRow; i < sheet.getRows(); i++) {
			sheet.setRowView(i, buildCellView(rowheight));
		}
	}
	
	public void resizeColumnWidth(WritableSheet sheet,int rowIndex,int[] cellWidth) {
		Cell[] cells = sheet.getRow(rowIndex);
		for (int i = 0; i < cells.length; i++) {
			int width = cellWidth.length>i?cellWidth[i]:300;
			sheet.setColumnView(i,  buildCellView(width));
		}
	}
	
	public void mergeCell(WritableSheet sheet,int columnIndex,int rowIndex,int columnNum,int rowNum) throws JXLException{
		sheet.mergeCells(columnIndex, rowIndex, columnIndex+columnNum, rowIndex+rowNum);
	}
	
	public WritableCell buildCell(int columnIndex,int rowIndex,Object content,CellFormat format){
		if(content!=null){
			String tmpStr = String.valueOf(content);
			if(tmpStr.matches("/^\\d+$/ig")){
				return buildNumber(columnIndex, rowIndex, Double.parseDouble(tmpStr), format);
			}else{
				return buildLabel(columnIndex, rowIndex, content, format);
			}
		}else {
			return buildBlank(columnIndex, rowIndex,format);
		}
	}
	
	public CellView buildCellView(int width){
		CellView view = new CellView();
		view.setAutosize(true);
		view.setSize(width);
		return view;
	}
	
	public Label buildLabel(int columnIndex,int rowIndex,Object content,CellFormat format){
		return new Label(columnIndex, rowIndex,String.valueOf(content), format);
	}
	
	public Blank buildBlank(int columnIndex,int rowIndex,CellFormat format){
		return new Blank(columnIndex, rowIndex,format);
	}
	
	public Number buildNumber(int columnIndex,int rowIndex,double num,CellFormat format){
		return new Number(columnIndex, rowIndex,num, format);
	}
	
	/**
	 * 
	 * @description: 根据表头集合，创建一个只有表头的xls空文件
	 * @author : kangzhidong
	 * @date 下午8:16:20 2014-11-22 
	 * @param sheetName
	 * @param rowModel
	 * @param out
	 * @throws IOException
	 * @return  void 返回类型
	 * @throws WriteException 
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static <T extends CellModel> void buildTemplate(String sheetName,RowModel<T> rowModel,OutputStream tempOut) throws IOException, WriteException{
		//生成workbook（JXL）
		WritableWorkbook wb = Workbook.createWorkbook(tempOut);
		//创建sheet
		WritableSheet sheet = wb.createSheet(sheetName, 0);
		//构建表头
		JXLWorkbookFiller.fillRow(sheet, rowModel);
		//输出xls文档
		wb.write();
		wb.close();
		tempOut.flush();
		tempOut.close();
	}
	
	public static <T extends CellModel> void buildToLocal(WorkBookModel<T> dataModel,String outFilePath) throws IOException, WriteException{
		buildToStream(dataModel, new FileOutputStream(outFilePath));
	}
	
	public static <T extends CellModel> void buildToLocal(SheetModel<T> sheetModel,String outFilePath) throws IOException, WriteException{
		buildToStream(sheetModel, new FileOutputStream(outFilePath));
	}
	
	public static <T extends CellModel> void buildToLocal(String sheetName,List<RowModel<T>> dataList,String outFilePath) throws IOException, WriteException{
		buildToStream(sheetName, dataList,new FileOutputStream(outFilePath));
	}
	
	public static <T extends CellModel> void buildToClent(WorkBookModel<T> dataModel,HttpServletResponse response) throws IOException, WriteException{
		buildToStream(dataModel, response.getOutputStream());
	}
	
	public static <T extends CellModel> void buildToClent(SheetModel<T> sheetModel,HttpServletResponse response) throws IOException, WriteException{
		buildToStream(sheetModel, response.getOutputStream());
	}
	
	public static <T extends CellModel> void buildToClent(String sheetName,List<RowModel<T>> dataList,HttpServletResponse response) throws IOException, WriteException{
		buildToStream(sheetName, dataList,response.getOutputStream());
	}
	
	public static <T extends CellModel> void buildToStream(WorkBookModel<T> dataModel,OutputStream tempOut) throws IOException, WriteException{
		WritableWorkbook workbook = Workbook.createWorkbook(tempOut);
		JXLWorkbookFiller.fillSheets(workbook,dataModel);
		//输出xls文档
		workbook.write();
		workbook.close();
		tempOut.flush();
		tempOut.close();
	}
	
	public static <T extends CellModel> void buildToStream(SheetModel<T> sheetModel,OutputStream tempOut) throws IOException, WriteException{
		WritableWorkbook workbook = Workbook.createWorkbook(tempOut);
		JXLWorkbookFiller.fillSheets(workbook,sheetModel,0);
		//输出xls文档
		workbook.write();
		workbook.close();
		tempOut.flush();
		tempOut.close();
	}
	
	public static <T extends CellModel> void buildToStream(String sheetName,List<RowModel<T>> dataList,OutputStream tempOut) throws IOException, WriteException{
		WritableWorkbook workbook = Workbook.createWorkbook(tempOut);
		JXLWorkbookFiller.fillRows(workbook.createSheet(sheetName, 0),dataList,0);
		//输出xls文档
		workbook.write();
		workbook.close();
		tempOut.flush();
		tempOut.close();
	}
	
	public static <T extends CellModel> void buildWithTempleteToLocal(WorkBookModel<T> dataModel,String templetePath,String outFilePath) throws IOException, WriteException  {
		buildWithTempleteToStream(dataModel,templetePath,new FileOutputStream(outFilePath));
	}
	
	public static <T extends CellModel> void buildWithTempleteToLocal(SheetModel<T> sheetModel,String templetePath,String outFilePath) throws IOException, WriteException  {
		buildWithTempleteToStream(sheetModel,templetePath,new FileOutputStream(outFilePath));
	}
	
	public static <T extends CellModel> void buildWithTempleteToLocal(String sheetName,List<RowModel<T>> dataList,String templetePath,String outFilePath) throws IOException, WriteException  {
		buildWithTempleteToStream(sheetName,dataList,templetePath,new FileOutputStream(outFilePath));
	}
	
	public static <T extends CellModel> void buildWithTempleteToClent(WorkBookModel<T> dataModel,String templetePath,HttpServletResponse response) throws IOException, WriteException  {
		buildWithTempleteToStream(dataModel,templetePath,response.getOutputStream());
	}
	
	public static <T extends CellModel> void buildWithTempleteToClent(SheetModel<T> sheetModel,String templetePath,HttpServletResponse response) throws IOException, WriteException  {
		buildWithTempleteToStream(sheetModel,templetePath,response.getOutputStream());
	}
	
	public static <T extends CellModel> void buildWithTempleteToClent(String sheetName,List<RowModel<T>> dataList,String templetePath,HttpServletResponse response) throws IOException, WriteException  {
		buildWithTempleteToStream(sheetName,dataList,templetePath,response.getOutputStream());
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(WorkBookModel<T> dataModel,String templetePath,OutputStream tempOut) throws IOException, WriteException  {
		Workbook in = JXLWorkbookReader.getWorkbook(templetePath);
		buildWithTempleteToStream(dataModel,in,tempOut);
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(WorkBookModel<T> dataModel,Workbook template,OutputStream tempOut) throws IOException, WriteException  {
		WritableWorkbook workbook = Workbook.createWorkbook(tempOut, template);
		JXLWorkbookFiller.fillSheets(workbook,dataModel);
		//输出xls文档
		workbook.write();
		workbook.close();
		tempOut.flush();
		tempOut.close();
		template.close();
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(SheetModel<T> sheetModel,String templetePath,OutputStream tempOut) throws IOException, WriteException  {
		Workbook in = JXLWorkbookReader.getWorkbook(templetePath);
		buildWithTempleteToStream(sheetModel,in,tempOut);
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(SheetModel<T> sheetModel,Workbook template,OutputStream tempOut) throws IOException, WriteException  {
		WritableWorkbook workbook = Workbook.createWorkbook(tempOut, template);
		JXLWorkbookFiller.fillSheets(workbook,sheetModel,0);
		//输出xls文档
		workbook.write();
		workbook.close();
		tempOut.flush();
		tempOut.close();
		template.close();
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(String sheetName,List<RowModel<T>> dataList,String templetePath,OutputStream tempOut) throws IOException, WriteException  {
		Workbook in = JXLWorkbookReader.getWorkbook(templetePath);
		buildWithTempleteToStream(sheetName,dataList,in,tempOut);
	}
	
	public static <T extends CellModel> void buildWithTempleteToStream(String sheetName,List<RowModel<T>> dataList,Workbook template,OutputStream tempOut) throws IOException, WriteException  {
		
		WritableWorkbook workbook = Workbook.createWorkbook(tempOut, template);
		JXLWorkbookFiller.fillRows(workbook.createSheet(sheetName, 0),dataList,0);
		//输出xls文档
		workbook.write();
		workbook.close();
		tempOut.flush();
		tempOut.close();
		template.close();
	}
	

}



