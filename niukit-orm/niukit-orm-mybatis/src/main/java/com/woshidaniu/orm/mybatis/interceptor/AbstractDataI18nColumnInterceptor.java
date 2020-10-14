package com.woshidaniu.orm.mybatis.interceptor;

import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.beanutils.reflection.AnnotationUtils;
import com.woshidaniu.orm.mybatis.annotation.I18nColumn;
import com.woshidaniu.orm.mybatis.annotation.I18nLocale;
import com.woshidaniu.orm.mybatis.annotation.I18nSwitch;
import com.woshidaniu.orm.mybatis.cache.BeanMethodDefinitionFactory;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

public abstract class AbstractDataI18nColumnInterceptor extends AbstractDataI18nInterceptor {

	protected static Logger LOG = LoggerFactory.getLogger(AbstractDataI18nColumnInterceptor.class);
	
	@Override
	public Object doStatementIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) throws Throwable {
		
		//检查是否需要进行拦截处理
		if (isRequireIntercept(invocation, statementHandler, metaStatementHandler)) {
			// 利用反射获取到FastResultSetHandler的mappedStatement属性，从而获取到MappedStatement；
			MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
						
			// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
			BoundSql boundSql = metaStatementHandler.getBoundSql();
			Object paramObject = boundSql.getParameterObject();
			MetaObject metaBoundSql = MetaObjectUtils.forObject(boundSql);
			// 获取当前上下文中的Locale对象
			Locale locale = this.getLocale();
			//提取被国际化注解标记的方法
			Method method = BeanMethodDefinitionFactory.getMethodDefinition(mappedStatement.getId(), paramObject != null ? new Class<?>[]{ paramObject.getClass()} : null);
			//获取替换模式下的国际化注解标记
			I18nSwitch i18nSwitch = AnnotationUtils.findAnnotation(method, I18nSwitch.class);
			//解析注解映射关系
			I18nColumn[] i18nColumns  = i18nSwitch.value();
			if(i18nColumns != null && i18nColumns.length > 0){
				String originalSQL = (String) metaBoundSql.getValue("sql");
				//循环标记对象
				for (I18nColumn i18nColumn : i18nColumns) {
					if(i18nColumn != null && !StringUtils.isEmpty(i18nColumn.column()) ){
						//获取国际化语言映射列
						I18nLocale[] locales = i18nColumn.i18n();
						for (I18nLocale i18nLocale : locales) {
							//国际化语言匹配
							if(locale.toString().equals(i18nLocale.locale().getLocale().toString())){
								//根据参数决定替换值
								String newColumn = StringUtils.isEmpty(i18nLocale.alias()) ? i18nLocale.column() : i18nLocale.column() + " as " + i18nLocale.alias();
								//替换特殊标记的语句，如：@name =>> name_yw as name
								originalSQL = originalSQL.replaceAll("@" + i18nColumn.column(), newColumn );
								break;
							}
						}
					}
				}
				// 将处理后的物理分页sql重新写入作为执行SQL
				metaBoundSql.setValue("sql", originalSQL);
				if (LOG.isDebugEnabled()) {
					LOG.debug(" I18n SQL : "+ statementHandler.getBoundSql().getSql());
				}
			}
		}
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
	
}
