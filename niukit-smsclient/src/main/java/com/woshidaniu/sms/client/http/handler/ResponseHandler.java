package com.woshidaniu.sms.client.http.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;


/**
 * Handler that encapsulates the process of generating a response object
 * from a {@link HttpResponse}.
 */
public interface ResponseHandler<T> {

	/**
	 * 对HttpURLConnection进行预处理
	 * @param httpConn
	 */
	void preHandle(HttpURLConnection httpConn);
	
    /**
     * Processes an {@link OutputStream} and returns some value corresponding to that response.
     * @throws IOException in case of a problem or the connection was aborted
     */
    T handleResponse(HttpURLConnection httpConn, String charset) throws IOException;

}
