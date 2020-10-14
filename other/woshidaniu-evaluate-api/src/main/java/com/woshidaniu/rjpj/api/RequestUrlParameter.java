package com.woshidaniu.rjpj.api;

/**
 * 请求参数名称
 * @author 1571
 */
public class RequestUrlParameter {
	/**
	 * 表单代码,必填
	 */
	public static final String FORM_CODE = "formCode";
	/**
	 * 学校代码,必填
	 */
	public static final String XXDM = "xxdm";
	/**
	 * 业务系统登录的人的姓名,必填
	 */
	public static final String XM = "xm";
	/**
	 * 手机号码,可选
	 */
	public static final String SJHM = "sjhm";
	/**
	 * 发起请求时间,可选
	 */
	public static final String RT = "rt";
	/**
	 * sign,放参数最后面
	 */
	public static final String SIGN = "sign";
}
