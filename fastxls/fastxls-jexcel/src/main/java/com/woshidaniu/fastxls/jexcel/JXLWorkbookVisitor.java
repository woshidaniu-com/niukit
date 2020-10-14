package com.woshidaniu.fastxls.jexcel;

import java.util.ArrayList;
import java.util.List;

import com.woshidaniu.fastxls.core.model.PairModel;

import jxl.Cell;
import jxl.Sheet;

 /**
  * 
  * @className: JXLWorkbookVisitor
  * @description: Excel文档内容访问
  * @author : kangzhidong
  * @date : 下午5:19:40 2014-11-22
  * @modify by:
  * @modify date :
  * @modify description :
  */
public abstract class JXLWorkbookVisitor{
	
	public static List<PairModel> getColumns(String filepath) throws Exception {
		List<PairModel> result = new ArrayList<PairModel>();
		// 获得上传xls文件的表头
		Sheet sheet = (Sheet) JXLWorkbookReader.getSheets(filepath)[0];
		//获得第一行的单元格
		Cell[] cells = sheet.getRow(0);
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			//单元格内容
			String  value = JXLWorkbookReader.getCellValue(cell);
			result.add(new PairModel(cell.getColumn()+"", value));
		}
		return result;
	}

	
}



