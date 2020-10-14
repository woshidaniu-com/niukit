package com.woshidaniu.shiro.cas.filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.beanutils.reflection.ClassUtils;
import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.niuca.tp.cas.client.filter.ZFSSOFilter;
/**
 * 
 *@类名称		：SsoLoginFilter.java
 *@类描述		：门户单点登录过滤器
 *@创建人		：kangzhidong
 *@创建时间	：Jul 7, 2016 10:20:19 AM
 *@修改人		：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class SsoLoginFilter extends ZFSSOFilter {

	static Logger log = LoggerFactory.getLogger(SsoLoginFilter.class);
	
	/**
	 * 重写参数初始化函数，以便可从配置文件读取相关参数
	 */
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		//获取不进行检查的URL参数e
		
		Object invoke = null;
		
		try {
			Class<?> messageUtilClazz = ClassUtils.forName("com.woshidaniu.util.base.MessageUtil", SsoLoginFilter.class.getClassLoader());
			
			Method method = messageUtilClazz.getMethod("getText", String.class);
			
			invoke = method.invoke(null, config.getInitParameter("notCheckURLList"));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		
		if(invoke == null)
			return;
		
		if(log.isDebugEnabled()){
			log.debug("Init zfssofilter [notCheckURLList] param.");
		}
		
		String notCheckURLListStr = StringUtils.getSafeStr(StringUtils.getSafeStr(invoke,config.getInitParameter("notCheckURLList")), "");
	    if (StringUtils.isNotBlank(notCheckURLListStr)) {
	    	List<String> notCheckURLList = new ArrayList<String>();
	    	for (String notCheckURL : StringUtils.tokenizeToStringArray(notCheckURLListStr)) {
	    		notCheckURLList.add(notCheckURL);
			}
	    	ReflectionUtils.setField("notCheckURLList", this, notCheckURLList);
	    }
	}
	
}
