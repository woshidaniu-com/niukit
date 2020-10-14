package com.woshidaniu.beanutils.exception;

import java.text.MessageFormat;

@SuppressWarnings("serial")
public class PropertyGettingException extends RuntimeException {

	public PropertyGettingException(String message) {
		super(message);
	}

	public PropertyGettingException(Throwable cause) {
		super(cause);
	}

	public PropertyGettingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyGettingException(String message, Throwable cause, String... replaceParas) {
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


}