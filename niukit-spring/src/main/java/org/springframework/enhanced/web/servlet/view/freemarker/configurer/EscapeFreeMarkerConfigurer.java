package org.springframework.enhanced.web.servlet.view.freemarker.configurer;

import java.util.List;

import org.springframework.enhanced.web.servlet.view.freemarker.cache.HtmlTemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.TemplateLoader;

public class EscapeFreeMarkerConfigurer extends FreeMarkerConfigurer{  
    
  @Override  
  protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {  
      return new HtmlTemplateLoader(super.getAggregateTemplateLoader(templateLoaders));  
  }  
  
}
