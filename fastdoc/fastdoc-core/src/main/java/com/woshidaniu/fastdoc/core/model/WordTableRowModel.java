package com.woshidaniu.fastdoc.core.model;

import java.util.ArrayList;
import java.util.List;

public class WordTableRowModel{
	
	private int hight;
	private List<WordTableCell> cells = new ArrayList<WordTableCell>(0);

	public WordTableRowModel(){}

	public WordTableRowModel(int hight){
		this.hight = hight;
	}
	
	public List<WordTableCell> getCells() {
		return cells;
	}

	public void setCells(List<WordTableCell> rows) {
		this.cells = rows;
	}
	
	public void setCell(int index,WordTableCell element) {
		this.cells.set(index, element);
	}
	
	public WordTableCell getCell(int index) {
		return this.cells.get(index);
	}
	
	public void addCell(WordTableCell cell) {
		this.cells.add(cell);
	}
	
	public int getNumOfCells(){
		return this.cells.size();
	}

	public int getHight() {
		return hight;
	}

	public void setHight(int hight) {
		this.hight = hight;
	}
	
}