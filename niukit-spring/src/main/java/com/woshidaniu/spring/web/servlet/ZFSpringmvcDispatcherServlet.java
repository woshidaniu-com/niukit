package com.woshidaniu.spring.web.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.spring.web.interceptor.ZFSpringmvcHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.util.UrlPathHelper;

import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.LocaleUtils;


/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：设置webContex上下文
 *	 <br>class：com.woshidaniu.common.ZFSpringmvcHandlerInterceptor.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ZFSpringmvcDispatcherServlet extends DispatcherServlet {

	private static final Logger log = LoggerFactory.getLogger(ZFSpringmvcModuleDispatcherServlet.class);
	
	private static final long serialVersionUID = 7755708083665181328L;
	
	private List<HandlerInterceptor> extendInterceptors = new ArrayList<HandlerInterceptor>();
	
	private List<MappedInterceptor> extendMappedInterceptors = new ArrayList<MappedInterceptor>();
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	private PathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		WebContext.bindServletContext(request.getSession().getServletContext());
		WebContext.bindRequest(request);
		WebContext.bindResponse(response);
		
		//国际化语言切换
		Locale locale = LocaleUtils.interceptLocale(request);
		WebContext.setLocale(locale);
		
		super.doService(request, response);
	}
	
	@Override
	protected void initFrameworkServlet() throws ServletException {

		WebApplicationContext webApplicationContext = this.getWebApplicationContext();
		this.scanExtendInterceptors(webApplicationContext,true);
		this.buildExtendMappedInterceptors();
		
		//方便快速找到部署目录，解决问题
		String deployPath = getServletContext().getRealPath("/");
		log.info("系统初始化完成，部署目录:{}",deployPath);
	}

	@Override
	protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		HandlerExecutionChain handlerExecutionChain = super.getHandler(request);
		if(handlerExecutionChain != null) {
			Object handler = handlerExecutionChain.getHandler();
			//方便快速找到处理请求的Controller和方法,便于业务方使用者排查基础平台部门的Controller,Service等
			if(handler != null && handler instanceof HandlerMethod) {
				if(log.isDebugEnabled()) {
					HandlerMethod hm = (HandlerMethod)handler;
					log.debug("处理请求的handler类:{} 方法:{}",hm.getBeanType().getName(),hm.getMethod().getName());
				}
			}
		}
		
		if(handlerExecutionChain != null && !this.extendMappedInterceptors.isEmpty()) {
			String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
			for(MappedInterceptor mappedInterceptor : this.extendMappedInterceptors) {
				if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
					handlerExecutionChain.addInterceptor(mappedInterceptor.getInterceptor());
				}
			}
		}
		return handlerExecutionChain;
	}
	
	protected void scanExtendInterceptors(ApplicationContext context,boolean scanParent) {
		
		log.info("通过ApplicationContext[{}]初始化扩展Interceptor",context.getId());
		
		List<HandlerInterceptor> results = new ArrayList<HandlerInterceptor>();
		
		Map<String, HandlerInterceptor> map = context.getBeansOfType(HandlerInterceptor.class);
		if(!map.isEmpty()) {
			
			Iterator<Entry<String, HandlerInterceptor>> it = map.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String, HandlerInterceptor> e = it.next();
				String beanName = e.getKey();
				HandlerInterceptor interceptor = e.getValue();
				Class<?> interceptorClass = interceptor.getClass();
				ZFSpringmvcHandlerInterceptor annotation = interceptorClass.getAnnotation(ZFSpringmvcHandlerInterceptor.class);
				if(annotation != null) {
					results.add(interceptor);
					log.info("bean:[name={},instance={}]携带[{}]注解",beanName,interceptor,ZFSpringmvcHandlerInterceptor.class.getSimpleName());
				}else {
					log.info("bean:[name={},instance={}]未携带[{}]注解,忽略",beanName,interceptor,ZFSpringmvcHandlerInterceptor.class.getSimpleName());
				}
			}
			
			this.extendInterceptors.addAll(results);
			
		}else {
			log.info("未发现扩展HandlerInterceptor");
		}
		
		if(scanParent) {
			//TODO why ???
			ApplicationContext parent = context.getParent();
			if(parent != null) {
				this.scanExtendInterceptors(parent,scanParent);
			}
		}
	}
	
	protected void buildExtendMappedInterceptors() {

		if(!this.extendInterceptors.isEmpty()) {
			Collections.sort(this.extendInterceptors,new Comparator<HandlerInterceptor>() {
				
				@Override
				public int compare(HandlerInterceptor h1, HandlerInterceptor h2) {
					ZFSpringmvcHandlerInterceptor annotation1 = h1.getClass().getAnnotation(ZFSpringmvcHandlerInterceptor.class);
					ZFSpringmvcHandlerInterceptor annotation2 = h2.getClass().getAnnotation(ZFSpringmvcHandlerInterceptor.class);
					return annotation1.order() - annotation2.order();
				}
			});
			for(HandlerInterceptor interceptor : this.extendInterceptors) {
				ZFSpringmvcHandlerInterceptor annotation = interceptor.getClass().getAnnotation(ZFSpringmvcHandlerInterceptor.class);
				int order = annotation.order();
				boolean enable = annotation.enable();
				String[] includePatterns = annotation.includePatterns();
				String[] excludePatterns = annotation.excludePatterns();
				log.info("HandlerInterceptor:[order={},enable={},includePatterns={},excludePatterns={},instance={}]",order,enable,includePatterns,excludePatterns,interceptor);
				
				if(enable) {
					MappedInterceptor mappedInterceptor = new MappedInterceptor(includePatterns, excludePatterns, interceptor);
					log.info("构建MapperedInterceptor:[{}]",mappedInterceptor);
					this.extendMappedInterceptors.add(mappedInterceptor);
				}else {
					log.info("enable=false,忽略构建MapperedInterceptor");
				}
			}
		}
	}
}
