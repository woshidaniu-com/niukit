/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.rocketmq.factory;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.AllocateMessageQueueStrategy;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueConsistentHash;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import com.woshidaniu.rocketmq.config.ConsumerConfig;
import com.woshidaniu.rocketmq.exception.RocketMQException;
import com.woshidaniu.rocketmq.hooks.MQPushConsumerShutdownHook;


public class MQPushConsumerFactoryBean implements FactoryBean<MQPushConsumer>, InitializingBean  {
	
	private static final Logger LOG = LoggerFactory.getLogger(MQPushConsumerFactoryBean.class);
	
	/**
	 * 客户端配置对象：必须存在
	 */
	private ConsumerConfig config;
	/**
	 * 消费者消息监听实现：必须存在的实现对象
	 */
	private MessageListenerConcurrently messageListener;
	private OffsetStore offsetStore;
	private AllocateMessageQueueStrategy allocateMessageQueueStrategy;
	
	@Override
	public void afterPropertiesSet() throws Exception {

		/*
		 * Queue allocation algorithm specifying how message queues are allocated to
		 * each consumer clients.
		 */
		if (null == allocateMessageQueueStrategy) {
			setAllocateMessageQueueStrategy(new AllocateMessageQueueConsistentHash());	
		}
		
	}
	
	/**
	 * 初始化消息消费者
	 * @param consumer
	 * @param config
	 */
	public void configure(DefaultMQPushConsumer consumer, ConsumerConfig config) {
		consumer.setAdjustThreadPoolNumsThreshold(config.getAdjustThreadPoolNumsThreshold());
		consumer.setClientCallbackExecutorThreads(config.getClientCallbackExecutorThreads());
		consumer.setClientIP(config.getClientIP());
		consumer.setConsumeConcurrentlyMaxSpan(config.getConsumeConcurrentlyMaxSpan());
		try {
			consumer.setConsumeFromWhere(ConsumeFromWhere.valueOf(config.getConsumeFromWhere()));
		} catch (Exception e) {
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		}
		consumer.setConsumeMessageBatchMaxSize(config.getConsumeMessageBatchMaxSize());
		consumer.setConsumerGroup(config.getConsumerGroup());
		// 设置批量消费个数;设置后会出现数据消费延时
		consumer.setConsumeThreadMax(config.getConsumeThreadMax());
		consumer.setConsumeThreadMin(config.getConsumeThreadMin());
		consumer.setConsumeTimeout(config.getConsumeTimeout());
		consumer.setConsumeTimestamp(config.getConsumeTimestamp());
		consumer.setHeartbeatBrokerInterval(config.getHeartbeatBrokerInterval());
		consumer.setInstanceName(config.getInstanceName());
		consumer.setMaxReconsumeTimes(config.getMaxReconsumeTimes());
		try {
			consumer.setMessageModel(MessageModel.valueOf(config.getMessageModel()));
		} catch (Exception e) {
		}
		consumer.setNamesrvAddr(config.getNamesrvAddr());
		consumer.setPersistConsumerOffsetInterval(config.getPersistConsumerOffsetInterval());
		consumer.setPollNameServerInterval(config.getPollNameServerInterval());
		consumer.setPostSubscriptionWhenPull(config.isPostSubscriptionWhenPull());
		consumer.setPullBatchSize(config.getPullBatchSize());
		consumer.setPullInterval(config.getPullInterval());
		consumer.setPullThresholdForQueue(config.getPullThresholdForQueue());
		consumer.setSuspendCurrentQueueTimeMillis(config.getSuspendCurrentQueueTimeMillis());
		consumer.setUnitMode(config.isUnitMode());
		consumer.setUnitName(config.getUnitName());
		consumer.setVipChannelEnabled(config.isVipChannelEnabled());
	}
	
	/**
	 * 初始化rocketmq消息监听方式的消费者
	 */
	@Override
	public MQPushConsumer getObject() throws Exception {
		
		// 消费者配置

		if (StringUtils.isEmpty(config.getConsumerGroup())) {
			throw new RocketMQException("consumerGroup is empty");
		}
		if (StringUtils.isEmpty(config.getNamesrvAddr())) {
			throw new RocketMQException("nameServerAddr is empty");
		}
		if (StringUtils.isEmpty(config.getInstanceName())) {
			throw new RocketMQException("instanceName is empty");
		}
		if (CollectionUtils.isEmpty(config.getSubscription())) {
			throw new RocketMQException("subscription is empty ");
		}

		try {

			/*
			 * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br> 注意：ConsumerGroupName需要由应用来保证唯一
			 */
			final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(config.getConsumerGroup());

			consumer.setAllocateMessageQueueStrategy(allocateMessageQueueStrategy);

			// 初始化参数
			this.configure(consumer, config);

			// consumer.setOffsetStore(offsetStore);
			/*
			 * 订阅指定topic下tags
			 */
			if(!CollectionUtils.isEmpty(config.getSubscription())){
				
				Iterator<Entry<String, String>> ite = config.getSubscription().entrySet().iterator();
				while (ite.hasNext()) {
					Entry<String, String> entry = ite.next();
					/* 
					 * entry.getKey() 	： topic名称 
					 * entry.getValue() : 根据实际情况设置消息的tag 
					 */
					consumer.subscribe(entry.getKey(), entry.getValue());
				}
				
			}
			
			/*
			 * 注册消费监听
			 */
			consumer.registerMessageListener(messageListener);

			/*
			 * 延迟5秒再启动，主要是等待spring事件监听相关程序初始化完成，否则，回出现对RocketMQ的消息进行消费后立即发布消息到达的事件，
			 * 然而此事件的监听程序还未初始化，从而造成消息的丢失
			 */
			Executors.newScheduledThreadPool(1).schedule(new Thread() {
				public void run() {
					try {

						/*
						 * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
						 */
						consumer.start();

						LOG.info("RocketMQ MQPushConsumer Started ! groupName:[%s],namesrvAddr:[%s],instanceName:[%s].",
								config.getConsumerGroup(), config.getNamesrvAddr(), config.getInstanceName());
						
						/**
						 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从RocketMQ服务器上注销自己
						 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
						 */
						Runtime.getRuntime().addShutdownHook(new MQPushConsumerShutdownHook(consumer));
						
					} catch (Exception e) {
						LOG.error(String.format("RocketMQ MQPushConsumer Start failure ：%s", e.getMessage(), e));
					}
				}
			}, 5, TimeUnit.SECONDS);

			return consumer;

		} catch (Exception e) {
			throw new RocketMQException(e);
		}
		 
	}

	@Override
	public Class<?> getObjectType() {
		return MQPushConsumer.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public ConsumerConfig getConfig() {
		return config;
	}

	public void setConfig(ConsumerConfig config) {
		this.config = config;
	}

	public MessageListenerConcurrently getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListenerConcurrently messageListener) {
		this.messageListener = messageListener;
	}

	public OffsetStore getOffsetStore() {
		return offsetStore;
	}

	public void setOffsetStore(OffsetStore offsetStore) {
		this.offsetStore = offsetStore;
	}

	public AllocateMessageQueueStrategy getAllocateMessageQueueStrategy() {
		return allocateMessageQueueStrategy;
	}

	public void setAllocateMessageQueueStrategy(AllocateMessageQueueStrategy allocateMessageQueueStrategy) {
		this.allocateMessageQueueStrategy = allocateMessageQueueStrategy;
	}

	
	
}
