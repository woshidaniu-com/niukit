package com.woshidaniu.orm.mybatis.spring;

import java.io.IOException;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.NestedIOException;

/**
 * 重写buildSqlSessionFactory方法，避免SQlMapping文件写错抛出异常
 * @author majun
 */
public class MyBatisSqlSessionFactoryBean extends SqlSessionFactoryBean {

	public  SqlSessionFactory buildSqlSessionFactory() throws IOException{
		try {
			return super.buildSqlSessionFactory();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new NestedIOException("Failed to parse mapping resource: ", e);
		} finally {
			ErrorContext.instance().reset();
		}
	}
}
