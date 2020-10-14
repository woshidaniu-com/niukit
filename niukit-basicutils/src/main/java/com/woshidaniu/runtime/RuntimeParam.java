package com.woshidaniu.runtime;

import java.util.Locale;

import org.apache.commons.lang3.CharEncoding;


public enum RuntimeParam {
	
	/**
	 * 默认字符集
	 */
	APPLICATION_NAME("application.name", ""),
	 
	/**
	 * Application Runtime Environment Locale
	 */
	RUNTIME_LOCALE("runtime.env.locale", Locale.getDefault().toString()),

	/**
	 * 默认字符集
	 */
	RUNTIME_CHARSET("runtime.env.charset", CharEncoding.UTF_8),
	
	/**
	 * 临时存储目录
	 */
	RUNTIME_STORAGE_DIRECTORY("runtime.storage.directory", "tmpdir"); 

	protected String name;
	protected String defaultValue;

	private RuntimeParam(String name,String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDefault() {
		return defaultValue;
	}

	static RuntimeParam valueOfIgnoreCase(String parameter,String defaultValue) {
		RuntimeParam parm = valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
		parm.defaultValue = defaultValue;
		return parm;
	}
	 
}
