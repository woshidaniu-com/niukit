/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.web.captcha.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.woshidaniu.web.captcha.FastCaptcha;

/**
 *@类名称	: FastCaptchaFactory.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 10, 2016 10:31:06 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class FastCaptchaFactory {

	protected static ConcurrentMap<String, FastCaptcha> cachedCaptcha = new ConcurrentHashMap<String, FastCaptcha>();
	protected static FastCaptchaFactory INSTANCE = new FastCaptchaFactory();  
	
	public static FastCaptchaFactory getInstance(){
        return INSTANCE;
    }
	
	public FastCaptcha register(String captchaType, FastCaptcha captcha) {
    	return cachedCaptcha.putIfAbsent(captchaType, captcha);
    }
	
	public FastCaptcha getCaptcha(String captchaType) {
    	return cachedCaptcha.get(captchaType);
    }
	
}
