package com.woshidaniu.orm.mybatis.interceptor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.woshidaniu.beanutils.reflection.AnnotationUtils;
import com.woshidaniu.orm.mybatis.cache.BeanMethodDefinitionFactory;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;
import com.woshidaniu.search.annotation.AdvancedSearch;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询拦截器
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2017年3月8日下午2:17:55
 */
@Intercepts( { 
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) ,
})
public class AdvancedSearchInterceptor extends AbstractInterceptorAdapter{

	@Override
	protected boolean isRequireIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) {
		// 通过反射获取到当前MappedStatement
		MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
		// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
		BoundSql boundSql = metaStatementHandler.getBoundSql();
		Object paramObject = boundSql.getParameterObject();
		//提取被国际化注解标记的方法
		Method method = BeanMethodDefinitionFactory.getMethodDefinition(mappedStatement.getId(), paramObject != null ? new Class<?>[]{ paramObject.getClass()} : null);
		// 拦截需要分页的SQL语句。通过MappedStatement的ID匹配，默认重写以包含Paged字符的ID
		return SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType()) && method != null && AnnotationUtils.hasAnnotation(method, AdvancedSearch.class);
	}
	
	@Override
	public Object doStatementIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) throws Throwable {
		
		// 将执行权交给下一个拦截器  
		return invocation.proceed();
	}
	
	
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {  
            return Plugin.wrap(target, this);  
        } else {  
            return target;  
        }
	}

	@Override
	public void setInterceptProperties(Properties properties) {
		
	}

}
