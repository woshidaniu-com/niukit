package com.woshidaniu.socket.code;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MsgCodecFactory implements ProtocolCodecFactory{
	
	private String encoding;
	
	public MsgCodecFactory(String encoding){
		this.encoding = encoding;
	}
	
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
        return new DataDecoder(encoding);
    }

    public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
        return new DataEncoder(encoding);
    }
}
