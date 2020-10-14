package com.woshidaniu.basicutils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *@类名称	: ExecutorUtils.java
 *@类描述	：线程Executor工具类
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 5:17:15 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class ExecutorUtils {

	protected static Logger LOG = LoggerFactory.getLogger(ExecutorUtils.class);
	
	public static int getPageSize(int count,int thread_num,int page_index){
		int page_size = (count - count%thread_num)/thread_num;
		int page_count = (count - count%thread_num)/(page_size==0?1:page_size);
		if(count%thread_num>0){
			if(page_index>page_count+1){
				throw new IndexOutOfBoundsException("page_index out of index !");
			}else {
				if(page_index==page_count+1){
					return count%thread_num;
				}else {
					return page_size;
				}
			}
		}else{
			if(page_index>page_count){
				throw new IndexOutOfBoundsException("page_index out of index !");
			}else {
				return page_size;
			}
		}
	}

	public static int getThreadCount(int count,int batch_size){
		//得到当前sheet 的总行数，并计算当前sheet需要多少个线程进行导入操作
		int thread_num = 1;
		if(count > batch_size){
			thread_num = (count - count%batch_size)/batch_size;
			if(count%batch_size>0){
				thread_num = thread_num+1;
			}
		}
		return thread_num;
	}
	
	/**
	 * 
	 *@描述：1、调用shutdown()停止接受任何新的任务且等待已经提交的任务执行完成
	 *		 2、调用awaitTermination(1, TimeUnit.SECONDS)每隔一秒监测一次ExecutorService的关闭情况
	 *@创建人:kangzhidong
	 *@创建时间:2015-3-12下午03:47:05
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param executor
	 *@return
	 */
	public static int shutdownWithLog(ExecutorService executor){
		long time =	System.currentTimeMillis();
		try {
			/**
			 * 这个方法会平滑地关闭ExecutorService，当我们调用这个方法时，
			 * ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成
			 * (已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
			 * 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
			 */
			executor.shutdown(); 
			//=====================等待所有任务完成 =========================
			/**
			 * 这个方法有两个参数，一个是timeout即超时时间，另一个是unit即时间单位。
			 * 这个方法会使线程等待timeout时长，当超过timeout时间后，会监测ExecutorService是否已经关闭，
			 * 若关闭则返回true，否则返回false。
			 * 一般情况下会和shutdown方法组合使用
			 * 
			 * 注意：这里每隔一秒监测一次ExecutorService的关闭情况
			 */
			while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {  
				//记录运行状态信息
				LOG.info("Thread [" + Thread.currentThread().getName() + "] Runing...");
			}   
			//记录完成信息
			LOG.info("Thread [" + Thread.currentThread().getName() + "] Runing.");
	    } catch (InterruptedException ie) {
		   executor.shutdownNow();
	    }
	    //返回任务执行时间
		return Double.valueOf(Math.ceil(System.currentTimeMillis() - time / 1000)).intValue();
	}
	
	public static int shutdown(ExecutorService executor){
		/**
		 * 这个方法会平滑地关闭ExecutorService，当我们调用这个方法时，
		 * ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成
		 * (已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
		 * 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
		 */
		executor.shutdown(); 
		//=====================等待所有任务完成 =========================
		/**
		 * 这个方法会校验ExecutorService当前的状态是否为“TERMINATED”即关闭状态，当为“TERMINATED”时返回true否则返回false
		 * 注意：该方法执行循环较快，可做完成时间统计
		 */
		long time =	System.currentTimeMillis();
		while (!executor.isTerminated()) {  
		}
		//返回任务执行时间
		return Double.valueOf(Math.ceil(System.currentTimeMillis() - time / 1000)).intValue();
	}
	
	public static void shutdown(ExecutorService executor,Object[] strs){
		try {
			executor.shutdown();//调用这个方法时，ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成
			//(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，当所有已经提交的任务执行完毕后将会关闭ExecutorService。
			//等待所有任务完成 
			//每隔一秒监测一次ExecutorService的关闭情况
			while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {  
				//记录运行状态信息
				//LOG.info(MessageUtils.getText("imexport.info.runing", strs));   
			}   
			//记录完成信息
			//LOG.info(MessageUtils.getText("imexport.info.complete", strs)); 
	    } catch (InterruptedException ie) {
		   executor.shutdownNow();
	    }
		
	}
	
	
	
	
}
