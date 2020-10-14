package com.woshidaniu.beanutils.exception;

import java.text.MessageFormat;
/**
 * 
 *@类名称	: PropertyCopyException.java
 *@类描述	：属性拷贝异常
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 9:55:49 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class PropertyCopyException extends RuntimeException {
	/**
	 * 国际化信息key
	 */
	protected String msgKey = null;
	
	public PropertyCopyException(String message) {
		super(message);
	}

	public PropertyCopyException(Throwable cause) {
		super(cause);
	}

	public PropertyCopyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyCopyException(String message, Throwable cause, String... replaceParas) {
		super(constructErrMsg(message, replaceParas));
	}

	/**
	 * 构造异常信息字符串
	 * @param modelName
	 * @param msgKey
	 * @param replaceParas
	 * @return
	 */
	private static String constructErrMsg(String msg, String... replaceParas){
		if(replaceParas!=null&&replaceParas.length>0){
			msg = msgFormat(msg, replaceParas);
		}
		return msg;
	}
	
	/**
	 * 格式化带有占位符的信息
	 * @param msg
	 * @param parm
	 * @return
	 */
	private static String msgFormat(String msg, String... replaceParas){
		MessageFormat form = new MessageFormat(msg);
		return form.format(replaceParas);
	}

	public String getMsgKey() {
		return msgKey;
	}

}