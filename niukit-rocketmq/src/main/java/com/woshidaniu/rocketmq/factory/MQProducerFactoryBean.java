/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.rocketmq.factory;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.woshidaniu.rocketmq.config.ProducerConfig;
import com.woshidaniu.rocketmq.exception.RocketMQException;
import com.woshidaniu.rocketmq.hooks.MQProducerShutdownHook;
import com.woshidaniu.rocketmq.listener.DefaultTransactionCheckListener;


public class MQProducerFactoryBean implements FactoryBean<MQProducer>, InitializingBean  {
	
	private static final Logger LOG = LoggerFactory.getLogger(MQProducerFactoryBean.class);
	
	/**
	 * 客户端配置对象：必须存在
	 */
	private ProducerConfig config;
	private TransactionCheckListener transactionCheckListener;

	@Override
	public void afterPropertiesSet() throws Exception {

		if (transactionCheckListener == null) {
			setTransactionCheckListener(new DefaultTransactionCheckListener());;
		}
		
	}
	
	/**
	 * 初始化消息生产者
	 * 
	 * @param producer
	 * @param config
	 */
	public void configure(DefaultMQProducer producer, ProducerConfig config) {
		producer.setClientCallbackExecutorThreads(config.getClientCallbackExecutorThreads());
		producer.setClientIP(config.getClientIP());
		producer.setCompressMsgBodyOverHowmuch(config.getCompressMsgBodyOverHowmuch());
		producer.setCreateTopicKey(config.getCreateTopicKey());
		producer.setDefaultTopicQueueNums(config.getDefaultTopicQueueNums());
		producer.setHeartbeatBrokerInterval(config.getHeartbeatBrokerInterval());
		producer.setInstanceName(config.getInstanceName());
		producer.setLatencyMax(config.getLatencyMax());
		producer.setMaxMessageSize(config.getMaxMessageSize());
		producer.setNamesrvAddr(config.getNamesrvAddr());
		producer.setNotAvailableDuration(config.getNotAvailableDuration());
		producer.setPersistConsumerOffsetInterval(config.getPersistConsumerOffsetInterval());
		producer.setPollNameServerInterval(config.getPollNameServerInterval());
		producer.setProducerGroup(config.getProducerGroup());
		producer.setRetryAnotherBrokerWhenNotStoreOK(config.isRetryAnotherBrokerWhenNotStoreOK());
		producer.setRetryTimesWhenSendAsyncFailed(config.getRetryTimesWhenSendAsyncFailed());
		producer.setRetryTimesWhenSendFailed(config.getRetryTimesWhenSendFailed());
		producer.setSendLatencyFaultEnable(config.isSendLatencyFaultEnable());
		producer.setSendMessageWithVIPChannel(config.isVipChannelEnabled());
		producer.setSendMsgTimeout(config.getSendMsgTimeout());
		producer.setUnitMode(config.isUnitMode());
		producer.setUnitName(config.getUnitName());
		producer.setVipChannelEnabled(config.isVipChannelEnabled());
	}
	
	/**
	 * 初始化向rocketmq发送普通消息的生产者
	 */
	@Override
	public MQProducer getObject() throws Exception {
		
		if (StringUtils.isEmpty(config.getProducerGroup())) {
			throw new RocketMQException("producerGroup is empty");
		}
		if (StringUtils.isEmpty(config.getNamesrvAddr())) {
			throw new RocketMQException("nameServerAddr is empty");
		}
		if (StringUtils.isEmpty(config.getInstanceName())) {
			throw new RocketMQException("instanceName is empty");
		}

		/*
		 * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
		 * 注意：ProducerGroupName需要由应用来保证唯一<br>
		 * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
		 * 因为服务器会回查这个Group下的任意一个Producer
		 */

		// 是否需要事物
		if (config.isTransaction()) {
			try {
				/*
				 * 初始化向rocketmq发送事务消息的生产者
				 */
				TransactionMQProducer producer = new TransactionMQProducer(config.getProducerGroup());

				// 初始化参数
				this.configure(producer, config);

				// 事务回查最小并发数
				producer.setCheckThreadPoolMinSize(config.getCheckThreadPoolMinSize());
				// 事务回查最大并发数
				producer.setCheckThreadPoolMaxSize(config.getCheckThreadPoolMaxSize());
				// 队列数
				producer.setCheckRequestHoldMax(config.getCheckRequestHoldMax());
				// TODO 由于社区版本的服务器阉割调了消息回查的功能，所以这个地方没有意义
				producer.setTransactionCheckListener(transactionCheckListener);

				/*
				 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br> 注意：切记不可以在每次发送消息时，都调用start方法
				 */
				producer.start();

				LOG.info("RocketMQ TransactionMQProducer Started ! groupName:[%s],namesrvAddr:[%s],instanceName:[%s].",
						config.getProducerGroup(), config.getNamesrvAddr(), config.getInstanceName());
				
				/**
				 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从RocketMQ服务器上注销自己
				 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
				 */
				Runtime.getRuntime().addShutdownHook(new MQProducerShutdownHook(producer));
				
				return producer;

			} catch (Exception e) {
				LOG.error(String.format("Producer is error {}", e.getMessage(), e));
				throw new RocketMQException(e);
			}

		} else {

			try {

				// 创建生产者对象
				final DefaultMQProducer producer = new DefaultMQProducer(config.getProducerGroup());

				// 初始化参数
				this.configure(producer, config);

				/*
				 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br> 注意：切记不可以在每次发送消息时，都调用start方法
				 */
				producer.start();

				LOG.info("RocketMQ MQProducer Started ! groupName:[%s],namesrvAddr:[%s],instanceName:[%s].",
						config.getProducerGroup(), config.getNamesrvAddr(), config.getInstanceName());

				/**
				 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从RocketMQ服务器上注销自己
				 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
				 */
				Runtime.getRuntime().addShutdownHook(new MQProducerShutdownHook(producer));

				return producer;
			} catch (Exception e) {
				LOG.error(String.format("RocketMQ MQProducer Start failure ：%s", e.getMessage(), e));
				throw new RocketMQException(e);
			}
		}
	}

	@Override
	public Class<?> getObjectType() {
		return MQProducer.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public ProducerConfig getConfig() {
		return config;
	}

	public void setConfig(ProducerConfig config) {
		this.config = config;
	}

	public TransactionCheckListener getTransactionCheckListener() {
		return transactionCheckListener;
	}

	public void setTransactionCheckListener(TransactionCheckListener transactionCheckListener) {
		this.transactionCheckListener = transactionCheckListener;
	}
	
}
