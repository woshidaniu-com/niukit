package com.woshidaniu.ftpclient.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

public class ReplyOnCloseInputStream extends InputStream {

	/**
	 * The input stream to be filtered.
	 */
	protected InputStream input;
	protected FTPClient ftpClient;

	public ReplyOnCloseInputStream(InputStream input, FTPClient ftpClient) {
		this.input = input;
		this.ftpClient = ftpClient;
	}

	/**
	 * 
	 * @description	：<pre>
	 *  1、ftpClient.retrieveFileStream(remote)方法不能完成整个FTP命令序列来完成交易。 必须调用completePendingCommand（）方法来接收来自服务器的答复，并确认完成了整个交易的成功。
	 *  2、ftpClient.retrieveFileStream(remote)方法 ;调用完之后必须调用completePendingCommand释放,否则FTP会断开连接
	 *	3、在使用该方法时需要特别注意，在调用这个接口后，一定要手动close掉返回的InputStream，然后再调用completePendingCommand方法，若不是按照这个顺序，则不对，伪代码：
	 *	
	 *  InputStream is = ftpClient.retrieveFileStream(remote);
	 *	is.close();
	 *	ftpClient.completePendingCommand();
	 *	
	 *	retrieveFileStream的API文档说的有点罗嗦，还可以使用下列方法来替换上述使用方式
	 *	使用一个中间文件来做一个转接，这种方式比上述方法的好处就是自己容易控制，不容易出问题。伪代码如下：
	 *	
	 *	File localFile = new File(localPath, localFileName);
	 *	OutputStream output = new FileOutputStream(localFile);
	 *	ftpClient.retrieveFile(remoteFileName, output);
	 *	output.close();
	 *	InputStream input = new FileInputStream(localFile);
	 *
	 *	关于原因这里有比较具体的分析:http://marc.info/?l=jakarta-commons-user&m=110443645016720&w=2
	 *	简单来说：completePendingCommand()会一直在等FTP Server返回226 Transfer complete，
	 *	但是FTP Server只有在接受到InputStream执行close方法时，才会返回。所以先要执行close方法
	 *
	 * </pre> 
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 11:15:43 AM
	 */
	@Override
	public void close() throws IOException {
		//一定要手动close掉返回的OutputStream，然后再调用completePendingCommand方法，若不是按照这个顺序，则不对
		input.close();
		//发送命令确认操作完成
		if (ftpClient.completePendingCommand()) {
			 throw new IOException("Error loading file from FTP server. Check FTP permissions and path.");
		}
	}

	@Override
	public int read() throws IOException {
		return input.read();
	}

}
