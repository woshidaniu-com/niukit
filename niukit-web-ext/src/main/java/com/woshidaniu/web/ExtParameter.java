package com.woshidaniu.web;

import java.util.Locale;

public enum ExtParameter {

	/**
	 * 用于获取装饰器名称参数的取值Key
	 */
	REQUEST_DECORATOR_NAME("request.decorator.name", "layout"),
	/**
	 * 指定参数的装饰器装饰页路径
	 */
	REQUEST_DECORATOR_PATH("request.decorator.path", "/WEB-INF/views/layouts/%s.jsp"),

	/** 是否Debug模式,是则不会进行js,css资源的压缩处理：该模式通常在开发时设置为true,部署时设置为false */
	YUI_DEBUG("yui.debug", "true"),
	/** 压缩后文件的后缀前的扩展名称 */
	YUI_SUFFIX("yui.suffix", "min"),
	/** 应用启动后启动js,css文件压缩线程的延时时间：单位秒 */
	YUI_DELAY("yui.delay", "3"),
	/** 是否对已压缩的文件进行重新生成:默认true */
	YUI_REBUILD("yui.rebuild", "true");

	protected String name;
	protected String defaultValue;

	private ExtParameter(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDefault() {
		return defaultValue;
	}

	static ExtParameter valueOfIgnoreCase(String parameter, String defaultValue) {
		ExtParameter parm = valueOf(parameter.toUpperCase(Locale.ENGLISH)
				.trim());
		parm.defaultValue = defaultValue;
		return parm;
	}

}
