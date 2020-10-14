package org.springframework.enhanced.context.event.aspect;

import org.springframework.enhanced.context.event.EventInvocation;

@SuppressWarnings("serial")
public class JoinPointBeforeEvent extends AbstractJoinPointEvent {

	public JoinPointBeforeEvent(Object source, EventInvocation invocation) {
		super(source, invocation);
	}
	
}
