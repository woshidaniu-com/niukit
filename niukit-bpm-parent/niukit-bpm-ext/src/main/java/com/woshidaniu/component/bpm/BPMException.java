package com.woshidaniu.component.bpm;

import org.activiti.engine.ActivitiException;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.BPMException.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class BPMException extends ActivitiException{
	private static final long serialVersionUID = 1L;
	
	protected String errorCode;
	
	public BPMException(String message) {
		super(message);
	}
	
	public BPMException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public BPMException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}
	
	public BPMException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public String getErrorMessage(){
		if(BPMUtils.isBlank(errorCode)){
			return BPMUtils.getMessage("BPM_EX_00");
		}
		String message = BPMUtils.getMessage(errorCode);
		return message == null ? BPMUtils.getMessage("BPM_EX_00") : message;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
