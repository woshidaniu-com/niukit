package com.woshidaniu.fastdoc.core.utils;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.woshidaniu.basicutils.Assert;
/**
 * 
 *@类名称	: ExtensionUtils.java
 *@类描述	：文档后缀判断方法
 *@创建人	：kangzhidong
 *@创建时间	：Mar 25, 2016 12:02:37 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ExtensionUtils {
	
	public static boolean isDoc(String filePath) {
		return ExtensionUtils.isDoc(new File(filePath));
	}
	
	public static boolean isDoc(File file) {
		Assert.notNull(file, " file is not specified!");
		Assert.isTrue(file.exists(), " file is not found !");
		Assert.isTrue(file.isFile(), " file is not a file !");
		return "doc".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
	public static boolean isDocx(String filePath) {
		return ExtensionUtils.isDocx(new File(filePath));
	}
	
	public static boolean isDocx(File file) {
		Assert.notNull(file, " file is not specified!");
		Assert.isTrue(file.exists(), " file is not found !");
		Assert.isTrue(file.isFile(), " file is not a file !");
		return "docx".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()));
	}
	
}
