package org.springframework.enhanced.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.enhanced.utils.SpringPropertiesBundleUtils;
import org.springframework.enhanced.utils.StringUtils;

/**
 * 
 * @className	： PropertiesBundleResourceInitializedListener
 * @description	： 根据location 路径 获取 Properties并进行缓存
 * <p>注意：location 采用 spring 资源路径匹配解析器
 * <ul>
 *    <li>1、“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
 *    <li>2、“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
 *    <li>3、或单一路径，如："file:C:/test.dat"、"classpath:test.dat"、"WEB-INF/test.dat"
 * </ul>
 * </p>
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:06:41
 * @version 	V1.0
 */
public class PropertiesBundleResourceInitializedListener implements ServletContextListener {

	protected static Logger LOG = LoggerFactory.getLogger(PropertiesBundleResourceInitializedListener.class);
	
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		String location = context.getInitParameter("location");
		String encoding = context.getInitParameter("encoding");
		if(!StringUtils.isEmpty(location)){
			SpringPropertiesBundleUtils.initProperties(location,StringUtils.getSafeStr(encoding,"utf-8"));
		}else{
			LOG.warn("location is null !");
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
	
}
