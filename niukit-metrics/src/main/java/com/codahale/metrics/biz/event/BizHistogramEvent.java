package com.codahale.metrics.biz.event;

/**
 * 
 * @className	： BizCountedEvent
 * @description	： 业务运行状态事件对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月9日 下午5:23:02
 * @version 	V1.0
 */
@SuppressWarnings("serial")
public class BizHistogramEvent extends BizEvent<BizEventPoint> {

	public BizHistogramEvent(Object source, BizEventPoint bind) {
		super(source, bind);
	}
	
	public BizHistogramEvent(Object source, String name, Long value) {
		super(source, new BizEventPoint(name, null, value));
	}
	
	public BizHistogramEvent(Object source, String name, String message, Long value) {
		super(source, new BizEventPoint(name, message, value));
	}
	
}
