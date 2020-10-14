package org.springframework.enhanced.context.event.aspect;

import org.springframework.enhanced.context.event.EventInvocation;

@SuppressWarnings("serial")
public class JoinPointAroundEvent extends AbstractJoinPointEvent {

	public JoinPointAroundEvent(Object source, EventInvocation invocation) {
		super(source, invocation);
	}
	
}
