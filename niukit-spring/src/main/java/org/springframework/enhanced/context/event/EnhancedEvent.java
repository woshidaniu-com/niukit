package org.springframework.enhanced.context.event;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class EnhancedEvent<T> extends ApplicationEvent {

	/**
	 * 当前事件绑定的数据对象
	 */
	protected T bind;

	public EnhancedEvent(Object source, T bind) {
		super(source);
		this.bind = bind;
	}

	public T getBind() {
		return bind;
	}
}
