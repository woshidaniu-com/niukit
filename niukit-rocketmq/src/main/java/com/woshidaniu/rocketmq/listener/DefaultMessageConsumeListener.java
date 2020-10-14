package com.woshidaniu.rocketmq.listener;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.rocketmq.config.ConsumerConfig;
import com.woshidaniu.rocketmq.event.handler.MessageHandler;

public class DefaultMessageConsumeListener implements MessageListenerConcurrently {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultMessageConsumeListener.class);

	/**
	 * 真正处理消息的实现对象
	 */
	private MessageHandler messageHandler;
	private ConsumerConfig config;

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgExts, ConsumeConcurrentlyContext context) {

		// 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
		LOG.debug(Thread.currentThread().getName() + " Receive New Messages: " + msgExts.size());
		// 重试次数
		int retryTimes = config.getRetryTimesWhenConsumeFailed();
		// 消费消息内容
		for (MessageExt msgExt : msgExts) {

			try {
				boolean result = messageHandler.handleMessage(msgExt, context);
				// 重复消费指定的次数
				if (!result && msgExt.getReconsumeTimes() < retryTimes) {
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				LOG.debug(String.format("Message （MsgID : %s ） Consumed.", msgExt.getMsgId()));
			} catch (Exception e) {
				String error = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
				if (msgExt.getReconsumeTimes() < retryTimes) {
					// TODO 进行日志记录
					LOG.debug(String.format("Consume Error : %s , Message （MsgID : %s ） Reconsume.", error,
							msgExt.getMsgId()));
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				} else {
					// TODO 消息消费失败，进行日志记录
					LOG.error(String.format("Consume Error : %s .", error));
				}
			}
		}
		// 如果没有return success，consumer会重复消费此信息，直到success。
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public ConsumerConfig getConfig() {
		return config;
	}

	public void setConfig(ConsumerConfig config) {
		this.config = config;
	}
	
}
