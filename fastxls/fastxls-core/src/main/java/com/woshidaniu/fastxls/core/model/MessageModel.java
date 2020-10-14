package com.woshidaniu.fastxls.core.model;

import java.util.ArrayList;
import java.util.List;
public class MessageModel {

	private String key;
	private List<Integer> rows = new ArrayList<Integer>();
	
	public MessageModel(String key,Integer value) {
		super();
		this.key = key;
		if(this.rows.indexOf(value)==-1){
			this.rows.add(value);
		}
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<Integer> getRows() {
		return rows;
	}
	public void setRows(List<Integer> rows) {
		this.rows = rows;
	}
	
	
	
}
