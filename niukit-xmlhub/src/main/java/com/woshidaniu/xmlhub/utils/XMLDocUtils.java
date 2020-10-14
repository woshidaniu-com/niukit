package com.woshidaniu.xmlhub.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 *@类名称	: XMLDocUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 4:26:47 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class XMLDocUtils {

	// Get the node'text from the document.
    public static String getNodeText(Document document, String nodeName) throws Exception {
        return getNodeText(document, nodeName, 0);
    }

    // Get the node'text from the document.
    public static String getNodeText(Document document, String nodeName, int index) throws Exception {
        NodeList nodeList = document.getElementsByTagName(nodeName);
        if (nodeList == null || index >= nodeList.getLength()) {
            return null;
        } else {
            return nodeList.item(index).getTextContent();
        }
    }

    // Get the attribute's value of the node from the document.
    public static String getNodeAttributeValue(Document document, String nodeName, String attributeName) throws Exception {
        NodeList nodeList = document.getElementsByTagName(nodeName);
        if (nodeList != null && nodeList.getLength() > 0) {
            Element element = (Element) nodeList.item(0);
            return element.getAttribute(attributeName);
        }
        return null;
    }
    
}
