package com.woshidaniu.orm.mybatis.i18n.handler;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class DataI18nMapper implements Serializable {

	protected String primaryName;
	protected Map<String, String> mapper;

	public String getPrimaryName() {
		return primaryName;
	}

	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}

	public Map<String, String> getMapper() {
		return mapper;
	}

	public void setMapper(Map<String, String> mapper) {
		this.mapper = mapper;
	}

}
