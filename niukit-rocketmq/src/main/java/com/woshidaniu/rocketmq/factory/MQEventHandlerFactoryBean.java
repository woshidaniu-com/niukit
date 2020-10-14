package com.woshidaniu.rocketmq.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.CollectionUtils;

import com.woshidaniu.rocketmq.config.Ini;
import com.woshidaniu.rocketmq.event.RocketmqEvent;
import com.woshidaniu.rocketmq.event.chain.HandlerChainManager;
import com.woshidaniu.rocketmq.event.chain.def.DefaultHandlerChainManager;
import com.woshidaniu.rocketmq.event.chain.def.PathMatchingHandlerChainResolver;
import com.woshidaniu.rocketmq.event.handler.AbstractRouteableMessageHandler;
import com.woshidaniu.rocketmq.event.handler.EventHandler;
import com.woshidaniu.rocketmq.event.handler.Nameable;
import com.woshidaniu.rocketmq.event.impl.RocketmqEventMessageHandler;

public class MQEventHandlerFactoryBean implements FactoryBean<EventHandler<RocketmqEvent>> {

	/**
	 * 处理器定义
	 */
	private Map<String, EventHandler<RocketmqEvent>> handlers;
	
	/**
	 * 处理器链定义
	 */
	private Map<String, String> handlerChainDefinitionMap;
	
	private AbstractRouteableMessageHandler<RocketmqEvent> instance;

	public MQEventHandlerFactoryBean() {
		handlers = new LinkedHashMap<String, EventHandler<RocketmqEvent>>();
		handlerChainDefinitionMap = new LinkedHashMap<String, String>();
	}

	@Override
	public EventHandler<RocketmqEvent> getObject() throws Exception {
		if(instance == null){
			instance = createInstance();
		}
		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		return RocketmqEventMessageHandler.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public Map<String, String> getHandlerChainDefinitionMap() {
		return handlerChainDefinitionMap;
	}

	public void setHandlerChainDefinitionMap(Map<String, String> handlerChainDefinitionMap) {
		this.handlerChainDefinitionMap = handlerChainDefinitionMap;
	}

	public Map<String, EventHandler<RocketmqEvent>> getHandlers() {
		return handlers;
	}

	public void setHandlers(Map<String, EventHandler<RocketmqEvent>> handlers) {
		this.handlers = handlers;
	}
	
	public void setHandlerChainDefinitions(String definitions) {
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setHandlerChainDefinitionMap(section);
    }
	
	protected HandlerChainManager<RocketmqEvent> createHandlerChainManager() {

		HandlerChainManager<RocketmqEvent> manager = new DefaultHandlerChainManager();
		Map<String, EventHandler<RocketmqEvent>> handlers = getHandlers();
		if (!CollectionUtils.isEmpty(handlers)) {
			for (Map.Entry<String, EventHandler<RocketmqEvent>> entry : handlers.entrySet()) {
				String name = entry.getKey();
				EventHandler<RocketmqEvent> handler = entry.getValue();
				if (handler instanceof Nameable) {
					((Nameable) handler).setName(name);
				}
				manager.addHandler(name, handler);
			}
		}

		Map<String, String> chains = getHandlerChainDefinitionMap();
		if (!CollectionUtils.isEmpty(chains)) {
			for (Map.Entry<String, String> entry : chains.entrySet()) {
				// topic/tags/keys
				String url = entry.getKey();
				String chainDefinition = entry.getValue();
				manager.createChain(url, chainDefinition);
			}
		}

		return manager;
	}
	
	protected AbstractRouteableMessageHandler<RocketmqEvent> createInstance() throws Exception {
		HandlerChainManager<RocketmqEvent> manager = createHandlerChainManager();
        PathMatchingHandlerChainResolver chainResolver = new PathMatchingHandlerChainResolver();
        chainResolver.setHandlerChainManager(manager);
        return new RocketmqEventMessageHandler(chainResolver);
    }
	
	
	
}
