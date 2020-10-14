package com.woshidaniu.fastxls.jexcel.cache;

import java.io.Serializable;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;

import com.woshidaniu.basicutils.StringUtils;

@SuppressWarnings("serial")
public class JXLCellFormatKey implements Serializable {

	protected String name;
	protected int size;
	protected boolean bold;
	protected Alignment alignment;
	protected VerticalAlignment valignment;
	protected Colour background;
	protected Border border;
	protected BorderLineStyle borderLine;
	
	public JXLCellFormatKey() {
		
	}
	
	public JXLCellFormatKey(String name, int size, boolean bold,
			Alignment alignment, VerticalAlignment valignment,
			Colour background, Border border, BorderLineStyle borderLine) {
		super();
		this.name = name;
		this.size = size;
		this.bold = bold;
		this.alignment = alignment;
		this.valignment = valignment;
		this.background = background;
		this.border = border;
		this.borderLine = borderLine;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public VerticalAlignment getValignment() {
		return valignment;
	}

	public void setValignment(VerticalAlignment valignment) {
		this.valignment = valignment;
	}

	public Colour getBackground() {
		return background;
	}

	public void setBackground(Colour background) {
		this.background = background;
	}

	public Border getBorder() {
		return border;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public BorderLineStyle getBorderLine() {
		return borderLine;
	}

	public void setBorderLine(BorderLineStyle borderLine) {
		this.borderLine = borderLine;
	}
	
	@Override
	public String toString() {
		return  "CellFormat [" +  StringUtils.join(new Object[]{ name , size , bold , alignment.getDescription() , 
				valignment.getDescription() , background.getDescription(), borderLine.getDescription()}, "-") + " ]" ;
	}
}
