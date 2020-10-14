package com.woshidaniu.disruptor.event;

@SuppressWarnings("serial")
public class DisruptorBindEvent extends DisruptorEvent {

	/**
	 * 当前事件绑定的数据对象
	 */
	protected Object bind;

	public DisruptorBindEvent(Object source) {
		super(source);
	}
	
	public DisruptorBindEvent(Object source, Object bind) {
		super(source);
		this.bind = bind;
	}

	public Object getBind() {
		return bind;
	}
	
	public void bind(Object bind) {
		this.bind = bind;
	}
	
}
