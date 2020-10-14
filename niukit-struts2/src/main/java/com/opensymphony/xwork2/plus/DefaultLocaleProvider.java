package com.opensymphony.xwork2.plus;

import java.util.Locale;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.woshidaniu.struts2.utils.LocaleUtils;
/**
 * 
 *@类名称	: DefaultLocaleProvider.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 2:52:54 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@see com.opensymphony.xwork2.DefaultLocaleProvider
 */
public class DefaultLocaleProvider implements LocaleProvider {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultLocaleProvider.class);

    /**
	 * 重写getLocale，实现优先从session中获取国际化语音
	 */
    public Locale getLocale() {
    	try {
			ActionContext ctx = ActionContext.getContext();
			if (ctx != null) {
				Locale locale = LocaleUtils.getLocale();
				if(null != locale){
					ctx.setLocale(locale);
					return locale;
				}
			    if (ctx.getLocale() == null) {
			    	locale = Locale.getDefault();
			    	ctx.setLocale(locale);
			    }
			    return locale;
			} else {
			    if (LOG.isDebugEnabled()) {
			    	LOG.debug("Action context not initialized");
			    }
			    return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	public boolean isValidLocaleString(String localeStr) {
		return true;
	}

	public boolean isValidLocale(Locale locale) {
		return true;
	}

}
