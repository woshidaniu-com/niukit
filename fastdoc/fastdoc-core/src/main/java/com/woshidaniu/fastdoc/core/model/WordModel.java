package com.woshidaniu.fastdoc.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 *@类名称	: WordModel.java
 *@类描述	：Word 文档抽象化对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 12:03:03 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class WordModel implements Serializable {

	private static final long serialVersionUID = 1L;
	// 文档长度
	private int characterLength = -1;
	//总页数
	private int pageCount;
	//总字符数 
	private int wordCount;
	// 标题
	private String title;
	// 作者
	private String author;
	// 主题
	private String subject;
	// 关键字
	private String keywords;
	// 创建时间
	private Date createDateTime;
	// 版本
	private String revNumber;
	
	//正文文本
	private String text;
	private String headerText;
	private String footerText;
	private String textFromPieces;
	
	private String[] paragraphText;
	private String[] footnoteText;
	private String[] endnoteText;
	//批注
	private String[] commentsText;
	
	private List<WordTableModel> tebles = new ArrayList<WordTableModel>();

	public WordModel() {}

	public List<WordTableModel> getTebles() {
		return tebles;
	}

	public void setTebles(List<WordTableModel> tebles) {
		this.tebles = tebles;
	}

	public void setTeble(int index, WordTableModel element) {
		this.tebles.set(index, element);
	}

	public WordTableModel getTeble(int index) {
		return this.tebles.get(index);
	}

	public void addTeble(WordTableModel table) {
		this.tebles.add(table);
	}

	public int getCharacterLength() {
		return characterLength;
	}

	public void setCharacterLength(int characterLength) {
		this.characterLength = characterLength;
	}
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getRevNumber() {
		return revNumber;
	}

	public void setRevNumber(String revNumber) {
		this.revNumber = revNumber;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public String getFooterText() {
		return footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public String getTextFromPieces() {
		return textFromPieces;
	}

	public void setTextFromPieces(String textFromPieces) {
		this.textFromPieces = textFromPieces;
	}

	public String[] getParagraphText() {
		return paragraphText;
	}

	public void setParagraphText(String[] paragraphText) {
		this.paragraphText = paragraphText;
	}

	public String[] getFootnoteText() {
		return footnoteText;
	}

	public void setFootnoteText(String[] footnoteText) {
		this.footnoteText = footnoteText;
	}

	public String[] getEndnoteText() {
		return endnoteText;
	}

	public void setEndnoteText(String[] endnoteText) {
		this.endnoteText = endnoteText;
	}

	public String[] getCommentsText() {
		return commentsText;
	}

	public void setCommentsText(String[] commentsText) {
		this.commentsText = commentsText;
	}

}
