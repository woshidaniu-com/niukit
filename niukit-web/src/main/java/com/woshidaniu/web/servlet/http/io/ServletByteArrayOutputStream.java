package com.woshidaniu.web.servlet.http.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

/**
 * 
 * @className	： ServletByteArrayOutputStream
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Jan 28, 2016 11:43:19 AM
 * @version 	V1.0
 */
public class ServletByteArrayOutputStream extends ServletOutputStream {
	
	private ByteArrayOutputStream bout;

	public ServletByteArrayOutputStream(ByteArrayOutputStream bout) { // 接收数据写到哪里
		this.bout = bout;
	}

	@Override
	public void write(int b) throws IOException {
		bout.write(b);
	}


}
