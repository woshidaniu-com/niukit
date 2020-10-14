package com.woshidaniu.basemodel;

import java.io.Serializable;

/**
 * 
 *@类名称	: PairModel.java
 *@类描述	：键值对基础元素
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:31:43 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class PairModel implements Cloneable, Serializable {
	
	protected String key;
	protected String value;

	public PairModel() {

	}

	public PairModel(String key, String value) {
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

	public String toString() {
		return "key:" + key + " value:" + value;
	}
}
