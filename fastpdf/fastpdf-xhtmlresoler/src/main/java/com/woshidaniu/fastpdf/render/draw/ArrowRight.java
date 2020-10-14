package com.woshidaniu.fastpdf.render.draw;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;

 /**
 * @package com.woshidaniu.fastpdf.render.elements
 * @className: Arrow
 * @description: 左边箭头
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 下午7:09:03 
 */
public class ArrowRight extends Arrow {
	
	public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
		canvas.beginText();
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "", BaseFont.EMBEDDED);
		} catch (Exception e) {

			e.printStackTrace();

		}
		canvas.setFontAndSize(bf, 12);
		// RIGHT
		canvas.showTextAligned(Element.ALIGN_CENTER,String.valueOf((char) 220), urx + 10, y + 8, 180);
		canvas.endText();
	}
	
}



