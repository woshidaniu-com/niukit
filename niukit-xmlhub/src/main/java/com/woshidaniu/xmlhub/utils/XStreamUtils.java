package com.woshidaniu.xmlhub.utils;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;

@SuppressWarnings("unchecked")
public abstract class XStreamUtils {

	private static XStream xstream = new XStream();
	
	public static String objectToXml(Object obj, Map<String, Class> aliasMap) {
		String xml = null;
		Set<String> key = aliasMap.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String alias = (String) it.next();
			Class type = aliasMap.get(alias);
			xstream.alias(alias, type);
		}
		xml = xstream.toXML(obj);
		return xml;
	}

	public static void objectToXml(Object obj, Map<String, Class> aliasMap, Writer out) {
		Set<String> key = aliasMap.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String alias = (String) it.next();
			Class type = aliasMap.get(alias);
			xstream.alias(alias, type);
		}
		xstream.toXML(obj, out);
	}

	public static void objectToXml(Object obj, Map<String, Class> aliasMap,OutputStream out) {
		Set<String> key = aliasMap.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			String alias = (String) it.next();
			Class type = aliasMap.get(alias);
			xstream.alias(alias, type);
		}
		xstream.toXML(obj, out);
	}

	public static <T> String collectionToXml(Collection<T> collection, Map<String, Class> aliasMap) {
		return objectToXml(collection, aliasMap);
	}

}
