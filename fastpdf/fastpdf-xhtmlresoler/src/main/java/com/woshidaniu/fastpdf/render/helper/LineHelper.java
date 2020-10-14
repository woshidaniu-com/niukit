package com.woshidaniu.fastpdf.render.helper;

import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.woshidaniu.fastpdf.render.draw.UnderLineSeparator;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;

 /**
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: LineHelper
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 下午6:46:12 
 */
public final class LineHelper {
	
	private static LineHelper instance = null;
	private LineHelper(){}
	
	public static LineHelper getInstance(){
		instance = instance==null?new LineHelper():instance;
		return instance;
	}
	
	public LineSeparator getLine(ItextXMLElement element){
		
		LineSeparator line = new LineSeparator();
		/*
		line.setAlignment(align)
		line.setLineColor(color)
		line.setLineWidth(lineWidth)
		line.setOffset(offset)
		line.setPercentage(percentage)
		*/
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(line, element);
		return line;
	}
	
	public LineSeparator getUnderLine(ItextXMLElement element){
		LineSeparator underLine = new UnderLineSeparator(); 
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(underLine, element);
		return underLine;
	}
	
	
	public LineSeparator getDottedLine(ItextXMLElement element){
		DottedLineSeparator dottedLine = new DottedLineSeparator(); 
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(dottedLine, element);
		return dottedLine;
	}
	
	
	public LineSeparator getDeleteLine(ItextXMLElement element){
		DottedLineSeparator dottedLine = new DottedLineSeparator(); 
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(dottedLine, element);
		return dottedLine;
	}
	
	
}



