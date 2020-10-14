package com.woshidaniu.fastxls.poi.utils;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.woshidaniu.fastxls.core.model.ValidateModel;


/**
 *@类名称:ValidateUtils.java
 *@类描述：构建Excel单元格数据有效性对象
 *@创建人：kangzhidong
 *@创建时间：2014-11-28 下午03:08:48
 *@版本号:v1.0
 */

public class ValidateUtils {
	
	public static DataValidation getValidation(Cell cell,ValidateModel model) {
		return ValidateUtils.getValidation(cell.getSheet(), model);
	}
	
	public static DataValidation getValidation(Sheet sheet, ValidateModel model) {
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList rangeAddress = new CellRangeAddressList(model.getFirstRow(),model.getLastRow(), model.getFirstCol(), model.getLastCol());
		DataValidationHelper helper = sheet.getDataValidationHelper();
		DataValidationConstraint constraint = null;
		DataValidation dataValidation = null;
		if (helper == null && sheet instanceof HSSFSheet){
			helper = new HSSFDataValidationHelper((HSSFSheet)sheet);
		}else if (helper == null && sheet instanceof XSSFSheet){
			// XSSFWorkbook || SXSSFWorkbook
			helper = new XSSFDataValidationHelper((XSSFSheet)sheet);
		}
		if(helper!=null){
			//根据不同验证类型创建数据有效性约束对象
			switch (model.getValidationType()) {
				case ValidationType.ANY:{
					if (sheet instanceof HSSFSheet){
						constraint = DVConstraint.createNumericConstraint(ValidationType.ANY, OperatorType.IGNORED, null, null);
					}else{
						constraint = new XSSFDataValidationConstraint(ValidationType.ANY, "");
					}
				};break;
				case ValidationType.INTEGER:{
					constraint = helper.createIntegerConstraint( model.getOperatorType(), model.getFormula1(), model.getFormula2());
				};break;
				case ValidationType.DECIMAL:{
					constraint = helper.createDecimalConstraint( model.getOperatorType(), model.getFormula1(), model.getFormula2());
				};break;
				case ValidationType.LIST:{
					constraint = helper.createExplicitListConstraint(model.getListOfValues());
				};break;
				case ValidationType.DATE:{
					constraint = helper.createDateConstraint( model.getOperatorType(), model.getFormula1(), model.getFormula2(), model.getDateFormat());
				};break;
				case ValidationType.TIME:{
					constraint = helper.createTimeConstraint( model.getOperatorType(), model.getFormula1(), model.getFormula2());
				};break;
				case ValidationType.TEXT_LENGTH:{
					constraint = helper.createTextLengthConstraint( model.getOperatorType(), model.getFormula1(), model.getFormula2());
				};break;
				case ValidationType.FORMULA:{
					constraint = helper.createFormulaListConstraint(model.getListFormula());
				};break;
			}
			// 数据有效性对象
			dataValidation = helper.createValidation(constraint, rangeAddress);
			//设置 ：选定单元格时显示的提示信息
			dataValidation.createErrorBox(model.getErrorTitle(), model.getErrorContent());
			//设置 ：输入无效数据时显示的警告信息
			dataValidation.createPromptBox(model.getPromptTitle(), model.getPromptContent());
		}
		//返回对象
		return dataValidation;
	}
	
}
