package com.woshidaniu.xmlhub.core.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.woshidaniu.xmlhub.core.exception.TransformException;

/**
 * 
 *@类名称	: XMLToJSONSAXHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 17, 2016 10:23:17 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XML2JSONSaxHandler extends DefaultHandler {
	
	//jsonStringBuilder 保存解析XML时生成的json字符串
	private StringBuilder jsonBuilder ;
	
	/*
	 *  isProcessing 表示 是否正在解析一个XML
	 *  startDocument 事件发生时设置 isProcessing = true
	 *  endDocument 事件发生时设置 isProcessing = false
	 */
	private boolean isProcessing;
	/*
	 *  justProcessStartElement 表示 是否刚刚处理完一个 startElement事件
	 *  引入这个标记的作用是为了判断什么时候输出逗号 
	 */
	private boolean justProcessStartElement;
	
	public XML2JSONSaxHandler(){
		jsonBuilder = new StringBuilder();
	}
	
	@Override
	public void startDocument() throws SAXException {
		/*
		 * 开始解析XML文档时 设定一些解析状态
		 *     设置isProcessing为true，表示XML正在被解析
		 *     设置justProcessStartElement为true，表示刚刚没有处理过 startElement事件
		 */
		isProcessing = true;
		justProcessStartElement = true;
		//清空 jsonStringBuilder 中的字符
		this.jsonBuilder.delete(0, this.jsonBuilder.length());
	}
	
	@Override
	public void endDocument() throws SAXException {
		isProcessing = false;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attrs) throws SAXException {	
		/*
		 * 是否刚刚处理完一个startElement事件
		 *     如果是 则表示这个元素是父元素的第一个元素 。
		 *     如果不是 则表示刚刚处理完一个endElement事件，即这个元素不是父元素的第一个元素
		 */
		if(!justProcessStartElement){
			jsonBuilder.append(',');
			justProcessStartElement = true;
		}
		jsonBuilder.append("{");
		jsonBuilder.append("localName:").append('\"').append(localName).append('\"').append(',');
		jsonBuilder.append("uri:").append('\"').append(uri).append('\"').append(',');
		jsonBuilder.append("qName:").append('\"').append(qName).append('\"').append(',');
		//将解析出来的元素属性添加到JSON字符串中
		jsonBuilder.append("attrs:{");
		if(attrs.getLength() > 0){
			jsonBuilder.append(attrs.getLocalName(0)).append(":").append(attrs.getValue(0));
			for(int i = 1 ; i < attrs.getLength() ; i++){
				jsonBuilder.append(',').append(attrs.getLocalName(i)).append(":").append(attrs.getValue(i));
			}	
		}
		jsonBuilder.append("},");
		//将解析出来的元素的子元素列表添加到JSON字符串中
		jsonBuilder.append("childElements:[").append('\n');
	}
	
	@Override
	public void endElement(String uri,String localName,String qName) throws SAXException {
		justProcessStartElement = false;
		jsonBuilder.append("]}").append('\n');
	}

	@Override
	public void characters(char[] ch, int begin, int length) throws SAXException {
		/*
		 * 是否刚刚处理完一个startElement事件
		 *     如果是 则表示这个元素是父元素的第一个元素 。
		 *     如果不是 则表示刚刚处理完一个endElement事件，即这个元素不是父元素的第一个元素
		 */
		if(!justProcessStartElement){
			jsonBuilder.append(',');
		}else{
			justProcessStartElement = false;
		}
		jsonBuilder.append('\"');
		for(int i = begin ; i < begin+length ; i++){
			switch(ch[i]){
				case '\'':jsonBuilder.append("\\'");break;
				case '\"':jsonBuilder.append("\\\"");break;
				case '\n':jsonBuilder.append("\\n");break;
				case '\t':jsonBuilder.append("\\t");break;
				case '\r':jsonBuilder.append("\\r");break;
				default:  jsonBuilder.append(ch[i]);break;
			}
		}
		jsonBuilder.append('\"').append('\n');
	}
	
	public String toJSONString() throws TransformException{
		if(this.isProcessing){
			throw new TransformException("getJsonString before resolution is finished");
		} else {
			return jsonBuilder.toString();
		}
	}
}
