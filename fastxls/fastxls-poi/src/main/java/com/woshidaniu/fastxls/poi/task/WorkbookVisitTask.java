package com.woshidaniu.fastxls.poi.task;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.poi.POIWorkbookReader;
import com.woshidaniu.fastxls.poi.task.callback.CellEventCallback;
import com.woshidaniu.fastxls.poi.task.callback.RowEventCallback;
import com.woshidaniu.fastxls.poi.task.callback.SheetEventCallback;
import com.woshidaniu.fastxls.poi.utils.POIConfigUtils;
/**
 * 
 *@类名称	: WorkbookVisitTask.java
 *@类描述	：POI组件实现的Excel文档读取访问线程
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 8:55:38 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class WorkbookVisitTask implements Runnable {
	
	protected static Logger LOG = LoggerFactory.getLogger(WorkbookVisitTask.class);
	protected ArgumentsModel arguments;
	protected Sheet sheet;
	protected SheetEventCallback sheetCallback;
	protected RowEventCallback rowCallback;
	protected CellEventCallback cellCallback;
	
	public WorkbookVisitTask(ArgumentsModel arguments,Sheet sheet,SheetEventCallback sheetCallback){
		this.arguments = arguments;
		this.sheet = sheet;
		this.sheetCallback = sheetCallback;
	}
	
	public WorkbookVisitTask(ArgumentsModel arguments,Sheet sheet,RowEventCallback rowCallback){
		this.arguments = arguments;
		this.sheet = sheet;
		this.rowCallback = rowCallback;
	}
	
	public WorkbookVisitTask(ArgumentsModel arguments,Sheet sheet,CellEventCallback cellCallback){
		this.arguments = arguments;
		this.sheet = sheet;
		this.cellCallback = cellCallback;
	}
	

	public void run() {
		//计算结束位置
		int toIndex = arguments.getOffset() + arguments.getLimit() - 1;
			toIndex = toIndex>= sheet.getLastRowNum()?sheet.getLastRowNum():toIndex;
		//根据起始结束位获取部分row
		List<Row> rows = POIWorkbookReader.getRows(sheet, arguments.getOffset(), toIndex);
		if(rows!=null&&rows.size()>0){
			String runningInfo = POIConfigUtils.getText(ConfigConstants.XLS_THREAD_RUNNING, new Object[]{arguments.getTaskName()});
			//获得起始行
			for (int rowIndex = 0;rowIndex < rows.size() ;rowIndex++) {
				//记录状态日志
				LOG.info(runningInfo);
				Row row = rows.get(rowIndex);
				if (row != null ) {
					List<Cell> cells = POIWorkbookReader.getCells(row);
					for (int i = 0; i < cells.size(); i++) {
						if(!BlankUtils.isBlank(cellCallback)){
							cellCallback.doCallback(sheet.getWorkbook(),cells.get(i));
						}
					}
				}
				if(!BlankUtils.isBlank(rowCallback)){
					rowCallback.doCallback(row);
				}
			}
			//记录状态日志
			LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_COMPLETE, new Object[]{arguments.getTaskName()}));
		}
		if(!BlankUtils.isBlank(sheetCallback)){
			sheetCallback.doCallback(sheet);
		}
	}
}
