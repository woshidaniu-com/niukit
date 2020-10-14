/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.httputils.handler;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;

/**
 * Handler that encapsulates the process of generating a response object
 * from a {@link HttpResponse}.
 */
public interface ResponseHandler<T> {

	/**
	 * 对HttpClient进行预处理
	 * @param httpclient
	 */
	public void handleClient(HttpClient httpclient);
	
    /**
     * Processes an {@link HttpResponse} and returns some value
     * corresponding to that response.
     *
     * @param response The response to process
     * @return A value determined by the response
     *
     * @throws IOException in case of a problem or the connection was aborted
     */
	public T handleResponse(HttpMethodBase httpMethod) throws IOException;

}
