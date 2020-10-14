package org.springframework.enhanced.web.servlet.view.freemarker;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.enhanced.web.servlet.view.freemarker.cache.ResourceTemplateLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarker {
	
	protected Configuration cfg;
	protected ResourceTemplateLoader loader;
	
	public FreeMarker(HttpServletRequest request,Class resJAR) throws IOException, TemplateException{
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		Properties settings = buildSettings();
		configurer.setFreemarkerSettings(settings);
		InputStream inputStream = loadConfigFile(configurer);
		loader = new ResourceTemplateLoader(request,resJAR);
		configurer.setPreTemplateLoaders(new TemplateLoader[]{loader});
		cfg = configurer.getConfiguration();
		cfg.setLocalizedLookup(false);
		cfg.setTemplateUpdateDelay(-1);
		if(inputStream!=null){
			inputStream.close();
		}
	}

	private InputStream loadConfigFile(FreeMarkerConfigurer configurer) {
		InputStream inputStream = this.getClass().getResourceAsStream("/freemarker.properties");
		if(inputStream!=null){
			Resource resource = new InputStreamResource(inputStream);
			configurer.setConfigLocation(resource);
		}
		return inputStream;
	}

	private Properties buildSettings() {
		Properties settings = new Properties();
		settings.put("classic_compatible", "true");
		settings.put("whitespace_stripping", "true");
		settings.put("template_update_delay", "300");
		settings.put("locale", "zh_CN");
		settings.put("default_encoding", "utf-8");
		settings.put("url_escaping_charset", "utf-8");
		settings.put("date_format", "yyyy-MM-dd");
		settings.put("time_format", "HH:mm:ss");
		settings.put("datetime_format", "yyyy-MM-dd HH:mm:ss");
		settings.put("number_format", "#");
		settings.put("boolean_format", "true,false");
		settings.put("output_encoding", "UTF-8");
		settings.put("tag_syntax", "auto_detect");
		return settings;
	}
	
	protected Template getTemplate(String name) throws IOException{
		disponseCache();//启用调式模式
		return cfg.getTemplate(name);
	}
	
	public void disponseCache(){
		synchronized (cfg) {
			cfg.clearTemplateCache();
		}
	}
	
	public void setSessionId(String sessionid){
		loader.setSessionId(sessionid);
	}
	
	public ResponseEntity<String> getFreeMark(String name,Map data) throws IOException{
		try {
			Template template = this.getTemplate(name);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Type", "text/html;charset=utf-8");
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
			return new ResponseEntity<String>(text,responseHeaders,HttpStatus.OK);
		} catch (TemplateException e) {
			throw new IOException(e);
		} 
	}
		
	public Configuration getConfiguration(){
		return cfg;
	}
}
