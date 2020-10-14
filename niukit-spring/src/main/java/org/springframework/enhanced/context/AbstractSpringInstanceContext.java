package org.springframework.enhanced.context;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public abstract class AbstractSpringInstanceContext implements SpringContext {
	
	protected static Logger LOG = LoggerFactory.getLogger(AbstractSpringInstanceContext.class);

	private ApplicationContext springContext;

	public AbstractSpringInstanceContext() {}
	
	public ApplicationContext getSpringContext() {
		return springContext;
	}

	public void setSpringContext(ApplicationContext springContext) {
		this.springContext = springContext;
	}


	public <T> T getInstance(Class<T> requiredType) {
		//获得首字母小写的类名称如：ApplicationContext 变为 applicationContext
		String simpleName = StringUtils.uncapitalize(requiredType.getSimpleName());
		//或bean
		T bean = springContext.getBean(simpleName,requiredType);
		//没有获得继续处理
		if(bean==null){
			//获得所有匹配bean类型的名称
			String beanNames[] = springContext.getBeanNamesForType(requiredType);
			if (beanNames.length == 0){
				return springContext.getBean(requiredType);
			}else{
				return springContext.getBean(beanNames[0],requiredType);
			}
		}else{
			return bean;
		}
	}
	
	public Object getInstance(String beanName) {
		return springContext.getBean(beanName);
	}
	
	public DataSource getDataSource(){
		return (DataSource) this.getInstance("dataSource");
	}
	
	public DataSource getDataSource(String datasourceName){
		return (DataSource) this.getInstance(datasourceName);
	}
}
