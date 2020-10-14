package com.codahale.metrics.biz.event;

import org.springframework.context.ApplicationEvent;

/**
 * 
 * @className	： BizEvent
 * @description	： 业务事件对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月9日 下午5:23:12
 * @version 	V1.0 
 * @param <T>
 */
@SuppressWarnings("serial")
public class BizEvent<T> extends ApplicationEvent {

	/**
	 * 当前事件绑定的数据对象
	 */
	protected T bind;

	public BizEvent(Object source, T bind) {
		super(source);
		this.bind = bind;
	}

	public T getBind() {
		return bind;
	}
	
}