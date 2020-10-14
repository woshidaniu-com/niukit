package com.woshidaniu.ftpclient.utils;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.ftpclient.exception.FTPClientException;

/**
 * 
 * @className ： FTPConnectUtils
 * @description ： TODO(描述这个类的作用)
 * @author ：kangzhidong
 * @date ： Jan 11, 2016 8:53:00 AM
 */
public class FTPConnectUtils {

	protected static Logger LOG = LoggerFactory.getLogger(FTPConnectUtils.class);

	/**
	 * 
	 * @description ： 连接到ftp服务器
	 * @author ：kangzhidong
	 * @date ：Jan 11, 2016 9:28:09 AM
	 * @param ftpClient
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static boolean connect(FTPClient ftpClient, String host, int port, String username, String password)
			throws Exception {
		if (ftpClient == null) {
			throw new IllegalArgumentException("ftpClient is null.");
		}
		// 使用制定用户名和密码登入 FTP 站点
		try {
			// 1.连接服务器
			// 与制定地址和端口的 FTP 站点建立 Socket 连接
			ftpClient.connect(host, port);
			// 检测连接是否成功
			int reply = ftpClient.getReplyCode();
			// 返回的code>=200&&code<300表示成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				// 释放占用的链接，并重置所有连接参数为初始值
				ftpClient.disconnect();
				LOG.warn("Unable to connect to FTP server.");
			}
			// Log success msg
			LOG.trace("...connection was successful.");
			// 2.登录ftp服务器 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			if (ftpClient.login(username, password)) {
				// 3.判断登陆是否成功
				if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))
						&& ftpClient.getAutodetectUTF8()) {
					// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
					ftpClient.setControlEncoding("UTF-8");
				}
				return true;
			} else {
				throw new FTPClientException(
						"ftpClient登陆失败! userName :[" + username + "] ; password:[" + password + "]");
			}
		} catch (Exception e) {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect(); // 断开连接
				} catch (IOException e1) {
					// do nothing
				}
			}
			throw new FTPClientException("Could not connect to server.", e);
		}
	}

	/**
	 * 
	 * @description ：释放FTPClient的连接
	 * @author ：kangzhidong
	 * @date ：Jan 11, 2016 9:27:53 AM
	 * @param ftpClient
	 */
	public static void releaseConnect(FTPClient ftpClient) {
		if (ftpClient != null) {
			try {
				if (ftpClient.isConnected()) {
					// 通过发送 QUIT 命令，登出 FTP 站点
					ftpClient.logout();
				}
			} catch (IOException e) {
				throw new FTPClientException("Could not disconnect from server.", e);
			} finally {
				// 注意,一定要在finally代码中断开连接，否则会导致占用ftp连接情况
				if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect(); // 断开连接
					} catch (IOException e1) {
						// do nothing
					}
				}
			}
		}
	}

	/**
	 * 
	 * @description ： 验证FTPClient连接的有效性
	 * @author ：kangzhidong
	 * @date ：Jan 12, 2016 9:06:01 AM
	 * @param ftpClient
	 * @return
	 */
	public static boolean validateConnect(FTPClient ftpClient) {
		if (ftpClient != null) {
			try {
				// 发送一个NOOP命令到FTP服务器。 这是为防止服务器超时有用，与 noop() 类似。防止连接超时，也可以根据返回值检查连接的状态。
				return ftpClient.isConnected() && ftpClient.isAvailable() && ftpClient.sendNoOp();
			} catch (IOException e) {
				LOG.warn("Failed to validate client: ", e);
				return false;
			}
		}
		return false;
	}

}
