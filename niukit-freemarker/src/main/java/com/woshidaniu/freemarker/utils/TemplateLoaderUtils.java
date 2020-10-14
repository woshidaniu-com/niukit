/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.freemarker.utils;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

/**
 *@类名称	: TemplateLoaderUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：May 24, 2016 3:25:35 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */

public class TemplateLoaderUtils {

	 public static String getClassNameForToString(TemplateLoader templateLoader) {
        final Class tlClass = templateLoader.getClass();
        final Package tlPackage = tlClass.getPackage();
        return tlPackage == Configuration.class.getPackage() || tlPackage == TemplateLoader.class.getPackage()
                ? getSimpleName(tlClass) : tlClass.getName();
    }

    // [Java 5] Replace with Class.getSimpleName()
    private static String getSimpleName(final Class tlClass) {
        final String name = tlClass.getName();
        int lastDotIdx = name.lastIndexOf('.'); 
        return lastDotIdx < 0 ? name : name.substring(lastDotIdx + 1);
    }
	
}
