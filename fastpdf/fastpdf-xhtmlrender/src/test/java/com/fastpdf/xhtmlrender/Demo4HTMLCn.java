
package com.fastpdf.xhtmlrender;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * 测试xml worker 页面包含中文字符的转换
 *
 * @author <a href="http://www.micmiu.com">Michael Sun</a>
 */
public class Demo4HTMLCn {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String pdfFile = "d:/demo-html-cn.pdf";
		String htmlFile = "d:/demo-cn.html";

		InputStream htmlFileStream = Demo4HTMLCn.class.getResourceAsStream("demo-cn.html");
		
		Document document = new Document(PageSize.A4,30,30,25,25);
		
		PdfWriter pdfwriter = PdfWriter.getInstance(document,new FileOutputStream(pdfFile));
		document.open();
		String text = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
						+"<html xmlns=\"http://www.w3.org/1999/xhtml\" dir=\"ltr\" lang=\"zh-CN\">"
								+"<head>"
								+"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"
							+"<title>iText xmlworker</title>"
						+"</head>"
						+"<body>"
						+"<div>"
							+"<p>[ Hello iText English ]</p>"
							+"<p class=\"test\">[ 你好 iText]</p>"
						+"</div>"
						+"</body>"
						+"</html>";
		// html文件
		String css = ".test{color:red;}";
		String fontsPath = "E:\\woshidaniu\\workspace2\\jpxt-zkgl\\WebRoot\\fonts";
		FontFactory.registerDirectory(fontsPath, true);
		InputStreamReader isr3 = new InputStreamReader(htmlFileStream, "UTF-8");
		XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, isr3);
		/*InputStreamReader isr = new InputStreamReader(new StringBufferInputStream(text),"UTF-8");
		XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, isr);
		
		XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, new StringBufferInputStream(text), new StringBufferInputStream(css), Charset.defaultCharset(), new XMLWorkerFontProvider(fontsPath));
		
		*/
		document.close();

	}

}


