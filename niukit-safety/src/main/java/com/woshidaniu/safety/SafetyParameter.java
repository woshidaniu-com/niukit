package com.woshidaniu.safety;

import java.util.Locale;

public enum SafetyParameter {

	/** 解析扫描器类型*/
	SAFETY_XSS_SCANTYPE("safety.xss.scanType","1"),
	/** 需要过滤的请求路径的正则匹配表达式*/
	SAFETY_XSS_INCLUDE_PATTERNS("safety.xss.include-patterns",""),
	/** 不需要过滤的请求路径的正则匹配表达式*/
	SAFETY_XSS_EXCLUDE_PATTERNS("safety.xss.exclude-patterns",""),
	/** 默认的防XSS攻击的规则配置*/
	SAFETY_XSS_DEFAULT_POLICY("safety.xss.default-policy", "antisamy.xml"),
	/** 需要进行Xss检查的Header*/
	SAFETY_XSS_POLICY_HEADERS("safety.xss.policy-headers",""),
	/** 防XSS攻击的模块对应的规则配置*/
	SAFETY_XSS_POLICY_MAPPINGS("safety.xss.policy-mappings","");
	
	protected String name;
	protected String defaultValue;

	private SafetyParameter(String name,String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDefault() {
		return defaultValue;
	}

	static SafetyParameter valueOfIgnoreCase(String parameter, String defaultValue) {
		SafetyParameter parm = valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
		parm.defaultValue = defaultValue;
		return parm;
	}
	
}
