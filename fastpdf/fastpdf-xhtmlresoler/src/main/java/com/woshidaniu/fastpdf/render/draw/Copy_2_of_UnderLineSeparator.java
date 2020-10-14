package com.woshidaniu.fastpdf.render.draw;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.draw.LineSeparator;

 /**
 * @package com.woshidaniu.fastpdf.render.draw
 * @className: UnderLineSeparator
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-15
 * @time : 上午8:48:28 
 */
public class Copy_2_of_UnderLineSeparator extends LineSeparator {

	public Copy_2_of_UnderLineSeparator(float lineWidth, float percentage, BaseColor lineColor) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.lineColor = lineColor;
        this.alignment = Element.ALIGN_CENTER;
        this.offset = -2;
    }

	public Copy_2_of_UnderLineSeparator(float lineWidth, float percentage, BaseColor lineColor, int align, float offset) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.lineColor = lineColor;
        this.alignment = align;
        this.offset = offset;
    }
	
	/** the gap between the dots. */
	protected float gap = 5;
	
	/**
	 * @see com.itextpdf.text.pdf.draw.DrawInterface#draw(com.itextpdf.text.pdf.PdfContentByte, float, float, float, float, float)
	 */
	public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
		
		
		canvas.saveState();
		canvas.setLineWidth(lineWidth);
		canvas.setLineCap(PdfContentByte.LINE_CAP_ROUND);
		canvas.setLineDash(0, gap, gap / 2);
        drawLine(canvas, llx, urx, y);
		canvas.restoreState();
	}

	/**
	 * Getter for the gap between the center of the dots of the dotted line.
	 * @return	the gap between the center of the dots
	 */
	public float getGap() {
		return gap;
	}

	/**
	 * Setter for the gap between the center of the dots of the dotted line.
	 * @param	gap	the gap between the center of the dots
	 */
	public void setGap(float gap) {
		this.gap = gap;
	}

	
}



