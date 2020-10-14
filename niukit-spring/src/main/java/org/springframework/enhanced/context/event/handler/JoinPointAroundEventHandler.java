package org.springframework.enhanced.context.event.handler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.enhanced.context.event.EventInvocation;
import org.springframework.enhanced.context.event.aspect.JoinPointAroundEvent;

public class JoinPointAroundEventHandler implements EventHandler<JoinPointAroundEvent> {

	protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Override
	public void handle(JoinPointAroundEvent event) {
		
		if (LOG.isDebugEnabled()) {
			// 获取事件环境对象
			EventInvocation invocation = event.getBind();
			JoinPoint point = invocation.getPoint();
			Signature signature = point.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;

			LOG.debug("JoinPoint Around [ Method ： {} , ArgNames : {} ] ", methodSignature.getName(),
					invocation.getArgNames());

		}
		
	}
	
}
