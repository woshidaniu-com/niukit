package com.woshidaniu.fastpdf.render.helper;

import java.net.URL;

import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.style.PDFStyleTransformer;

 /**
  * 
  * @package com.woshidaniu.fastpdf.render.helper
  * @className: ImageHelper
  * @description: TODO
  * @author : kangzhidong
  * @date : 2014-1-21
  * @time : 下午2:17:33
  */
public final class ImageHelper {
	
	private static ImageHelper instance = null;
	private ImageHelper() {}

	public static ImageHelper getInstance(){
		instance = instance==null?new ImageHelper():instance;
		return instance;
	}
	
	public <T> Image getImage(ItextXMLElement element,T data) throws Exception{
		if(element.isElement("img")){
			String srcAttr = element.attr("src");
			String imageAttr = element.attr("image");
			String byteAttr = element.attr("bytes");
			Image image = null;
			if(!BlankUtils.isBlank(srcAttr)){
				URL url = new URL(srcAttr);
				image = Image.getInstance(url);
			}else if(!BlankUtils.isBlank(imageAttr)){
				Image tmp = (Image)JavaBeanUtils.getProperty(data, imageAttr);
				image = Image.getInstance(tmp);
			}else if(!BlankUtils.isBlank(byteAttr)){
				byte[] bytes = (byte[])JavaBeanUtils.getProperty(data, byteAttr);
				image = Image.getInstance(bytes);
			}
			if(image != null){
				ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(image, element);
			}
			return image;
		}
		return null;
	}
	
	
}



