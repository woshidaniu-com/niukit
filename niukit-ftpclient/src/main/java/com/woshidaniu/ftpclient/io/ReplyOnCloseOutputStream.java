package com.woshidaniu.ftpclient.io;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

public class ReplyOnCloseOutputStream extends OutputStream {

	/**
	 * The output stream to be filtered.
	 */
	protected OutputStream output;
	protected FTPClient ftpClient;

	public ReplyOnCloseOutputStream(OutputStream output, FTPClient ftpClient) {
		this.output = output;
		this.ftpClient = ftpClient;
	}
	 
	/**
	 * 
	 * @description	：<pre>方法:
	 * 	
	 * 	ftpClient.storeFileStream(remote)
	 * 	ftpClient.storeUniqueFileStream()
	 * 	ftpClient.storeUniqueFileStream(remote)
	 * 	ftpClient.appendFileStream(remote)
	 * 
	 *  不能完成整个FTP命令序列来完成交易。 必须调用completePendingCommand（）方法来接收来自服务器的答复，并确认完成了整个交易的成功。
	 * 
	 *	在使用这些方法时需要特别注意，在调用这个接口后，一定要手动close掉返回的OutputStream，然后再调用completePendingCommand方法，若不是按照这个顺序，则不对，伪代码：
	 *
	 *  OutputStream out = ftpClient.storeFileStream(remote);
	 *	out.close();
	 *	ftpClient.completePendingCommand();
	 *
	 *	关于原因这里有比较具体的分析:http://marc.info/?l=jakarta-commons-user&m=110443645016720&w=2
	 *	简单来说：completePendingCommand()会一直在等FTP Server返回226 Transfer complete，
	 *	但是FTP Server只有在接受到OutputStream执行close方法时，才会返回。所以先要执行close方法
	 *
	 * </pre> 
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 11:15:43 AM
	 */
	@Override
	public void close() throws IOException {
		try {
		  flush();
		} catch (IOException ignored) {
		}
		//一定要手动close掉返回的OutputStream，然后再调用completePendingCommand方法，若不是按照这个顺序，则不对
		output.close();
		//发送命令确认操作完成
		if (ftpClient.completePendingCommand()) {
			throw new IOException("File upload failed . Check FTP permissions and path.");
		}
	}

	@Override
	public void flush() throws IOException {
		output.flush();
	}
	
	@Override
	public void write(int b) throws IOException {
		output.write(b);
	}

}
