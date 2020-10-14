package com.woshidaniu.spring.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PerformanceInterceptor extends HandlerInterceptorAdapter {

    protected Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch stopWatch = new Log4JStopWatch();
        request.setAttribute("stopWatch", stopWatch);
        stopWatch.lap("firstBlock");
        String reqURI = request.getRequestURI();
        stopWatch.start(reqURI);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch stopWatch = (StopWatch) request.getAttribute("stopWatch");
        long spentTime = stopWatch.getElapsedTime();
        String reqURI = request.getRequestURI();
        stopWatch.stop(reqURI);
        stopWatch.lap("secondBlock");
        if (spentTime > 1000) {
            stopWatch.lap("["+reqURI+"]");
        }
        super.afterCompletion(request, response, handler, ex);
    }
}