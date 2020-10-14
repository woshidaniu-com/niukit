package com.woshidaniu.javamail;

/**
 * @author 1571
 */
public interface JavaMailAntispamHandlerFactory {

	JavaMailAntispamHandler getHandler(String name);
}
