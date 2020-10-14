package com.woshidaniu.socket.client;

import java.util.logging.Logger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：客户端消息适配器
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月21日下午2:19:20
 */
public class ClientHandler extends IoHandlerAdapter {

	private ClientListener listener;
	
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ClientHandler(ClientListener listener){
    	this.listener = listener;
    }
    
    
    public void messageReceived(IoSession session, Object message) throws Exception {
        String messageStr = message.toString();
        logger.info("客户端收到一条消息 : " + messageStr);
        
        if (listener != null){
        	listener.callBack(session, message);
        }
        
    }
    
    public void messageSent(IoSession session , Object message) throws Exception{
        logger.info("客户端发送一条消息 ：" + message.toString());
    }
    
}
