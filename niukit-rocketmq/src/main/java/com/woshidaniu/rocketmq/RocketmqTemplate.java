package com.woshidaniu.rocketmq;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionExecuter;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByRandoom;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;

public class RocketmqTemplate {

	public final MessageQueueSelector HASH_SELECTOR = new SelectMessageQueueByHash();
	public final MessageQueueSelector RANDOOM_SELECTOR = new SelectMessageQueueByRandoom();
	@Autowired
	protected MQProducer producer;
	@Autowired(required = false)
	protected MQPushConsumer consumer;

	public RocketmqTemplate() {
	}
	
	public RocketmqTemplate(MQProducer producer) {
		this.producer = producer;
	}

	public List<MessageQueue> fetchPublishMessageQueues(final String topic) throws MQClientException {
		return producer.fetchPublishMessageQueues(topic);
	}

	public SendResult send(final String topic, final String tags, final String keys, final byte[] body)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {

		Message msg = new Message(topic, // topic
				tags, // tag
				keys, // key用于标识业务的唯一性
				body// body 二进制字节数组
		);

		return producer.send(msg);
	}

	public SendResult send(final String topic, final String tags, final String keys, final String body)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException, UnsupportedEncodingException {
		Message msg = new Message(topic, // topic
				tags, // tag
				keys, // key用于标识业务的唯一性
				body.getBytes(RemotingHelper.DEFAULT_CHARSET)// body 二进制字节数组
		);
		return producer.send(msg);
	}

	public SendResult send(final Message msg)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msg);
	}

	public SendResult send(final Message msg, final long timeout)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msg, timeout);
	}

	public void send(final Message msg, final SendCallback sendCallback)
			throws MQClientException, RemotingException, InterruptedException {
		producer.send(msg, sendCallback);
	}

	public void send(final Message msg, final SendCallback sendCallback, final long timeout)
			throws MQClientException, RemotingException, InterruptedException {
		producer.send(msg, sendCallback, timeout);
	}

	public void sendOneway(final Message msg) throws MQClientException, RemotingException, InterruptedException {
		producer.sendOneway(msg);
	}

	public SendResult send(final Message msg, final MessageQueue mq)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msg, mq);
	}

	public SendResult send(final Message msg, final MessageQueue mq, final long timeout)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msg, mq, timeout);
	}

	public void send(final Message msg, final MessageQueue mq, final SendCallback sendCallback)
			throws MQClientException, RemotingException, InterruptedException {
		producer.send(msg, mq, sendCallback);
	}

	public void send(final Message msg, final MessageQueue mq, final SendCallback sendCallback, long timeout)
			throws MQClientException, RemotingException, InterruptedException {
		producer.send(msg, mq, sendCallback, timeout);
	}

	public void sendOneway(final Message msg, final MessageQueue mq)
			throws MQClientException, RemotingException, InterruptedException {
		producer.sendOneway(msg, mq);
	}

	public SendResult send(final Message msg, final MessageQueueSelector selector, final Object arg)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msg, selector, arg);
	}

	public SendResult send(final Message msg, final MessageQueueSelector selector, final Object arg, final long timeout)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msg, selector, arg, timeout);
	}

	public void send(final Message msg, final MessageQueueSelector selector, final Object arg,
			final SendCallback sendCallback) throws MQClientException, RemotingException, InterruptedException {
		producer.send(msg, selector, arg, sendCallback);
	}

	public void send(final Message msg, final MessageQueueSelector selector, final Object arg,
			final SendCallback sendCallback, final long timeout)
			throws MQClientException, RemotingException, InterruptedException {
		producer.send(msg, selector, arg, sendCallback, timeout);
	}

	public void sendOneway(final Message msg, final MessageQueueSelector selector, final Object arg)
			throws MQClientException, RemotingException, InterruptedException {
		producer.sendOneway(msg, selector, arg);
	}

	public TransactionSendResult sendMessageInTransaction(final Message msg,
			final LocalTransactionExecuter tranExecuter, final Object arg) throws MQClientException {
		return producer.sendMessageInTransaction(msg, tranExecuter, arg);
	}

	// for batch
	public SendResult send(final Collection<Message> msgs)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msgs);
	}

	public SendResult send(final Collection<Message> msgs, final long timeout)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msgs, timeout);
	}

	public SendResult send(final Collection<Message> msgs, final MessageQueue mq)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msgs, mq);
	}

	public SendResult send(final Collection<Message> msgs, final MessageQueue mq, final long timeout)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		return producer.send(msgs, mq, timeout);
	}
	
	public void registerMessageListener(final MessageListenerConcurrently messageListener){
		consumer.registerMessageListener(messageListener);
	}

	public void registerMessageListener(final MessageListenerOrderly messageListener){
		consumer.registerMessageListener(messageListener);
	}

	public MQProducer getProducer() {
		return producer;
	}

	public void setProducer(MQProducer producer) {
		this.producer = producer;
	}

	public MQPushConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MQPushConsumer consumer) {
		this.consumer = consumer;
	}
	
}
