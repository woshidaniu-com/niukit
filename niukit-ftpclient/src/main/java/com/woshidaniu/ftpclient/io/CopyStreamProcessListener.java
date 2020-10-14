package com.woshidaniu.ftpclient.io;

import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
/**
 * 
 * @className	： CopyProcessStreamListener
 * @description	： 数据处理进度监听抽象实现，可继承该对象进行进度检测
 * @author 		：kangzhidong
 * @date		： Jan 13, 2016 4:34:26 PM
 */
public abstract class CopyStreamProcessListener implements CopyStreamListener {
	
	// 文件名称
	protected String fileName;
	
	public CopyStreamProcessListener(){
		
	}
	 
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public void bytesTransferred(CopyStreamEvent event) {
		 bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
	}
	
}
