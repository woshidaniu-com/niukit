/**
 * @title: CSSVisiter.java
 * @package com.fastkit.css.parser
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-8
 * @time : 下午7:02:58 
 */

package com.fastkit.xmlresolver.css;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;

import com.fastkit.xmlresolver.xml.resolver.XMLPathResolver;
import com.steadystate.css.parser.CSSOMParser;
import com.woshidaniu.basicutils.BlankUtils;

/**
 * @package com.fastkit.css.parser
 * @className: CSSVisiter
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-8
 * @time : 下午7:02:58
 */

public class XMLCSSResolver2 implements CSSResolver{

	private static XMLCSSResolver2 instance = null;
	private static Object initLock = new Object();
	private XMLCSSResolver2(){}
	
	public static XMLCSSResolver2 getInstance(){
		if (instance == null) {
			synchronized(initLock) {
				instance= new XMLCSSResolver2();
			}
		}
		return instance;
	}

	public static void main(String[] args) throws FileNotFoundException {

		String sStyle = "@import '/uiwidget.skin-core-1.0.0.css';"
				+ "div{background:fixed url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAIAAAACUFjqAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAALEgAACxIB0t1+/AAAAAd0SU1FB9EFBAoYMhVvMQIAAAAtSURBVHicY/z//z8DHoBH+v///yy4FDEyMjIwMDDhM3lgpaEuh7gTEzDiDxYA9HEPDF90e5YAAAAASUVORK5CYII=) !important;}\n"
				+ "span { background-image:url('20111218_140711_559_u.jpg');}" +
				" p{background:url(20111218_140711_559_u.jpg) no-repeat -10px 20px;}}";

		XMLCSSResolver2.getInstance().resolveText(sStyle);
		//XMLCSSResolver2.getInstance().resolveURI("/uiwidget.skin-core-1.0.0.css");
	}

	public void resolveText(String styleText) {
		if(!BlankUtils.isBlank(styleText)){
			CSSOMParser cssparser = new CSSOMParser();  
			CSSStyleSheet css = null;   
	        try {
				css = cssparser.parseStyleSheet(new InputSource(new StringReader(styleText)), null,null);
				XMLCSSVisitor2.getInstance().visitSheet(css);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("解析css文件异常:" + e);
			}
		}
	}
	
	public void resolveURI(String path) throws FileNotFoundException {
		if(null!=path){
			CSSOMParser cssparser = new CSSOMParser();  
			CSSStyleSheet css = null;   
			List<String> paths = XMLPathResolver.getInstance().resolverAll(path);
			for (String uri : paths) {
				try {
					css = cssparser.parseStyleSheet(new InputSource(new FileReader(uri)), null,null);
					XMLCSSVisitor2.getInstance().visitSheet(css);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("解析css文件异常:" + e.getMessage());
				}
			}
		}
	}

}
