package com.woshidaniu.regexp.annotation;

public enum RegexpType {

	DATE("regexp_date.properties"),
	HTML("regexp_html.properties"),
	MATH("regexp_math.properties"),
	MOBILE("regexp_mobile.properties"),
	NET("regexp_net.properties"),
	SQL("regexp_sql.properties"),
	NORMAL("regexp_normal.properties"),
	SPECIAL("regexp_special.properties"); 
	
	private final String file;

	private RegexpType(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}
	
}
 