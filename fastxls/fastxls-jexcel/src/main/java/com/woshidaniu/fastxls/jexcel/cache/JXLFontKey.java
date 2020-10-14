package com.woshidaniu.fastxls.jexcel.cache;

import java.io.Serializable;

import com.woshidaniu.basicutils.StringUtils;

@SuppressWarnings("serial")
public class JXLFontKey implements Serializable {

	protected String name;
	protected int size;
	protected boolean bold;

	public JXLFontKey() {
		
	}
	
	public JXLFontKey(String name, int size, boolean bold) {
		super();
		this.name = name;
		this.size = size;
		this.bold = bold;
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
	
	@Override
	public String toString() {
		return  "Font [" + StringUtils.join(new Object[]{ name , size , (isBold() ? "BOLD" : "NO_BOLD" ) }, "-") + " ]" ;
	}

}
