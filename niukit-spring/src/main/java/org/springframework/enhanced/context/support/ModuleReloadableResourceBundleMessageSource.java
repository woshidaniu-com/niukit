package org.springframework.enhanced.context.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.enhanced.web.servlet.support.RequestContextUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class ModuleReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {
	
	protected static List<Locale> supporedLocales = new ArrayList<Locale>();
	protected String[] basefilenames = new String[0];
	
	public void setBasenames(String... basenames) {
		if (basenames != null) {
			this.basefilenames = new String[basenames.length];
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				Assert.hasText(basename, "Basename must not be empty");
				this.basefilenames[i] = basename.trim();
			}
		} else {
			this.basefilenames = new String[0];
		}
		super.setBasenames(basenames);
	}

	public static List<Locale> getSupporedLocales() {
		return supporedLocales;
	}

	public Properties getAllProperties(String filename) {
		return super.getProperties(filename).getProperties();
	}

	public Properties getAllProperties(Locale locale) {
		return super.getMergedProperties(locale).getProperties();

	}

	public String getMessage(String code, Locale locale) {
		return super.resolveCodeWithoutArguments(code, locale);
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String module = RequestContextUtils.getModule();
		Assert.notNull(module, " module is not specified!");
		if(logger.isDebugEnabled()){
			logger.debug("module:" + module);
		}
		for (String basename : basefilenames) {
			List<String> filenames = calculateAllFilenames(basename, locale);
			for (String filename : filenames) {
				if (StringUtils.endsWithIgnoreCase(filename, module)) {
					PropertiesHolder propHolder = getProperties(filename);
					String result = propHolder.getProperty(code);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return super.resolveCodeWithoutArguments(code, locale);
	}
	
}
