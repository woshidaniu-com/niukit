package com.woshidaniu.xmlhub.utils;

import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.LocaleUtils;

public abstract class XMLLocaleUtils {
    
	public final static Locale DEFAULT_LOCALE = new Locale("zh","CN");
	
	public static String getLocalePath(Locale locale,String filepath){
		if (locale == null) {
			locale = DEFAULT_LOCALE;
		}
		String fullPath = FilenameUtils.getFullPath(filepath);
		String baseName = FilenameUtils.getBaseName(filepath);
		String extension = FilenameUtils.getExtension(filepath);
		String finalpath = fullPath + baseName + "_" + locale.getLanguage() + "_" + locale.getCountry() + "." + extension;
		return finalpath;
	}
	
    public static String getLocalePath(String locale,String filepath){
		return getLocalePath(LocaleUtils.toLocale(locale),filepath);
	}

}
