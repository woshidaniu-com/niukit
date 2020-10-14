package com.woshidaniu.fastxls.core.model;

import java.util.Map;

import com.woshidaniu.fastxls.core.SplitType;
import com.woshidaniu.fastxls.core.Suffix;

/**
 * 
 * @className: ArgumentsModel
 * @description: 参数模型
 * @author : kangzhidong
 * @date : 下午3:40:31 2014-11-21
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class ArgumentsModel {

	private String taskName;
	private int thread_max;
	private int batch_size;
	private int sheet_limit;
	/**
	 * 生成的Excel文件后缀;决定了使用什么对象去构建wookbook：默认xls;支持xls,xlsx
	 */
	private Suffix suffix = Suffix.XLS;
	/**
	 * 生成Excel文件时,数据过多时候采用拆分方式sheet:多个工作簿|wookbook：多个xls文件，默认：sheet 即 拆分为多个工作簿
	 */
	private SplitType splitType = SplitType.SHEET;
	
	/**
	 * 生成Excel表头
	 */
	private Map<String,String> sheetTitles;
		
	public ArgumentsModel() {
		
	}
	
	public ArgumentsModel(Suffix suffix,String sheetName) {
		this.suffix = suffix;
		this.sheetName = sheetName;
	}

	public ArgumentsModel(String taskName, int thread_max, int batch_size) {
		this.taskName = taskName;
		this.thread_max = thread_max;
		this.batch_size = batch_size;
	}
	
	public ArgumentsModel(String taskName, int thread_max, int sheet_limit,Suffix suffix) {
		this.taskName = taskName;
		this.thread_max = thread_max;
		this.sheet_limit = sheet_limit;
		this.suffix = suffix;
	}
	
	private Integer offset = 0;// 分页起始位置
	private Integer limit = 15;// 每页记录数
	private String title;
	
	
	// -------------导入导出过程逻辑处理相关参数-------------------------------------------------------

	// 导出或者导入的表名
	private String tableName;
	private String viewName;
	private int sheetIndex;
	private String sheetName;
	
	// 导出的文件的文件名模板路径
	private String templateDir;
	private String templatePath;
	private String templateName;
	//临时文件目录，路径，名称
	private String tempDir;
	private String tempPath;
	private String tempName;
	
	public int getSheet_limit() {
		return sheet_limit;
	}
	
	public void setSheet_limit(int sheet_limit) {
		this.sheet_limit = sheet_limit;
	}
	
	public Integer getOffset() {
	
		return offset;
	}
	public void setOffset(Integer offset) {
	
		this.offset = offset;
	}
	public Integer getLimit() {
	
		return limit;
	}
	public void setLimit(Integer limit) {
	
		this.limit = limit;
	}
	public int getThread_max() {
	
		return thread_max;
	}
	public void setThread_max(int thread_max) {
	
		this.thread_max = thread_max;
	}
	public int getBatch_size() {
	
		return batch_size;
	}
	public void setBatch_size(int batch_size) {
	
		this.batch_size = batch_size;
	}
	public String getTitle() {
	
		return title;
	}
	public void setTitle(String title) {
	
		this.title = title;
	}
	public String getTaskName() {
	
		return taskName;
	}
	public void setTaskName(String taskName) {
	
		this.taskName = taskName;
	}
	public String getTableName() {
	
		return tableName;
	}
	public void setTableName(String tableName) {
	
		this.tableName = tableName;
	}
	public String getViewName() {
	
		return viewName;
	}
	public void setViewName(String viewName) {
	
		this.viewName = viewName;
	}
	public int getSheetIndex() {
	
		return sheetIndex;
	}
	public void setSheetIndex(int sheetIndex) {
	
		this.sheetIndex = sheetIndex;
	}
	public String getSheetName() {
	
		return sheetName;
	}
	public void setSheetName(String sheetName) {
	
		this.sheetName = sheetName;
	}
	public String getTemplateDir() {
	
		return templateDir;
	}
	public void setTemplateDir(String templateDir) {
	
		this.templateDir = templateDir;
	}
	public String getTemplatePath() {
	
		return templatePath;
	}
	public void setTemplatePath(String templatePath) {
	
		this.templatePath = templatePath;
	}
	public String getTemplateName() {
	
		return templateName;
	}
	public void setTemplateName(String templateName) {
	
		this.templateName = templateName;
	}
	public String getTempDir() {
	
		return tempDir;
	}
	public void setTempDir(String tempDir) {
	
		this.tempDir = tempDir;
	}
	public String getTempPath() {
	
		return tempPath;
	}
	public void setTempPath(String tempPath) {
	
		this.tempPath = tempPath;
	}
	public String getTempName() {
	
		return tempName;
	}
	public void setTempName(String tempName) {
	
		this.tempName = tempName;
	}

	public Suffix getSuffix() {
	
		return suffix;
	}

	public void setSuffix(Suffix suffix) {
		this.suffix = suffix;
	}

	public SplitType getSplitType() {
	
		return splitType;
	}

	public void setSplitType(SplitType splitType) {
	
		this.splitType = splitType;
	}

	public Map<String, String> getSheetTitles() {
	
		return sheetTitles;
	}

	public void setSheetTitles(Map<String, String> sheetTitles) {
	
		this.sheetTitles = sheetTitles;
	}
	 
	
	
}
