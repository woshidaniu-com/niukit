package com.woshidaniu.web.monitor;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.alibaba.druid.filter.stat.StatFilterContext;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.http.stat.WebAppStat;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.DruidWebUtils;
import com.woshidaniu.beanutils.reflection.ReflectionUtils;
import com.woshidaniu.web.ExtParameters;
import com.woshidaniu.web.utils.WebParameterUtils;

public class DruidWebStatFilter extends WebStatFilter {
	
	private final static Log   LOG_  = LogFactory.getLog(DruidWebStatFilter.class);
	private FilterConfig config;
	
	public FilterConfig getFilterConfig() {
		return config;
	}
	
	protected final String getFilterName() {
		return (this.config != null ? this.config.getFilterName() : this.getClass().getSimpleName());
	}
	
	/**
	 * 覆盖父级初始化参数
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		//初始化web运行参数
		ExtParameters.initialize(config);
        
        {
        	String exclusions = WebParameterUtils.getString(config.getFilterName(), PARAM_NAME_EXCLUSIONS , null);
            if (exclusions != null && exclusions.trim().length() != 0) {
            	//利用反射改变父级对象的拦截过滤表达式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置
            	ReflectionUtils.setField("excludesPattern", this, new HashSet<String>(Arrays.asList(exclusions.split("\\s*,\\s*"))));
            }
        }

        {
        	//复写参数获取方式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置
        	String param = WebParameterUtils.getString(config.getFilterName(), PARAM_NAME_PRINCIPAL_SESSION_NAME , null);
            if (param != null) {
                param = param.trim();
                if (param.length() != 0) {
                    this.principalSessionName = param;
                }
            }
        }

        {
        	//复写参数获取方式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置
            String param = WebParameterUtils.getString(config.getFilterName(), PARAM_NAME_PRINCIPAL_COOKIE_NAME , null);
            if (param != null) {
                param = param.trim();
                if (param.length() != 0) {
                    this.principalCookieName = param;
                }
            }
        }

        {
        	//复写参数获取方式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置
            String param = WebParameterUtils.getString(config.getFilterName(), PARAM_NAME_SESSION_STAT_ENABLE , null);
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                if ("true".equals(param)) {
                    this.sessionStatEnable = true;
                } else if ("false".equals(param)) {
                    this.sessionStatEnable = false;
                } else {
                	LOG_.error("WebStatFilter Parameter '" + PARAM_NAME_SESSION_STAT_ENABLE + "' config error");
                }
            }
        }

        {
        	//复写参数获取方式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置
            String param = WebParameterUtils.getString(config.getFilterName(), PARAM_NAME_PROFILE_ENABLE , null);
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                if ("true".equals(param)) {
                    this.profileEnable = true;
                } else if ("false".equals(param)) {
                    this.profileEnable = false;
                } else {
                	LOG_.error("WebStatFilter Parameter '" + PARAM_NAME_PROFILE_ENABLE + "' config error");
                }
            }
        }
        {
        	//复写参数获取方式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置
            String param = WebParameterUtils.getString(config.getFilterName(), PARAM_NAME_SESSION_STAT_MAX_COUNT , null);
            if (param != null && param.trim().length() != 0) {
                param = param.trim();
                try {
                    this.sessionStatMaxCount = Integer.parseInt(param);
                } catch (NumberFormatException e) {
                	LOG_.error("WebStatFilter Parameter '" + PARAM_NAME_SESSION_STAT_ENABLE + "' config error", e);
                }
            }
        }

        // realIpHeader
        {
        	//复写参数获取方式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置
            String param = WebParameterUtils.getString(config.getFilterName(), PARAM_NAME_REAL_IP_HEADER , null);
            if (param != null) {
                param = param.trim();
                if (param.length() != 0) {
                    this.realIpHeader = param;
                }
            }
        }

        StatFilterContext.getInstance().addContextListener(statFilterContextListener);

        this.contextPath = DruidWebUtils.getContextPath(config.getServletContext());
        if (webAppStat == null) {
            webAppStat = new WebAppStat(contextPath, this.sessionStatMaxCount);
        }
        WebAppStatManager.getInstance().addWebAppStatSet(webAppStat);
        
	}

}
