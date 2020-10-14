/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.orm.mybatis.scripting;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

import com.woshidaniu.orm.mybatis.MybatisParameterHandler;

/**
 * 
 *@类名称	: MybatisXMLLanguageDriver.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Aug 25, 2016 9:20:24 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class MybatisXMLLanguageDriver extends XMLLanguageDriver {

	@Override
	public ParameterHandler createParameterHandler(
			MappedStatement mappedStatement, Object parameterObject,
			BoundSql boundSql) {
		return new MybatisParameterHandler(mappedStatement, parameterObject, boundSql);
	}
 
}
