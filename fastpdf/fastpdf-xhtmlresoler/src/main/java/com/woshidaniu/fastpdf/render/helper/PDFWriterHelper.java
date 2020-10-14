package com.woshidaniu.fastpdf.render.helper;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import com.woshidaniu.fastpdf.render.elements.ItextXMLElement;
import com.woshidaniu.fastpdf.render.events.PDFPageEvent;
/**
  * 
  * @package com.woshidaniu.fastpdf.render.helper
  * @className: PDFWriterHelper
  * @description: TODO
  * @author : kangzhidong
  * @date : 2014-1-14
  * @time : 上午11:47:08
  */
public final class PDFWriterHelper {
	
	protected static final Map<String,PdfName> versions = new HashMap<String, PdfName>();
	private static PDFWriterHelper instance = null;
	private Map<String,String> attrs;
	private PDFWriterHelper(Map<String,String> attrs){
		this.attrs = attrs;
		versions.put("1.2", PdfWriter.PDF_VERSION_1_2);
		versions.put("1.3", PdfWriter.PDF_VERSION_1_3);
		versions.put("1.4", PdfWriter.PDF_VERSION_1_4);
		versions.put("1.5", PdfWriter.PDF_VERSION_1_5);
		versions.put("1.6", PdfWriter.PDF_VERSION_1_6);
		versions.put("1.7", PdfWriter.PDF_VERSION_1_7);
	}
	
	public static PDFWriterHelper getInstance(Map<String,String> attrs){
		instance = instance==null?new PDFWriterHelper(attrs):instance;
		return instance;
	}
	
	
	public PdfWriter getPDFWriter(Document document,OutputStream out,String documentID) throws DocumentException{
		//得到二级xml配置元素，即document元素
		ItextXMLElement element = ItextContext.getElement(documentID);
		return this.getPDFWriter(document, out, element); 
	}
	
	public PdfWriter getPDFWriter(Document document,OutputStream out,ItextXMLElement element) throws DocumentException{
		//初始化PdfWriter
		PdfWriter writer = PdfWriter.getInstance(document, out);
		
		if(null!=element){
			//设置阅读器参数 
			String version = element.attr("version"); 
			//PDF版本(默认1.4)
			PdfName pdfName = versions.get(version);
					pdfName = BlankUtils.isBlank(pdfName)?PdfWriter.PDF_VERSION_1_4:pdfName;
			writer.setPdfVersion(pdfName); 
			
			//设置文档展现方式
			/*文件被打开时，页面布局用到下面的其中一个 ：
			PdfWriter.PageLayoutSinglePage – 同时只显示一个页面 
			PdfWriter.PageLayoutOneColumn –单列显示 
			PdfWriter.PageLayoutTwoColumnLeft –双列显示,奇数页在左 
			PdfWriter.PageLayoutTwoColumnRight -双列显示,奇数页在右 
			文件打开时，页面模式用到下面其中之一：
			PdfWriter.PageModeUseNone – 既不显示大钢也不显示缩略图 
			PdfWriter.PageModeUseOutlines – 显示大纲 
			PdfWriter.PageModeUseThumbs – 显示缩略图 
			PdfWriter.PageModeFullScreen – 全屏模式，没有菜单、windows控件或者其他任何windows可见控件 
			PdfWriter.HideToolbar – 当文档激活时，是否隐藏阅读程序（如Adobe Reader）的工具条
			PdfWriter.HideMenubar -当文档激活时，是否隐藏阅读程序的菜单.
			PdfWriter.HideWindowUI -当文档激活时，是否隐藏阅读程序的界面元素，如滚动条、导航条等，而仅仅保留文档显示
			PdfWriter.FitWindow – 是否调整文档窗口尺寸以适合显示第一页。
			PdfWriter.CenterWindow – 是否将文档窗口放到屏幕中央
			在全屏模式下，指定如何显示界面元素（选择一个）
			PdfWriter.NonFullScreenPageModeUseNone -既不显示大钢也不显示缩略图 
			PdfWriter.NonFullScreenPageModeUseOutlines – 显示大钢 
			PdfWriter.NonFullScreenPageModeUseThumbs – 显示缩略图 */
			writer.setViewerPreferences(PdfWriter.PageLayoutOneColumn | PdfWriter.PageModeFullScreen | PdfWriter.NonFullScreenPageModeUseNone);
			
			//设置密码
			if(element.hasChild("encrypt")){
				ItextXMLElement encrypt = (ItextXMLElement)element.getChild("encrypt");
				String user = encrypt.attr("user");
				String password = encrypt.attr("password");
				if(!BlankUtils.isBlank(user)&&!BlankUtils.isBlank(password)){
					//进行转义
					user = StringPatternFormat.getInstance().format(user, attrs);
					password = StringPatternFormat.getInstance().format(password, attrs);
					//打开文档之前还要做的一件事情就是加密（如果你希望该文档加密），要达到这个目的，你可以使用下面的方法
					writer.setEncryption(user.getBytes(), password.getBytes(),PdfWriter.ALLOW_SCREENREADERS,PdfWriter.STANDARD_ENCRYPTION_128);
					/*strength 是下面两个常量之一： 
					PdfWriter.STRENGTH40BITS: 40 位 
					PdfWriter.STRENGTH128BITS: 128位 (Acrobat Reader 5.0及以上版本支持) 
					UserPassword和ownerPassword 可以为空或零长度， 这种情况下， ownerPassword 将被随机的字符串代替 
					Permissions 为下列常量之一： 
					PdfWriter.AllowPrinting 
					PdfWriter.AllowModifyContents 
					PdfWriter.AllowCopy 
					PdfWriter.AllowModifyAnnotations 
					PdfWriter.AllowFillIn 
					PdfWriter.AllowScreenReaders 
					PdfWriter.AllowAssembly 
					PdfWriter.AllowDegradedPrinting */
				}
			}
			//设置事件 
			writer.setPageEvent(new PDFPageEvent(element)); 
		}
	    //返回渲染后的Rectangle对象
		return writer; 
	}
	
}
