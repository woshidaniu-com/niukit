package com.woshidaniu.orm.mybatis.interceptor;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
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
import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;

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
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) ,
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }),
	@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class })
})
public class PartitionPaginationInterceptor extends AbstractPaginationInterceptor {

	protected static Logger LOG = LoggerFactory.getLogger(PartitionPaginationInterceptor.class);
	  
	@Override
	public Object doPrepareIntercept(Invocation invocation,StatementHandler statementHandler,MetaObject metaStatementHandler) throws Throwable {
		 // 通过反射获取到当前RoutingStatementHandler对象的delegate属性
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		// 拦截需要分页的SQL语句。通过MappedStatement的ID匹配，默认重写以包含Paged字符的ID
		if (mappedStatement.getId().matches(this.getPaginationID())) {
			//参数对象
			Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
			//获取分页参数对象
			QueryModel queryModel = getQueryModel(parameterObject);
			//如果参数对象不为空，则进行记录数查询
			if(queryModel != null){
				//获取分页信息:从mybats源码可以看出默认情况mybats会在方法中设为RowBounds.DEFAULT
				//RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
				// 对原始SQL进行分页SQL和总数SQL的处理
				String originalSQL = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
				String finalSQL = dialectObject.getOnceLimitSQL(originalSQL,queryModel.getOffset(), queryModel.getLimit());// 获得物理分页SQL
				// 将处理后的物理分页sql重新写入作为执行SQL
				metaStatementHandler.setValue("delegate.boundSql.sql", finalSQL);
				// 采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
				metaStatementHandler.setValue("delegate.rowBounds.offset",RowBounds.NO_ROW_OFFSET);
				metaStatementHandler.setValue("delegate.rowBounds.limit",RowBounds.NO_ROW_LIMIT);
				if (LOG.isDebugEnabled()) {
					LOG.debug(" Pagination SQL : "+ statementHandler.getBoundSql().getSql());
				}
			}
		} 
		// 将执行权交给下一个拦截器  
		return invocation.proceed();
	}

	@Override
	public Object doResultSetIntercept(Invocation invocation,ResultSetHandler resultSetHandler) throws Throwable{
		// 利用反射获取到FastResultSetHandler的mappedStatement属性，从而获取到MappedStatement；
		MappedStatement mappedStatement = (MappedStatement) ReflectionUtils.getField("mappedStatement",resultSetHandler);
		// 获取到当前的Statement
		Statement stmt = (Statement) invocation.getArgs()[0];
		// 利用反射获取到FastResultSetHandler的ParameterHandler属性，从而获取到ParameterObject；
		ParameterHandler parameterHandler = (ParameterHandler) ReflectionUtils.getField("parameterHandler",resultSetHandler);
		Object parameterObject = parameterHandler.getParameterObject();
		// 利用反射获取到FastResultSetHandler的ParameterHandler属性，从而获取到ParameterObject；
		//ParameterHandler parameterHandler = (ParameterHandler) ReflectionUtils.getFieldValue(resultSetHandler, "parameterHandler");
		List<Object> resultList = resultSetHandler.handleResultSets(stmt);
		//拦截分页的结果处理
		if(mappedStatement.getId().matches(this.getPaginationID())){ 
			//获取分页参数对象
			QueryModel queryModel = getQueryModel(parameterObject);
			if(queryModel == null ){
				queryModel = new QueryModel();
			}
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
			setQueryModel(parameterObject,queryModel);
		}
		//返回结果集
		return resultList;
	}

}
