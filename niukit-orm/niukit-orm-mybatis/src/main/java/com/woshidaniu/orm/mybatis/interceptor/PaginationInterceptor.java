package com.woshidaniu.orm.mybatis.interceptor;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basemodel.QueryModel;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.beanutils.reflection.AnnotationUtils;
import com.woshidaniu.orm.mybatis.annotation.Pagination;
import com.woshidaniu.orm.mybatis.cache.BeanMethodDefinitionFactory;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;
import com.woshidaniu.orm.mybatis.utils.BoundSQLUtils;
import com.woshidaniu.orm.mybatis.utils.MetaObjectUtils;
import com.woshidaniu.orm.mybatis.utils.ParameterUtils;

/**
 * 
 *@类名称		: PaginationInterceptor.java
 *@类描述		： 通过拦截<code>StatementHandler</code>的<code>prepare</code>方法， 重写mybatis的SQL语句，实现物理分页
 *@创建人		：kangzhidong
 *@创建时间	：Jun 16, 2016 7:24:23 PM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v1.0
 */
@Intercepts( { 
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }), 
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) 
})
public class PaginationInterceptor extends AbstractPaginationInterceptor {

	protected static Logger LOG = LoggerFactory.getLogger(PaginationInterceptor.class);

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
			// 通过反射获取到当前MappedStatement
			MappedStatement mappedStatement = metaStatementHandler.getMappedStatement();
			//获取分页信息:从mybats源码可以看出默认情况mybats会在方法中设为RowBounds.DEFAULT
			RowBounds rowBounds = metaStatementHandler.getRowBounds();
			MetaObject metaRowBounds = MetaObjectUtils.forObject(rowBounds);
			// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
			BoundSql boundSql = metaStatementHandler.getBoundSql();
			MetaObject metaBoundSql = MetaObjectUtils.forObject(boundSql);
			// 分页参数作为参数对象parameterObject的一个属性  
			Object paramObject = boundSql.getParameterObject();
			//获取分页参数对象
			QueryModel queryModel = ParameterUtils.getQueryModel(paramObject);
			//存在分页的情况
			if(!BlankUtils.isBlank(queryModel)){
				// 获取到我们自己写在Mapper映射语句中对应的Sql语句
				String originalSQL = (String) metaBoundSql.getValue("sql");
				// 对原始SQL进行分页SQL和总数SQL的处理
				String finalSQL = dialectObject.getLimitSQL(originalSQL,queryModel.getOffset(), queryModel.getLimit());// 获得物理分页SQL
				// 将处理后的物理分页sql重新写入作为执行SQL
				metaBoundSql.setValue("sql", finalSQL);
				try {
					//当前连接
					Connection connection = (Connection) invocation.getArgs()[0];
					//获取新的连接
					//Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
					//connection = configuration.getEnvironment().getDataSource().getConnection();
					int totalCount  = this.getTotalCount(originalSQL, connection, mappedStatement, boundSql);
					//总记录数
					queryModel.setTotalCount(totalCount);
					//计算总页数
					int totalPage = totalCount / queryModel.getPageSize() + ((totalCount % queryModel.getPageSize() == 0) ? 0 : 1);  
					queryModel.setTotalPage(totalPage); 
					//将计算过总记录数和总页数的queryModel对象重新设置回原对象中
					ParameterUtils.setQueryModel(paramObject,queryModel);
					// 禁用内存分页 ：采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
					metaRowBounds.setValue("offset",RowBounds.NO_ROW_OFFSET);
					metaRowBounds.setValue("limit",RowBounds.NO_ROW_LIMIT);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.getCause());
				}
			}else if (rowBounds instanceof PaginationBounds) {
				PaginationBounds pagination = (PaginationBounds) rowBounds;
				
				// 获取到我们自己写在Mapper映射语句中对应的Sql语句
				String originalSQL = (String) metaBoundSql.getValue("sql");
				// 对原始SQL进行分页SQL和总数SQL的处理
				String finalSQL = dialectObject.getLimitSQL(originalSQL,pagination.getOffset(), pagination.getLimit());// 获得物理分页SQL
				// 将处理后的物理分页sql重新写入作为执行SQL
				metaBoundSql.setValue("sql", finalSQL);
				
				if (pagination.getTotal() == 0) {
					try {
						//当前连接
						Connection connection = (Connection) invocation.getArgs()[0];
						//获取新的连接
						//Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
						//connection = configuration.getEnvironment().getDataSource().getConnection();
						int totalCount  = this.getTotalCount(originalSQL, connection, mappedStatement, boundSql);
						pagination.setTotal(totalCount);
						// 禁用内存分页 ：采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数
						metaRowBounds.setValue("offset",RowBounds.NO_ROW_OFFSET);
						metaRowBounds.setValue("limit",RowBounds.NO_ROW_LIMIT);
					} catch (Exception e) {
						LOG.error(e.getMessage(), e.getCause());
					}
				}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug(" Pagination SQL : "+ statementHandler.getBoundSql().getSql());
			}
		}
		// 将执行权交给下一个拦截器  
		return invocation.proceed();
	}
	
	private int getTotalCount(String originalSQL,Connection connection,MappedStatement mappedStatement,BoundSql boundSql){
		// 通过connection建立一个countSql对应的PreparedStatement对象。
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		int totalCount  = 0;
		try {
			/* 记录总记录数 SQL : 通过查询Sql语句获取到对应的计算总记录数的sql语句 */
			String countSQL = dialectObject.getCountSQL(originalSQL);
			// 通过BoundSql获取对应的参数映射
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			// 利用Configuration、查询记录数的Sql语句countSql、参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。
			BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSQL, parameterMappings, boundSql.getParameterObject());
			/*解决 MyBatis 物理分页foreach 参数失效 */
            BoundSQLUtils.setBoundSql(boundSql, countBoundSql);
			//对SQL进行处理，生成预编译Statement
            countStmt = connection.prepareStatement(countSQL);
			// 通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
    		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
    		// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(countStmt);
			if (LOG.isDebugEnabled()) {
				LOG.debug(" Generated Count SQL : " + countBoundSql.getSql());
			}
			// 之后就是执行获取总记录数的Sql语句和获取结果了。
			rs = countStmt.executeQuery();
			if (rs.next()) {
				totalCount  = rs.getInt(1);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.getCause());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (countStmt != null) {
					countStmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				 LOG.error("Ignore this exception", e);
			}
		}
		return totalCount;
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
