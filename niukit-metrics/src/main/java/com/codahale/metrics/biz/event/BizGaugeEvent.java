package com.codahale.metrics.biz.event;

/**
 * 
 * @className	： BizGaugeEvent
 * @description	：业务计量事件对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月9日 下午5:23:57
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class BizGaugeEvent extends BizEvent<BizEventPoint> {

	public BizGaugeEvent(Object source, BizEventPoint bind) {
		super(source, bind);
	}
	
	public BizGaugeEvent(Object source, String name, Long value) {
		super(source, new BizEventPoint(name, null, value));
	}
	
	public BizGaugeEvent(Object source, String name, String message, Long value) {
		super(source, new BizEventPoint(name, message, value));
	}
	
}
