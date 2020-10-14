package com.woshidaniu.orm.mybatis.interceptor;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.builder.impl.MybatisDynamicSQLBuilder;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

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
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class})
})
public class DynamicMapperInterceptor extends AbstractInterceptorAdapter {
	
	protected static Logger LOG = LoggerFactory.getLogger(DynamicMapperInterceptor.class);
	protected AnnotationSQLBuilder builder = new MybatisDynamicSQLBuilder();
	/* 默认拦截动态SQL方法的正则表达式 */
	protected String DEFAULT_DYNAMIC_METHOD_REGEXP = ".*Dynamic*.*";
	
	@Override
	protected boolean isRequireIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) {
		// 通过反射获取到当前MappedStatement
		MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
		// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
		BoundSql boundSql = metaStatementHandler.getBoundSql();
		MetaObject metaBoundSql = MetaObjectUtils.forObject(boundSql);
		String originalSql = (String) metaBoundSql.getValue("sql");
		// 拦截需要分页的SQL语句。通过MappedStatement的ID匹配，默认重写以包含Dynamic字符的ID
		return ( mappedStatement.getId().matches(this.getMethodRegexp()) && BlankUtils.isBlank(originalSql) );
	}
	
    @Override
    public Object doStatementIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) throws Throwable {
    	//检查是否需要进行拦截处理
    	if(isRequireIntercept(invocation, statementHandler, metaStatementHandler)){
    		// 通过反射获取到当前MappedStatement
    		MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
    		// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
			BoundSql boundSql = metaStatementHandler.getBoundSql();
			MetaObject metaBoundSql = MetaObjectUtils.forObject(boundSql);
			
            Configuration configuration = metaStatementHandler.getConfiguration();
            Object parameterObject = metaStatementHandler.getBoundSql().getParameterObject();
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
            metaBoundSql.setValue("sql", sqlSource.getBoundSql(parameterObject).getSql());
            metaBoundSql.setValue("parameterMappings", parameterMappings);
            
            // 调用原始statementHandler的prepare方法完成原本的逻辑
            statementHandler = (StatementHandler) metaStatementHandler.getMetaObject().getOriginalObject();
            statementHandler.prepare((Connection) invocation.getArgs()[0],2000);
    	}
        // 传递给下一个拦截器处理
        return invocation.proceed();
    }

    @Override
    public void setInterceptProperties(Properties properties) {
    	if(BlankUtils.isBlank(getMethodRegexp())){
			setMethodRegexp(DEFAULT_DYNAMIC_METHOD_REGEXP);
			LOG.warn("Property methodRegexp is not setted,use default '{0}' !" , DEFAULT_DYNAMIC_METHOD_REGEXP );
		}
    }
    
    @Override
	public Object plugin(Object target) {
    	if (target instanceof StatementHandler) {  
            return Plugin.wrap(target, this);  
        } else {  
            return target;  
        }
	}

	public AnnotationSQLBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(AnnotationSQLBuilder builder) {
		this.builder = builder;
	}
    
}
