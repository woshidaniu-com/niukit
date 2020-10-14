package com.woshidaniu.fastxls.poi.task.callback;
import org.apache.poi.ss.usermodel.Row;
/**
 * 
 * @className: RowEventCallback
 * @description: 行
 * @author : kangzhidong
 * @date : 下午5:25:20 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public interface RowEventCallback {
	
	public void doCallback(Row row);

}
