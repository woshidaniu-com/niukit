package com.woshidaniu.web.monitor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.woshidaniu.web.ExtParameters;
import com.woshidaniu.web.utils.WebParameterUtils;

@SuppressWarnings("serial")
public class DruidStatViewServlet extends StatViewServlet{
	
	protected final static Log LOG_ = LogFactory.getLog(DruidStatViewServlet.class);

	public DruidStatViewServlet(){
        super();
    }
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		//初始化web运行参数
		ExtParameters.initialize(config);
		// 调用父级初始化方法
		super.init(config);
	}
	
	/** 复写参数获取方式，实现参数化的参数提供方式，以便在runtime.properties配置文件中直接进行参数配置*/
	@Override
	public String getInitParameter(String name) {
		ServletConfig sc = getServletConfig();
		if (sc == null) {
			throw new IllegalStateException("ServletConfig has not been initialized");
		}
		return WebParameterUtils.getString(sc.getServletName(), name , null);
	}
	 
	

}
