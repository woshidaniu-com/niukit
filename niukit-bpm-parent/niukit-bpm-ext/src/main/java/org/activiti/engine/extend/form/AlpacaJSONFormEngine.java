package org.activiti.engine.extend.form;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.EnumFormType;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：把表单配置转化为alpaca表单渲染JSON
 *	 <br>class：org.activiti.engine.extend.form.AlpacaJSONFormEngin.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class AlpacaJSONFormEngine implements FormEngine {

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	static final String DEFAULT_LOCALE = "zh_CN";
	
	static final String DEFAULT_UI_VIEW = "bootstrap-create";
	
	static final String DEFAULT_FORM_TITLE = "流程环节表单";
	
	static final String DEFAULT_FORM_DESCRIPTION = "请填写流程环节表单";
	
	static final String DEFAULT_FORM_HELPER = "带['*']的数据项必填！";
	
	static final String DEFAULT_DATE_FORMATE = "YYYY-MM-DD";
	
	static final Logger log = LoggerFactory.getLogger(AlpacaJSONFormEngine.class);
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	protected String formLocale = DEFAULT_LOCALE;
	protected String formTitle = DEFAULT_FORM_TITLE;
	protected String formDesciption = DEFAULT_FORM_DESCRIPTION;
	protected String formHelper = DEFAULT_FORM_HELPER;
	protected String dateFormate = DEFAULT_DATE_FORMATE;
	/* (non-Javadoc)
	 * @see org.activiti.engine.impl.form.FormEngine#getName()
	 */
	@Override
	public String getName() {
		return "alpacaJSONFormEngine";
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.impl.form.FormEngine#renderStartForm(org.activiti.engine.form.StartFormData)
	 */
	@Override
	public Object renderStartForm(StartFormData startForm) {
		if(log.isDebugEnabled()){
			log.debug("Rendering Start Form: {}", startForm);
		}
		List<FormProperty> formProperties = startForm.getFormProperties();
		if(formProperties == null || formProperties.isEmpty()){
			return null;
		}
		ObjectNode createAlpacaJSONObject = createAlpacaJSONObject(formProperties);
		log.info(createAlpacaJSONObject.toString());
		return createAlpacaJSONObject;
	}

	/* (non-Javadoc)
	 * @see org.activiti.engine.impl.form.FormEngine#renderTaskForm(org.activiti.engine.form.TaskFormData)
	 */
	@Override
	public Object renderTaskForm(TaskFormData taskForm) {
		if(log.isDebugEnabled()){
			log.debug("Rendering Task Form: {}", taskForm);
		}
		List<FormProperty> formProperties = taskForm.getFormProperties();
		if(formProperties == null || formProperties.isEmpty()){
			return null;
		}
		ObjectNode createAlpacaJSONObject = createAlpacaJSONObject(formProperties);
		log.info(createAlpacaJSONObject.toString());
		return createAlpacaJSONObject;
	}

	protected ObjectNode createAlpacaJSONObject(List<FormProperty> formProperties){
		ObjectNode alpacaJSONObject = objectMapper.createObjectNode();
		initialViewProperty(alpacaJSONObject, DEFAULT_UI_VIEW);
		ObjectNode schemaJSONObject = initialSchemaProperty(alpacaJSONObject);
		ObjectNode optionsJSONObject = initialOptions(alpacaJSONObject);
		initialView(alpacaJSONObject);
		initialProperties(schemaJSONObject, formProperties);
		initialFields(optionsJSONObject,formProperties);
		
		alpacaJSONObject.put("schema", schemaJSONObject);
		alpacaJSONObject.put("options", optionsJSONObject);
		return alpacaJSONObject;
	}

	protected void initialView(ObjectNode alpacaJSONObject) {
		ObjectNode fieldsJSONObject = objectMapper.createObjectNode();
		fieldsJSONObject.put("locale", getFormLocale());
		alpacaJSONObject.put("view", fieldsJSONObject);
	}

	protected ObjectNode initialFields(ObjectNode optionsJSONObject, List<FormProperty> formProperties) {
		ObjectNode fieldsJSONObject =objectMapper.createObjectNode();
		for (FormProperty formProperty : formProperties) {
			if(formProperty.isWritable()){
				fieldsJSONObject.put(formProperty.getId(), initialField(formProperty));
			}
		}
		//optionsJSONObject.put("helper", getFormHelper());
		optionsJSONObject.put("fields", fieldsJSONObject);
		return fieldsJSONObject;
	}

	protected ObjectNode initialProperties(ObjectNode schemaJSONObject, List<FormProperty> formProperties) {
		ObjectNode propertiesJSONObject = objectMapper.createObjectNode();
		for (FormProperty formProperty : formProperties) {
			if(formProperty.isWritable()){
				propertiesJSONObject.put(formProperty.getId(), initialProperty(formProperty));
			}
		}
		schemaJSONObject.put("properties", propertiesJSONObject);
		return propertiesJSONObject;
	}
	
	protected ObjectNode initialProperty(FormProperty formProperty){
		ObjectNode propertyJSONObject = objectMapper.createObjectNode();
		FormType type = formProperty.getType();
		propertyJSONObject.put("title", formProperty.getName());
		//propertyJSONObject.put("required", formProperty.isRequired());
		//if(formProperty.getValue() != null){
		//	propertyJSONObject.put("default", formProperty.getValue());
		//}
		if(type instanceof StringFormType){
			propertyJSONObject.put("type", "string");
		}
		if(type instanceof LongFormType){
			propertyJSONObject.put("type", "string");
		}
		if(type instanceof DateFormType){
			propertyJSONObject.put("type", "string");
			propertyJSONObject.put("formate", "date");
		}
		if(type instanceof TextareaFormType){
			propertyJSONObject.put("type", "string");
		}
		if(type instanceof EnumFormType){
			propertyJSONObject.put("type", "string");
			Map<String, String> values = (Map<String, String>) type.getInformation("values");
			Set<String> keySet = values.keySet();
			ArrayNode valueArray = objectMapper.createArrayNode();
			for (String key : keySet) {
				valueArray.add(key);
			}
			propertyJSONObject.put("enum", valueArray);
		}
		return propertyJSONObject;
	}

	protected ObjectNode initialField(FormProperty formProperty){
		ObjectNode fieldJSONObject = objectMapper.createObjectNode();
		FormType type = formProperty.getType();
		if(type instanceof StringFormType){
			fieldJSONObject.put("size", 1000);
		}
		if(type instanceof LongFormType){
			fieldJSONObject.put("type", "integer");
		}
		if(type instanceof DateFormType){
			Object datePattern = type.getInformation("datePattern");
			if(datePattern == null){
				datePattern = getDateFormate();
			}
			fieldJSONObject.put("dateFormate", datePattern.toString());
		}
		if(type instanceof TextareaFormType){
			fieldJSONObject.put("type", "textarea");
			fieldJSONObject.put("rows", 2);
		}
		if(type instanceof EnumFormType){
			fieldJSONObject.put("type", "select");
			fieldJSONObject.put("removeDefaultNone", true);
			Map<String, String> values = (Map<String, String>) type.getInformation("values");
			Set<String> keySet = values.keySet();
			ArrayNode valueArray = objectMapper.createArrayNode();
			for (String key : keySet) {
				String value = values.get(key);
				valueArray.add(value);
			}
			fieldJSONObject.put("optionLabels", valueArray);
		}
		return fieldJSONObject;
	}
	
	protected void initialViewProperty(ObjectNode alpacaJSONObject, String defaultUiView) {
		alpacaJSONObject.put("view", defaultUiView);
	}
	
	protected ObjectNode initialSchemaProperty(ObjectNode alpacaJSONObject){
		ObjectNode schemaObject = objectMapper.createObjectNode();
		schemaObject.put("title", getFormTitle());
		schemaObject.put("description", getFormDesciption());
		schemaObject.put("type", "object");
		alpacaJSONObject.put("schema", schemaObject);
		return schemaObject;
	}
	
	protected ObjectNode initialOptions(ObjectNode alpacaJSONObject) {
		ObjectNode optionsObject = objectMapper.createObjectNode();
		alpacaJSONObject.put("options", optionsObject);
		return optionsObject;
	}

	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public String getFormDesciption() {
		return formDesciption;
	}

	public void setFormDesciption(String formDesciption) {
		this.formDesciption = formDesciption;
	}

	public String getFormHelper() {
		return formHelper;
	}

	public void setFormHelper(String formHelper) {
		this.formHelper = formHelper;
	}

	public String getDateFormate() {
		return dateFormate;
	}

	public void setDateFormate(String dateFormate) {
		this.dateFormate = dateFormate;
	}

	public String getFormLocale() {
		return formLocale;
	}

	public void setFormLocale(String formLocale) {
		this.formLocale = formLocale;
	}
	
}
