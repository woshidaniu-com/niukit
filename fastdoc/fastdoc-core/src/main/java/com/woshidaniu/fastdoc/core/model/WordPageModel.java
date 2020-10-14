package com.woshidaniu.fastdoc.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WordPageModel {
	
	private int pageNum = 0;
	
	//可以是普通bean，也可以是map
	private Map<Integer,String> contents = null;
	
	private List<WordPicture> pictures = new ArrayList<WordPicture>(0);

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public Map<Integer, String> getContents() {
		return contents;
	}

	public void setContents(Map<Integer, String> contents) {
		this.contents = contents;
	}

	public List<WordPicture> getPictures() {
		return pictures;
	}

	public void setPictures(List<WordPicture> pictures) {
		this.pictures = pictures;
	}
	
	
}
