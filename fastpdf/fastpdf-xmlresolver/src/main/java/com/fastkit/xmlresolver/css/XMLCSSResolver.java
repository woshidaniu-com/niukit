/**
 * @title: CSSVisiter.java
 * @package com.fastkit.css.parser
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-8
 * @time : 下午7:02:58 
 */

package com.fastkit.xmlresolver.css;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fastkit.xmlresolver.charset.CCharset;
import com.fastkit.xmlresolver.xml.resolver.XMLPathResolver;
import com.phloc.css.ECSSVersion;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.reader.CSSReader;
import com.phloc.css.writer.CSSWriterSettings;
import com.woshidaniu.basicutils.BlankUtils;

/**
 * @package com.fastkit.css.parser
 * @className: CSSVisiter
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-8
 * @time : 下午7:02:58
 */

public class XMLCSSResolver implements CSSResolver {
	
	protected static Logger LOG = LoggerFactory.getLogger(XMLCSSResolver.class);
	//private static Lock lock = new ReentrantLock();// 锁  
	private static XMLCSSResolver instance = null;
	private CSSWriterSettings cssSetting = null;
	private XMLCSSResolver(){}
	
	private static ThreadLocal<XMLCSSResolver> threadLocal = new ThreadLocal<XMLCSSResolver>(){
		
		protected XMLCSSResolver initialValue() {
			//lock.lock();
			if (instance == null) {
				instance= new XMLCSSResolver();
			}
			//lock.unlock();
			return instance;
		};
		
	};
	
	public static XMLCSSResolver getInstance(){
		return threadLocal.get();
	}

	public static void main(String[] args) {
		final String sStyle = "p{background:url(20111218_140711_559_u.jpg) no-repeat -10px 20px;}"
				+ "div{background:fixed url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAIAAAACUFjqAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAALEgAACxIB0t1+/AAAAAd0SU1FB9EFBAoYMhVvMQIAAAAtSURBVHicY/z//z8DHoBH+v///yy4FDEyMjIwMDDhM3lgpaEuh7gTEzDiDxYA9HEPDF90e5YAAAAASUVORK5CYII=) !important;}"
				+ "span { background-image:url('20111218_140711_559_u.jpg');}";
		
		Thread t1 = new Thread(){
			@Override
			public void run() {
        		XMLCSSResolver s1 = XMLCSSResolver.getInstance();
        		s1.resolveText(sStyle);
        		System.out.println(s1.hashCode());
	//			log.info("t1 :"+(System.currentTimeMillis()-t));
			}
		};
        Thread t2 = new Thread(){
        	@Override
			public void run() {
        		XMLCSSResolver s1 = XMLCSSResolver.getInstance();
        		s1.resolveText(sStyle);
        		System.out.println(s1.hashCode());
//        		log.info("t2 :"+(System.currentTimeMillis()-t));
			}
        };
        
        t1.start();
        t2.start();
        /*
		
		XMLCSSResolver.getInstance().resolveText(sStyle);*/

	}


	public void resolveText(String styleText) {
		if(!BlankUtils.isBlank(styleText)){
			CascadingStyleSheet aCSS = null;
			try {
				LOG.info("css30 Reader");
				aCSS = CSSReader.readFromString(styleText, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS30);
				cssSetting = new CSSWriterSettings(ECSSVersion.CSS30,true);
			} catch (Exception e1) {
				try {
					LOG.info("css21 Reader");
					aCSS = CSSReader.readFromString(styleText, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS21);
					cssSetting = new CSSWriterSettings(ECSSVersion.CSS21,true);
				} catch (Exception e2) {
					try {
						LOG.info("css10 Reader");
						aCSS = CSSReader.readFromString(styleText, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS10);
						cssSetting = new CSSWriterSettings(ECSSVersion.CSS10,true);
					} catch (Exception e3) {
						
					}
				}
			}
			if(null!=aCSS){
				XMLCSSVisitor.getInstance().visitSheet(aCSS);
			}
		}
	}
	
	public void resolveURI(String path) throws FileNotFoundException {
		if(null!=path){
			List<String> paths = XMLPathResolver.getInstance().resolverAll(path);
			for (String uri : paths) {
				try {
					File styleFile = new File(uri);
					if(!BlankUtils.isBlank(styleFile)&&styleFile.exists()){
						CascadingStyleSheet aCSS = null;
						try {
							aCSS = CSSReader.readFromFile(styleFile, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS30);
							cssSetting = new CSSWriterSettings(ECSSVersion.CSS30,true);
						} catch (Exception e1) {
							try {
								aCSS = CSSReader.readFromFile(styleFile, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS21);
								cssSetting = new CSSWriterSettings(ECSSVersion.CSS21,true);
							} catch (Exception e2) {
								try {
									aCSS = CSSReader.readFromFile(styleFile, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS10);
									cssSetting = new CSSWriterSettings(ECSSVersion.CSS10,true);
								} catch (Exception e3) {
									
								}
							}
						}
						if(null!=aCSS){
							XMLCSSVisitor.getInstance().visitSheet(aCSS);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("解析css文件异常:" + e.getMessage());
				}
			}
		}
	}

	public CSSWriterSettings getCssSetting() {
		return cssSetting;
	}
	
}
