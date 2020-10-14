package com.woshidaniu.ftpclient.utils;

import com.woshidaniu.ftpclient.FTPClient;
import com.woshidaniu.ftpclient.io.CopyStreamProcessListener;

public class FTPCopyListenerUtils {

	public static void initCopyListener(FTPClient ftpClient,String fileName){
		//进度监听
		CopyStreamProcessListener listener = ftpClient.getCopyStreamProcessListener();
		//判断监听存在
		if(listener != null){
	    	listener.setFileName(fileName);
	    }
	}
	
}
