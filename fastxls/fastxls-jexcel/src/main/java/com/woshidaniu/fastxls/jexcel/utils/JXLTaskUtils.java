package com.woshidaniu.fastxls.jexcel.utils;

import java.util.List;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ExecutorUtils;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.jexcel.task.WorkbookBuildTask;
/**
 * 
 * @className: JXLTaskUtils
 * @description: JXL导出线程构建助手
 * @author : kangzhidong
 * @date : 下午7:48:37 2014-11-22
 * @modify by:
 * @modify date :
 * @modify description :
 */
public final class JXLTaskUtils {
	

	protected static Logger LOG = LoggerFactory.getLogger(JXLTaskUtils.class);
	
	//创建固定大小的线程池
	public static <T extends CellModel> WorkbookBuildTask<T> getTask(WritableWorkbook wb,ArgumentsModel arguments,int index,List<RowModel<T>> list){
		//组织sheet名称
		String sheet_name = arguments.getSheetName()+"-"+index;
		//创建sheet
		WritableSheet sheet = wb.createSheet(sheet_name,index);
		//取得sheet允许容量
		int sheet_limit = arguments.getSheet_limit();
		//取得需要的sheet数量
		int sheet_book_num = ExecutorUtils.getThreadCount(list.size(), sheet_limit);
		//计算出当前页的数据行数
		int page_size = ExecutorUtils.getPageSize(list.size(), sheet_book_num, index);
		//线程名称
		//String task_name = MessageUtils.getText(ConfigConstants.THREAD_TASKNAME_KEY, new Object[]{sheet_name,"导出",index});
		
		//计算起始位置
		int fromIndex = index * sheet_limit;
			fromIndex = fromIndex>=page_size?page_size:fromIndex;
			
		arguments.setSheetIndex(index);
		arguments.setSheetName(sheet_name);
		//设置起始位置
		arguments.setOffset(fromIndex);
		arguments.setLimit(sheet_limit);
		//arguments.setTaskName(task_name);
	
		//记录状态日志
		//LOG.info(MessageUtils.getText(ConfigConstants.THREAD_STATUS_KEY, new Object[]{sheet_name,sheet_limit,task_name}));
		
		return new WorkbookBuildTask<T>(arguments,sheet,list);
	}
}
