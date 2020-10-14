 package com.woshidaniu.httpclient.handler;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.woshidaniu.basicutils.BlankUtils;

/**
 * 
 *@类名称	: ResponseDocumentHandler.java
 *@类描述	：http请求响应处理：返回org.w3c.dom.Document对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:57:15 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XMLResponseHandler extends AbstractResponseHandler<Document> {

	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	public XMLResponseHandler(HttpClientContext context,String charset){
		super(context, charset); 
	}
	
	@Override
    public Document handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			//获取响应类型
			ContentType contentType = ContentType.getOrDefault(entity);
            if (!contentType.equals(ContentType.APPLICATION_XML)) {
                throw new ClientProtocolException("Unexpected content type:" +contentType);
            }
	        try {
	            DocumentBuilder docBuilder = factory.newDocumentBuilder();
	            String charset = BlankUtils.isBlank(contentType.getCharset()) ? null : contentType.getCharset().name();
	            if (charset == null) {
	                charset = charsetStr;
	            }
	            return docBuilder.parse(entity.getContent(), charset);
	        } catch (ParserConfigurationException ex) {
	            throw new IllegalStateException(ex);
	        } catch (SAXException ex) {
	            throw new ClientProtocolException("Malformed XML document", ex);
	        }
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
    }
}

 
