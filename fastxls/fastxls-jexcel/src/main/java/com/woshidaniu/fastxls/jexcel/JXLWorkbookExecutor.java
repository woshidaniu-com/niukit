package com.woshidaniu.fastxls.jexcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ExecutorUtils;
import com.woshidaniu.compress.ZipUtils;
import com.woshidaniu.fastxls.core.SplitType;
import com.woshidaniu.fastxls.core.model.ArgumentsModel;
import com.woshidaniu.fastxls.core.model.CellModel;
import com.woshidaniu.fastxls.core.model.RowModel;
import com.woshidaniu.fastxls.jexcel.task.WorkbookVisitTask;
import com.woshidaniu.fastxls.jexcel.task.callback.CellEventCallback;
import com.woshidaniu.fastxls.jexcel.utils.JXLTaskUtils;

/**
 * 
 * @className: JXLWorkbookExecutor
 * @description: TODO(描述这个类的作用)
 * @author : kangzhidong
 * @date : 下午5:50:10 2014-11-22
 * @modify by:
 * @modify date :
 * @modify description :
 */
public abstract class JXLWorkbookExecutor {
	
	protected static Logger LOG = LoggerFactory.getLogger(JXLWorkbookExecutor.class);
	
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
		
		//LOG.info(MessageUtils.getText(ConfigConstants.THREAD_INFO_KEY, new Object[]{arguments.getSheetName(),list.size(),sheet_limit,thread_num}));
		//判断数据过多时候采用拆分方式sheet:多个工作簿|wookbook：多个xls文件，默认：sheet 即 拆分为多个工作簿
		if(SplitType.SHEET.equals(arguments.getSplitType())){
			//生成workbook（JXL）
			WritableWorkbook wb = Workbook.createWorkbook(outStream);
			//逐个创建Sheet,一页的结果集代表着一个sheet
			for (int sheetIndex = 0; sheetIndex < thread_num; sheetIndex++) {
				//向线程池增加一个线程任务
				executor.execute(JXLTaskUtils.getTask(wb, arguments, sheetIndex, list));
			}
			ExecutorUtils.shutdown(executor, new Object[]{taskName});
		    try {
				//输出文档
				wb.write();
				wb.close();
			} catch (WriteException e) {
				LOG.error(e.getMessage(), e.getCause());
			}
			outStream.flush();
		}else if(SplitType.WOOKBOOK.equals(arguments.getSplitType())){
			//逐个创建Wookbook,一页的结果集代表着一个Wookbook
			List<File> tempFiles = new ArrayList<File>();
			List<WritableWorkbook> tempWorkbooks = new ArrayList<WritableWorkbook>();
			//获得sheet文件名
			String sheet_name = arguments.getSheetName();
			for (int wookbookIndex = 0; wookbookIndex < thread_num; wookbookIndex++) {
				//创建临时文件
				File tempFile = new File(tmpDir.getAbsolutePath()+File.separator+sheet_name+"-"+wookbookIndex+".xls");
				tempFiles.add(tempFile);
				//创建输出流
				FileOutputStream tempOut = new FileOutputStream(tempFile);
				//生成workbook（JXL）
				WritableWorkbook wb = Workbook.createWorkbook(tempOut);
				tempWorkbooks.add(wb);
				//向线程池增加一个线程任务
				executor.execute(JXLTaskUtils.getTask(wb, arguments, wookbookIndex, list));
			}
			//等待所有任务完成 
			ExecutorUtils.shutdown(executor, new Object[]{taskName});
			//输出处理后的workbook
			for (WritableWorkbook workbook : tempWorkbooks) {
				try {
					//输出一个xls文档
					workbook.write();
					workbook.close();
				} catch (WriteException e) {
					LOG.error(e.getMessage(), e.getCause());
				}
			}
			//压缩打包文档，并输出
			ZipUtils.compressFiles(tempFiles,outStream,tmpDir.getAbsolutePath());
		}
		//记录状态日志
		//LOG.info(MessageUtils.getText(ConfigConstants.THREAD_TIME_KEY, new Object[]{taskName,(System.currentTimeMillis() - currentMs)/1000}));
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
	 * @throws IOException 
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void visitWorkbook(ArgumentsModel arguments,String workbook,CellEventCallback cellEventCallback) throws IOException{
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
		Sheet[] sheets = JXLWorkbookReader.getSheets(workbook);
	    //循环sheet
		for (int index = 0; index < sheets.length; index++) {
			//第 n 个 sheet
			Sheet sheet = sheets[index];
			//总行数
			int count = sheet.getRows();
			if(count > 0 ){
				//得到当前sheet 的总行数，并计算当前sheet需要多少个线程进行处理
				int thread_num = ExecutorUtils.getThreadCount(count, batch_size);
				//记录状态日志
				//LOG.info(MessageUtils.getText(ConfigConstants.THREAD_INFO_KEY, new Object[]{sheet.getName(),count,batch_size,thread_num}));
				//循环线程数，准备数据
				for(int thread_index= 0;thread_index<thread_num;thread_index++){
					
					int fromIndex = thread_index * batch_size;
						fromIndex = fromIndex==0?1:fromIndex;
						fromIndex = fromIndex>=count?count:fromIndex;
						
					arguments.setSheetIndex(index);
					arguments.setOffset(fromIndex);
					arguments.setLimit(batch_size);
					arguments.setSheetName(sheet.getName());
					//线程名称
					//String task_name = MessageUtils.getText(ConfigConstants.THREAD_TASKNAME_KEY, new Object[]{sheet.getName(),arguments.getTaskName(),thread_index});
					//arguments.setTaskName(task_name);
					//记录状态日志
					//LOG.info(MessageUtils.getText(ConfigConstants.THREAD_STATUS_KEY, new Object[]{sheet.getName(),count,task_name}));
					//添加一个内容检查线程
					executor.execute(new WorkbookVisitTask(arguments, sheet , cellEventCallback)); 
				}
			}
		}
	    //等待所有任务完成 
		ExecutorUtils.shutdown(executor, new Object[]{taskName});
		//记录状态日志
		//LOG.info(MessageUtils.getText(ConfigConstants.THREAD_TIME_KEY, new Object[]{taskName,(System.currentTimeMillis() - currentMs)/1000}));
	}
	

}
