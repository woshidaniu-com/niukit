package org.springframework.enhanced.context.support;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.enhanced.web.servlet.support.RequestContextUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class ModuleResourceBundleMessageSource extends org.springframework.context.support.ResourceBundleMessageSource {
	
	protected String[] basenames = new String[0];
	
	public void setBasenames(String... basenames) {
		if (basenames != null) {
			this.basenames = new String[basenames.length];
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				Assert.hasText(basename, "Basename must not be empty");
				this.basenames[i] = basename.trim();
			}
		} else {
			this.basenames = new String[0];
		}
		super.setBasenames(basenames);
	}
	
	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String module = RequestContextUtils.getModule();
		Assert.notNull(module, " module is not specified!");
		if(logger.isDebugEnabled()){
			logger.debug("module:" + module);
		}
		String result = null;
		for (String basename : basenames) {
			if (StringUtils.endsWithIgnoreCase(basename, module)) {
				ResourceBundle bundle = getResourceBundle(basename, locale);
				if (bundle != null) {
					result = getStringOrNull(bundle, code);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return super.resolveCodeWithoutArguments(code, locale);
	}

	protected String getStringOrNull(ResourceBundle bundle, String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException ex) {
			return null;
		}
	}
	
}
