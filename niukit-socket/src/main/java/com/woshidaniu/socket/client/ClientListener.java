package com.woshidaniu.socket.client;

import org.apache.mina.core.session.IoSession;

public interface ClientListener {

	void callBack(IoSession session, Object message);
}
