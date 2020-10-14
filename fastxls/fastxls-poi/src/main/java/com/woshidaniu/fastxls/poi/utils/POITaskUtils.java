package com.woshidaniu.fastxls.poi.utils;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ExecutorUtils;
import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.poi.task.WorkbookBuildTask;


public  final class POITaskUtils {

	protected static Logger LOG = LoggerFactory.getLogger(POITaskUtils.class);
	
	public static <T extends CellModel> WorkbookBuildTask<T> getTask(Workbook wb,ArgumentsModel arguments,int index,List<RowModel<T>> list){
		//组织sheet名称
		String sheet_name = arguments.getSheetName()+"-"+index;
		//创建一个sheet
		Sheet sheet = wb.createSheet(sheet_name);
		//取得sheet允许容量
		int sheet_limit = arguments.getSheet_limit();
		//取得需要的sheet数量
		int sheet_book_num = ExecutorUtils.getThreadCount(list.size(), sheet_limit);
		//计算出当前页的数据行数
		int page_size = ExecutorUtils.getPageSize(list.size(), sheet_book_num, index);
		//线程名称
		String task_name = POIConfigUtils.getText(ConfigConstants.XLS_THREAD_RUNNING, new Object[]{sheet_name,"生成",index});
		//计算起始位置
		int fromIndex = index * sheet_limit;
			fromIndex = fromIndex>=page_size?page_size:fromIndex;
		
		arguments.setSheetIndex(index);
		arguments.setSheetName(sheet_name);
		//设置起始位置
		arguments.setOffset(fromIndex);
		arguments.setLimit(sheet_limit);
		arguments.setTaskName(task_name);
		//记录状态日志
		LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_COMPLETE, new Object[]{sheet_name,sheet_limit,task_name}));
		//向线程池增加一个线程任务
		return new WorkbookBuildTask<T>(arguments,sheet,list);
	}
	
}
