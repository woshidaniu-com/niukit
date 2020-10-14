/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.idsplus.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlConverUtil {
	/**
	 * map to xml xml <node><key label="key1">value1</key><key label=
	 * "key2">value2</key>......</node>
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToXml(Map<String, String> map) {
		Document document = DocumentHelper.createDocument();
		Element nodeElement = document.addElement("node");
		for (Object obj : map.keySet()) {
			Element keyElement = nodeElement.addElement("key");
			keyElement.addAttribute("label", String.valueOf(obj));
			keyElement.setText(String.valueOf(map.get(obj)));
		}
		return doc2String(document);
	}

	public static String mapToStandardXml(Map<String, String> map) {
		Document document = DocumentHelper.createDocument();
		Element nodeElement = document.addElement("node");
		for (Object obj : map.keySet()) {
			Element keyElement = nodeElement.addElement(String.valueOf(obj));
			keyElement.setText(String.valueOf(map.get(obj)));
		}
		return doc2String(document);
	}

	/**
	 * list to xml xml <nodes><node><key label="key1">value1</key><key label=
	 * "key2">value2</key>......</node><node><key label="key1">value1</key><key
	 * label="key2">value2</key>......</node></nodes>
	 * 
	 * @param list
	 * @return
	 */
	public static String listToXml(List<Map<String, String>> list) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element nodesElement = document.addElement("nodes");
		int i = 0;
		for (Map<String, String> o : list) {
			Element nodeElement = nodesElement.addElement("node");
			if (o instanceof Map) {
				for (Object obj : ((Map<String, String>) o).keySet()) {
					Element keyElement = nodeElement.addElement("key");
					keyElement.addAttribute("label", String.valueOf(obj));
					keyElement.setText((((Map<String, String>) o).get(obj) == null ? ""
							: String.valueOf(((Map<String, String>) o).get(obj))));
				}
			} else {
				Element keyElement = nodeElement.addElement("key");
				keyElement.addAttribute("label", String.valueOf(i));
				keyElement.setText(String.valueOf(o));
			}
			i++;
		}
		return doc2String(document);
	}

	public static String listToStandardXml(List<Map<String, String>> list) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element nodesElement = document.addElement("nodes");
		int i = 0;
		for (Map<String, String> o : list) {
			Element nodeElement = nodesElement.addElement("node");
			if (o instanceof Map) {
				for (Object obj : ((Map<String, String>) o).keySet()) {
					Element keyElement = nodeElement.addElement(String.valueOf(obj));
					keyElement.setText((((Map<String, String>) o).get(obj) == null ? ""
							: String.valueOf(((Map<String, String>) o).get(obj))));
				}
			} else {
				Element keyElement = nodeElement.addElement(String.valueOf(i));
				keyElement.setText(String.valueOf(o));
			}
			i++;
		}
		return doc2String(document);
	}

	/**
	 * json to xml {"node":{"key":{"@label":"key1","#text":"value1"}}} conver
	 * <o><node class="object"><key class="object" label=
	 * "key1">value1</key></node></o>
	 * 
	 * @param json
	 * @return
	 */
	public static String jsonToXml(String json) {
		try {
			XMLSerializer serializer = new XMLSerializer();
			JSON jsonObject = JSONSerializer.toJSON(json);
			return serializer.write(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * xml to map xml <node><key label="key1">value1</key><key label=
	 * "key2">value2</key>......</node>
	 * 
	 * @param xml
	 * @return
	 */
	public static Map<String, String> xmlToMap(String xml) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			Document document = DocumentHelper.parseText(xml);
			Element nodeElement = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> node = nodeElement.elements();
			for (Iterator<Element> it = node.iterator(); it.hasNext();) {
				Element elm = it.next();
				map.put(elm.attributeValue("label"), elm.getText());
				elm = null;
			}
			node = null;
			nodeElement = null;
			document = null;
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * xml to list xml <nodes><node><key label="key1">value1</key><key label=
	 * "key2">value2</key>......</node><node><key label="key1">value1</key><key
	 * label="key2">value2</key>......</node></nodes>
	 * 
	 * @param xml
	 * @return
	 */
	public static List<Map<String, String>> xmlToList(String xml) {
		try {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Document document = DocumentHelper.parseText(xml);
			Element nodesElement = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> nodes = nodesElement.elements();
			for (Iterator<Element> its = nodes.iterator(); its.hasNext();) {
				Element nodeElement = its.next();
				Map<String, String> map = xmlToMap(nodeElement.asXML());
				list.add(map);
				map = null;
			}
			nodes = null;
			nodesElement = null;
			document = null;
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * xml to json <node><key label="key1">value1</key></node> 转化为
	 * {"key":{"@label":"key1","#text":"value1"}}
	 * 
	 * @param xml
	 * @return
	 */
	public static String xmlToJson(String xml) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		return xmlSerializer.read(xml).toString();
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	public static String doc2String(Document document) {
		String s = "";
		try {
			// 使用输出流来进行转化
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			// 使用UTF-8编码
			OutputFormat format = new OutputFormat("   ", true, "UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return s;
	}

	public static Document readXmlDocument(String filePath) {
		InputStream in = null;
		Document doc = null;
		// 解析xml文档内容
		try {
			SAXReader reader = new SAXReader();
			// in = XMLUtil.class.getClassLoader().getResourceAsStream(filePath);// 获取到xml文件
			in = new FileInputStream(new File(filePath));
			doc = reader.read(in);
		} catch (Exception e) {
			// logger.error("XMLUtil.readXml error: "+ e);
			e.printStackTrace();
			return null;
		} finally {
			// close(null,null,in);
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}

	/**
	 * 
	 * @param xml
	 * @return
	 */
	public static String getXmlElement(String filePath, String key) {
		String result = "";
		Document document = readXmlDocument(filePath);
		try {
			Element nodeElement = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> node = nodeElement.elements();
			for (Iterator<Element> it = node.iterator(); it.hasNext();) {
				Element elm = it.next();
				if (key.equals(elm.getName())) {
					result = elm.getText();
					break;
				}
			}
			node = null;
			nodeElement = null;
			document = null;
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}