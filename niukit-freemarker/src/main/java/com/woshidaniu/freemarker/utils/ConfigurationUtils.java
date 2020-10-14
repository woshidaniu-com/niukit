package com.woshidaniu.freemarker.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import com.woshidaniu.freemarker.loader.ClasspathTemplateLoader;
import com.woshidaniu.freemarker.loader.LocationTemplateLoader;
import com.woshidaniu.freemarker.loader.ResourceTemplateLoader;
import com.woshidaniu.freemarker.model.LookupModel;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * 
 *@类名称	: ConfigurationUtils.java
 *@类描述	：Freemarker Configuration 初始化对象工具
 *@创建人	：kangzhidong
 *@创建时间	：Mar 4, 2016 2:33:46 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class ConfigurationUtils {
	
	private static String charset = "UTF-8";
	// 创建 Configuration 实例
	private static Configuration DEFAULT_CONFIGURATION = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
	
	/**
	 * 
	 *@描述：配置默认配置实例Cofiguration
	 *@创建人:kangzhidong
	 *@创建时间:Dec 29, 20154:17:39 PM
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Configuration getDefaultConfiguration() {
		
		/*
		 （2）内建模板加载器
			在Configuration中可以使用下面的方法来方便建立三种模板加载。（每种方法都会在其内部新建一个模板加载器对象，
			然后创建Configuration实例来使用它。）
		*/
		
		DEFAULT_CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		DEFAULT_CONFIGURATION.setLocalizedLookup(true);
		DEFAULT_CONFIGURATION.setWhitespaceStripping(true);
		
		/*configuration.setAutoFlush(autoFlush)
		configuration.setCustomAttribute(name, value)
		configuration.setDateFormat(dateFormat)
		configuration.setDateTimeFormat(dateTimeFormat)
		configuration.setDefaultEncoding(encoding)
		configuration.setNumberFormat(numberFormat)*/
		//DEFAULT_CONFIGURATION.setSharedVariable("fmXmlEscape", new XmlEscape());
		//DEFAULT_CONFIGURATION.setSharedVariable("fmHtmlEscape", new HtmlEscape());
		
		DEFAULT_CONFIGURATION.setEncoding(Locale.getDefault(), charset);  
		DEFAULT_CONFIGURATION.setOutputEncoding(charset);
		
		return (Configuration) DEFAULT_CONFIGURATION.clone();
	}
	
	/**
	 * 
	 *@描述：创建基于【classpath根目录】 的配置实例Cofiguration
	 *@创建人:kangzhidong
	 *@创建时间:Dec 29, 20154:17:20 PM
	 *@param clazz
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Configuration getClassRootConfiguration(Class<?> clazz) {
		return ConfigurationUtils.getConfiguration( clazz, "/");
	}
	
	/**
	 * 
	 *@描述：创建基于【指定class所在目录】 的配置实例Cofiguration
	 *@创建人:kangzhidong
	 *@创建时间:Dec 29, 20154:13:33 PM
	 *@param clazz
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Configuration getClasspathConfiguration(Class<?> clazz) {
		String clazzPath = clazz.getName().replace(".", "/");
		return ConfigurationUtils.getConfiguration( clazz, "/" + clazzPath.substring(0, clazzPath.lastIndexOf("/")));
	}
	
	/**
	 * 
	 *@描述：创建基于【指定classpath目录】 的配置实例Cofiguration
	 *@创建人:kangzhidong
	 *@创建时间:Dec 29, 20154:13:59 PM
	 *@param clazz
	 *@param basePackagePath
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Configuration getConfiguration(Class<?> clazz, String basePackagePath) {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		if(clazz != null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{
					new ClassTemplateLoader(clazz, basePackagePath),
					new ResourceTemplateLoader(clazz, basePackagePath),
                    new LocationTemplateLoader()
            }));
		}
		return configuration;
	}
	
	public static Configuration getConfiguration(ClassLoader classLoader, String basePackagePath) {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		if(classLoader != null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{
					new ClassTemplateLoader(classLoader, basePackagePath),
					new ResourceTemplateLoader(classLoader, basePackagePath),
                    new LocationTemplateLoader()
            }));
		}
		return configuration;
	}
	
	public static Configuration getSpringClasspathConfiguration() {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
		// classpath根目录
		configuration.setTemplateLoader(new ClasspathTemplateLoader());
		return configuration;
	}
	
	/**
	 * 
	 *@描述：创建基于Servlet上下文的配置实例Cofiguration
	 *@创建人:kangzhidong
	 *@创建时间:Dec 29, 20154:12:01 PM
	 *@param sctxt
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Configuration getContextRootConfiguration(ServletContext sctxt) {
		return ConfigurationUtils.getConfiguration( sctxt, sctxt.getRealPath(""));
	}
	
	/**
	 * 
	 *@描述：创建基于Servlet上下文的配置实例Cofiguration
	 *@创建人:kangzhidong
	 *@创建时间:Dec 29, 20154:11:57 PM
	 *@param sctxt
	 *@param basePackagePath
	 *@return
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 */
	public static Configuration getConfiguration(ServletContext sctxt, String basePackagePath) {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		if(sctxt != null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setServletContextForTemplateLoading(sctxt, basePackagePath);
			configuration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{
					configuration.getTemplateLoader(),
                    new LocationTemplateLoader()
            }));
		}
		return configuration;
	}

	 /**
	  * 
	  * @description	： 创建基于文件系统的配置实例Cofiguration
	  * <pre>
	  *（1）src目录下的目录（template在src下）  
      *	cfg.setDirectoryForTemplateLoading(new File("src/template"));  
      *	（2）完整路径（template在src下）  
      *	cfg.setDirectoryForTemplateLoading(new File( "D:/cpic-env/workspace/javaFreemarker/src/template"));  
      *	（3）工程目录下的目录（template/main在工程下）--推荐 
      *	cfg.setDirectoryForTemplateLoading(new File("src/template"));
      * </pre> 
	  * @author 		： kangzhidong
	  * @date 		：Dec 29, 2015 10:02:57 AM
	  * @param directory
	  * @return
	  * @throws IOException
	  */
	public static Configuration getConfiguration(File directory) throws IOException {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		if(directory!=null){
			// - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
			// classpath根目录
			configuration.setDirectoryForTemplateLoading(directory);
			configuration.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{
					configuration.getTemplateLoader(),
                    new LocationTemplateLoader()
            }));
		}
		return configuration;
	}
	
	public static Configuration getLookupConfiguration(LookupModel lookup, String basePackagePath) throws IOException {
		// 创建 Configuration 实例
		Configuration configuration = getDefaultConfiguration();
		//模板加载器集合
		List<TemplateLoader> loaderList = new ArrayList<TemplateLoader>();
		loaderList.add(new LocationTemplateLoader());
		if(lookup != null){
			
			if(lookup.getClazz() != null){
				loaderList.add(new ResourceTemplateLoader(lookup.getClazz(), basePackagePath ));
			}
			if(lookup.getClassLoader() != null){
				loaderList.add(new ResourceTemplateLoader(lookup.getClassLoader() , basePackagePath ));
			}
			if(lookup.getServletContext() != null){
				loaderList.add(new WebappTemplateLoader(lookup.getServletContext() , basePackagePath  ));
			}
			if(lookup.getBaseDirList() != null){
				for (File baseDir : lookup.getBaseDirList()) {
					loaderList.add(new FileTemplateLoader(baseDir));
				}
			}
		}
		if(loaderList.size() > 0){
			/*（3）从多个位置加载模板
			如果需要从多个位置加载模板，那就不得不为每个位置都实例化模板加载器对象，将它们包装到一个被称为MultiTemplateLoader的特殊模板加载器，
			最终将这个加载器传递给Configuration对象的setTemplateLoader(TemplateLoader loader)方法。*/
			MultiTemplateLoader mtl = new MultiTemplateLoader(loaderList.toArray(new TemplateLoader[loaderList.size()]));
			configuration.setTemplateLoader(mtl);
		}
		return configuration;
	}
	
}
