package com.woshidaniu.disruptor.context;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.woshidaniu.disruptor.context.event.DisruptorEventPublisher;
import com.woshidaniu.disruptor.event.DisruptorBindEvent;
import com.woshidaniu.disruptor.event.DisruptorEvent;
import com.woshidaniu.disruptor.event.translator.DisruptorEventTranslator;

public class DisruptorApplicationContext implements ApplicationContextAware, DisruptorEventPublisher, InitializingBean {

	protected ApplicationContext applicationContext;
	
	protected Disruptor<DisruptorEvent> disruptor = null;
	
	protected EventTranslatorOneArg<DisruptorEvent, Object> eventTranslator = null;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if(eventTranslator == null){
			eventTranslator = new DisruptorEventTranslator();
		}
		
	}
	
	@Override
	public void publishEvent(DisruptorEvent event) {
		
		if(event instanceof DisruptorBindEvent){
			DisruptorBindEvent bindEvent = (DisruptorBindEvent)event;
			disruptor.publishEvent(eventTranslator, bindEvent.getBind());
		} else {
			disruptor.publishEvent(eventTranslator, null);
		}
		
		//disruptor.getRingBuffer().tryPublishEvent(eventTranslator);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public Disruptor<DisruptorEvent> getDisruptor() {
		return disruptor;
	}

	public void setDisruptor(Disruptor<DisruptorEvent> disruptor) {
		this.disruptor = disruptor;
	}

	public EventTranslatorOneArg<DisruptorEvent, Object> getEventTranslator() {
		return eventTranslator;
	}

	public void setEventTranslator(EventTranslatorOneArg<DisruptorEvent, Object> eventTranslator) {
		this.eventTranslator = eventTranslator;
	}
	
	
}
	

