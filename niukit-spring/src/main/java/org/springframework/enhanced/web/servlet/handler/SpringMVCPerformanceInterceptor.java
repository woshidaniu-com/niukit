package org.springframework.enhanced.web.servlet.handler;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.NamedThreadLocal;
import org.springframework.enhanced.utils.DateUtils;
import org.springframework.enhanced.utils.SystemClock;
import org.springframework.enhanced.utils.WebUtils;
import org.springframework.web.context.support.RequestHandledEvent;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SpringMVCPerformanceInterceptor extends HandlerInterceptorAdapter implements ApplicationEventPublisherAware, InitializingBean {

	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");
    protected Logger LOG = LoggerFactory.getLogger(this.getClass());
    protected ApplicationEventPublisher eventPublisher;
    protected SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS") ;
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	if (LOG.isDebugEnabled()){
	        LOG.debug("Inited at : {} ", DateUtils.formatDateTime( SystemClock.now() ));
		}
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//1、开始时间  
		long beginTime = SystemClock.now();
		//线程绑定变量（该数据只有当前请求的线程可见）  
        startTimeThreadLocal.set(beginTime);
    	if (LOG.isDebugEnabled()){
	        LOG.debug("开始计时: {}  URI: {}", sdf.format(beginTime), request.getRequestURI());
		}
        return super.preHandle(request, response, handler);
    }

    @Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	super.postHandle(request, response, handler, modelAndView);
    	if (modelAndView != null){
			LOG.info("ViewName: " + modelAndView.getViewName());
		}
	}

    /**
     * 该方法需要当前对应的Interceptor 的preHandle 方法的返回值为true 时才会执行。
     * 顾名思义，该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。这个方法的主要作用是用于进行资源清理工作的。
     */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
		long endTime = SystemClock.now(); 	//2、结束时间  
		long processingTimeMillis = endTime - beginTime;
		// 打印JVM信息。
		if (LOG.isDebugEnabled()){
	        LOG.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}MB  已分配内存: {}MB  已分配内存中的剩余空间: {}MB  最大可用内存: {}MB",
	        		sdf.format(endTime), 
	        		DateUtils.formatDateTime(processingTimeMillis),
					request.getRequestURI(), 
					Runtime.getRuntime().maxMemory()/1024/1024, 
					Runtime.getRuntime().totalMemory()/1024/1024, 
					Runtime.getRuntime().freeMemory()/1024/1024, 
					(Runtime.getRuntime().maxMemory()- Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory())/1024/1024
				);
		}
		/*
		 * Object source, String requestUrl, String clientAddress, String
		 * method, String servletName, String sessionId, String userName, long
		 * processingTimeMillis
		 */
		
		RequestHandledEvent event = new ServletRequestHandledEvent(this, request.getRequestURL().toString(),
				WebUtils.getRemoteAddr(request), request.getMethod(), request.getServerName(),
				request.getRequestedSessionId(), request.getRemoteUser(), processingTimeMillis);
		getEventPublisher().publishEvent(event);
		
		super.afterCompletion(request, response, handler, ex);
	}
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public ApplicationEventPublisher getEventPublisher() {
		return eventPublisher;
	}
	 
}