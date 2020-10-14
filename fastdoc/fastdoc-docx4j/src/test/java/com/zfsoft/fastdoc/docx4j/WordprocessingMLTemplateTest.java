/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.fastdoc.docx4j;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * *******************************************************************
 * @className	： WordprocessingMLTemplateTest
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Dec 30, 2016 10:48:48 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class WordprocessingMLTemplateTest {

	protected WordprocessingMLTemplateWriter wemplateWriter = null;
	
	@Before
	public void Before() {
		wemplateWriter = new WordprocessingMLTemplateWriter();
		
		/*------解决方案--------------------
		添加xercesImpl.jar和 xml-apis.jar 到 /WEB-INF/lib；

		如果不行，加以下代码：*/
		//System.setProperty( "javax.xml.parsers.DocumentBuilderFactory ", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl "); 
		//org.eclipse.persistence.jaxb.JAXBContextFactory
	}
	
	@Test
	public void test() throws Exception {
		
		try {
			System.out.println(wemplateWriter.writeToString(new File("E:\\woshidaniu\\关于实现教务系统国际化方案.docx")));;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@After
	public void after() {
		wemplateWriter = null;
	}
	
	
}
