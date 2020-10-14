/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.disruptor.context.factory;

import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ObjectUtils;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.woshidaniu.disruptor.config.DisruptorConfig;
import com.woshidaniu.disruptor.event.DisruptorEvent;
import com.woshidaniu.disruptor.event.factory.DisruptorEventThreadFactory;
import com.woshidaniu.disruptor.event.handler.DisruptorEventHandler;
import com.woshidaniu.disruptor.hooks.DisruptorShutdownHook;
import com.woshidaniu.disruptor.util.WaitStrategys;

/**
 * 
 * @className	： DisruptorFactoryBean
 * @description	： TODO(描述这个类的作用)
 * @author 		：大康（743）
 * @date		： 2017年9月6日 下午7:29:30
 * @version 	V1.0
 * http://blog.csdn.net/a314368439/article/details/72642653?utm_source=itdadao&utm_medium=referral
 */

@SuppressWarnings("unchecked")
public class DisruptorFactoryBean implements FactoryBean<Disruptor<DisruptorEvent>>, InitializingBean {

	/**
	 * 决定一个消费者将如何等待生产者将Event置入Disruptor的策略。用来权衡当生产者无法将新的事件放进RingBuffer时的处理策略。
	 * （例如 ：当生产者太快，消费者太慢，会导致生成者获取不到新的事件槽来插入新事件，则会根据该策略进行处理，默认会堵塞）
	 */
	private WaitStrategy waitStrategy;
	private DisruptorConfig config;
	private ThreadFactory threadFactory;
	private EventFactory<DisruptorEvent> eventFactory;
	private DisruptorEventHandler preEventHandler;
	private DisruptorEventHandler postEventHandler;

	@Override
	public void afterPropertiesSet() throws Exception {

		if (waitStrategy == null) {
			setWaitStrategy(WaitStrategys.YIELDING_WAIT);
		}
		if (threadFactory == null) {
			setThreadFactory(new DisruptorEventThreadFactory());
		}
	}
	
	//创建Disruptor
	//1 eventFactory 为
	//2 ringBufferSize为RingBuffer缓冲区大小，最好是2的指数倍 
	//3 线程池，进行Disruptor内部的数据接收处理调用
	//4 第四个参数ProducerType.SINGLE和ProducerType.MULTI，用来指定数据生成者有一个还是多个
	//5 第五个参数是一种策略：WaitStrategy
	/**
	 * 创建Disruptor
	 * @param eventFactory 工厂类对象，用于创建一个个的LongEvent， LongEvent是实际的消费数据，初始化启动Disruptor的时候，Disruptor会调用该工厂方法创建一个个的消费数据实例存放到RingBuffer缓冲区里面去，创建的对象个数为ringBufferSize指定的
	 * @param ringBufferSize RingBuffer缓冲区大小
	 * @param executor 线程池，Disruptor内部的对数据进行接收处理时调用
	 * @param producerType 用来指定数据生成者有一个还是多个，有两个可选值ProducerType.SINGLE和ProducerType.MULTI
	 * @param waitStrategy 一种策略，用来均衡数据生产者和消费者之间的处理效率，默认提供了3个实现类
	 */
	
	@Override
	public Disruptor<DisruptorEvent> getObject() throws Exception {
		
		Disruptor<DisruptorEvent> disruptor = null;
		if (config.isMultiProducer()) {
			disruptor = new Disruptor<DisruptorEvent>(eventFactory,
					config.getRingBufferSize(), threadFactory, ProducerType.MULTI, waitStrategy);
		} else {
			disruptor = new Disruptor<DisruptorEvent>(eventFactory,
					config.getRingBufferSize(), threadFactory, ProducerType.SINGLE, waitStrategy);
		}
		
		if (!ObjectUtils.isEmpty(preEventHandler)) {
			//连接消费事件方法，其中EventHandler的是为消费者消费消息的实现类
			// 使用disruptor创建消费者组
			EventHandlerGroup<DisruptorEvent> handlerGroup = disruptor.handleEventsWith(preEventHandler);
			//后置处理;可以在完成前面的逻辑后执行新的逻辑
			if(!ObjectUtils.isEmpty(postEventHandler)) {
				// 完成前置事件处理之后执行后置事件处理
				handlerGroup.then(postEventHandler);
			}
		}
	    
		// 启动
		disruptor.start();
		
		/** 
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己 
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法 
         */  
        Runtime.getRuntime().addShutdownHook(new DisruptorShutdownHook(disruptor));
		
		return disruptor;
		
	}

	@Override
	public Class<?> getObjectType() {
		return Disruptor.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	
	public DisruptorConfig getConfig() {
		return config;
	}

	public void setConfig(DisruptorConfig config) {
		this.config = config;
	}

	public WaitStrategy getWaitStrategy() {
		return waitStrategy;
	}

	public void setWaitStrategy(WaitStrategy waitStrategy) {
		this.waitStrategy = waitStrategy;
	}

	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
	}

	public EventFactory<DisruptorEvent> getEventFactory() {
		return eventFactory;
	}

	public void setEventFactory(EventFactory<DisruptorEvent> eventFactory) {
		this.eventFactory = eventFactory;
	}

	public DisruptorEventHandler getPreEventHandler() {
		return preEventHandler;
	}

	public void setPreEventHandler(DisruptorEventHandler preEventHandler) {
		this.preEventHandler = preEventHandler;
	}

	public DisruptorEventHandler getPostEventHandler() {
		return postEventHandler;
	}

	public void setPostEventHandler(DisruptorEventHandler postEventHandler) {
		this.postEventHandler = postEventHandler;
	}
	
}
