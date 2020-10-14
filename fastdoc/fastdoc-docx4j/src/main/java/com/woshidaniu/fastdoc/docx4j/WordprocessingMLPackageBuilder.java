/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j;

import java.io.File;
import java.net.URL;

import org.docx4j.events.StartEvent;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.nodes.Document;

import com.woshidaniu.fastdoc.docx4j.events.BuildFinishedEvent;
import com.woshidaniu.fastdoc.docx4j.events.BuildJobTypes;
import com.woshidaniu.fastdoc.docx4j.events.BuildStartEvent;
import com.woshidaniu.fastdoc.docx4j.fonts.ChineseFont;
import com.woshidaniu.fastdoc.docx4j.handler.XHTMLImporterHandler;
import com.woshidaniu.fastdoc.docx4j.utils.PhysicalFontUtils;

public class WordprocessingMLPackageBuilder {

	protected static XHTMLImporterHandler DEFAULT_HANDLER = null;
	protected XHTMLImporterHandler handler = null;
	
	public WordprocessingMLPackageBuilder() {
		try {
			this.handler = (XHTMLImporterHandler) Class.forName("com.woshidaniu.fastdoc.docx4j.handler.def.DefaultXHTMLImporterHandler").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public WordprocessingMLPackageBuilder(XHTMLImporterHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置中文字体;解决中文乱码问题
	 */
	public WordprocessingMLPackageBuilder configChineseFonts(WordprocessingMLPackage wmlPackage) throws Exception {
		//初始化中文字体
		PhysicalFontUtils.setWmlPackageFonts(wmlPackage);
        //返回WordprocessingMLPackage对象
      	return this;
    }
	
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置默认字体
	 */
	public WordprocessingMLPackageBuilder configDefaultFont(WordprocessingMLPackage wmlPackage,String fontName) throws Exception {
		//设置文件默认字体
		PhysicalFontUtils.setDefaultFont(wmlPackage, fontName);
        //返回WordprocessingMLPackage对象
      	return this;
    }
	
	/**
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置中文字体
	 */
	public WordprocessingMLPackageBuilder configSimSunFont(WordprocessingMLPackage wmlPackage) throws Exception {
		//初始化中文字体，解决中文乱码问题
		configChineseFonts(wmlPackage);
        //设置文件默认字体
		configDefaultFont(wmlPackage,ChineseFont.SIMSUM.getFontName());
		//返回WordprocessingMLPackage对象
		return this;
    }
	
	/**
	 * 获取初始化后的 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage}对象
	 */
	public WordprocessingMLPackage initialize(WordprocessingMLPackage wmlPackage) {
		
		/*MBassador<Docx4jEvent> bus = new MBassador<Docx4jEvent>();
			Docx4jEvent.setEventNotifier(bus);
		*/
		
		
		return wmlPackage;
	}
	
	public WordprocessingMLPackage buildWhithDoc(Document doc) throws Exception {
		/*	
		 * 	根据docx4j.properties配置文件中:
		 * 	docx4j.PageSize = A4
		 * 	docx4j.PageOrientationLandscape = true
		 * 	创建默认的WordProcessingML package
		 */
        WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage();
        //返回WordprocessingMLPackage对象
        return buildWhithDoc(wmlPackage , doc);
    }
	
	public WordprocessingMLPackage buildWhithDoc(Document doc,boolean landscape) throws Exception {
        //返回WordprocessingMLPackage对象
        return buildWhithDoc(doc, PageSizePaper.A4, landscape);
    }
	
	public WordprocessingMLPackage buildWhithDoc(Document doc,PageSizePaper pageSize,boolean landscape) throws Exception {
		//创建指定纸张大小和方向的WordprocessingMLPackage对象
        WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage(pageSize, landscape); //A4纸，
        //返回WordprocessingMLPackage对象
        return buildWhithDoc(wmlPackage , doc);
    }
	
	public WordprocessingMLPackage buildWhithDoc(WordprocessingMLPackage wmlPackage,Document doc) throws Exception {
		//构建任务开始
		StartEvent jobStartEvent = new BuildStartEvent(BuildJobTypes.DOC, wmlPackage);
		jobStartEvent.publish();
		//配置中文字体
        wmlPackage = initialize(wmlPackage);
        //使用Handler渲染WordprocessingMLPackage对象
  		handler.handle(wmlPackage, doc, false);
        //构建任务结束
        new BuildFinishedEvent(jobStartEvent).publish();
        //返回WordprocessingMLPackage对象
        return wmlPackage;
    }
	
	/**
	 * 将 {@link org.jsoup.nodes.Document} 对象转为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage}
	 * @param htmlFile
	 */
	public WordprocessingMLPackage buildWhithHtml(File htmlFile) throws Exception {
		/*	
		 * 	根据docx4j.properties配置文件中:
		 * 	docx4j.PageSize = A4
		 * 	docx4j.PageOrientationLandscape = true
		 * 	创建默认的WordProcessingML package
		 */
		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage();
		//返回WordprocessingMLPackage对象
		return buildWhithHtml(wmlPackage,htmlFile);
    }
	
	public WordprocessingMLPackage buildWhithHtml(File htmlFile,boolean landscape) throws Exception {
        //返回WordprocessingMLPackage对象
        return buildWhithHtml(htmlFile, PageSizePaper.A4, landscape);
    }
	
	public WordprocessingMLPackage buildWhithHtml(File htmlFile,PageSizePaper pageSize,boolean landscape) throws Exception {
		//创建指定纸张大小和方向的WordprocessingMLPackage对象
        WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage(pageSize, landscape); //A4纸，
        //返回WordprocessingMLPackage对象
        return buildWhithHtml(wmlPackage, htmlFile);
    }
	
	public WordprocessingMLPackage buildWhithHtml(WordprocessingMLPackage wmlPackage,File htmlFile) throws Exception {
		//构建任务开始
  		StartEvent jobStartEvent = new BuildStartEvent(BuildJobTypes.HTML, wmlPackage);
  		jobStartEvent.publish();
		//配置中文字体
		wmlPackage = initialize(wmlPackage); 
		//使用Handler渲染WordprocessingMLPackage对象
		handler.handle(wmlPackage, htmlFile, false);
		//构建任务结束
		new BuildFinishedEvent(jobStartEvent).publish();
		//返回WordprocessingMLPackage对象
		return wmlPackage;
    }
	
	/**
	 * 将 {@link org.jsoup.nodes.Document} 对象转为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage}
	 * @param doc
	 * @param landscape true 横版，false 竖版
	 */
	public WordprocessingMLPackage buildWhithHtml(String html) throws Exception {
		/*	
		 * 	根据docx4j.properties配置文件中:
		 * 	docx4j.PageSize = A4
		 * 	docx4j.PageOrientationLandscape = true
		 * 	创建默认的WordProcessingML package
		 */
  		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage();
		//返回WordprocessingMLPackage对象
		return buildWhithHtml(wmlPackage,html);
    }
	
	public WordprocessingMLPackage buildWhithHtml(String html,boolean landscape) throws Exception {
        //返回WordprocessingMLPackage对象
        return buildWhithHtml(html, PageSizePaper.A4, landscape);
    }
	
	public WordprocessingMLPackage buildWhithHtml(String html,PageSizePaper pageSize,boolean landscape) throws Exception {
		//创建指定纸张大小和方向的WordprocessingMLPackage对象
        WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage(pageSize, landscape); //A4纸，
        //返回WordprocessingMLPackage对象
        return buildWhithHtml(wmlPackage, html);
    }
	
	public WordprocessingMLPackage buildWhithHtml(WordprocessingMLPackage wmlPackage,String html) throws Exception {
		//构建任务开始
  		StartEvent jobStartEvent = new BuildStartEvent(BuildJobTypes.HTML, wmlPackage);
  		jobStartEvent.publish();
		//配置中文字体
		wmlPackage = initialize(wmlPackage); 
		//使用Handler渲染WordprocessingMLPackage对象
		handler.handle(wmlPackage, html , false );
		//构建任务结束
		new BuildFinishedEvent(jobStartEvent).publish();
		//返回WordprocessingMLPackage对象
		return wmlPackage;
    }
	
	/**
	 * 将 {@link org.jsoup.nodes.Document} 对象转为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage}
	 * @param doc
	 * @param landscape true 横版，false 竖版
	 */
	public WordprocessingMLPackage buildWhithHtmlFragment(String html) throws Exception {
		/*	
		 * 	根据docx4j.properties配置文件中:
		 * 	docx4j.PageSize = A4
		 * 	docx4j.PageOrientationLandscape = true
		 * 	创建默认的WordProcessingML package
		 */
  		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage();
		//返回WordprocessingMLPackage对象
		return buildWhithHtmlFragment(wmlPackage,html);
    }
	
	public WordprocessingMLPackage buildWhithHtmlFragment(String html,boolean landscape) throws Exception {
        //返回WordprocessingMLPackage对象
        return buildWhithHtmlFragment(html, PageSizePaper.A4, landscape);
    }
	
	public WordprocessingMLPackage buildWhithHtmlFragment(String html,PageSizePaper pageSize,boolean landscape) throws Exception {
		//创建指定纸张大小和方向的WordprocessingMLPackage对象
        WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage(pageSize, landscape); //A4纸，
        //返回WordprocessingMLPackage对象
        return buildWhithHtmlFragment(wmlPackage, html);
    }
	
	public WordprocessingMLPackage buildWhithHtmlFragment(WordprocessingMLPackage wmlPackage,String html) throws Exception {
		//构建任务开始
  		StartEvent jobStartEvent = new BuildStartEvent(BuildJobTypes.HTML, wmlPackage);
  		jobStartEvent.publish();
		//配置中文字体
		wmlPackage = initialize(wmlPackage); 
		//使用Handler渲染WordprocessingMLPackage对象
		handler.handle(wmlPackage, html , true );
		//构建任务结束
		new BuildFinishedEvent(jobStartEvent).publish();
		//返回WordprocessingMLPackage对象
		return wmlPackage;
    }
	
	/**
	 * 将 {@link org.jsoup.nodes.Document} 对象转为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage}
	 * @param doc
	 * @param landscape true 横版，false 竖版
	 */
	public WordprocessingMLPackage buildWhithURL(URL url) throws Exception {
		/*	
		 * 	根据docx4j.properties配置文件中:
		 * 	docx4j.PageSize = A4
		 * 	docx4j.PageOrientationLandscape = true
		 * 	创建默认的WordProcessingML package
		 */
  		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage();
		//返回WordprocessingMLPackage对象
		return buildWhithURL(wmlPackage,url);
    }
	
	public WordprocessingMLPackage buildWhithURL(URL url,boolean landscape) throws Exception {
        //返回WordprocessingMLPackage对象
        return buildWhithURL(url, PageSizePaper.A4, landscape);
    }
	
	public WordprocessingMLPackage buildWhithURL(URL url,PageSizePaper pageSize,boolean landscape) throws Exception {
		//创建指定纸张大小和方向的WordprocessingMLPackage对象
        WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.createPackage(pageSize, landscape); //A4纸，
        //返回WordprocessingMLPackage对象
        return buildWhithURL(wmlPackage, url);
    }
	
	public WordprocessingMLPackage buildWhithURL(WordprocessingMLPackage wmlPackage,URL url) throws Exception {
		//构建任务开始
  		StartEvent jobStartEvent = new BuildStartEvent(BuildJobTypes.URL, wmlPackage);
  		jobStartEvent.publish();
		//配置中文字体
		wmlPackage = initialize(wmlPackage); 
		//使用Handler渲染WordprocessingMLPackage对象
		handler.handle(wmlPackage, url, false);
		//构建任务结束
		new BuildFinishedEvent(jobStartEvent).publish();
		//返回WordprocessingMLPackage对象
		return wmlPackage;
    }
	
	public WordprocessingMLPackage buildWhithURL(WordprocessingMLPackage wmlPackage,String url, DataMap dataMap) throws Exception {
		//构建任务开始
  		StartEvent jobStartEvent = new BuildStartEvent(BuildJobTypes.URL, wmlPackage);
  		jobStartEvent.publish();
		//配置中文字体
		wmlPackage = initialize(wmlPackage); 
		//使用Handler渲染WordprocessingMLPackage对象
		handler.handle(wmlPackage, url, dataMap, false);
		//构建任务结束
		new BuildFinishedEvent(jobStartEvent).publish();
		//返回WordprocessingMLPackage对象
		return wmlPackage;
    }
	
}
