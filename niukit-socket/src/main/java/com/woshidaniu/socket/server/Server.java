package com.woshidaniu.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.woshidaniu.socket.code.MsgCodecFactory;


/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：Socket 服务端
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月21日下午2:39:49
 */
public class Server {

	private final int default_port = 11200; 
	private String encoding;
	
	public Server(int port,ServerListener listener,String encoding){
		this.encoding = encoding;
		init(port,listener);
	}
	
	public Server(int port,String encoding){
		this.encoding = encoding;
		init(port,null);
	}
	
	public Server(){
		init(default_port,null);
	}
	
	public Server(ServerListener listener,String encoding){
		this.encoding = encoding;
		init(default_port,listener);
	}
	
	
	//初始化socket服务
    private void init(final int port,ServerListener listener){
    	SocketAcceptor acceptor = new NioSocketAcceptor();
    	ServerHandler handler = new ServerHandler(listener);
    	ProtocolCodecFilter filter = new ProtocolCodecFilter(new MsgCodecFactory(encoding));
    	
        acceptor.getFilterChain().addLast("codec", filter);
        LoggingFilter log = new LoggingFilter();
        log.setMessageReceivedLogLevel(LogLevel.DEBUG);
        
        acceptor.getFilterChain().addLast("logger", log);
        acceptor.setHandler(handler);//配置handler
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        
        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
