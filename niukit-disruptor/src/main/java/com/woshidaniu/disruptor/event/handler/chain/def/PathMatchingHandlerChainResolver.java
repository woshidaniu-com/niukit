package com.woshidaniu.disruptor.event.handler.chain.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.disruptor.event.DisruptorEvent;
import com.woshidaniu.disruptor.event.handler.chain.HandlerChain;
import com.woshidaniu.disruptor.event.handler.chain.HandlerChainManager;
import com.woshidaniu.disruptor.event.handler.chain.HandlerChainResolver;
import com.woshidaniu.disruptor.util.AntPathMatcher;
import com.woshidaniu.disruptor.util.PathMatcher;

public class PathMatchingHandlerChainResolver implements HandlerChainResolver<DisruptorEvent> {

	private static final Logger log = LoggerFactory.getLogger(PathMatchingHandlerChainResolver.class);
	/**
	 * handlerChain管理器
	 */
	private HandlerChainManager<DisruptorEvent> handlerChainManager;
	
	/**
	 * 路径匹配器
	 */
	private PathMatcher pathMatcher;
	
	 public PathMatchingHandlerChainResolver() {
        this.pathMatcher = new AntPathMatcher();
        this.handlerChainManager = new DefaultHandlerChainManager();
    }

	public HandlerChainManager<DisruptorEvent> getHandlerChainManager() {
		return handlerChainManager;
	}

	public void setHandlerChainManager(HandlerChainManager<DisruptorEvent> handlerChainManager) {
		this.handlerChainManager = handlerChainManager;
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}
	
	
	public HandlerChain<DisruptorEvent> getChain(DisruptorEvent event, HandlerChain<DisruptorEvent> originalChain) {
        HandlerChainManager<DisruptorEvent> handlerChainManager = getHandlerChainManager();
        if (!handlerChainManager.hasChains()) {
            return null;
        }
        String eventURI = getPathWithinEvent(event);
        for (String pathPattern : handlerChainManager.getChainNames()) {
            if (pathMatches(pathPattern, eventURI)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for eventURI [" + eventURI + "].  " +
                            "Utilizing corresponding handler chain...");
                }
                return handlerChainManager.proxy(originalChain, pathPattern);
            }
        }
        return null;
    }

    protected boolean pathMatches(String pattern, String path) {
        PathMatcher pathMatcher = getPathMatcher();
        return pathMatcher.match(pattern, path);
    }

    protected String getPathWithinEvent(DisruptorEvent event) {
    	return event.getRouteExpression();
    }
	
}
