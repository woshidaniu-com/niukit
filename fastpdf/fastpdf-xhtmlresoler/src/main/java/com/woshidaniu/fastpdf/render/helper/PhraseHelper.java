package com.woshidaniu.fastpdf.render.helper;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;

/**
 * @package com.woshidaniu.fastpdf.render.helper
 * @className: ChunkHelper
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-14
 * @time : 下午2:31:21
 */
public final class PhraseHelper {

	private static PhraseHelper instance = null;
	private PhraseHelper(){}
	
	public static PhraseHelper getInstance(){
		instance = instance==null?new PhraseHelper():instance;
		return instance;
	}
	
	public <T> Phrase getPhrase(ItextXMLElement element,T data){
		Phrase p = new Phrase(element.text(data));
		
		//"p","h1","h2","h3","h4","h5","h6","h7"
		
		
		
		LineSeparator underLine =LineHelper.getInstance().getDeleteLine(element);
		p.add(underLine);
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(p, element);
		return p;
		
	}
	
	
	public Phrase getUnderLinePhrase(ItextXMLElement element){
		Phrase p = new Phrase(element.text()); 
		LineSeparator underLine = LineHelper.getInstance().getUnderLine(element);
		p.add(underLine);
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(p, element);
		return p;
		
	}
	
	
	public Phrase getDeleteLinePhrase(ItextXMLElement element){
		Phrase p = new Phrase(element.text()); 
		LineSeparator underLine =LineHelper.getInstance().getDeleteLine(element);
		p.add(underLine);
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(p, element);
		return p;
	}
	
}
