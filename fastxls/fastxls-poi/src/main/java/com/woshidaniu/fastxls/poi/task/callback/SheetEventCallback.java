package com.woshidaniu.fastxls.poi.task.callback;
import org.apache.poi.ss.usermodel.Sheet;
/**
 * 
 * @className: SheetEventCallback
 * @description: 线程完成当前sheet上的操作后的回调
 * @author : kangzhidong
 * @date : 下午5:28:11 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public interface SheetEventCallback {
	
	public void doCallback(Sheet sheet);

}
