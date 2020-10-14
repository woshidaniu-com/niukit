package org.springframework.enhanced.context.event.aspect;

import org.springframework.enhanced.context.event.EventInvocation;

@SuppressWarnings("serial")
public class JoinPointThrowingEvent extends AbstractJoinPointEvent {

	public JoinPointThrowingEvent(Object source, EventInvocation invocation) {
		super(source, invocation);
	}
	
}
