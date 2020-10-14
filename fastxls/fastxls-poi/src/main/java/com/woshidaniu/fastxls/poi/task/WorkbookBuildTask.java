package com.woshidaniu.fastxls.poi.task;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.poi.POIWorkbookFiller;
import com.woshidaniu.fastxls.poi.utils.POIConfigUtils;
/**
 * 
 *@类名称	: WorkbookBuildTask.java
 *@类描述	：POI组件实现的Excel数据提取线程
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 8:54:35 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <T>
 */
public class WorkbookBuildTask<T extends CellModel> implements Runnable {
	
	protected static Logger LOG = LoggerFactory.getLogger(WorkbookBuildTask.class);
	protected ArgumentsModel arguments;
	protected List<RowModel<T>> list;
	protected Sheet sheet;
	
	public WorkbookBuildTask(
			ArgumentsModel arguments,
			Sheet sheet,
			List<RowModel<T>> collection){
		this.arguments = arguments;
		this.sheet = sheet;
		this.list = collection;
	}
	
	public void run() {
		
		if(list!=null&&list.size()>0){
			//计算结束位置
			int toIndex = arguments.getOffset() + arguments.getLimit() - 1;
				toIndex = toIndex>= sheet.getLastRowNum()?sheet.getLastRowNum():toIndex;
			//截取集合片段
			List<RowModel<T>> rowList = list.subList(arguments.getOffset() , toIndex);
			int rowIndex = 0;
			boolean isIn = true;
			Row row = null;
			//迭代循环当前配置的列
			Iterator<RowModel<T>> iterator = rowList.iterator();
			String runningInfo = POIConfigUtils.getText(ConfigConstants.XLS_THREAD_RUNNING, new Object[]{arguments.getTaskName()});
			while (iterator.hasNext()&&isIn == true) {
				//记录状态日志
				LOG.info(runningInfo);
				//判断当前线程内行下标是否在限制范围内，否则跳过交给其他线程处理
				if(rowIndex<=arguments.getLimit()){
					RowModel<T> rowModel = iterator.next();
					LOG.info(arguments.getTaskName()+"-正在执行...");
					//创建xls行对象
					row = sheet.createRow((arguments.getOffset()==0?1:arguments.getOffset())+rowIndex);
					//渲染数据
					POIWorkbookFiller.fillRow(row, rowModel);
					isIn = true;
					rowIndex++;
				}else{
					//改变参数，终止循环
					isIn = false;
					
				}
			}
			//记录状态日志
			LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_COMPLETE, new Object[]{arguments.getTaskName()}));
		}
		
	}
	
}
