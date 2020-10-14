package com.woshidaniu.fastdoc.core.model;

import java.util.ArrayList;
import java.util.List;

public class WordTableModel {
	
	private String text;
	private List<WordTableRowModel> rows = new ArrayList<WordTableRowModel>(0);

	public List<WordTableRowModel> getRows() {
		return rows;
	}

	public void setRows(List<WordTableRowModel> rows) {
		this.rows = rows;
	}
	
	public void setRow(int index,WordTableRowModel element) {
		this.rows.set(index, element);
	}
	
	public WordTableRowModel getRow(int index) {
		return this.rows.get(index);
	}
	
	public void addRow(WordTableRowModel row) {
		this.rows.add(row);
	}
	
	public int getNumOfRows(){
		return this.rows.size();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
