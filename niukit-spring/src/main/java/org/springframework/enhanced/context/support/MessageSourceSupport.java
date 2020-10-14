package org.springframework.enhanced.context.support;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

/**
 * 修改Spring的org.springframework.context.support.MessageSourceSupport去除Locale支持
 */
public abstract class MessageSourceSupport {

	protected static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");

	protected static Logger LOG = LoggerFactory.getLogger(MessageSourceSupport.class);

	protected boolean alwaysUseMessageFormat = false;

	protected final Map<String, MessageFormat> messageFormatsPerMessage = new HashMap<String, MessageFormat>();

	public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
		this.alwaysUseMessageFormat = alwaysUseMessageFormat;
	}

	protected boolean isAlwaysUseMessageFormat() {
		return this.alwaysUseMessageFormat;
	}

	protected String renderDefaultMessage(String defaultMessage, Object[] args) {
		return formatMessage(defaultMessage, args);
	}

	protected String formatMessage(String msg, Object[] args) {
		if (msg == null || (!this.alwaysUseMessageFormat && ObjectUtils.isEmpty(args))) {
			return msg;
		}
		MessageFormat messageFormat = null;
		synchronized (this.messageFormatsPerMessage) {
			messageFormat = this.messageFormatsPerMessage.get(msg);
			if (messageFormat == null) {
				try {
					messageFormat = createMessageFormat(msg);
				}
				catch (IllegalArgumentException ex) {
					// invalid message format - probably not intended for formatting,
					// rather using a message structure with no arguments involved
					if (this.alwaysUseMessageFormat) {
						throw ex;
					}
					// silently proceed with raw message if format not enforced
					messageFormat = INVALID_MESSAGE_FORMAT;
				}
				messageFormatsPerMessage.put(msg, messageFormat);
			}
		}
		if (messageFormat == INVALID_MESSAGE_FORMAT) {
			return msg;
		}
		synchronized (messageFormat) {
			return messageFormat.format(resolveArguments(args));
		}
	}

	/**
	 * Create a MessageFormat for the given message and Locale.
	 * @param msg the message to create a MessageFormat for
	 * @param locale the Locale to create a MessageFormat for
	 * @return the MessageFormat instance
	 */
	protected MessageFormat createMessageFormat(String msg) {
		return new MessageFormat((msg != null ? msg : ""));
	}

	/**
	 * Template method for resolving argument objects.
	 * <p>The default implementation simply returns the given argument array as-is.
	 * Can be overridden in subclasses in order to resolve special argument types.
	 * @param args the original argument array
	 * @param locale the Locale to resolve against
	 * @return the resolved argument array
	 */
	protected Object[] resolveArguments(Object[] args) {
		return args;
	}
	
}
