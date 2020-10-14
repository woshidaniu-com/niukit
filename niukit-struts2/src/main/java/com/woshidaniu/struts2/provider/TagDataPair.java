package com.woshidaniu.struts2.provider;

public class TagDataPair {

	protected Object listData;
	protected String listKey = "key";
	protected String listValue = "value";

	public TagDataPair(Object listData) {
		this.listData = listData;
	}
	
	public TagDataPair(Object listData, String listKey, String listValue) {
		this.listData = listData;
		this.listKey = listKey;
		this.listValue = listValue;
	}

	public Object getListData() {
		return listData;
	}

	public void setListData(Object listData) {
		this.listData = listData;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getListValue() {
		return listValue;
	}

	public void setListValue(String listValue) {
		this.listValue = listValue;
	}

}
