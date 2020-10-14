package com.woshidaniu.xmlhub.xstream.io.json;

import java.io.IOException;
import java.io.Writer;

import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.thoughtworks.xstream.io.json.AbstractJsonWriter;

/**
 * *******************************************************************
 * @className	： FastJSON
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Mar 12, 2016 2:06:02 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class FastJsonWriter extends AbstractJsonWriter {

	private JSONSerializer jsonObject = new JSONSerializer();
	private JSONWriter writer;
	
	public FastJsonWriter(Writer out) {
		writer = new JSONWriter(out);
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 * @param name
	 */
	
	@Override
	protected void addLabel(String name) {
		writer.writeKey(name);
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 * @param value
	 * @param type
	 */
	
	@Override
	protected void addValue(String value, Type type) {
		writer.writeObject(value);
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 */
	
	@Override
	protected void endArray() {
		writer.endArray();
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 */
	
	@Override
	protected void endObject() {
		writer.endObject();
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 */
	
	@Override
	protected void nextElement() {
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 */
	
	@Override
	protected void startArray() {
		writer.startArray();
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 */
	
	@Override
	protected void startObject() {
		writer.startObject();
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 */
	
	@Override
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description	： TODO(描述这个方法的作用)
	 * @author 		： kangzhidong
	 * @date 		：Mar 12, 2016 2:06:37 PM
	 */
	
	@Override
	public void flush() {
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
