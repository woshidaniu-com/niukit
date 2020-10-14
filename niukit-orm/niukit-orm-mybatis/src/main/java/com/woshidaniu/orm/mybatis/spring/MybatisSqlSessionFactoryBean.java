package com.woshidaniu.orm.mybatis.spring;

import java.io.IOException;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedIOException;

/**
 * <p>
 * 拷贝类 org.mybatis.spring.SqlSessionFactoryBean 修改方法 buildSqlSessionFactory()
 * 加载自定义 MybatisXmlConfigBuilder
 * </p>
 */
public class MybatisSqlSessionFactoryBean extends SqlSessionFactoryBean {

	protected static Logger LOG = LoggerFactory.getLogger(MybatisSqlSessionFactoryBean.class);
	
	/**
	 * 重写buildSqlSessionFactory方法，避免SQlMapping文件写错抛出异常
	 */
	protected SqlSessionFactory buildSqlSessionFactory() throws IOException{
		try {
			return super.buildSqlSessionFactory();
		} catch (Throwable e) {
			throw new NestedIOException("Failed to parse mapping resource: ", e);
		} finally {
			ErrorContext.instance().reset();
		}
	}

	/**
	 * Build a {@code SqlSessionFactory} instance.
	 *
	 * The default implementation uses the standard MyBatis {@code XMLConfigBuilder} API to build a
	 * {@code SqlSessionFactory} instance based on an Reader.
	 *
	 * @return SqlSessionFactory
	 * @throws IOException if loading the config file failed
	 */
	/*protected SqlSessionFactory buildSqlSessionFactory() throws IOException {

		try {
			Configuration configuration;

			*//**
			 * 加载自定义 MybatisXmlConfigBuilder
			 *//*
			MybatisXMLConfigBuilder xmlConfigBuilder = null;
			if ( this.configLocation != null ) {
				xmlConfigBuilder = new MybatisXMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
				configuration = xmlConfigBuilder.getConfiguration();
			} else {
				if ( LOG.isDebugEnabled() ) {
					LOG.debug("Property 'configLocation' not specified, using default MyBatis Configuration");
				}
				configuration = new Configuration();
				configuration.setVariables(this.configurationProperties);
			}

			if ( this.objectFactory != null ) {
				configuration.setObjectFactory(this.objectFactory);
			}

			if ( this.objectWrapperFactory != null ) {
				configuration.setObjectWrapperFactory(this.objectWrapperFactory);
			}

			if ( hasLength(this.typeAliasesPackage) ) {
				String[] typeAliasPackageArray = tokenizeToStringArray(this.typeAliasesPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
				for ( String packageToScan : typeAliasPackageArray ) {
					configuration.getTypeAliasRegistry().registerAliases(packageToScan, typeAliasesSuperType == null ? Object.class : typeAliasesSuperType);
					if ( LOG.isDebugEnabled() ) {
						LOG.debug("Scanned package: '" + packageToScan + "' for aliases");
					}
				}
			}

			if ( !isEmpty(this.typeAliases) ) {
				for ( Class<?> typeAlias : this.typeAliases ) {
					configuration.getTypeAliasRegistry().registerAlias(typeAlias);
					if ( LOG.isDebugEnabled() ) {
						LOG.debug("Registered type alias: '" + typeAlias + "'");
					}
				}
			}

			if ( !isEmpty(this.plugins) ) {
				for ( Interceptor plugin : this.plugins ) {
					configuration.addInterceptor(plugin);
					if ( LOG.isDebugEnabled() ) {
						LOG.debug("Registered plugin: '" + plugin + "'");
					}
				}
			}

			if ( hasLength(this.typeHandlersPackage) ) {
				String[] typeHandlersPackageArray = tokenizeToStringArray(this.typeHandlersPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
				for ( String packageToScan : typeHandlersPackageArray ) {
					configuration.getTypeHandlerRegistry().register(packageToScan);
					if ( LOG.isDebugEnabled() ) {
						LOG.debug("Scanned package: '" + packageToScan + "' for type handlers");
					}
				}
			}

			if ( !isEmpty(this.typeHandlers) ) {
				for ( TypeHandler<?> typeHandler : this.typeHandlers ) {
					configuration.getTypeHandlerRegistry().register(typeHandler);
					if ( LOG.isDebugEnabled() ) {
						LOG.debug("Registered type handler: '" + typeHandler + "'");
					}
				}
			}

			if ( xmlConfigBuilder != null ) {
				try {
					xmlConfigBuilder.parse();

					if ( LOG.isDebugEnabled() ) {
						LOG.debug("Parsed configuration file: '" + this.configLocation + "'");
					}
				} catch ( Exception ex ) {
					throw new NestedIOException("Failed to parse config resource: " + this.configLocation, ex);
				} finally {
					ErrorContext.instance().reset();
				}
			}

			if ( this.transactionFactory == null ) {
				this.transactionFactory = new SpringManagedTransactionFactory();
			}

			configuration.setEnvironment(new Environment(this.environment, this.transactionFactory, this.dataSource));

			if ( this.databaseIdProvider != null ) {
				try {
					configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
				} catch ( SQLException e ) {
					throw new NestedIOException("Failed getting a databaseId", e);
				}
			}

			if ( !isEmpty(this.mapperLocations) ) {
				for ( Resource mapperLocation : this.mapperLocations ) {
					if ( mapperLocation == null ) {
						continue;
					}

					try {
						XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
								configuration, mapperLocation.toString(), configuration.getSqlFragments());
						xmlMapperBuilder.parse();
					} catch ( Exception e ) {
						throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
					} finally {
						ErrorContext.instance().reset();
					}

					if ( LOG.isDebugEnabled() ) {
						LOG.debug("Parsed mapper file: '" + mapperLocation + "'");
					}
				}
			} else {
				if ( LOG.isDebugEnabled() ) {
					LOG.debug("Property 'mapperLocations' was not specified or no matching resources found");
				}
			}

			return this.sqlSessionFactoryBuilder.build(configuration);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new NestedIOException("Failed to parse mapping resource: ", e);
		} finally {
			ErrorContext.instance().reset();
		}
	}*/

}

