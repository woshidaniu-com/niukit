package com.woshidaniu.httputils.handler;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;

import com.thoughtworks.xstream.XStream;
import com.woshidaniu.httputils.exception.HttpResponseException;

/**
 * 
 *@类名称	: ObjectResponseHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：2016年4月27日 下午3:17:33
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ObjectResponseHandler implements ResponseHandler<Object> {

	protected XStream xstream = new XStream();
	
	@Override
	public void handleClient(HttpClient httpclient) {
		
	}
	
	@Override
	public Object handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			return xstream.fromXML(httpMethod.getResponseBodyAsStream());
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}
 
}
