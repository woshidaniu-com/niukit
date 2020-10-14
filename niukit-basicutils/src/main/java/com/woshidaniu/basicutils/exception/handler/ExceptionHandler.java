package com.woshidaniu.basicutils.exception.handler;

public interface ExceptionHandler {
	
	public void handle(Exception e);
	public void handle(Exception e, String contextMessage);
    
}
