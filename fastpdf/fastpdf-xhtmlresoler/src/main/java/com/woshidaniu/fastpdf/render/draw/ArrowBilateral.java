package com.woshidaniu.fastpdf.render.draw;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * 
 *@类名称	: ArrowBilateral.java
 *@类描述	：左边箭头
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 7:51:03 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ArrowBilateral extends Arrow {
	public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
		canvas.beginText();

		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(BaseFont.ZAPFDINGBATS, "", BaseFont.EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();

		}
		canvas.setFontAndSize(bf, 12);
		// LEFT
		canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), llx - 10, y, 0);
		// RIGHT
		canvas.showTextAligned(Element.ALIGN_CENTER, String.valueOf((char) 220), urx + 10, y + 8, 180);
		canvas.endText();
	}
}
