/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.rocketmq.event;

import java.io.UnsupportedEncodingException;

import org.apache.rocketmq.common.message.MessageExt;

import com.woshidaniu.disruptor.event.DisruptorEvent;
import com.woshidaniu.rocketmq.util.StringUtils;

@SuppressWarnings("serial")
public class RocketmqDisruptorEvent extends DisruptorEvent {

	private MessageExt messageExt;
	private String topic;
	private String tag;
	private byte[] body;

	public RocketmqDisruptorEvent(Object source) {
		super(source);
	}
	
	@Override
	public String getRouteExpression() {
		String expression = super.getRouteExpression();
		if(StringUtils.isEmpty(expression)){
			return this.buildRouteExpression(messageExt);
		}
		return expression;
	}
	
	private String buildRouteExpression(MessageExt msgExt) {
		return new StringBuilder("/").append(msgExt.getTopic()).append("/").append(msgExt.getTags()).append("/")
				.append(msgExt.getKeys()).toString();
	}

	public String getMsgBody() {
		try {
			return new String(this.body, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public String getMsgBody(String code) {
		try {
			return new String(this.body, code);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public MessageExt getMessageExt() {
		return messageExt;
	}

	public void setMessageExt(MessageExt messageExt) {
		this.messageExt = messageExt;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
	
}
