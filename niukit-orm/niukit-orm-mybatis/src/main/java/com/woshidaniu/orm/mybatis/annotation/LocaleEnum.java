package com.woshidaniu.orm.mybatis.annotation;

import java.util.Locale;

public enum LocaleEnum {

	zh_CN(Locale.CHINA),
	
	en_US(Locale.US);
	
	private final Locale locale;

	private LocaleEnum(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	static LocaleEnum valueOfIgnoreCase(String parameter) {
		return valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
	}
	
	
}
