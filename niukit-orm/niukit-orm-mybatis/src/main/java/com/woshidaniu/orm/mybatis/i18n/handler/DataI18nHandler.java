package com.woshidaniu.orm.mybatis.i18n.handler;

import java.util.Locale;

import org.apache.ibatis.plugin.Invocation;

import com.woshidaniu.orm.mybatis.interceptor.meta.MetaResultSetHandler;

public interface DataI18nHandler {

	Object wrap(Locale locale, Invocation invocation, MetaResultSetHandler metaResultSetHandler, Object result, Object orginParam) throws Exception ;
	
	Object handle(Locale locale, Invocation invocation, MetaResultSetHandler metaResultSetHandler, Object orginData,Object i18nData) throws Exception ;

	
}
