package com.woshidaniu.orm.mybatis.i18n.handler;

import java.util.Locale;

import com.woshidaniu.orm.mybatis.annotation.I18nMapper;
import com.woshidaniu.orm.mybatis.annotation.I18nPrimary;


public interface DataI18nMappedHandler {

	String getPrimaryName(I18nPrimary i18nPrimary, Object source) throws Exception ;
	
	DataI18nMapper handle(Locale locale,I18nMapper i18nMapper, String primaryName , Object orginObject, Object i18nObject) throws Exception ;
	
}
