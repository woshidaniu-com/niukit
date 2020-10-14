/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 高级查询SQL解析器
 * @author Penghui.Qu
 * 	v1版本基础代码
 * @author zhangxiaobin
 *  v2版本基础代码
 * @author weiguangyue
 * 	v2版本整理，适配v1版本
 *  FIX 多列放置在一个块内且是not_like或not_equal,则添加 xx is not null 并且使用 and 连接
 *  //TODO 优化sql拼接逻辑
 */
class SearchParserSqlBuilderV2 extends SearchParserSqlBuilder{
	
	private static final Logger log = LoggerFactory.getLogger(SearchParserSqlBuilderV2.class);
	
	public static void build(SearchModel searchModel) {
		
		Map<String,String> params = new HashMap<String, String>();
		
		//多文本条件
		JSONArray multipleInputJson = JSONArray.fromObject(unescapeHtml(searchModel.getMultipleInputType()));
		JSONObject selectJson = JSONObject.fromObject(unescapeHtml(searchModel.getSelectType()));
		JSONObject dateJson = JSONObject.fromObject(unescapeHtml(searchModel.getDateType()));
		JSONObject numberJson = JSONObject.fromObject(unescapeHtml(searchModel.getNumberType()));

		String eqSql = getEqualSQL(selectJson, params, SQLType.MYBATIS);
		String likeSql = getInputSQL(multipleInputJson, params,SQLType.MYBATIS);
		String dateSql = getDateSQL(dateJson, params, SQLType.MYBATIS);
		String numberSql = getNumberSQL(numberJson, params, SQLType.MYBATIS);
		
		StringBuilder sql = new StringBuilder();
		if (!StringUtils.isNull(eqSql)){
			sql.append(eqSql);
		}
		
		if (!StringUtils.isNull(likeSql) && sql.length() > 0){
			sql.append(" and ");
		}
		sql.append(likeSql);
		
		if (!StringUtils.isNull(dateSql) && sql.length() > 0){
			sql.append(" and ");
		}
		sql.append(dateSql);
		
		if (!StringUtils.isNull(numberSql) && sql.length() > 0){
			sql.append(" and ");
		}
		sql.append(numberSql);
		
		String querySql = sql.toString();
		if(log.isDebugEnabled()) {
			log.debug("高级查询版本:{},releaseDate:{},构建文本查询sql:{}",Version.getVersion(),Version.getReleaseDate(),querySql);			
		}
		searchModel.setQuerySQL(querySql);
		searchModel.setParams(params);
	}
	
	private static String getInputSQL(JSONArray inputJsonObjects,Map<String,String> params,SQLType sqlType){
		if(null == inputJsonObjects || inputJsonObjects.isEmpty()){
			return "";
		}
		
		List<String> inputJsonSqls = new ArrayList<String>();
		
		for(int i=0;i<inputJsonObjects.size();i++) {
			
			JSONObject inputJson = inputJsonObjects.getJSONObject(i);
			int inputSqlTypeIntValue = inputJson.getInt("sqlType");
			MultipleInputType prasedInputSqlType= MultipleInputType.getByKey(inputSqlTypeIntValue);

			//单个条件中的连接方式是or,多个条件且是not_like和not_equal则是and
			String doGetSql = doGetInputSQL(inputJson, params, prasedInputSqlType, sqlType);
			if(StringUtils.isNotEmpty(doGetSql)){
				inputJsonSqls.add(doGetSql);
			}
		}
		//各个条件块之间才是and
		if(inputJsonSqls.isEmpty()) {
			return "";
		}else {
			int size = inputJsonSqls.size();
			if(size == 1) {
				String sql = inputJsonSqls.get(0);
				StringBuilder sb = new StringBuilder(" ( ").append(sql).append(" ) ");
				return sb.toString();
			}else {
				StringBuilder sb = new StringBuilder(" ( ");
				for(int i=0;i<size;i++) {
					String sql = inputJsonSqls.get(i);
					sb.append(sql);
					boolean last = (i == size - 1);
					if(!last) {
						sb.append(" and ");
					}
				}
				sb.append(" ) ");
				return sb.toString();
			}
		}
	}
	//like 语句拼写
	//{"blurType":"zgh,xm","blurValue":"weiguangyue","sqlType":"0"}
	private static String doGetInputSQL(JSONObject inputJson,Map<String,String> params,MultipleInputType inputSqlType, SQLType sqlType){
		if (inputJson.isEmpty()){
			return "";
		}
		//这里的key是多个列名称
	    String key = inputJson.getString("blurType");
	    String[] array = StringUtils.delimitedListToStringArray(key, ",");
	    if(array == null || array.length <= 0) {
	    	return "";
	    }
	    String linkType = "or";
	    //存在多列且是不等于，不包含，则使用and连接
	    if(array.length > 1 && ( inputSqlType.equals(MultipleInputType.NOT_EQUAL) || inputSqlType.equals(MultipleInputType.NOT_LIKE))) {
	    	linkType = "and";
	    }
	    List<String> orConditionSqls = new ArrayList<String>();
	    
	    for(int i=0;i<array.length;i++) {
	    	
	    	String k = array[i];
	    	String value = inputJson.getString("blurValue");
	    	String index = inputJson.getString("index");
	    	switch (sqlType) {
	    	case MYBATIS:
	    		String sqlStr = getMybatisInputSQL(k,value,params,inputSqlType, index);
	    		if(StringUtils.isNotEmpty(sqlStr)) {
	    			String orSqlStr = " ( " + sqlStr + " ) ";
	    			orConditionSqls.add(orSqlStr);	    			
	    		}
	    		break;
	    	case JDBC:
	    		log.warn("暂未实现sqlType类型为{}的sql拼接!!!",sqlType);
	    		break;
	    	default:
	    		log.warn("暂未实现sqlType类型为{}的sql拼接!!!",sqlType);
	    	}
	    }
	    if(orConditionSqls.isEmpty()) {
	    	return "";
	    }else {
	    	int size = orConditionSqls.size();
	    	if(size == 1) {
	    		String sql = orConditionSqls.get(0);
				StringBuilder sb = new StringBuilder(" ( ").append(sql).append(" ) ");
				return sb.toString();
	    	}else {
				StringBuilder sb = new StringBuilder(" ( ");
				for(int i=0;i<size;i++) {
					String sql = orConditionSqls.get(i);
					sb.append(sql);
					boolean last = (i == size - 1);
					if(!last) {
						sb.append("  ").append(linkType).append("  ");
					}
				}
				sb.append(" ) ");
				return sb.toString();
	    	}
	    }
	}

	//mybatis like sql 拼接
	private static String getMybatisInputSQL(String key, String value, Map<String, String> params, MultipleInputType inputSqlType, String index) {
		
		StringBuilder sb = new StringBuilder();
		
		if (StringUtils.isNull(value.trim())) return "";
		
		String paramKey = String.format("%s_%s", key, index);
		
		switch (inputSqlType) {
			case NOT_LIKE:
				if (sb.length() > 0){
					sb.append(" and ");
				}
				sb.append(key).append(" is null ").append(" or ").append(key).append(" not like concat('%',concat(#{searchModel.params.").append(paramKey).append("},'%'))");
				break;
			case EQUAL:
				if (sb.length() > 0){
					sb.append(" or ");
				}
				sb.append(key).append("=#{searchModel.params.").append(paramKey).append("}");
				break;
			case NOT_EQUAL:
				if (sb.length() > 0){
					sb.append(" and ");
				}
				sb.append(key).append(" is null ").append(" or ").append(key).append(" <> #{searchModel.params.").append(paramKey).append("}");
				break;
			case LIKE:
				if (sb.length() > 0){
					sb.append(" or ");
				}
				sb.append(key).append(" like concat('%',concat(#{searchModel.params.").append(paramKey).append("},'%'))");
				break;
			case IN:
			{
				//key --> xm
				//value --> 康康;张三;李四
				String[] array = value.split(";");
				if(array == null || array.length <= 0){
					break;
				}
				if (sb.length() > 0){
					sb.append(" and ");
				}
				
				List<String> mybatisParamNameList = new ArrayList<String>(array.length);
				for(int i =0;i<array.length;i++){
					
					String val = array[i];
					
					//设置mybatis的mapper方法的参数
					String paramName = paramKey + "_" +i;
					params.put(paramName, val);
					
					//设置构建的mytbatis动态sql片段
					String mybatisParamName =  String.format("#{searchModel.params.%s_%s}", paramKey,i);
					mybatisParamNameList.add(mybatisParamName);
				}
				
				//#{searchModel.params.xm_0_0},#{searchModel.params.xm_0_1}
				String inCondition = StringUtils.join(mybatisParamNameList, " , ");
				
				sb.append(key).append(" in ( ").append(inCondition).append(" ) ");
			}
				break;
			case NOT_IN:
			{
				//key --> xm
				//value --> 康康;张三;李四
				String[] array = value.split(";");
				if(array == null || array.length <= 0){
					break;
				}
				if (sb.length() > 0){
					sb.append(" and ");
				}
				
				List<String> mybatisParamNameList = new ArrayList<String>(array.length);
				for(int i =0;i<array.length;i++){
					
					String val = array[i];
					
					//设置mybatis的mapper方法的参数
					String paramName = paramKey + "_" +i;
					params.put(paramName, val);
					
					//设置构建的mytbatis动态sql片段
					String mybatisParamName =  String.format("#{searchModel.params.%s_%s}", paramKey,i);
					mybatisParamNameList.add(mybatisParamName);
				}
				
				//#{searchModel.params.xm_0_0},#{searchModel.params.xm_0_1}
				String inCondition = StringUtils.join(mybatisParamNameList, " , ");
				
				sb.append(key).append(" not in ( ").append(inCondition).append(" ) ");
			}
				break;
			case GE:
				if (sb.length() > 0){
					sb.append(" and ");
				}
				sb.append(key).append(" >= #{searchModel.params.").append(paramKey).append("}");
				break;
			case LE:
				if (sb.length() > 0){
					sb.append(" and ");
				}
				sb.append(key).append(" <= #{searchModel.params.").append(paramKey).append("}");
				break;
			//*****************************************不需要文本输入************************************************
			case IS_NULL:
				if (sb.length() > 0){
					sb.append(" and ");
				}
				sb.append(key).append(" is null ");
				break;
			case IS_NOT_NULL:
				if (sb.length() > 0){
					sb.append(" and ");
				}
				sb.append(key).append(" is not null ");
				break;
			default:
				log.warn("暂未实现inputSqlType类型为{}的sql拼接",inputSqlType);
		}
			
		params.put(paramKey, value);
		return sb.toString();
	}
}
