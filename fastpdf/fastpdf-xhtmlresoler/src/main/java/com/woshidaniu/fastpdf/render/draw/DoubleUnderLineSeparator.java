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
public class DoubleUnderLineSeparator extends LineSeparator {
	
	public DoubleUnderLineSeparator() {
		this(100f);
	}
	
	public DoubleUnderLineSeparator(float percentage) {
        this(1,percentage);
    }
	
	public DoubleUnderLineSeparator(float lineWidth, float percentage) {
		this(lineWidth,percentage, BaseColor.BLACK);
    }
	
	public DoubleUnderLineSeparator(float lineWidth, float percentage, BaseColor lineColor) {
        this(lineWidth,percentage,lineColor,Element.ALIGN_CENTER,-2);
    }

	public DoubleUnderLineSeparator(float lineWidth, float percentage, BaseColor lineColor, int align, float offset) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.lineColor = lineColor;
        this.alignment = align;
        this.offset = offset;
    }
	
	/**
     * Draws a horizontal line.
     * @param canvas	the canvas to draw on
     * @param leftX		the left x coordinate
     * @param rightX	the right x coordindate
     * @param y			the y coordinate
     */
    public void drawLine(PdfContentByte canvas, float leftX, float rightX, float y) {
    	float w;
        if (getPercentage() < 0){
            w = -getPercentage();
        }else{
            w = (rightX - leftX) * getPercentage() / 100.0f;
        }
        float s;
        switch (getAlignment()) {
            case Element.ALIGN_LEFT:
                s = 0;
                break;
            case Element.ALIGN_RIGHT:
                s = rightX - leftX - w;
                break;
            default:
                s = (rightX - leftX - w) / 2;
                break;
        }
        canvas.setLineWidth(getLineWidth());
        //设置字符间隔
        canvas.setCharacterSpacing(10);
        //
       // canvas.setMiterLimit(miterLimit);
        
        if (getLineColor() != null){
            canvas.setColorStroke(getLineColor());
        }
        canvas.moveTo(s + leftX, y + offset);
        canvas.lineTo(s + w + leftX, y + offset);
        canvas.stroke();
    }
}



