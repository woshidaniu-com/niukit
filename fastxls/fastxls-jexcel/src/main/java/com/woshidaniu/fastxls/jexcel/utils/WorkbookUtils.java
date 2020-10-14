package com.woshidaniu.fastxls.jexcel.utils;

import java.util.Iterator;
import java.util.List;

import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;

/**
 * @title: WorkbookUtils.java
 * @package com.fastkit.workbook.utils
 * @fescription: TODO(添加描述)
 * @author: kangzhidong
 * @date : 下午3:47:07 2014-11-21
 */
public class WorkbookUtils {
	
	public static <T extends CellModel> int getLastNumOfRow(List<RowModel<T>> data) {
		int num = 0;
		for (RowModel<T> map : data) {
			num = Math.max(num,map.getRowNum());
		}
		return num;
	}
	
	public static <T extends CellModel> int getLastNumOfCell(RowModel<T> rowModel) {
		Iterator<String> iterator = rowModel.keySet().iterator();
		int num = 0;
		while (iterator.hasNext()) {
			T item = rowModel.get(iterator.next());
			num = Math.max(num,item.getColumnIndex());
		}
		return num;
	}
	
}
