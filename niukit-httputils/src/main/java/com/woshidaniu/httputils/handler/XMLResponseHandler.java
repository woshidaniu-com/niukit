 package com.woshidaniu.httputils.handler;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.woshidaniu.httputils.exception.HttpResponseException;

/**
 * 
 * @className: XMLResponseHandler
 * @description: http请求响应处理：返回org.w3c.dom.Document对象
 * @author : kangzhidong
 * @date : 下午01:43:19 2015-7-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class XMLResponseHandler implements ResponseHandler<Document> {

	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	@Override
	public void handleClient(HttpClient httpclient) {
		
	}
	
	@Override
    public Document handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
	        try {
	            DocumentBuilder docBuilder = factory.newDocumentBuilder();
	            return docBuilder.parse(httpMethod.getResponseBodyAsString());
	        } catch (ParserConfigurationException ex) {
	            throw new IllegalStateException(ex);
	        } catch (SAXException ex) {
	            throw new HttpResponseException("Malformed XML document", ex);
	        }
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
    }

}

 
