package com.woshidaniu.struts2.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.ng.ExecuteOperations;
import org.apache.struts2.dispatcher.ng.InitOperations;
import org.apache.struts2.dispatcher.ng.PrepareOperations;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;
import org.apache.struts2.plus.dispatcher.StrutsInitOperations;

import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.LocaleUtils;


/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.struts2.filter.ZFStrutsPrepareAndExecuteFilter.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ZFStrutsPrepareAndExecuteFilter extends StrutsPrepareAndExecuteFilter{

	/**
     * Creates a new instance of {@link InitOperations} to be used during
     * initialising {@link Dispatcher}
     *
     * @return instance of {@link InitOperations}
     
    protected InitOperations createInitOperations() {
    	//初始化辅助类 类似一个Delegate ,本行代码重写了初始化操作对象
        return new StrutsInitOperations();
    }*/
    
    /**
     * Creates a new instance of {@link PrepareOperations} to be used during
     * initialising {@link Dispatcher}
     *
     * @return instance of {@link PrepareOperations}
    
    protected PrepareOperations createPrepareOperations(Dispatcher dispatcher) {
        return new PrepareOperations(dispatcher);
    } */

    /**
     * Creates a new instance of {@link ExecuteOperations} to be used during
     * initialising {@link Dispatcher}
     *
     * @return instance of {@link ExecuteOperations}
     
    protected ExecuteOperations createExecuteOperations(Dispatcher dispatcher) {
        return new ExecuteOperations(dispatcher);
    }*/

    /**
     * Callback for post initialization
     *
     * @param dispatcher the dispatcher
     * @param filterConfig the filter config
     */
    protected void postInit(Dispatcher dispatcher, FilterConfig filterConfig) {
    }
    
    @Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
    	//绑定上下文请求
    	HttpSession session = ((HttpServletRequest)req).getSession();
		WebContext.bindServletContext(session.getServletContext());
		WebContext.bindRequest(req);
		WebContext.bindResponse(res);
		
		//国际化语言切换
		Locale locale = LocaleUtils.interceptLocale((HttpServletRequest)req);
		WebContext.setLocale(locale);
		
		super.doFilter(req, res, chain);
	}
	
}
