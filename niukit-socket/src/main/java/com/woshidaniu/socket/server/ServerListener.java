package com.woshidaniu.socket.server;

import org.apache.mina.core.session.IoSession;

public interface ServerListener {

	void callBack(IoSession session, Object message);
}
