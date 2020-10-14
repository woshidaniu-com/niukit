package com.woshidaniu.orm.mybatis.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.beanutils.BeanUtils;
import com.woshidaniu.beanutils.reflection.AnnotationUtils;
import com.woshidaniu.orm.mybatis.annotation.I18nMapper;
import com.woshidaniu.orm.mybatis.annotation.I18nSwitch;
import com.woshidaniu.orm.mybatis.cache.BeanMethodDefinitionFactory;
import com.woshidaniu.orm.mybatis.i18n.handler.DataI18nHandler;
import com.woshidaniu.orm.mybatis.i18n.handler.def.DefaultDataI18nHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaResultSetHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;
 
public abstract class AbstractDataI18nInterceptor extends AbstractInterceptorAdapter {

	protected static Logger LOG = LoggerFactory.getLogger(AbstractDataI18nInterceptor.class);
	protected DataI18nHandler i18nHandler;
	
	@Override
	protected boolean isRequireIntercept(Invocation invocation, StatementHandler statementHandler, MetaStatementHandler metaStatementHandler) {
		// 通过反射获取到当前MappedStatement
		MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
		// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
		BoundSql boundSql = metaStatementHandler.getBoundSql();
		Object paramObject = boundSql.getParameterObject();
		//提取被国际化注解标记的方法
		Method method = BeanMethodDefinitionFactory.getMethodDefinition(mappedStatement.getId(), paramObject != null ? new Class<?>[]{ paramObject.getClass()} : null);
		return  SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType()) && method != null &&
				AnnotationUtils.hasAnnotation(method, I18nSwitch.class);
	}
	
	@Override
	protected boolean isRequireIntercept(Invocation invocation,ResultSetHandler resultSetHandler,MetaResultSetHandler metaResultSetHandler) {
		// 通过反射获取到当前MappedStatement
		MappedStatement mappedStatement = metaResultSetHandler.getMappedStatement();
		// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
		BoundSql boundSql = metaResultSetHandler.getBoundSql();
		Object paramObject = boundSql.getParameterObject();
		//提取被国际化注解标记的方法
		Method method = BeanMethodDefinitionFactory.getMethodDefinition(mappedStatement.getId(), paramObject != null ? new Class<?>[]{ paramObject.getClass()} : null);
		return  SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType()) && method != null &&
				AnnotationUtils.hasAnnotation(method, I18nMapper.class);
	}
	
	protected boolean isIntercepted(CacheKey cacheKey) {
		String uniqueKey = DigestUtils.md5Hex(cacheKey.toString());
		if(! extraContext.containsKey(uniqueKey)){
			return true;
		}
		extraContext.put(uniqueKey, cacheKey);
		return false;
	}
	
	public abstract Locale getLocale();

	protected Object wrapI18nParam(Locale locale, Invocation invocation, MetaResultSetHandler metaResultSetHandler, Object result,Object orginParam) throws Exception {
		if(this.i18nHandler == null){
			this.i18nHandler = new DefaultDataI18nHandler();
		}
		return this.i18nHandler.wrap(locale, invocation, metaResultSetHandler, result, orginParam);
	}
	
	protected Object doI18nMapper(Locale locale, Invocation invocation,MetaResultSetHandler metaResultSetHandler, Object orginList, List<Object> i18nDataList) throws Exception {
		if(this.i18nHandler == null){
			this.i18nHandler = new DefaultDataI18nHandler();
		}
		return this.i18nHandler.handle(locale, invocation, metaResultSetHandler, orginList, i18nDataList);
	}
	 
	
	@Override
	public void setInterceptProperties(Properties properties) {
		String i18nHandlerClazz = properties.getProperty("i18nHandler");
		if(!StringUtils.isEmpty(i18nHandlerClazz)){
			try {
				Class<?> clazz = Class.forName(i18nHandlerClazz);
				this.i18nHandler = BeanUtils.instantiateClass(clazz, DataI18nHandler.class);
			} catch (ClassNotFoundException e) {
				LOG.warn("Class :" + this.getDialectClass()+ " is not found !");
			} catch (Exception e) {
				LOG.warn(e.getMessage());
			}
		}
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);  
	}

	@Override
	public void doDestroyIntercept(Invocation invocation) throws Throwable {
		extraContext.clear();
	}
	
	
}
