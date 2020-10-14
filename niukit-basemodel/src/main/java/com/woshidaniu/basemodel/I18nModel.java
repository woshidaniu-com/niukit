package com.woshidaniu.basemodel;

import java.io.Serializable;

/**
 * 
 *@类名称	: I18nModel.java
 *@类描述	： 国际化Model
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:31:23 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class I18nModel implements Serializable {


	private String i18nKey;
	private String i18nValue;

	/**
	 * @return the i18nKey
	 */
	public String getI18nKey() {
		return i18nKey;
	}

	/**
	 * @param i18nKey the i18nKey to set
	 */
	public void setI18nKey(String i18nKey) {
		this.i18nKey = i18nKey;
	}

	/**
	 * @return the i18nValue
	 */
	public String getI18nValue() {
		return i18nValue;
	}

	/**
	 * @param i18nValue the i18nValue to set
	 */
	public void setI18nValue(String i18nValue) {
		this.i18nValue = i18nValue;
	}

	public String toString() {
		return "i18nKey:" + i18nKey + " ,i18nValue:" + i18nValue;
	}
	
}
