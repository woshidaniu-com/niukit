package com.woshidaniu.fastpdf.core.model;

import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfPCellx extends PdfPCell {
	
	protected boolean flag = false;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public PdfPCellx(){
		super();
	}
	
	public PdfPCellx(Paragraph paragraph) {
		super(paragraph);
	}

	public PdfPCellx(Image img) {
		super(img);
	}

	public PdfPCellx(PdfPTable table) {
		super(table);
	}

	public PdfPCellx setBorder(float left,float top,float right,float bottom){
		this.setBorderWidthLeft(left<0?0:left);
		this.setBorderWidthTop(top<0?0:top);
		this.setBorderWidthRight(right<0?0:right);
		this.setBorderWidthBottom(bottom<0?0:bottom);
		return this;
	}
	
	public PdfPCellx noBorder(){
		this.setBorderWidth(0);
		return this;
	}
	
	public PdfPCellx setFixedHeight(int height){
		super.setFixedHeight(height);
		return this;
	}
	
	public PdfPCellx setHAlignment(int position){
		super.setHorizontalAlignment(position);
		return this;
	}
	
	public PdfPCellx setAlignmentx(int horizontalAlignment, int verticalAlignment){
		super.setHorizontalAlignment(horizontalAlignment);
		super.setVerticalAlignment(verticalAlignment);
		return this;
	}
	
	public PdfPCellx setPaddingLeftx(float left){
		this.setPaddingLeft(left<0?0:left);		
		return this;
	}
	
	public PdfPCellx setPaddingTopx(float top){
		this.setPaddingTop(top<0?0:top);
		return this;
	}
	
	public PdfPCellx setPaddingRightx(float right){
		this.setPaddingRight(right<0?0:right);
		return this;
	}
	
	public PdfPCellx setPaddingBottomx(float bottom){
		this.setPaddingBottom(bottom<0?0:bottom);
		return this;
	}
	
	public PdfPCellx setPaddingTopBottomx(float top, float bottom){
		this.setPaddingTop(top<0?0:top);
		this.setPaddingBottom(bottom<0?0:bottom);
		return this;
	}
	
	public PdfPCellx setPaddingx(float left,float top,float right,float bottom){
		this.setPaddingLeft(left<0?0:left);
		this.setPaddingTop(top<0?0:top);
		this.setPaddingRight(right<0?0:right);
		this.setPaddingBottom(bottom<0?0:bottom);
		return this;
	}
	
}	



