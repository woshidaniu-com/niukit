package com.woshidaniu.fastpdf.render.draw;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.draw.LineSeparator;


 /**
  * 
  *@类名称	: UnderLineSeparator.java
  *@类描述	：
  *@创建人	：kangzhidong
  *@创建时间	：Mar 29, 2016 7:52:46 PM
  *@修改人	：
  *@修改时间	：
  *@版本号	:v1.0
  */
public class UnderLineSeparator extends LineSeparator {
	
	public UnderLineSeparator() {
		this(100f);
	}
	
	public UnderLineSeparator(float percentage) {
        this(1,percentage);
    }
	
	public UnderLineSeparator(float lineWidth, float percentage) {
		this(lineWidth,percentage,BaseColor.BLACK);
    }
	
	public UnderLineSeparator(float lineWidth, float percentage, BaseColor lineColor) {
        this(lineWidth,percentage,lineColor,Element.ALIGN_CENTER,-2);
    }

	public UnderLineSeparator(float lineWidth, float percentage, BaseColor lineColor, int align, float offset) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.lineColor = lineColor;
        this.alignment = align;
        this.offset = offset;
    }
	
}



