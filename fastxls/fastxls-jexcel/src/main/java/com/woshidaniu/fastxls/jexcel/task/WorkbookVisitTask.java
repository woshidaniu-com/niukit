package com.woshidaniu.fastxls.jexcel.task;

import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.jexcel.JXLWorkbookReader;
import com.woshidaniu.fastxls.jexcel.task.callback.CellEventCallback;
import com.woshidaniu.fastxls.jexcel.task.callback.RowEventCallback;
import com.woshidaniu.fastxls.jexcel.task.callback.SheetEventCallback;
import com.woshidaniu.fastxls.jexcel.utils.JXLConfigUtils;
/**
 * 
 * @className: WorkbookVisitTask
 * @description: JXL组件实现的Excel文档读取访问线程
 * @author : kangzhidong
 * @date : 下午4:43:07 2014-11-22
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class WorkbookVisitTask implements Runnable {
	
	protected static Logger LOG = LoggerFactory.getLogger(WorkbookVisitTask.class);
	private ArgumentsModel arguments;
	private Workbook workbook;
	private Sheet sheet;
	private SheetEventCallback sheetCallback;
	private RowEventCallback rowCallback;
	private CellEventCallback cellCallback;
	
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
			toIndex = toIndex>= sheet.getRows()?sheet.getRows():toIndex;
		//根据起始结束位获取部分row
		List<Cell[]> rows = JXLWorkbookReader.getRows(sheet, arguments.getOffset(), toIndex);
		if(rows!=null&&rows.size()>0){
			String runningInfo = JXLConfigUtils.getText(ConfigConstants.XLS_THREAD_RUNNING, new Object[]{arguments.getTaskName()});
			//获得起始行
			for (int rowIndex = 0;rowIndex < rows.size() ;rowIndex++) {
				//记录状态日志
				LOG.info(runningInfo);
				Cell[] row = rows.get(rowIndex);
				if (row != null ) {
					List<Cell> cells = JXLWorkbookReader.getCells(row);
					for (int i = 0; i < cells.size(); i++) {
						if(!BlankUtils.isBlank(cellCallback)){
							cellCallback.doCallback(workbook,cells.get(i));
						}
					}
				}
				if(!BlankUtils.isBlank(rowCallback)){
					rowCallback.doCallback(row);
				}
			}
			//记录状态日志
			LOG.info(JXLConfigUtils.getText(ConfigConstants.XLS_THREAD_COMPLETE, new Object[]{arguments.getTaskName()}));
		}
		if(!BlankUtils.isBlank(sheetCallback)){
			sheetCallback.doCallback(sheet);
		}
	}
}
