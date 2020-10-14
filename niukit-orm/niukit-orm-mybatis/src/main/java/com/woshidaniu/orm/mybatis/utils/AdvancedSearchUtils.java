package com.woshidaniu.orm.mybatis.utils;

import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.search.core.SearchModel;

public class AdvancedSearchUtils {

	protected static Logger LOG = LoggerFactory.getLogger(ParameterUtils.class);
	
	/**
	 * 
	 * <p>方法说明：获取高级查询参数对象<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2017年3月9日上午9:18:01<p>
	 * @param parameterObject
	 * @return
	 */
	public static SearchModel getSearchModel(Object parameterObject){
		if(parameterObject == null){
			return null;
		}
		SearchModel queryModel = null;
		//参数就是Page实体
		if(parameterObject instanceof SearchModel){	
			queryModel = (SearchModel) parameterObject;
		}else {
			//参数为某个实体，该实体拥有queryModel属性或者包含QueryModel类型对象
			MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
			if(metaObject.hasGetter("searchModel")){
				queryModel = (SearchModel) metaObject.getValue("searchModel");
			}else {
				LOG.error(parameterObject.getClass().getName()+"不存在 searchModel 属性 或 SearchModel类型对象！");
			}
		}
		return queryModel;
	}
	
}
