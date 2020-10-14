package org.springframework.enhanced.context;

import javax.sql.DataSource;

public interface SpringContext {

	/**
	 * 
	 * @description	： 根据对象类型获取服务对象
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午8:57:33
	 * @param clazz
	 * @return
	 */
	public abstract <T> T getInstance(Class<T> requiredType);
	
	/**
	 * 
	 * @description	： 根据服务名获取服务对象
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午8:57:42
	 * @param name
	 * @return
	 */
	public abstract Object getInstance(String name);
	
	/**
	 * 
	 * @description	： 获取名称为 dataSource 的数据源对象
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午8:57:51
	 * @return
	 */
	public abstract DataSource getDataSource();
	
	/**
	 * 
	 * @description	： 根据数据源名称获取数据源对象
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午8:57:59
	 * @param datasourceName
	 * @return
	 */
	public abstract DataSource getDataSource(String datasourceName);
	
}
