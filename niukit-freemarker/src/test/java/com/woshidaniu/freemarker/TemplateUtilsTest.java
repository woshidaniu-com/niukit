/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.freemarker;

import junit.framework.TestCase;

import org.junit.Test;

import com.woshidaniu.freemarker.utils.TemplateUtils;

import freemarker.template.Template;

/**
 *@类名称	: TemplateUtilsTest.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：May 24, 2016 5:11:44 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class TemplateUtilsTest extends TestCase {

	@Test
	public void testName() throws Exception {
		
		//
		Template tmp =TemplateUtils.getClasspathTemplate(getClass(), "tt.ftl");
		
		System.out.println("sss:" + tmp.toString());
	}
	
}
