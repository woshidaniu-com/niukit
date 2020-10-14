 package com.opensymphony.xwork2.plus.validators;

import java.util.List;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
 /**
  * 
  *@类名称	: DatabaseUniqueValidator.java
  *@类描述	：
  *@创建人	：kangzhidong
  *@创建时间	：Mar 23, 2016 3:37:22 PM
  *@修改人	：
  *@修改时间	：
  *@版本号	:v1.0
  */
@SuppressWarnings("unchecked")
public class DatabaseUniqueValidator extends FieldValidatorSupport {

	//private IValidationService service;
	// 校验唯一数据的表或视图名称
	private String unique_table = null;
	
	
	@Override
	public void validate(Object object) throws ValidationException {
		/*// 获得要校验的字段名
		String fieldName = this.getFieldName();
	        
		ValidationModel model = new ValidationModel();
		//取值
		model.setFiled_name(objToString(getFieldValue("filed_name", object)));
		model.setOld_filed_value(objToString(getFieldValue("old_filed_value", object)));
		model.setFiled_value(objToString(getFieldValue("filed_value", object)));
		model.setFiled_list(objToList(getFieldValue("filed_list", object)));
		model.setOld_filed_list(objToList(getFieldValue("old_filed_list", object)));
		
		model.setTable(unique_table);
		
		stack.set("filed_name", fieldName);
		
		//不是唯一的数据
		if(!service.unique(model)){
			//getValidatorContext().addFieldError(fieldName, IDCardUtils.getCodeError());
			addFieldError(fieldName, object);
		}*/
	}
	

	/*public IValidationService getService() {
		return service;
	}*/

	public String getUnique_table() {
		return unique_table;
	}

	public void setUnique_table(String uniqueTable) {
		unique_table = uniqueTable;
	}

	private String objToString(Object object){
		return object == null ? null : String.valueOf(object);
	}
	
	private List objToList(Object object){
		return object == null ? null : (List)object;
	}

}

