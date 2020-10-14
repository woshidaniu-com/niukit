package org.springframework.enhanced.listener;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.enhanced.utils.SpringResourceUtils;
import org.springframework.enhanced.utils.StringUtils;
/**
 * 
 * @className	： PropertiesResourceInitializedListener
 * @description	： 根据 location 路径 获取 *.properties 并将 key-value 设置到java.lang.System 对象;
 * 				以便方便在程序运行期间的任何代码中根据System.getProperty(key)获取需要的参数
 * 				通常该对象加载 runtime-*.properties 信息
 * <p> 注意：location 采用 spring 资源路径匹配解析器
 * <ul>
 *    <li>1、“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
 *    <li>2、“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。
 *    <li>3、或单一路径，如："file:C:/test.dat"、"classpath:test.dat"、"WEB-INF/test.dat"
 * </ul>
 * </p> 
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:06:29
 * @version 	V1.0
 */

public class PropertiesSystemPropertyInitializedListener implements ServletContextListener {

	protected static Logger LOG = LoggerFactory.getLogger(PropertiesSystemPropertyInitializedListener.class);
	public static final String location = "classpath*:**/*runtime*.properties";

	public void contextInitialized(ServletContextEvent event) {
		LOG.info("System init begain ...... ");
		try {
			ServletContext context = event.getServletContext();
			String encoding = context.getInitParameter("encoding");
			if (!StringUtils.isEmpty(location)) {
				try {
					LOG.info("Loading file from [" + location + "] !");
					Properties properties = new Properties();
					Resource[] resources = SpringResourceUtils.getRuntimeProperties();
					for (Resource resource : resources) {
						try {
							LOG.info("Loading file [" + resource.getFile().getName() + "] !");
							// 加载Properties对象
							PropertiesLoaderUtils.fillProperties(properties,
									new EncodedResource(resource, StringUtils.getSafeStr(encoding, "utf-8")));
						} catch (IOException ex) {
							LOG.warn("Could not load file from " + resource.getFilename() + ": " + ex.getMessage());
						}
					}
					// 将初始化参数设置到java.lang.System 对象
					System.getProperties().putAll(properties);
					properties.clear();
					properties = null;
					resources = null;
					LOG.info("Loading [" + location + "] finished !");
				} catch (Exception ex) {
					LOG.error("Loading [" + location + "] error : " + ex.getMessage());
				}
			}
			String realPath = event.getServletContext().getRealPath(File.separator);
			if (!realPath.endsWith(System.getProperty("file.separator"))) {
				realPath += System.getProperty("file.separator");
			}
			LOG.info("[web_root]:" + realPath);
			System.setProperty("web_root", realPath);
		} catch (Exception e) {
			LOG.error("System init error .", e.getCause());
		}
		LOG.info("System init succsess.");
	}

	public void contextDestroyed(ServletContextEvent event) {

	}

}
