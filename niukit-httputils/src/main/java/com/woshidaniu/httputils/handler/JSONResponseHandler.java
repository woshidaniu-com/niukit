package com.woshidaniu.httputils.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.woshidaniu.httputils.ContentType;
import com.woshidaniu.httputils.HttpResponeUtils;
import com.woshidaniu.httputils.exception.HttpResponseException;

/**
 * 
 * @className: JSONResponseHandler
 * @description: http请求响应处理：返回JSONObject对象
 * @author : kangzhidong
 * @date : 下午12:48:53 2015-7-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
@SuppressWarnings("unchecked")
public class JSONResponseHandler implements ResponseHandler<JSONObject> {

	// 读取输入流
	protected SAXReader reader = new SAXReader();
	
	@Override
	public void handleClient(HttpClient httpclient) {
		
	}
	
	@Override
	public JSONObject handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			String contentType = HttpResponeUtils.getContentType(httpMethod);
			// xml转换成JSON对象
			if (contentType.startsWith(ContentType.APPLICATION_XML)) {
				// 将解析结果存储在JSONObject中
				JSONObject resultXML = new JSONObject();
				// 从request中取得输入流
				InputStream inputStream = null;
				try {
					inputStream = httpMethod.getResponseBodyAsStream();
					Document document = reader.read(inputStream);
					// 得到xml根元素
					Element root = document.getRootElement();
					// 得到根元素的所有子节点
					List<Element> elementList = root.elements();
					List<Element> childElements = null;
					// 遍历所有子节点
					for (Element e : elementList) {
						childElements = e.elements();
						if (childElements != null && !childElements.isEmpty()) {
							resultXML.put(e.getName(),parseJSONObject(childElements));
						} else {
							resultXML.put(e.getName(), e.getTextTrim());
						}
					}
				} catch (DocumentException ex) {
					throw new HttpResponseException("Malformed XML document",ex);
				} finally {
					// 释放资源
					IOUtils.closeQuietly(inputStream);
					inputStream = null;
				}
				return resultXML;
			} else if (contentType.startsWith(ContentType.APPLICATION_JSON)) {
				return JSONObject.parseObject(httpMethod.getResponseBodyAsString());
			} else {
				throw new HttpResponseException("Unexpected content type:" + contentType);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

	private static JSONObject parseJSONObject(List<Element> childElements) {
		// 将解析结果存储在JSONObject中
		JSONObject resultXML = new JSONObject();
		if (childElements != null && !childElements.isEmpty()) {
			// 遍历所有子节点
			for (Element e2 : childElements) {
				// 得到根元素的所有子节点
				List<Element> childElements2 = e2.elements();
				if (childElements2 != null && !childElements2.isEmpty()) {
					resultXML.put(e2.getName(), parseJSONObject(childElements2));
				} else {
					resultXML.put(e2.getName(), e2.getTextTrim());
				}
			}
		}
		return resultXML;
	}
}
