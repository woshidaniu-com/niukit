/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j;

/**
 * *******************************************************************
 * @className	： TestHtmlConverter
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Dec 26, 2016 10:43:01 AM
 * @version 	V1.0 
 * *******************************************************************
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Created by noah on 3/12/15.
 */
public class TestHtmlConverter {
	@BeforeClass
	public static void before() {
	}
	@AfterClass
	public static void after() {
	}
	@Test
	public void test() throws Exception {
		
		String html = "<html><head><title>First parse</title></head>"
				  + "<body><p>Parsed HTML into a doc.</p></body></html>";
				Document doc = Jsoup.parse(html,"http://example.com/");
				System.out.println(doc.baseUri());
		/*HtmlConverter converter = new HtmlConverter();
		String url = null;//"http://127.0.0.1:" + htmlServer.getPort() + "/report.html"; //输入要转换的网址
		File fileDocx = converter.saveUrlToDocx(url);
		File filePdf = converter.saveUrlToPdf(url);
		Desktop.getDesktop().open(fileDocx); //由操作系统打开
		Desktop.getDesktop().open(filePdf);*/
	}
}