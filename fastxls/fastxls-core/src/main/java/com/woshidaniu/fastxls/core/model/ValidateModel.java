package com.woshidaniu.fastxls.core.model;

import java.util.List;

import com.woshidaniu.fastxls.core.OperatorType;
import com.woshidaniu.fastxls.core.ValidationType;

/**
 * 
 *@类名称	: ValidateModel.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 22, 2016 5:28:31 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ValidateModel {

	//有效类型
	private int validationType = ValidationType.ANY;
	//校验类型
	private int operatorType = OperatorType.IGNORED;
	
	private short firstRow = 0;
	private short lastRow = 1;
	private short firstCol = 0;
	private short lastCol = 1;
	
	//数据限制范围：起始值，结束值 ；不是范围时取起始值
	private String formula1;
	private String formula2;
	
	//日期格式时的日期格式
	private String dateFormat;
	
	private String listFormula;
	//预制的数据,用于显示下拉框
	private List<String> listOfValues;
	
	//选定单元格时显示的提示信息
	private String promptTitle = "提示";
	private String promptContent = "";
	//输入无效数据时显示的警告信息
	private String errorTitle = "警告";
	private String errorContent = "";
	
	/**
	 * 
	 */
	public ValidateModel() {
		// TODO Auto-generated constructor stub
	}
	
	public ValidateModel(int validationType, int operatorType, int firstRow, int lastRow, int firstCol, int lastCol, String formula1,String formula2) {
		this(validationType, operatorType, firstRow, lastRow, firstCol, lastCol, formula1, formula2 , null);
	}
	
	public ValidateModel(int validationType, int operatorType, int firstRow, int lastRow, int firstCol, int lastCol, String formula1,String formula2,String dateFormat) {
		this.validationType = validationType;
		this.operatorType = operatorType;
		this.firstRow = (short)firstRow;
		this.lastRow = (short)lastRow;
		this.firstCol = (short)firstCol;
		this.lastCol = (short)lastCol;
		this.formula1 = formula1;
		this.formula2 = formula2;
		this.dateFormat = dateFormat;
	}

	public ValidateModel(int validationType, int firstRow, int lastRow, int firstCol, int lastCol, String listFormula) {
		this.validationType = validationType;
		this.firstRow = (short)firstRow;
		this.lastRow = (short)lastRow;
		this.firstCol = (short)firstCol;
		this.lastCol = (short)lastCol;
		this.listFormula = listFormula;
	}

	public ValidateModel(int validationType, int firstRow, int lastRow, int firstCol, int lastCol, List<String> listOfValues) {
		this.validationType = validationType;
		this.firstRow = (short)firstRow;
		this.lastRow = (short)lastRow;
		this.firstCol = (short)firstCol;
		this.lastCol = (short)lastCol;
		this.listOfValues = listOfValues;
	}

	public short getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(short firstRow) {
		this.firstRow = firstRow;
	}

	public short getLastRow() {
		return lastRow;
	}

	public void setLastRow(short lastRow) {
		this.lastRow = lastRow;
	}

	public short getFirstCol() {
		return firstCol;
	}

	public void setFirstCol(short firstCol) {
		this.firstCol = firstCol;
	}

	public short getLastCol() {
		return lastCol;
	}

	public void setLastCol(short lastCol) {
		this.lastCol = lastCol;
	}

	public int getValidationType() {
		return validationType;
	}

	public void setValidationType(int validationType) {
		this.validationType = validationType;
	}

	public String getFormula1() {
		return formula1;
	}

	public void setFormula1(String formula1) {
		this.formula1 = formula1;
	}

	public String getFormula2() {
		return formula2;
	}

	public void setFormula2(String formula2) {
		this.formula2 = formula2;
	}

	public int getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getListFormula() {
		return listFormula;
	}

	public void setListFormula(String listFormula) {
		this.listFormula = listFormula;
	}

	public String[] getListOfValues() {
		return listOfValues.toArray(new String[listOfValues.size()]);
	}

	public void setListOfValues(List<String> listOfValues) {
		this.listOfValues = listOfValues;
	}

	public String getPromptTitle() {
		return promptTitle;
	}

	public void setPromptTitle(String promptTitle) {
		this.promptTitle = promptTitle;
	}

	public String getPromptContent() {
		return promptContent;
	}

	public void setPromptContent(String promptContent) {
		this.promptContent = promptContent;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	public String getErrorContent() {
		return errorContent;
	}

	public void setErrorContent(String errorContent) {
		this.errorContent = errorContent;
	}
	
	
	
}
