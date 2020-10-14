package com.woshidaniu.socket.code;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;

public class DataEncoder extends ProtocolEncoderAdapter{
	   Charset charset = null;
	    
	   public DataEncoder(String encoding){
		   charset = Charset.forName(encoding);
	   }
	 
	    @Override
	    public void dispose(IoSession session) throws Exception {
	        
	    }

	    public void encode(IoSession session, Object message, ProtocolEncoderOutput output)
	            throws Exception {
	        IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
	        
	        buf.putString(message.toString(), charset.newEncoder());
	        
	        buf.putString(LineDelimiter.DEFAULT.getValue(), charset.newEncoder());
	        buf.flip();
	        
	        output.write(buf);
	        
	    }
	
}
