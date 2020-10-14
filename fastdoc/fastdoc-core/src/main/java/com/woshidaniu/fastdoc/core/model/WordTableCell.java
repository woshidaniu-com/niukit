package com.woshidaniu.fastdoc.core.model;

import java.util.List;

public class WordTableCell{
	
	private String text;
	private List<WordParagraphModel> paragraphs;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public List<WordParagraphModel> getParagraphs() {
		return paragraphs;
	}
	
	public void setParagraphs(List<WordParagraphModel> paragraphs) {
		this.paragraphs = paragraphs;
	}
	
	public void setParagraph(int index,WordParagraphModel paragraph) {
		this.paragraphs.set(index, paragraph);
	}
	
	public WordParagraphModel getParagraph(int index) {
		return this.paragraphs.get(index);
	}
	
	public void addParagraph(WordParagraphModel paragraph) {
		this.paragraphs.add(paragraph);
	}
	
	
}