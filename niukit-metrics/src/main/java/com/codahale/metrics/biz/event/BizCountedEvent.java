package com.codahale.metrics.biz.event;

import java.util.Map;

/**
 * 
 * @className	： BizCountedEvent
 * @description	： 业务运行状态事件对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月9日 下午5:23:02
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class BizCountedEvent extends BizEvent<BizEventPoint> {

	public BizCountedEvent(Object source, BizEventPoint bind) {
		super(source, bind);
	}
	
	public BizCountedEvent(Object source, String name) {
		super(source, new BizEventPoint(name, null));
	}
	
	public BizCountedEvent(Object source, String name, String message) {
		super(source, new BizEventPoint(name, message));
	}
	
	public BizCountedEvent(Object source, String name, String message, Map<String, Object> data) {
		super(source, new BizEventPoint(name, message, data));
	}
	
}
