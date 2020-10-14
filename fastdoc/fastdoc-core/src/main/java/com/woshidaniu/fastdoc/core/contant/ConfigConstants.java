package com.woshidaniu.fastdoc.core.contant;

public final class ConfigConstants {
	
	//---------初始化---------------------------------------
	
	/**
	 * Key[document.config.encoding] : 编码格式 ，默认： UTF-8
	 */
	public static  String DOC_CONFIG_ENCODING = "document.config.encoding";
	
	/**
	 * Key[document.config.support] : 用于导入导出的类包支持 POI或者JXL，默认： POI
	 */
	public static  String DOC_CONFIG_SUPPORT = "document.config.support";
	
	//---------UI组件---------------------------------------
	/**
	 * Key[document.uiwidget.width] : 页面document组件的宽度，默认：800px
	 */
	public static String DOC_UIWIDGET_WIDTH = "document.uiwidget.width";
	/**
	 * Key[document.uiwidget.width] : 页面document组件的高度，默认：500px
	 */
	public static String DOC_UIWIDGET_HEIGHT = "document.uiwidget.height";

	//---------存储---------------------------------------
	/**
	 * Key[document.store.dir] : document组件使用的根目录  ，默认 ：documentDir
	 */
	public static String DOC_STORE_DIR = "document.store.dir";
	/**
	 * Key[document.store.tmpDir] : xls 临时文件存储路径 ，默认 ：tmpDir
	 */
	public static String DOC_STORE_TMPDIR = "document.store.tmpDir";
	/**
	 * Key[document.store.template] : xls 模板文件存储路径 ，默认 ：templateDir
	 */
	public static String DOC_STORE_TEMPLATE = "document.store.template";
	/**
	 * Key[document.store.prefix] : xls 文件存储时，文件名后的前缀字符串 【前缀字符串-文件名.xls】 ，默认 ：空
	 */
	public static String DOC_STORE_PREFIX = "document.store.prefix";
	/**
	 * Key[document.store.suffix] : xls 文件存储时，文件名后的后缀字符串 【文件名-后缀字符串.xls】生成方式 ，可选【Date,UUID】，默认 ：UUID
	 */
	public static String DOC_STORE_SUFFIX = "document.store.suffix";
	
	//---------验证---------------------------------------
	/**
	 * Key[document.validate.thread.max] : 每次的数据验证中允许创建的线程池最大容量，默认：20.
	 */
	public static String DOC_VALIDATE_THREAD_MAX = "document.validate.thread.max";
	/**
	 * Key[document.validate.thread.batchSize] : 数据验证xls文件时,单个线程最大处理行,默认 500.
	 */
	public static String DOC_VALIDATE_THREAD_BATCHSIZE = "document.validate.thread.batchSize";
	
	//---------导入---------------------------------------
	/**
	 * Key[document.import.store] : 是否存储导入的文件 . 默认 false.
	 */
	public static String DOC_IMPORT_STORE = "document.import.store";
	/**
	 * Key[document.import.store.prefix] : 是否在存储导入的文件时，文件名称加上前缀字符串  【前缀字符串-文件名.xls】, 默认 false
	 */
	public static String DOC_IMPORT_STORE_PREFIX = "document.import.store.prefix";
	/**
	 * Key[document.import.store.suffix] : 是否在存储导入的文件时，文件名称加上后缀字符串  【文件名-后缀字符串.xls】, 默认 false
	 */
	public static String DOC_IMPORT_STORE_SUFFIX = "document.import.store.suffix";
	/**
	 * Key[document.import.thread.max] : 每次的导入中允许创建的线程池最大容量，默认：20.
	 */
	public static String DOC_IMPORT_THREAD_MAX = "document.import.thread.max";
	/**
	 * Key[document.import.thread.batchSize] : 导入xls文件时,单个线程最大处理行,默认 500.
	 */
	public static String DOC_IMPORT_THREAD_BATCHSIZE = "document.import.thread.batchSize";

	
	//---------导出---------------------------------------
	/**
	 * Key[document.export.store] :是否在存储导出的文件 . 默认 false.
	 */
	public static String DOC_EXPORT_STORE = "document.export.store";
	/**
	 * Key[document.export.store.prefix] : 是否在存储导出的文件时，文件名称加上前缀字符串  【前缀字符串-文件名.xls】, 默认 false
	 */
	public static String DOC_EXPORT_STORE_PREFIX = "document.export.store.prefix";
	/**
	 * Key[document.export.store.suffix] : 是否在存储导出的文件时，文件名称加上后缀字符串  【文件名-后缀字符串.xls】, 默认 false
	 */
	public static String DOC_EXPORT_STORE_SUFFIX = "document.export.store.suffix";
	/**
	 * Key[document.export.thread.max] : 每次的导出中允许创建的线程池最大容量，默认：20
	 */
	public static String DOC_EXPORT_THREAD_MAX = "document.export.thread.max";
	/**
	 * Key[document.export.thread.batchSize] : 导出xls文件时,单个线程最大处理量,默认 500.
	 */
	public static String DOC_EXPORT_THREAD_BATCHSIZE = "document.export.thread.batchSize";
	/**
	 * Key[document.export.row.limit] : 导出xls文件时,每个Sheet最大允许有多少行，超过工作簿最大65536时候则以65536为最大值
	 */
	public static String DOC_EXPORT_ROW_LIMIT = "document.export.row.limit";
	/**
	 * Key[document.export.row.destruct] : 导出xls文件时,数据过多时候采用拆分方式sheet:多个工作簿|wookbook：多个xls文件，默认：sheet 即 拆分为多个工作簿
	 */
	public static String DOC_EXPORT_ROW_DESTRUCT = "document.export.row.destruct";
	
	//---------线程信息---------------------------------------
	/**
	 * Key[document.thread.info] : 使用线程池进行document 创建或者读取时线程信息key
	 */
	public static String DOC_THREAD_INFO = "document.thread.info";
	/**
	 * Key[document.thread.taskName] : 使用线程池进行document 创建或者读取时线程名称模板Key
	 */
	public static String DOC_THREAD_TASKNAME = "document.thread.taskName";
	/**
	 * Key[document.thread.status] : 使用线程池进行document 创建或者读取时线程状态信息模板Key
	 */
	public static String DOC_THREAD_STATUS = "document.thread.status";
	/**
	 * Key[document.thread.runing] : 使用线程池进行document 创建或者读取时线程允许时信息模板Key
	 */
	public static String DOC_THREAD_RUNNING = "document.thread.runing";
	/**
	 * Key[document.thread.complete] : 使用线程池进行document 创建或者读取时一个子线程执行完成信息模板Key
	 */
	public static String DOC_THREAD_COMPLETE = "document.thread.complete";
	/**
	 * Key[document.thread.time] : 使用线程池进行document 创建或者读取时线程池执行完成使用时间信息模板Key
	 */
	public static String DOC_THREAD_TIME = "document.thread.time";
	
}