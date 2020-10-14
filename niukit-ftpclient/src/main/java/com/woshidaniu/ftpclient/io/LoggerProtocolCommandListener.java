package com.woshidaniu.ftpclient.io;


import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.SocketClient;
import org.slf4j.Logger;

public class LoggerProtocolCommandListener implements ProtocolCommandListener {

	private final Logger __logger;
	private final boolean __nologin;
	private final char __eolMarker;
	private final boolean __directionMarker;

	public LoggerProtocolCommandListener(Logger logger) {
		this(logger, false, (char) 0);
	}
	
	public LoggerProtocolCommandListener(Logger logger, boolean suppressLogin) {
		this(logger, suppressLogin, (char) 0);
	}

	public LoggerProtocolCommandListener(Logger logger, boolean suppressLogin,
			char eolMarker) {
		this(logger, suppressLogin, eolMarker, false);
	}

	public LoggerProtocolCommandListener(Logger logger, boolean suppressLogin,
			char eolMarker, boolean showDirection) {
		__logger = logger;
		__nologin = suppressLogin;
		__eolMarker = eolMarker;
		__directionMarker = showDirection;
	}

	public void protocolCommandSent(ProtocolCommandEvent event) {
		if (__directionMarker) {
			logger("> ");
		}
		if (__nologin) {
			String cmd = event.getCommand();
			if ("PASS".equalsIgnoreCase(cmd) || "USER".equalsIgnoreCase(cmd)) {
				logger(cmd);
				logger(" *******"); // Don't bother with EOL marker for this!
			} else {
				final String IMAP_LOGIN = "LOGIN";
				if (IMAP_LOGIN.equalsIgnoreCase(cmd)) { // IMAP
					String msg = event.getMessage();
					msg = msg.substring(0, msg.indexOf(IMAP_LOGIN)
							+ IMAP_LOGIN.length());
					logger(msg);
					logger(" *******"); // Don't bother with EOL marker for
										// this!
				} else {
					logger(getPrintableString(event.getMessage()));
				}
			}
		} else {
			logger(getPrintableString(event.getMessage()));
		}
	}

	private String getPrintableString(String msg) {
		if (__eolMarker == 0) {
			return msg;
		}
		int pos = msg.indexOf(SocketClient.NETASCII_EOL);
		if (pos > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(msg.substring(0, pos));
			sb.append(__eolMarker);
			sb.append(msg.substring(pos));
			return sb.toString();
		}
		return msg;
	}

	public void protocolReplyReceived(ProtocolCommandEvent event) {
		if (__directionMarker) {
			logger("< ");
		}
		logger(event.getMessage());
	}

	private void logger(String message) {
		if (__logger.isInfoEnabled()) {
			__logger.info(message);
		} else if (__logger.isDebugEnabled()) {
			__logger.debug(message);
		} else if (__logger.isTraceEnabled()) {
			__logger.trace(message);
		}
	}

}
