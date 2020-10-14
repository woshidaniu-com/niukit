package org.springframework.enhanced.context.event.handler;

public interface EventHandler<T> {
	
	public void handle(T event);
	
}
