package com.woshidaniu.orm.mybatis.utils;

import java.lang.reflect.Field;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.reflection.MetaObject;

import com.woshidaniu.beanutils.reflection.ReflectionUtils;

/**
 * 
 *@类名称		: BoundSQLUtils.java
 *@类描述		： BoundSql对象操作工具
 *@创建人		： kangzhidong
 *@创建时间	： 2016年4月11日 上午10:05:52
 *@修改人		：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class BoundSQLUtils {
	
	/**
	 * 
	 * @description	：解决 MyBatis 物理分页foreach 参数失效 
	 * <pre>
	 * 	场景：MyBatis 物理分页，查询条件中需要用到foreach ，参数失效，查不到结果
	 * 	分析：把java.sql的debug打开，sql语句正常，参数也正常。debug物理分页代码，setParameters时，
	 * 	   boundSql.getAdditionalParameter(propertyName)获取值始终是null，没有拿到参数。但是BoundSql的metaParameters中可以看到相关的参数值。
	 * 	解决方法：
	 * 		BoundSql countBS = new BoundSql(configuration, sql, boundSql.getParameterMappings(), parameterObject);
     *      Field metaParamsField = ReflectUtil.getFieldByFieldName(boundSql, "metaParameters");
     *      if (metaParamsField != null) {
     *           MetaObject mo = (MetaObject) ReflectUtil.getValueByFieldName(boundSql, "metaParameters");
     *           ReflectUtil.setValueByFieldName(countBS, "metaParameters", mo);
     *      }
     *      setParameters(prepStat, configuration, countBS, parameterObject);
     * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 27, 2016 10:19:55 AM
	 * @param sourceBoundSql
	 * @param targetBoundSql
	 */
	public static void setBoundSql(BoundSql sourceBoundSql,BoundSql targetBoundSql){
		Field metaParamsField = ReflectionUtils.getAccessibleField(sourceBoundSql, "metaParameters");
		if (metaParamsField != null) {
	       try {
				MetaObject metaParameters = (MetaObject) ReflectionUtils.getField("metaParameters",sourceBoundSql);
				ReflectionUtils.setField("metaParameters", targetBoundSql, metaParameters);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
