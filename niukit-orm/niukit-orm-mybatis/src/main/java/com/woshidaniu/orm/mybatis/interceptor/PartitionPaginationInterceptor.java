package com.woshidaniu.orm.mybatis.interceptor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basemodel.BaseMap;
import com.woshidaniu.basemodel.BaseModel;
import com.woshidaniu.basemodel.QueryModel;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.beanutils.reflection.AnnotationUtils;
import com.woshidaniu.orm.mybatis.annotation.Pagination;
import com.woshidaniu.orm.mybatis.cache.BeanMethodDefinitionFactory;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaResultSetHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;
import com.woshidaniu.orm.mybatis.utils.ParameterUtils;

/**
 * 
 * @className: PartitionPaginationInterceptor
 * @description: 通过拦截<code>StatementHandler</code>的<code>prepare</code>方法，
 *               重写mybatis的SQL语句，实现物理分页;处理使用开窗函数获取分页数据中记录总数解析
 * @author : kangzhidong
 * @date : 上午11:09:07 2015-4-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
@Intercepts( { 
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
	@Signature(type = ParameterHandler.class, method = "setParameters", args = { PreparedStatement.class }) ,
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) ,
	@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class })
})
public class PartitionPaginationInterceptor extends AbstractPaginationInterceptor {

	protected static Logger LOG = LoggerFactory.getLogger(PartitionPaginationInterceptor.class);
	  
	@Override
	protected boolean isRequireIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) {
		// 通过反射获取到当前MappedStatement
		MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
		// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
		BoundSql boundSql = metaStatementHandler.getBoundSql();
		//参数对象
		Object paramObject = boundSql.getParameterObject();
		//是否可分页
		boolean pageable = ParameterUtils.isPageable(paramObject);
		//提取方法
		Method method = BeanMethodDefinitionFactory.getMethodDefinition(mappedStatement.getId(), paramObject != null ? new Class<?>[]{ paramObject.getClass()} : null);
		// 拦截需要分页的SQL语句。通过MappedStatement的ID匹配，默认重写以包含Paged字符的ID
		return (mappedStatement.getId().matches(this.getMethodRegexp()) && pageable) || 
				AnnotationUtils.hasAnnotation(method, Pagination.class);
	}
	
	@Override
	public Object doStatementIntercept(Invocation invocation,StatementHandler statementHandler,MetaStatementHandler metaStatementHandler) throws Throwable {
		
		//检查是否需要进行拦截处理
		if (isRequireIntercept(invocation, statementHandler, metaStatementHandler)) {
			
			//获取分页信息:从mybats源码可以看出默认情况mybats会在方法中设为RowBounds.DEFAULT
			RowBounds rowBounds = metaStatementHandler.getRowBounds();
			MetaObject metaRowBounds = MetaObjectUtils.forObject(rowBounds);
			// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
			BoundSql boundSql = metaStatementHandler.getBoundSql();
			MetaObject metaBoundSql = MetaObjectUtils.forObject(boundSql);
			//参数对象
			Object parameterObject = boundSql.getParameterObject();
			//获取分页参数对象
			QueryModel queryModel = ParameterUtils.getQueryModel(parameterObject);
			//如果参数对象不为空，则进行记录数查询
			if(queryModel != null){
				//获取分页信息:从mybats源码可以看出默认情况mybats会在方法中设为RowBounds.DEFAULT
				//RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
				// 对原始SQL进行分页SQL和总数SQL的处理
				String originalSQL = (String) metaBoundSql.getValue("sql");
				String finalSQL = dialectObject.getOnceLimitSQL(originalSQL,queryModel.getOffset(), queryModel.getLimit());// 获得物理分页SQL
				// 将处理后的物理分页sql重新写入作为执行SQL
				metaBoundSql.setValue("sql", finalSQL);
				// 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
				metaRowBounds.setValue("offset",RowBounds.NO_ROW_OFFSET);
				metaRowBounds.setValue("limit",RowBounds.NO_ROW_LIMIT);
				if (LOG.isDebugEnabled()) {
					LOG.debug(" Pagination SQL : "+ statementHandler.getBoundSql().getSql());
				}
			}
			
		}
		// 将执行权交给下一个拦截器  
		return invocation.proceed();
	}
	
	@Override
	protected boolean isRequireIntercept(Invocation invocation, ResultSetHandler resultSetHandler, MetaResultSetHandler metaResultSetHandler) {
		// 利用反射获取到FastResultSetHandler的mappedStatement属性，从而获取到MappedStatement；
		MappedStatement mappedStatement = metaResultSetHandler.getMappedStatement();
		// 利用反射获取到FastResultSetHandler的ParameterHandler属性，从而获取到ParameterObject；
		ParameterHandler parameterHandler = metaResultSetHandler.getParameterHandler();
		//参数对象
		Object parameterObject = parameterHandler.getParameterObject();
		//是否可分页
		boolean pageable = ParameterUtils.isPageable(parameterObject);
		//拦截分页的结果处理
		return (mappedStatement.getId().matches(this.getMethodRegexp()) && pageable) || 
				AnnotationUtils.hasAnnotation(invocation.getMethod(), Pagination.class);
	}

	@Override
	public Object doResultSetIntercept(Invocation invocation,ResultSetHandler resultSetHandler,MetaResultSetHandler metaResultSetHandler) throws Throwable{
		
		//检查是否需要进行拦截处理
		if (isRequireIntercept(invocation, resultSetHandler, metaResultSetHandler)) {
			
			// 获取到当前的Statement
			Statement stmt = (Statement) invocation.getArgs()[0];
			// 利用反射获取到FastResultSetHandler的ParameterHandler属性，从而获取到ParameterObject；
			ParameterHandler parameterHandler = metaResultSetHandler.getParameterHandler();
			//参数对象
			Object parameterObject = parameterHandler.getParameterObject();
			//获取分页参数对象
			QueryModel queryModel = ParameterUtils.getQueryModel(parameterObject);
			if(queryModel == null ){
				queryModel = new QueryModel();
			}
			//结果集
			List<Object> resultList = resultSetHandler.handleResultSets(stmt);
			if(!BlankUtils.isBlank(resultList)){
				int total = 0;
				Object rowData = resultList.get(0);
				if(rowData instanceof BaseMap){
					total = Integer.parseInt(((BaseMap)rowData).getTotalResult().toString());
				}else if(rowData instanceof BaseModel){
					total = Integer.parseInt(((BaseModel)rowData).getTotalResult());
				}else{
					//参数为某个实体，该实体拥有queryModel属性或者包含QueryModel类型对象
					MetaObject metaObject = MetaObjectUtils.forObject(rowData);
					if(metaObject.hasGetter("totalresult")){
						total = Integer.parseInt(metaObject.getValue("totalresult").toString());
					}else if(metaObject.hasGetter("TOTALRESULT")){
						total = Integer.parseInt(metaObject.getValue("TOTALRESULT").toString());
					}
				}
				queryModel.setTotalCount(total);
			}
			//将计算过总记录数和总页数的queryModel对象重新设置回原对象中
			ParameterUtils.setQueryModel(parameterObject,queryModel);
			//返回结果集
			return resultList;
			
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
