package org.springframework.enhanced.web.servlet.i18n;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

/**
 * 
 *@类名称		： NestedLocaleResolver.java
 *@类描述		：嵌套Locale解析器；解决同时设置Locale到Session和Cookie的问题
 *@创建人		：kangzhidong
 *@创建时间	：2017年7月27日 下午3:29:13
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class NestedLocaleResolver extends AbstractLocaleResolver implements LocaleResolver {
	
	protected List<LocaleResolver> resolvers;
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		if(isNested()){
			Locale def = getDefaultLocale();
			for (LocaleResolver localeResolver : getResolvers()) {
				//解析locale
				Locale locale = localeResolver.resolveLocale(request);
				if(locale == null || locale.equals(def)){
					continue;
				}
				return locale;
			}
		}
		return getDefaultLocale();
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		if(isNested()){
			for (LocaleResolver localeResolver : getResolvers()) {
				localeResolver.setLocale(request, response, locale);
			}
		}
	}

	protected boolean isNested() {
		if(getResolvers() != null){
			return true;
		}
		return false;
	}
	
	public List<LocaleResolver> getResolvers() {
		return resolvers;
	}

	public void setResolvers(List<LocaleResolver> resolvers) {
		this.resolvers = resolvers;
	}

}
