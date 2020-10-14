package com.woshidaniu.fastxls.poi.task.callback;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
/**
 * 
 * @className: CellEventCallback
 * @description: 表格检查线程回调
 * @author : kangzhidong
 * @date : 下午5:24:22 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public interface CellEventCallback {
	
	public void doCallback(Workbook workbook,Cell cell);

}
