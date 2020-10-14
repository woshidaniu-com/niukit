package com.woshidaniu.socket.code;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class DataDecoder extends CumulativeProtocolDecoder{
	
	Charset charset =null;
    IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
    
    public DataDecoder(String encoding){
    	charset = Charset.forName(encoding);
    }
    
    
    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput output)
            throws Exception {
        while(in.hasRemaining()){
            byte b = in.get();
            if(b == '\n'){
                buf.flip();
                byte[] bytes = new byte[buf.limit()];
                buf.get(bytes);
                String message = new String(bytes,charset);
                buf = IoBuffer.allocate(100).setAutoExpand(true);
                
                output.write(message);
            }else{
                buf.put(b);
            }
        }
    }

    @Override
    public void dispose(IoSession arg0) throws Exception {
        
    }

    @Override
    public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
            throws Exception {
        
    }

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		return false;
	}

}
