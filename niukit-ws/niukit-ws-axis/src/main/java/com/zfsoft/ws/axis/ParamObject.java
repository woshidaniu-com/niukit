package com.woshidaniu.ws.axis;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

public class ParamObject {

	protected String name;
	protected QName xmlType;
	protected Object value;
	protected ParameterMode mode;
	
	public ParamObject(String name, QName xmlType, Object value) {
		this.name = name;
		this.xmlType = xmlType;
		this.value = value;
	}
	
	public ParamObject(String name, QName xmlType, Object value,ParameterMode mode) {
		this.name = name;
		this.xmlType = xmlType;
		this.value = value;
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QName getXmlType() {
		return xmlType;
	}

	public void setXmlType(QName xmlType) {
		this.xmlType = xmlType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public ParameterMode getMode() {
		return mode == null ? ParameterMode.IN : mode;
	}

	public void setMode(ParameterMode mode) {
		this.mode = mode;
	}

}
