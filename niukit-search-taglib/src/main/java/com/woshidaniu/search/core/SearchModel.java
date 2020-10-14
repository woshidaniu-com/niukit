/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 高级查询SQL解析器
 * @author Penghui.Qu
 * 	v1版本基础代码
 * @author zhangxiaobin
 *  v2版本基础代码
 * @author weiguangyue
 * 	v2版本整理，适配v1版本
 */
public class SearchModel implements Serializable{

	private static final long serialVersionUID = 1L;
	//单文本条件输入
	private String inputType;
	private String inputSqlType;
	
	private String selectType;
	private String dateType;
	private String numberType;
	//多文本输入条件,v2版本新增
	private String multipleInputType;
	
	private String querySQL;
	private Map<String,String> params;
	
	/*
	 * 查询条件参数(String)
	 */
	private List<String> stringSearchConditions;
	/*
	 * 查询条件参数(List)
	 */
	private List<String> listSearchConditions;
	/*
	 * 查询条件参数(Map)
	 */
	private Map<String,String> mapSearchConditions;
	
	public List<String> getStringSearchConditions() {
		return stringSearchConditions;
	}
	public void setStringSearchConditions(List<String> stringSearchConditions) {
		this.stringSearchConditions = stringSearchConditions;
	}
	public List<String> getListSearchConditions() {
		return listSearchConditions;
	}
	public void setListSearchConditions(List<String> listSearchConditions) {
		this.listSearchConditions = listSearchConditions;
	}
	public Map<String, String> getMapSearchConditions() {
		return mapSearchConditions;
	}
	public void setMapSearchConditions(Map<String, String> mapSearchConditions) {
		this.mapSearchConditions = mapSearchConditions;
	}
	public String getQuerySQL() {
		return querySQL;
	}
	public void setQuerySQL(String querySQL) {
		this.querySQL = querySQL;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public String getInputSqlType() {
		return inputSqlType;
	}
	public void setInputSqlType(String inputSqlType) {
		this.inputSqlType = inputSqlType;
	}
	public String getNumberType() {
		return numberType;
	}
	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}
	public String getMultipleInputType() {
		return multipleInputType;
	}
	public void setMultipleInputType(String multipleInputType) {
		this.multipleInputType = multipleInputType;
	}
	@Override
	public String toString() {
		return "SearchModel [inputType=" + inputType + ", inputSqlType=" + inputSqlType + ", selectType=" + selectType
				+ ", dateType=" + dateType + ", numberType=" + numberType + ", multipleInputType=" + multipleInputType
				+ ", querySQL=" + querySQL + ", params=" + params + "]";
	}
}
