package com.woshidaniu.disruptor.event.translator;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.woshidaniu.disruptor.event.DisruptorBindEvent;
import com.woshidaniu.disruptor.event.DisruptorEvent;

public class DisruptorEventTranslator implements EventTranslatorOneArg<DisruptorEvent, Object> {

	@Override
	public void translateTo(DisruptorEvent event, long sequence, Object bind) {
		
		if(event instanceof DisruptorBindEvent){
			DisruptorBindEvent bindEvent = (DisruptorBindEvent)event;
			bindEvent.bind(bind);
		}
		
	}

	 
	
}
