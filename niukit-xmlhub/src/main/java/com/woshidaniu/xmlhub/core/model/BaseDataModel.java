package com.woshidaniu.xmlhub.core.model;

/**
 * 
 *@类名称	: BaseDataModel.java
 *@类描述	：页面基础数据
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:27:11 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class BaseDataModel {

	/**
	 * baseData对应的key值
	 */
	private String key;
	/**
	 * baseData对应的value值
	 */
	private String value;

	public BaseDataModel() {
	}
	
	public BaseDataModel(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
