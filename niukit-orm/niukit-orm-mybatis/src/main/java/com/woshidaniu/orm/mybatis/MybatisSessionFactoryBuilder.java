package com.woshidaniu.orm.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 
 *@类名称	: MybatisSessionFactoryBuilder.java
 *@类描述	：replace default SqlSessionFactoryBuilder class
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 4:21:36 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 * @see org.apache.ibatis.session.SqlSessionFactoryBuilder
 */
public class MybatisSessionFactoryBuilder extends SqlSessionFactoryBuilder {

	@Override
	public SqlSessionFactory build( Reader reader, String environment, Properties properties ) {
		try {
			MybatisXMLConfigBuilder parser = new MybatisXMLConfigBuilder(reader, environment, properties);
			return build(parser.parse());
		} catch ( Exception e ) {
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		} finally {
			ErrorContext.instance().reset();
			try {
				reader.close();
			} catch ( IOException e ) {
				// Intentionally ignore. Prefer previous error.
			}
		}
	}

	@Override
	public SqlSessionFactory build( InputStream inputStream, String environment, Properties properties ) {
		try {
			MybatisXMLConfigBuilder parser = new MybatisXMLConfigBuilder(inputStream, environment, properties);
			return build(parser.parse());
		} catch ( Exception e ) {
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		} finally {
			ErrorContext.instance().reset();
			try {
				inputStream.close();
			} catch ( IOException e ) {
				// Intentionally ignore. Prefer previous error.
			}
		}
	}

}
