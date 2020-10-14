/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.orm.mybatis;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *@类名称	: MybatisParameterHandler.java
 *@类描述	： 对SQL参数(?)设值
 *<pre>
 *	通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
 *	ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);  
 *	 通过parameterHandler给PreparedStatement对象设置参数
 *	parameterHandler.setParameters(pstmt);
 *</pre>
 *@创建人	：kangzhidong
 *@创建时间	：Aug 25, 2016 8:46:39 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@see org.apache.ibatis.executor.parameter.DefaultParameterHandler
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class MybatisParameterHandler implements ParameterHandler {
	
	protected static Logger LOG = LoggerFactory.getLogger(MybatisParameterHandler.class);
	protected final TypeHandlerRegistry typeHandlerRegistry;
	protected final MappedStatement mappedStatement;
	protected final Object parameterObject;
	protected BoundSql boundSql;
	protected Configuration configuration;

	public MybatisParameterHandler(MappedStatement mappedStatement,Object parameterObject, BoundSql boundSql) {
		this.mappedStatement = mappedStatement;
		this.configuration = mappedStatement.getConfiguration();
		this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
	}

	@Override
	public Object getParameterObject() {
		return parameterObject;
	}

	@Override
	public void setParameters(PreparedStatement ps) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value = null;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					 if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						MetaObject metaObject = configuration.newMetaObject(parameterObject);
						value = metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					JdbcType jdbcType = parameterMapping.getJdbcType();
			        if (value == null && jdbcType == null) {
			            jdbcType = configuration.getJdbcTypeForNull();
			        }
			        try {
			            typeHandler.setParameter(ps, i + 1, value, jdbcType);
			        } catch (TypeException e) {
			            throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
			        } catch (SQLException e) {
			            throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
			        }
				}
			}
		}
	}

}
