package com.woshidaniu.fastpdf.render.helper;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;


 /**
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: RectangleHelper
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 上午11:08:53 
 */

public final class DirectContentHelper {
	
	private static DirectContentHelper instance = null;
	private DirectContentHelper(){}
	
	public static DirectContentHelper getInstance(){
		instance = instance==null?new DirectContentHelper():instance;
		return instance;
	}
	
	//设置左右文字 
	public void setDirectContent(PdfWriter writer){
		//渲染过样式的页面大小 颜色 
		
		PdfContentByte canvas = writer.getDirectContent(); 

		Phrase phrase1 = new Phrase("This is a test!left"); 

		Phrase phrase2 = new Phrase("This is a test!right"); 

		Phrase phrase3 = new Phrase("This is a test!center"); 

		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase1, 10, 500, 0); 

		ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, phrase2, 10, 536, 0); 

		ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase3, 10, 572, 0); 




	}
	
}



