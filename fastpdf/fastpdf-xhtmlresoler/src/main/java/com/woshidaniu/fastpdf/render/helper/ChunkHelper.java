package com.woshidaniu.fastpdf.render.helper;

import com.itextpdf.text.Chunk;
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
public final class ChunkHelper {

	private static ChunkHelper instance = null;
	private ChunkHelper(){}
	
	public static ChunkHelper getInstance(){
		instance = instance==null?new ChunkHelper():instance;
		return instance;
	}
	
	public Chunk getChunk(ItextXMLElement element){
		if(element.isElement("br")){
			//换行
			return Chunk.NEWLINE;
  		}else if(element.isElement("a")){
  			//带连接的文字
  			Chunk chunk = ChunkHelper.getInstance().getChunk(element);
  			chunk.setAnchor(element.attr("href"));
			return chunk;
  		}else if(element.isElement("i")){
  			//空白行
  			return Chunk.SPACETABBING;
  		}else if(element.isElement("span")){
  			//空白行
  			return Chunk.SPACETABBING;
  		}else if(element.isElement("font")){
  			Chunk chunk = new Chunk(element.text(), element.getFont()); 
  			//chunk.setBackground(color)
  			
  			/*chunk.setBackground(color)
  			
  			chunk.setHorizontalScaling(scale)*/
  			ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(chunk, element);
  			return chunk;
  		}
		return null;
	}
	
	public Chunk getUnderLineChunk(ItextXMLElement element){
		return Chunk.SPACETABBING;
	}
	
	public Chunk getDeleteLineChunk(ItextXMLElement element){
		return Chunk.SPACETABBING;
	}
}
