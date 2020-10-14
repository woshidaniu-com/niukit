package com.woshidaniu.socket.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.woshidaniu.socket.code.MsgCodecFactory;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：Socket 客户端
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月21日下午2:21:42
 */
public class Client {

	public SocketConnector connector = null;
    public ConnectFuture future;
    public IoSession session = null;
    
    private static final int timeout = 3000;
    
    private String address;
    private int port;
    private String encoding;
    
    /**
     * 客户端构造函数
     * @param address IP地址
     * @param port 端口号
     * @param encoding 编码格式
     */
    public Client(String address,int port,String encoding){
    	this.address = address;
    	this.port = port;
    	this.encoding = encoding;
    	connect(null);
    }
    
    
    /**
     * 
     * 客户端构造函数
     * @param address IP地址
     * @param port 端口号
     * @param listener 客户端消息监听器
     * @param encoding 编码格式
     */
    public Client(String address,int port,ClientListener listener,String encoding){
    	this.address = address;
    	this.port = port;
    	this.encoding = encoding;
    	connect(listener);
    }
    
    //建立连接
    private void connect(ClientListener listener){
        try{
            connector = new NioSocketConnector();
            connector.setConnectTimeoutMillis(timeout);
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MsgCodecFactory(encoding)));
            LoggingFilter log = new LoggingFilter();
            log.setMessageReceivedLogLevel(LogLevel.INFO);
            connector.getFilterChain().addLast("logger", log);
            connector.setHandler(new ClientHandler(listener));
            future = connector.connect(new InetSocketAddress(address , port));
            future.awaitUninterruptibly();
            session = future.getSession();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void setAttribute(Object key , Object value){
        session.setAttribute(key, value);
    }
    
    
    public void sentMsg(Object message){
        session.write(message);
    }
    
    
    public void close(){
        CloseFuture future = session.getCloseFuture();
        future.awaitUninterruptibly(1000);
        connector.dispose();
    }
    
    public SocketConnector getConnector() {
        return connector;
    }


    public IoSession getSession() {
        return session;
    }



}
