package com.woshidaniu.disruptor.context.event;

import java.lang.reflect.Constructor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

import com.woshidaniu.disruptor.event.DisruptorEvent;

public class EventPublicationInterceptor
		implements MethodInterceptor, DisruptorEventPublisherAware, InitializingBean {

	private Constructor<?> applicationEventClassConstructor;

	private DisruptorEventPublisher applicationEventPublisher;


	/**
	 * Set the application event class to publish.
	 * <p>The event class <b>must</b> have a constructor with a single
	 * {@code Object} argument for the event source. The interceptor
	 * will pass in the invoked object.
	 * @throws IllegalArgumentException if the supplied {@code Class} is
	 * {@code null} or if it is not an {@code ApplicationEvent} subclass or
	 * if it does not expose a constructor that takes a single {@code Object} argument
	 */
	public void setApplicationEventClass(Class<?> applicationEventClass) {
		if (DisruptorEvent.class == applicationEventClass || !DisruptorEvent.class.isAssignableFrom(applicationEventClass)) {
			throw new IllegalArgumentException("applicationEventClass needs to extend DisruptorEvent");
		}
		try {
			this.applicationEventClassConstructor = applicationEventClass.getConstructor( new Class<?>[] {Object.class} );
		}
		catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException("applicationEventClass [" +
					applicationEventClass.getName() + "] does not have the required Object constructor: " + ex);
		}
	}

	@Override
	public void setDisruptorEventPublisher(DisruptorEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.applicationEventClassConstructor == null) {
			throw new IllegalArgumentException("applicationEventClass is required");
		}
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object retVal = invocation.proceed();
		DisruptorEvent event = (DisruptorEvent) this.applicationEventClassConstructor.newInstance(new Object[] {invocation.getThis()});
		this.applicationEventPublisher.publishEvent(event);
		return retVal;
	}

}
