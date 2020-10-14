package com.woshidaniu.basemodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 *@类名称	: MapRowModel.java
 *@类描述	： 因为Struts2无法直接JSON到List<Map>类型的转换，为了实现此转换，可采用此对象List<MapRowModel>
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:31:31 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class MapRowModel implements Cloneable, Serializable{

	private Map<String, String> row = new HashMap<String, String>();

	public Map<String, String> getRow() {
		return row;
	}

	public void setRow(Map<String, String> row) {
		this.row = row;
	}

}
