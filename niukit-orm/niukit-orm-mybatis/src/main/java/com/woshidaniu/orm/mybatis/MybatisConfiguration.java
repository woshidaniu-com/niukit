package com.woshidaniu.orm.mybatis;

import java.util.Set;

import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.orm.mybatis.mapper.AnnotationMapper;
import com.woshidaniu.orm.mybatis.mapper.AnnotationMapperSQLInjector;
import com.woshidaniu.orm.mybatis.scripting.MybatisXMLLanguageDriver;

/**
 * 
 *@类名称	: MybatisConfiguration.java
 *@类描述	：替换默认的 Configuration 
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 4:21:18 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 * @see org.apache.ibatis.builder.xml.XMLConfigBuilder
 */
public class MybatisConfiguration extends Configuration {

	protected static Logger LOG = LoggerFactory.getLogger(MybatisConfiguration.class);
	
	/* 方言类型 */
	private String dialectType;
	
	/**
	 * 初始化调用
	 */
	public MybatisConfiguration() {
		languageRegistry.register(MybatisXMLLanguageDriver.class);
		this.dialectType = "mysql5";
	}
	
	/**
	 * Mybatis加载sql的顺序：<br>
	 * 1、加载xml中的sql；<br>
	 * 2、加载sqlprovider中的sql<br>
	 * 3、xmlsql 与 sqlprovider不能包含相同的sql <br>
	 * <br>
	 * 调整后的sql优先级：xmlsql > sqlprovider > crudsql
	 */
	@Override
	public void addMappedStatement(MappedStatement ms) {
		if (this.mappedStatements.containsKey(ms.getId())) {
			// 说明已加载了xml中的节点；忽略mapper中的SqlProvider数据
			LOG.warn("mapper[ + " + ms.getId() + " + ] is ignored, because it's exists, maybe from xml file");
			return;
		}
		super.addMappedStatement(ms);
	}

	@Override
	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		return super.getMapper(type, sqlSession);
	}

	/**
	 * 重写 addMapper 方法
	 */
	@Override
	public <T> void addMapper(Class<T> type) {
		super.addMapper(type);
		if (!AnnotationMapper.class.isAssignableFrom(type)) {
			return;
		}
		/* 自动注入 SQL */
		new AnnotationMapperSQLInjector(this).inject(type,dialectType);
	}

	@Override
	public void addMappers(String packageName) {
		this.addMappers(packageName, Object.class);
	}

	@Override
	public void addMappers(String packageName, Class<?> superType) {
		ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<Class<?>>();
		resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
		Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
		for (Class<?> mapperClass : mapperSet) {
			this.addMapper(mapperClass);
		}
	}

}
