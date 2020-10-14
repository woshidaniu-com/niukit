package com.woshidaniu.fastxls.jexcel.task;

import java.util.Iterator;
import java.util.List;

import jxl.write.WritableSheet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.jexcel.JXLWorkbookFiller;
import com.woshidaniu.fastxls.jexcel.utils.JXLConfigUtils;
/**
 * 
 * @className: WorkbookBuildTask
 * @description: JXL组件实现的Excel数据提取线程
 * @author : kangzhidong
 * @date : 下午6:11:00 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class WorkbookBuildTask<T extends CellModel> implements Runnable {
	
	protected static Logger LOG = LoggerFactory.getLogger(WorkbookBuildTask.class);
	
	private ArgumentsModel arguments;
	private List<RowModel<T>> list;
	private WritableSheet sheet;
	
	public WorkbookBuildTask(
			ArgumentsModel arguments,
			WritableSheet sheet,
			List<RowModel<T>> collection){
		this.arguments = arguments;
		this.sheet = sheet;
		this.list = collection;
	}
	
	public void run() {
		
		if(!BlankUtils.isBlank(list)){
			//计算结束位置
			int toIndex = arguments.getOffset() + arguments.getLimit() - 1;
				toIndex = toIndex>= sheet.getRows()?sheet.getRows():toIndex;
			//截取集合片段
			List<RowModel<T>> rowList = list.subList(arguments.getOffset() , toIndex);
			int rowIndex = 0;
			boolean isIn = true;
			//迭代循环当前配置的列
			Iterator<RowModel<T>> iterator = rowList.iterator();
			String runningInfo = JXLConfigUtils.getText(ConfigConstants.XLS_THREAD_RUNNING, new Object[]{arguments.getTaskName()});
			while (iterator.hasNext()&&isIn == true) {
				//记录状态日志
				LOG.info(runningInfo);
				//判断当前线程内行下标是否在限制范围内，否则跳过交给其他线程处理
				if(rowIndex<=arguments.getLimit()){
					RowModel<T> rowModel = iterator.next();
					//渲染数据
					JXLWorkbookFiller.fillRow(sheet, rowModel);
					isIn = true;
					rowIndex++;
				}else{
					//改变参数，终止循环
					isIn = false;
					
				}
			}
			//记录状态日志
			LOG.info(JXLConfigUtils.getText(ConfigConstants.XLS_THREAD_COMPLETE, new Object[]{arguments.getTaskName()}));
		}
		
	}
	
}
