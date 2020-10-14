package com.woshidaniu.io.utils;

import java.nio.ByteBuffer;

/**
 * 
 *@类名称	: ByteBufferUtils.java
 *@类描述	： ByteBuffer和 byte 数组相互转换
 *@创建人	：kangzhidong
 *@创建时间	：Mar 5, 2016 8:57:52 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ByteBufferUtils {

	public static ByteBuffer getByteBuffer(byte[] bytes){
		return ByteBuffer.wrap(bytes);
	}

	public static byte[] getBytes(ByteBuffer buf){
		// Retrieve all bytes in the buffer
		buf.clear();
		// Create a byte array
		byte[] bytes = new byte[buf.capacity()];
		// transfer bytes from this buffer into the given destination array
		buf.get(bytes, 0, bytes.length);
		return bytes;
	}
	
	public static byte[] getBytes(ByteBuffer buf,byte[] bytes){
		// Retrieve all bytes in the buffer
		buf.clear();
		// transfer bytes from this buffer into the given destination array
		buf.get(bytes, 0, bytes.length);
		return bytes;
	}

}
