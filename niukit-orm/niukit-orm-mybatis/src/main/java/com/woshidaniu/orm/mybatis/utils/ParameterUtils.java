package com.woshidaniu.orm.mybatis.utils;

import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basemodel.BaseModel;
import com.woshidaniu.basemodel.QueryModel;

public abstract class ParameterUtils {

	protected static Logger LOG = LoggerFactory.getLogger(ParameterUtils.class);
	
	@SuppressWarnings("unchecked")
	public static QueryModel getQueryModel(Object parameterObject){
		if(parameterObject == null){
			return null;
		}
		QueryModel queryModel = null;
		//参数就是Page实体
		if(parameterObject instanceof QueryModel){	
			queryModel = (QueryModel) parameterObject;
		}if(parameterObject instanceof Map){
			//参数就是Map
			Map<String,Object> parameterMap = (Map<String,Object>)parameterObject;
			for (String key : parameterMap.keySet()) {
				if(parameterMap.get(key) instanceof QueryModel){	
					queryModel = (QueryModel) parameterMap.get(key);
					break;
				}
			}
		}else {
			//参数为某个实体，该实体拥有queryModel属性或者包含QueryModel类型对象
			MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
			if(metaObject.hasGetter("queryModel")){
				queryModel = (QueryModel) metaObject.getValue("queryModel");
			}else {
				LOG.error(parameterObject.getClass().getName()+"不存在 queryModel 属性 或 QueryModel类型对象！");
			}
		}
		return queryModel;
	}
	
	@SuppressWarnings("unchecked")
	public static void setQueryModel(Object parameterObject,QueryModel queryModel){
		//参数就是Page实体
		if(parameterObject instanceof QueryModel){	
			parameterObject = queryModel;
		}else if(parameterObject instanceof Map){
			//参数就是Map
			Map<String,Object> parameterMap = (Map<String,Object>)parameterObject;
			for (String key : parameterMap.keySet()) {
				if(parameterMap.get(key) instanceof QueryModel){	
					parameterMap.remove(key);
					parameterMap.put(key, queryModel);
					break;
				}
			}
		} else{	
			//参数为某个实体，该实体拥有queryModel属性或者包含QueryModel类型对象
			MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
			if(metaObject.hasGetter("queryModel")){
				metaObject.setValue("queryModel",queryModel);
			}
		}
	}
	
	public static boolean isPageable(Object parameterObject) {
		if(parameterObject == null){
			return false;
		}
		//参数就是BaseModel子类
		boolean pageable = false;
		if(parameterObject instanceof BaseModel){	
			BaseModel model = (BaseModel) parameterObject;
			pageable = model.isPageable();
		}else{	
			//参数为某个实体，该实体拥有queryModel属性或者包含QueryModel类型对象
			MetaObject metaObject = MetaObjectUtils.forObject(parameterObject);
			if(metaObject.hasGetter("pageable")){
				pageable = Boolean.parseBoolean(String.valueOf(metaObject.getValue("pageable")));
			}
		}
		return pageable;
	}
	
}
