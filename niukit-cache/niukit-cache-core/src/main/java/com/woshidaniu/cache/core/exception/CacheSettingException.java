package com.woshidaniu.cache.core.exception;

import java.text.MessageFormat;

@SuppressWarnings("serial")
public class CacheSettingException extends CacheException {
	/**
	 * 国际化信息key
	 */
	protected String msgKey = null;
	
	public CacheSettingException(String message) {
		super(message);
	}

	public CacheSettingException(Throwable cause) {
		super(cause);
	}

	public CacheSettingException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheSettingException(String message, Throwable cause, String... replaceParas) {
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