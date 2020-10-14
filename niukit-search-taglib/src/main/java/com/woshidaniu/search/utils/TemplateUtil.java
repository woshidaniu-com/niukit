/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：高级查询模版工具类
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年9月20日下午1:44:48
 */
public class TemplateUtil {

	/**
	 * 编码格式
	 */
	private static final String ENCODEING = "UTF-8";
	
	/**
	 * 模版文件目录
	 */
	private static final String TEMPLATES_PATH = "/templates/";
	
	/**
	 * 
	 * <p>方法说明：获取模版内容<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年9月20日下午1:45:06<p>
	 * @param data 填充数据
	 * @param theme 样式风格
	 * @param ftl 模版名称
	 * @return html内容
	 */
	public static String getTemplateContent(Map<String,Object> data,String theme,String ftl){
		try {
			Configuration configuration = ConfigFactory.configuration;
//			configuration.setDirectoryForTemplateLoading(ResourceUtils.getFile("classpath:"+TEMPLATES_PATH+theme));
			configuration.setClassForTemplateLoading(TemplateUtil.class,TEMPLATES_PATH+theme);  
			//模版文件名
			Template template= configuration.getTemplate(ftl);
			template.setOutputEncoding(ENCODEING);
			StringWriter writer = new StringWriter();
			template.process(data, writer);
			writer.flush();
			String html = writer.toString();
			return html;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 //内部类，构建Freemarkey配置
	 private static class ConfigFactory{
		 
		static final Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS); 
		 
		static {
            // setEncoding这个方法一定要设置国家及其编码，不然在flt中的中文在生成html后会变成乱码  
        	configuration.setEncoding(Locale.getDefault(), ENCODEING);  
            // 设置对象的包装器  
        	configuration.setObjectWrapper(new DefaultObjectWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build());  
            // 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错  
        	configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);  
		}
		 
	 }
}
