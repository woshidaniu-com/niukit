/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;

/**
 * 高级查询SQL解析器
 * @author Penghui.Qu
 * 	v1版本基础代码
 * @author zhangxiaobin
 *  v2版本基础代码
 * @author weiguangyue
 * 	v2版本整理，适配v1版本
 * 
 */
public class SearchParser {
	
	private static final Logger log = LoggerFactory.getLogger(SearchParser.class);
	//重复地反射操寻找Field,比较消耗时间,所以缓存具有searchModel的对象的class，直接通过属性获得searchModel对象,大概减少160ms时间消耗
	private static final Map<Class<?>,Field> Processed_Object_To_Field_Mapping = new HashMap<Class<?>,Field>(64);
	//设计有误的类,searchModel不是真正的SearchModel,发现就一直提示，直到开发人员解决
	//FIXME  tomcat或spring的start,stop事件无法捕获,无法及时remove这样的bad class
	private static final Map<Class<?>,String> Bad_Clss_Error_Message_Mapping = new HashMap<Class<?>,String>(1);

	private static final String SearchModel_Property_Name = "searchModel";
	
	private static final String SearchModel_Class_Name = SearchModel.class.getName();
	
	/**
	 * 获取预编译SQL
	 * @param model
	 * @return
	 */
	public static String getJdbcSQL(SearchModel model){
		parseMybatisSQL(model);
		String sql = model.getQuerySQL();
		return sql.replaceAll("#\\{[^\\}]*\\}", "?");
	}
	
	/**
	 * 解析object对象,构建高级查询必须的对象内部状态
	 * 1.若object是com.woshidaniu.search.core.SearchModel类的对象,包括其子类对象，构建这个对象能够满足高级搜索条件的内部属性状态
	 * 2.若object是具有com.woshidaniu.search.core.SearchModel类的属性的对象,构建这个对象的searchModel属性的值能够满足高级搜索条件的内部属性状态
	 * 3.其他情况，不做任何处理，只是提示错误信息
	 * 
	 * @param object 待处理的对象 
	 */
	public static void parseMybatisSQL(Object object){
		log.debug("高级查询版本:{},搜索条件对象:{}",Version.getVersion(),object);
		if(object == null) {
			return;
		}
		SearchModel searchModel = null;
		if (object instanceof SearchModel){
			searchModel = (SearchModel) object;
		}else {
			searchModel = lookupSearchModelProperty(object);
		}
		if(searchModel == null) {
			return;
		}
		String inputSqlTypeStr = searchModel.getInputSqlType();
		if(StringUtils.isNotEmpty(inputSqlTypeStr)) {
			//旧的模式v1版本
			SearchParserSqlBuilderV1.build(searchModel);
		}else {
			//新的模式v2版本
			//FIXME 有时间再来看这个空处理
			//优化第一次页面访问,快速返回,减少对象创建,v2版本有效
			//if(isEmptySearchModel(searchModel)) {
			//	searchModel.setQuerySQL("");
			//	return;
			//}
			SearchParserSqlBuilderV2.build(searchModel);
		}
	}
	
	private static boolean isEmptySearchModel(SearchModel searchModel) {
		String selectTytpe = searchModel.getSelectType();
		if(!"{}".equals(selectTytpe)) {
			return false;
		}
		
		String numberType = searchModel.getNumberType();
		if(!"{}".equals(numberType)) {
			return false;
		}
		
		String dateType = searchModel.getDateType();
		if(!"{}".equals(dateType)) {
			return false;
		}
		
		String inputSqlType =  searchModel.getInputSqlType();
		if(StringUtils.isNotEmpty(inputSqlType)) {
			return false;
		}
		
		String inputType = searchModel.getInputType();
		if(StringUtils.isNotEmpty(inputType) && !"[]".equals(inputType)) {
			return false;
		}
		
		String multipleInputType =  searchModel.getMultipleInputType();
		if(StringUtils.isNotEmpty(multipleInputType) && !"[]".equals(multipleInputType)) {
			return false;
		}
		
		return true;
	}

	private synchronized static SearchModel lookupSearchModelProperty(Object o) {
		Class<?> clazz = o.getClass();
		if (Processed_Object_To_Field_Mapping.containsKey(clazz)) {
			Field field = Processed_Object_To_Field_Mapping.get(clazz);
			if (field != null) {// 具有searchModel属性
				try {
					SearchModel searchModel = (SearchModel) field.get(o);
					return searchModel;
				} catch (IllegalArgumentException e) {
					log.error("对对象[{}]反射获取searchModel字段的值异常", o, e);
					return null;
				} catch (IllegalAccessException e) {
					log.error("对对象[{}]反射获取searchModel字段的值异常", o, e);
					return null;
				}
			} else {// 没有searchModel属性
				String error = Bad_Clss_Error_Message_Mapping.get(clazz);
				if(error != null) {
					log.error(error);
				}
				return null;
			}
		} else {
			Field f = getField(o, SearchModel_Property_Name);
			if (f != null) {//有search属性
				f.setAccessible(true);
				Object maybeSearchModel = null;
				try {
					maybeSearchModel = f.get(o);
				} catch (IllegalArgumentException e) {
					log.error("对对象[{}]反射获取searchModel字段的值异常", o, e);
					return null;
				} catch (IllegalAccessException e) {
					log.error("对对象[{}]反射获取searchModel字段的值异常", o, e);
					return null;
				}
				if (maybeSearchModel == null) {
					Processed_Object_To_Field_Mapping.put(clazz, null);
					String error = "对象["+ o +"]中的属性searchModel没有被实例化!!!";
					log.error(error, o);
					return null;
				} else {
					if (maybeSearchModel instanceof SearchModel) {
						SearchModel searchModel = (SearchModel) maybeSearchModel;
						Processed_Object_To_Field_Mapping.put(clazz, f);
						return searchModel;
					} else {
						Processed_Object_To_Field_Mapping.put(clazz, null);
						
						String error = "对象类型["+ clazz.getName() +"]中的属性searchModel的类别不是预期的类["+ SearchModel_Class_Name +"]";
						log.error(error);
						Bad_Clss_Error_Message_Mapping.put(clazz, error);
						return null;
					}
				}
			}else {//没有search属性
				return null;
			}
		}
	}
	
	private static Field getField(Object object, String propertyName) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field f = clazz.getDeclaredField(propertyName);
				f.setAccessible(true);
				return f;
			} catch (Exception e) {
				//ignore exception
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}
}

