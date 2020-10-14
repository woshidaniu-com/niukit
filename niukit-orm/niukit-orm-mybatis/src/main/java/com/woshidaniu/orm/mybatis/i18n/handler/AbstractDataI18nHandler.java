package com.woshidaniu.orm.mybatis.i18n.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import org.apache.ibatis.plugin.Invocation;
import org.springframework.util.ObjectUtils;

import com.woshidaniu.orm.mybatis.interceptor.meta.MetaResultSetHandler;

@SuppressWarnings("unchecked")
public abstract class AbstractDataI18nHandler implements DataI18nHandler {

	@Override
	public Object handle(Locale locale,Invocation invocation,MetaResultSetHandler metaResultSetHandler, Object orginData, Object i18nData) throws Exception  {
		Collection<Object> orginList  = null;
		Collection<Object> i18nList   = null;
		// 原始数据集合化转换
		if(!Collection.class.isAssignableFrom(orginData.getClass())){
			orginList  = Arrays.asList(ObjectUtils.toObjectArray(orginData));
		} else {
			orginList  = (Collection<Object>) orginData;
		}
		// 原始数据为空，则跳过后面逻辑
		if(orginList == null || orginList.size() == 0){
			return orginList;
		}
		// 国际化数据集合化转换
		if(!Collection.class.isAssignableFrom(i18nData.getClass())){
			i18nList  = Arrays.asList(ObjectUtils.toObjectArray(i18nData));
		} else {
			i18nList  = (Collection<Object>) i18nData;
		}
		//国际化数据为空，则跳过后面逻辑
		if(i18nList == null || i18nList.size() == 0){
			return orginList;
		}
		return doHandle(locale, invocation, metaResultSetHandler, orginList , i18nList );
	}
	
	public abstract Object doHandle(Locale locale,Invocation invocation,MetaResultSetHandler metaResultSetHandler,Collection<Object> orginList,Collection<Object> i18nList) throws Exception ;

}
