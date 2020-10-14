package com.woshidaniu.fastxls.core.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
/**
 * 
 *@类名称	: ExtensionUtils.java
 *@类描述	：文档后缀判断方法
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 11:14:34 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ExtensionUtils {
	
	public static boolean isXls(String filePath) {
		return ExtensionUtils.isXls(new File(filePath));
	}
	
	public static boolean isXls(File file) {
		notNull(file, " file is not specified!");
		isTrue(file.exists(), " file is not found !");
		isTrue(file.isFile(), " file is not a file !");
		return "xls".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
	public static boolean isXlsx(String filePath) {
		return ExtensionUtils.isXlsx(new File(filePath));
	}
	
	public static boolean isXlsx(File file) {
		notNull(file, " file is not specified!");
		isTrue(file.exists(), " file is not found !");
		isTrue(file.isFile(), " file is not a file !");
		return "xlsx".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
	/**
 	 * Assert that an object is not <code>null</code> .
 	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
 	 * @param object the object to check
 	 * @param message the exception message to use if the assertion fails
 	 * @throws IllegalArgumentException if the object is <code>null</code>
 	 */
 	private static void notNull(Object object, String message) {
 		if (object == null) {
 			throw new IllegalArgumentException(message);
 		}
 	}
 	
 	private static void isTrue(boolean expression, String message) {
 		if (!expression) {
 			throw new IllegalArgumentException(message);
 		}
 	}
	
}
