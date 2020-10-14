package com.woshidaniu.orm.mybatis.interceptor;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.builder.impl.MybatisDynamicSQLBuilder;

/**
 * 
 * @className: DynamicMapperInterceptor
 * @description: 通过拦截<code>StatementHandler</code>的<code>prepare</code>方法，
 * 				  根据参数parameterObject配置的注解信息，自动生成sql语句。 老规矩，签名里要拦截的类型只能是接口。
				<insert id="dynamic_add" parameterClass="gjbb"></insert>
				<delete id="dynamic_del" parameterClass="gjbb"></delete>
				<update id="dynamic_upd" parameterClass="gjbb"></update>
				<select id="dynamic_query" parameterClass="gjbb"></select>
 * @author : kangzhidong
 * @date : 上午10:52:11 2013-10-12
 * @modify by:
 * @modify date :
 * @modify description :
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class DynamicMapperInterceptor extends AbstractStatementInterceptor {
	
	protected static Logger LOG = LoggerFactory.getLogger(DynamicMapperInterceptor.class);
	protected AnnotationSQLBuilder builder = new MybatisDynamicSQLBuilder();
	/* 拦截动态SQL方法的正则表达式 */
	protected String dynamicID = ".*Dynamic*.*"; // 分页Id,mapper.xml中需要拦截的ID(正则匹配)
	
    @Override
    public Object doPrepareIntercept(Invocation invocation,StatementHandler statementHandler,MetaObject metaStatementHandler) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        // 拦截需要分页的SQL语句。通过MappedStatement的ID匹配，默认重写以包含Dynamic字符的ID
        if ( mappedStatement.getId().matches(this.getDynamicID()) && BlankUtils.isBlank(originalSql)) {
            Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
            Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
            String dynamicSql = "";
            // 根据xml的配置类型生成相应类型的sql语句
            if(SqlCommandType.INSERT.ordinal() == mappedStatement.getSqlCommandType().ordinal()){
            	dynamicSql = builder.buildInsertSQL(parameterObject);
            }else if(SqlCommandType.DELETE.ordinal() == mappedStatement.getSqlCommandType().ordinal()){
            	dynamicSql = builder.buildUpdateSQL(parameterObject);
            }else if(SqlCommandType.UPDATE.ordinal() == mappedStatement.getSqlCommandType().ordinal()){
            	dynamicSql = builder.buildDeleteSQL(parameterObject);
            }else if(SqlCommandType.SELECT.ordinal() == mappedStatement.getSqlCommandType().ordinal()){
            	dynamicSql = builder.buildQuerySQL(parameterObject);
            }
            LOG.debug("Auto generated sql:" + dynamicSql);
            SqlSource sqlSource = buildSqlSource(configuration, dynamicSql, parameterObject.getClass());
            List<ParameterMapping> parameterMappings = sqlSource.getBoundSql(parameterObject).getParameterMappings();
            metaStatementHandler.setValue("delegate.boundSql.sql", sqlSource.getBoundSql(parameterObject).getSql());
            metaStatementHandler.setValue("delegate.boundSql.parameterMappings", parameterMappings);
            
            // 调用原始statementHandler的prepare方法完成原本的逻辑
            statementHandler = (StatementHandler) metaStatementHandler.getOriginalObject();
            statementHandler.prepare((Connection) invocation.getArgs()[0]);
        }
        // 传递给下一个拦截器处理
        return invocation.proceed();
    }

    @Override
    public void setInterceptProperties(Properties properties) {
    	String dynamicID = properties.getProperty("dynamicID");
		if (dynamicID != null && dynamicID.length() != 0) {
			this.setDynamicID(dynamicID);
		}else{
			LOG.warn("Property dynamicID is not setted,use default '.*Dynamic*' !");
		}
    }

	public String getDynamicID() {
		return dynamicID;
	}

	public void setDynamicID(String dynamicID) {
	
		this.dynamicID = dynamicID;
	}
	
    
}
