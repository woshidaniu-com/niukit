package com.woshidaniu.fastxls.core.constant;

public abstract class ConfigConstants {
	
	//---------UI组件---------------------------------------
	
	/**
	 * Key[workbook.font.names] : XLS默认初始的字体名称
	 */
	public static String XLS_FONT_NAMES = "workbook.font.names";
	
	//---------线程信息---------------------------------------
	
	/**
	 * Key[workbook.thread.info] : 使用线程池进行workbook 创建或者读取时线程信息key
	 */
	public static String XLS_THREAD_INFO = "workbook.thread.info";
	/**
	 * Key[workbook.thread.taskName] : 使用线程池进行workbook 创建或者读取时线程名称模板Key
	 */
	public static String XLS_THREAD_TASKNAME = "workbook.thread.taskName";
	/**
	 * Key[workbook.thread.status] : 使用线程池进行workbook 创建或者读取时线程状态信息模板Key
	 */
	public static String XLS_THREAD_STATUS = "workbook.thread.status";
	/**
	 * Key[workbook.thread.runing] : 使用线程池进行workbook 创建或者读取时线程允许时信息模板Key
	 */
	public static String XLS_THREAD_RUNNING = "workbook.thread.runing";
	/**
	 * Key[workbook.thread.complete] : 使用线程池进行workbook 创建或者读取时一个子线程执行完成信息模板Key
	 */
	public static String XLS_THREAD_COMPLETE = "workbook.thread.complete";
	/**
	 * Key[workbook.thread.time] : 使用线程池进行workbook 创建或者读取时线程池执行完成使用时间信息模板Key
	 */
	public static String XLS_THREAD_TIME = "workbook.thread.time";
	
}