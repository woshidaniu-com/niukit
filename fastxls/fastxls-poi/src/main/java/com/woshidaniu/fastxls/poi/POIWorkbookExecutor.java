package com.woshidaniu.fastxls.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ExecutorUtils;
import com.woshidaniu.compress.ZipUtils;
import com.woshidaniu.fastxls.core.SplitType;
import com.woshidaniu.fastxls.core.constant.ConfigConstants;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.poi.task.WorkbookVisitTask;
import com.woshidaniu.fastxls.poi.task.callback.CellEventCallback;
import com.woshidaniu.fastxls.poi.utils.POIConfigUtils;
import com.woshidaniu.fastxls.poi.utils.POITaskUtils;
import com.woshidaniu.fastxls.poi.utils.POIWorkbookUtils;

 /**
  * 
  * @package com.woshidaniu.imexport.document.builder.poi
  * @className: POIWorkbookChecker
  * @description: xls文档内容检查
  * @author : kangzhidong
  * @date : 2014-4-15
  * @time : 下午03:28:36
  */
public abstract class POIWorkbookExecutor{
	
	protected static Logger LOG = LoggerFactory.getLogger(POIWorkbookExecutor.class);
	
	public static <T extends CellModel> void buildWorkbook(ArgumentsModel arguments,File tmpDir,OutputStream outStream,List<RowModel<T>> list) throws IOException  {
		if(CollectionUtils.isEmpty(list)){
			return;
		}
		long currentMs = System.currentTimeMillis();
		//获取数据验证操作允许的最大线程数
		int thread_max = arguments.getThread_max();
		//固定大小的创建线程池
		ExecutorService executor = Executors.newFixedThreadPool(thread_max);
		//取得sheet允许容量
		int sheet_limit = arguments.getSheet_limit();
		//得到当前集合 的总记录数，并计算需要多少个线程进行处理
		int thread_num = ExecutorUtils.getThreadCount(list.size(), sheet_limit);
		//取得初始任务名称
		String taskName = arguments.getTaskName();
		//记录状态日志
		LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_INFO, new Object[]{arguments.getSheetName(),list.size(),sheet_limit,thread_num}));
		//判断数据过多时候采用拆分方式sheet:多个工作簿|wookbook：多个xls文件，默认：sheet 即 拆分为多个工作簿
		if(SplitType.SHEET.equals(arguments.getSplitType())){
			//创建一个workbook（POI）
			Workbook wb = POIWorkbookUtils.getWorkbook(arguments);
			//逐个创建Sheet,一页的结果集代表着一个sheet
			for (int sheetIndex = 0; sheetIndex < thread_num; sheetIndex++) {
				//向线程池增加一个线程任务
				executor.execute(POITaskUtils.getTask(wb, arguments, sheetIndex, list ));
			}
			//等待所有任务完成
			ExecutorUtils.shutdown(executor, new Object[]{taskName});
		    //输出文档
			wb.write(outStream);
			outStream.flush();
			
		}else if(SplitType.WOOKBOOK.equals(arguments.getSplitType())){
			//文件集合
			List<File> tempFiles = Collections.synchronizedList(new ArrayList<File>());
			List<Workbook> tempWorkbooks = Collections.synchronizedList(new ArrayList<Workbook>());
			String sheet_name = arguments.getSheetName();
			//逐个创建Wookbook,一页的结果集代表着一个Wookbook；循环每个Workbook的数据
			for (int workbookIndex = 0; workbookIndex < thread_num; workbookIndex++) {
				//创建一个workbook（POI）
				Workbook wb = POIWorkbookUtils.getWorkbook(arguments);
				//向线程池增加一个线程任务
				executor.execute(POITaskUtils.getTask(wb,arguments,workbookIndex, list));
				//创建文件路径
				File tempFile = new File(tmpDir.getAbsolutePath()+File.separator+sheet_name+"-"+workbookIndex+arguments.getSuffix().toString());
				//缓存路径和Workbook
				tempFiles.add(tempFile);
				tempWorkbooks.add(wb);
			}
			//等待所有任务完成
			ExecutorUtils.shutdown(executor, new Object[]{taskName});
		    //输出文档
		    for (int i = 0; i < tempWorkbooks.size(); i++) {
		    	Workbook wb = tempWorkbooks.get(i);
				FileOutputStream tempOut = new FileOutputStream(tempFiles.get(i));
		    	wb.write(tempOut);
		    	tempOut.flush();
				tempOut.close();
			}
		    //压缩打包文档，并输出
		    ZipUtils.compressFiles(tempFiles,outStream);
		}
		//记录状态日志
		LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_COMPLETE, new Object[]{taskName,(System.currentTimeMillis() - currentMs)/1000}));
	}

	/**
	 * 
	 * @description: Excel 工作簿 范围多线程访问访问，可借助此方法实现多线程同时进行内容检查，单元格数据校验
	 * @author : kangzhidong
	 * @date 下午8:52:39 2014-11-21 
	 * @param arguments
	 * @param workbook
	 * @param cellEventCallback
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void visitWorkbook(ArgumentsModel arguments,Workbook workbook,CellEventCallback cellEventCallback){
		long currentMs = System.currentTimeMillis();
		//获取数据验证操作允许的最大线程数
		int thread_max = arguments.getThread_max();
		//获取数据验证操作允许的单线程处理量
		int batch_size = arguments.getBatch_size();
		//取得初始任务名称
		String taskName = arguments.getTaskName();
		//创建固定容量线程池
		ExecutorService executor = Executors.newFixedThreadPool(thread_max);
		//获取当前会话中上传的xls文件的sheet
		Sheet[] sheets = POIWorkbookReader.getSheets(workbook);
	    //循环sheet
		for (int index = 0; index < sheets.length; index++) {
			//第 n 个 sheet
			Sheet sheet = sheets[index];
			//总行数
			int count = sheet.getLastRowNum();
			if(count > 0 ){
				//得到当前sheet 的总行数，并计算当前sheet需要多少个线程进行处理
				int thread_num = ExecutorUtils.getThreadCount(count, batch_size);
				//记录状态日志
				LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_INFO, new Object[]{sheet.getSheetName(),count,batch_size,thread_num}));
				//循环线程数，准备数据
				for(int thread_index= 0;thread_index<thread_num;thread_index++){
					
					int fromIndex = thread_index * batch_size;
						fromIndex = fromIndex==0?1:fromIndex;
						fromIndex = fromIndex>=count?count:fromIndex;
						
					arguments.setSheetIndex(index);
					arguments.setOffset(fromIndex);
					arguments.setLimit(batch_size);
					arguments.setSheetName(sheet.getSheetName());
					//线程名称
					String task_name = POIConfigUtils.getText(ConfigConstants.XLS_THREAD_TASKNAME, new Object[]{sheet.getSheetName(),arguments.getTaskName(),thread_index});
					arguments.setTaskName(task_name);
					//记录状态日志
					LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_STATUS, new Object[]{sheet.getSheetName(),count,task_name}));
					//添加一个内容检查线程
					executor.execute(new WorkbookVisitTask(arguments, sheet , cellEventCallback)); 
				}
			}
		}
	    //等待所有任务完成 
		ExecutorUtils.shutdown(executor, new Object[]{taskName});
		//记录状态日志
		LOG.info(POIConfigUtils.getText(ConfigConstants.XLS_THREAD_TIME, new Object[]{taskName,(System.currentTimeMillis() - currentMs)/1000}));
	}
	
}



