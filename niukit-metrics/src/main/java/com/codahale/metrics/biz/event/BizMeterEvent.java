package com.codahale.metrics.biz.event;

import java.util.Map;

/**
 * 
 * @className	： BizStatusEvent
 * @description	：业务运行状态事件对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月9日 下午5:23:57
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class BizMeterEvent extends BizEvent<BizEventPoint> {

	public BizMeterEvent(Object source, BizEventPoint bind) {
		super(source, bind);
	}
	
	public BizMeterEvent(Object source, String name) {
		super(source, new BizEventPoint(name, null));
	}
	
	public BizMeterEvent(Object source, String name, String message) {
		super(source, new BizEventPoint(name, message));
	}
	
	public BizMeterEvent(Object source, String name, String message, Map<String, Object> data) {
		super(source, new BizEventPoint(name, message, data));
	}
	
}
