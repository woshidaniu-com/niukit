package com.woshidaniu.fastdoc.core.model;

public class WordParagraphModel {

	private String text;
	private String paragraphText;
	private String pictureText;
	private String footnoteText;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParagraphText() {
		return paragraphText;
	}

	public void setParagraphText(String paragraphText) {
		this.paragraphText = paragraphText;
	}

	public String getPictureText() {
		return pictureText;
	}

	public void setPictureText(String pictureText) {
		this.pictureText = pictureText;
	}

	public String getFootnoteText() {
		return footnoteText;
	}

	public void setFootnoteText(String footnoteText) {
		this.footnoteText = footnoteText;
	}

}
