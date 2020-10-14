package com.woshidaniu.fastdoc.render;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFFootnote;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.woshidaniu.fastdoc.core.model.WordModel;
import com.woshidaniu.fastdoc.core.model.WordParagraphModel;
import com.woshidaniu.fastdoc.core.model.WordTableCell;
import com.woshidaniu.fastdoc.core.model.WordTableModel;
import com.woshidaniu.fastdoc.core.model.WordTableRowModel;
import com.woshidaniu.fastdoc.core.utils.ExtensionUtils;


public class WordDocumentExtractor  {

	private static WordDocumentExtractor instance = null;
	private static Object initLock = new Object();
	
	private WordDocumentExtractor(){};
	public static WordDocumentExtractor getInstance(){
		if (instance == null) {
			synchronized(initLock) {
				instance= new WordDocumentExtractor();
			}
		}
		return  instance;
	}

	public WordModel extractDoc(String inFilePath) throws Exception {
		if(ExtensionUtils.isDoc(inFilePath)){
			WordModel temp = new WordModel();
			
			HWPFDocument doc = WordDocumentReader.getDocument(inFilePath);   
	           
			
            SummaryInformation info = doc.getSummaryInformation();
            temp.setPageCount(info.getPageCount());
            temp.setWordCount(info.getWordCount());
            temp.setTitle( info.getTitle());
            temp.setSubject(info.getSubject());
            temp.setAuthor(info.getAuthor());
            temp.setKeywords(info.getKeywords());
            temp.setCreateDateTime( info.getCreateDateTime());
            temp.setRevNumber(info.getRevNumber());

            WordExtractor extactor = WordDocumentReader.getDocumentExtractor(doc);  
            temp.setCharacterLength(doc.characterLength());
            temp.setText(extactor.getText().trim());
            temp.setHeaderText(extactor.getHeaderText());
            temp.setFooterText(extactor.getFooterText());
            temp.setTextFromPieces(extactor.getTextFromPieces());
            temp.setParagraphText(extactor.getParagraphText());
            temp.setFootnoteText(extactor.getFootnoteText());
            temp.setEndnoteText(extactor.getEndnoteText());
            temp.setCommentsText(extactor.getCommentsText());
            
        	Range range = doc.getRange();//得到文档的读取范围  
            
            TableIterator it = new TableIterator(range);  
            //迭代文档中的表格  
            while (it.hasNext()) { 
                Table tb = (Table) it.next();     
                WordTableModel table = new WordTableModel();
                //迭代行，默认从0开始  
                for (int i = 0; i < tb.numRows(); i++) {     
                    TableRow tr = tb.getRow(i);   
                    WordTableRowModel row = new WordTableRowModel();
                    row.setHight(tr.getRowHeight());
                    //迭代列，默认从0开始  
                    for (int j = 0; j < tr.numCells(); j++) {     
                        TableCell td = tr.getCell(j);//取得单元格  
                        //取得单元格的内容  
                        WordTableCell cell = new WordTableCell();
                        cell.setText(td.text());
                        for(int k=0;k<td.numParagraphs();k++){   
                            Paragraph para = td.getParagraph(k); 
                            WordParagraphModel paragraph = new WordParagraphModel();
                        	paragraph.setText(para.text());
                            cell.addParagraph(paragraph);
                        } //end for  
                        row.addCell(cell);
                    }   //end for  
                    table.addRow(row);
                }   //end for  
                temp.addTeble(table);
            } //end while  
		}
		return null;
	}
	
	public WordModel extractXDoc(String inFilePath) throws Exception {
		if(ExtensionUtils.isDocx(inFilePath)){
			WordModel temp = new WordModel();
			
			XWPFDocument docx = WordDocumentReader.getDocumentx(inFilePath); 
            POIXMLTextExtractor extactor = WordDocumentReader.getDocumentxExtractor(docx);  
            
            temp.setText(extactor.getText().trim());
            temp.setCharacterLength(extactor.getText().length());
			String[] headersText = new String[docx.getHeaderList().size()];
			for (int i = 0; i < docx.getHeaderList().size(); i++) {
				headersText[i] = docx.getHeaderList().get(i).getText();
			}
		    temp.setHeaderText(headersText.toString());
		    List<XWPFFooter> footers = docx.getFooterList();
		    String footerText =footers.size()>0?footers.get(0).getText():"";
            temp.setFooterText(footerText);
            temp.setTextFromPieces("");
			String[] paragraphText = new String[docx.getParagraphs().size()];
			for (int i = 0; i < docx.getParagraphs().size(); i++) {
				paragraphText[i] = docx.getParagraphs().get(i).getText();
			}
            temp.setParagraphText(paragraphText);
            String[] footnoteText = new String[docx.getFootnotes().size()];
			Iterator<XWPFFootnote>  itef = docx.getFootnotes().iterator();
			int index = 0;
			while (itef.hasNext()) {
				XWPFFootnote xwpfFootnote = (XWPFFootnote) itef.next();
				List<XWPFParagraph> paragraphs = xwpfFootnote.getParagraphs();
				for (Iterator<XWPFParagraph> iterator = paragraphs.iterator(); iterator.hasNext();) {
					XWPFParagraph xwpfParagraph = iterator.next();
					footnoteText[index] += xwpfParagraph.getText();
				}
			}
            temp.setFootnoteText(footnoteText);
            temp.setEndnoteText(null);
            String[] commentsText = new String[docx.getComments().length];
			for (int i = 0; i < docx.getComments().length; i++) {
				commentsText[i] = docx.getComments()[i].getText();
			}
            temp.setCommentsText(commentsText);
            
            Iterator<XWPFTable> ite =  docx.getTablesIterator();
			 //迭代文档中的表格  
            while (ite.hasNext()) { 
				XWPFTable tb = ite.next();
				WordTableModel table = new WordTableModel();
				table.setText(tb.getText());
                //迭代行，默认从0开始  
				for (int r = 0; r < tb.getNumberOfRows(); r++) { 
					XWPFTableRow xrow =  tb.getRow(r);
	                List<XWPFTableCell> cells = xrow.getTableCells();    
                    WordTableRowModel row = new WordTableRowModel(xrow.getHeight());
                    //迭代列，默认从0开始  
                    for (int j = 0; j < cells.size(); j++) {     
                    	XWPFTableCell td = cells.get(j);//取得单元格  
                        //取得单元格的内容  
                        WordTableCell cell = new WordTableCell();
                        cell.setText(td.getText());
                        List<XWPFParagraph> paragraphs =  td.getParagraphs();
                        for(int k=0;k< paragraphs.size();k++){   
                        	WordParagraphModel paragraph = new WordParagraphModel();
                        	XWPFParagraph para = paragraphs.get(k);   
                        	paragraph.setText(para.getText());
                        	paragraph.setFootnoteText(para.getFootnoteText());
                        	paragraph.setParagraphText(para.getParagraphText());
                        	paragraph.setPictureText(para.getPictureText());
                            cell.addParagraph(paragraph);
                        } //end for 
                        row.addCell(cell);
                    }   //end for  
                    table.addRow(row);
                }   //end for  
                temp.addTeble(table);
			}
            return temp;
		}
		return null;
	}

}
