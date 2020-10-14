package com.woshidaniu.socket.server;

import java.util.logging.Logger;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：Socket服务端消息适配器
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月21日下午2:17:49
 */
public class ServerHandler extends IoHandlerAdapter {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
    private ServerListener listener;
	
	public ServerHandler(ServerListener listener){
		this.listener = listener;
	}
	
	
    @Override
    public void exceptionCaught(IoSession session, Throwable arg1)
            throws Exception {
        logger.warning("server have a exception : " + arg1.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String messageStr = message.toString();
        logger.info(String.format("服务器收到一条消息息:%s", messageStr));
        //回调
        if (listener != null){
        	listener.callBack(session, message);
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info(String.format("服务器返回一条消息:%s", message));
    }

	@Override
	@SuppressWarnings("rawtypes")
    public void sessionClosed(IoSession session) throws Exception {
        CloseFuture future = session.close(true);
        future.addListener(new IoFutureListener(){
            public void operationComplete(IoFuture future){
                if(future instanceof CloseFuture){
                    ((CloseFuture)future).setClosed();
                }
            }
        });
        logger.info("there is a session closed");
    }

}
