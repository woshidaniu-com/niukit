package org.springframework.enhanced.context.support;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
/**
 * PropertiesHolder for caching.
 * Stores the last-modified timestamp of the source file for efficient
 * change detection, and the timestamp of the last refresh attempt
 * (updated every time the cache entry gets re-validated).
 */
public class PropertiesHolder {
	
	protected final Properties properties;

	protected final long fileTimestamp;

	protected volatile long refreshTimestamp = -2;

	public final ReentrantLock refreshLock = new ReentrantLock();

	/** Cache to hold already generated MessageFormats per message code */
	protected final ConcurrentMap<String, MessageFormat> cachedMessageFormats = new ConcurrentHashMap<String, MessageFormat>();

	public PropertiesHolder() {
		this.properties = null;
		this.fileTimestamp = -1;
	}

	public PropertiesHolder(Properties properties, long fileTimestamp) {
		this.properties = properties;
		this.fileTimestamp = fileTimestamp;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public long getFileTimestamp() {
		return this.fileTimestamp;
	}

	public void setRefreshTimestamp(long refreshTimestamp) {
		this.refreshTimestamp = refreshTimestamp;
	}

	public long getRefreshTimestamp() {
		return this.refreshTimestamp;
	}

	public String getProperty(String code) {
		if (this.properties == null) {
			return null;
		}
		return this.properties.getProperty(code);
	}

	public MessageFormat getMessageFormat(String code) {
		if (this.properties == null) {
			return null;
		}
		MessageFormat result = this.cachedMessageFormats.get(code);
		if (result != null) {
			return result;
		}
		String msg = this.properties.getProperty(code);
		if (msg != null) {
			result = createMessageFormat(msg);
			this.cachedMessageFormats.put(code, result);
			return result;
		}
		return null;
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

}