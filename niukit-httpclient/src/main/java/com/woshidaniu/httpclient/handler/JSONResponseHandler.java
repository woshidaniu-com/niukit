package com.woshidaniu.httpclient.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.woshidaniu.basicutils.BlankUtils;

/**
 * 
 *@类名称	: ResponseJSONObjectHandler.java
 *@类描述	：http请求响应处理：返回JSONObject对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:57:36 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class JSONResponseHandler extends AbstractResponseHandler<JSONObject> {

	// 读取输入流
	protected SAXReader reader = new SAXReader();

	public JSONResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
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
			String charset = BlankUtils.isBlank(contentType.getCharset()) ? null : contentType.getCharset().name();
			if (charset == null) {
				charset = getCharset();
			}
			// xml转换成JSON对象
			if (contentType.equals(ContentType.APPLICATION_XML)) {
				// 将解析结果存储在JSONObject中
				JSONObject resultXML = new JSONObject();
				// 从request中取得输入流
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
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
					throw new ClientProtocolException("Malformed XML document",ex);
				} finally {
					// 释放资源
					IOUtils.closeQuietly(inputStream);
					inputStream = null;
				}
				return resultXML;
			} else if (contentType.equals(ContentType.APPLICATION_JSON)) {
				return JSONObject.parseObject(EntityUtils.toString(entity,charset));
			} else {
				throw new ClientProtocolException("Unexpected content type:" + contentType);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

	@SuppressWarnings("unchecked")
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
