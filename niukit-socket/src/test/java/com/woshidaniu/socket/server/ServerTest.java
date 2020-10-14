package com.woshidaniu.socket.server;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.junit.Test;

public class ServerTest {

	@Test
	public void testServer(){
		new Server(11200,new ServerListener() {
			@Override
			public void callBack(IoSession session, Object message) {
				System.out.println("处理收到的消息");
			}
		},"GBK");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
