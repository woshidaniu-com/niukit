package com.woshidaniu.smbclient;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Properties;

import com.woshidaniu.smbclient.io.CopyStreamProcessListener;
import com.woshidaniu.smbclient.utils.SMBConfigUtils;

/**
 * 
 * @className ： SMBClientConfig
 * @description ： SMB客户端的配置
 * @author ：kangzhidong
 * @date ： Dec 24, 2015 9:59:06 AM
 */
public class SMBClientConfig {

	/** 默认连接超时时间 ：30秒 */
	public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
	/** 默认数据连接读取/发送数据超时时间 ：30秒 */
	public static final int DEFAULT_READ_TIMEOUT = 30 * 1000;
	/** 匿名账号：anonymous */
	public static final String ANONYMOUS_LOGIN = "anonymous";
	/** 默认缓存大小： 8M */
	public static final int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;
	/** 默认FileChannel缓存大小： 2M */
	public static final int DEFAULT_CHANNEL_SIZE = 2 * 1024 * 1024;

	// ===============================================================================
	// =============SMBClient对象池配置===================================================
	// ===============================================================================

	/** 【共享文件服务器】域名 */
	protected String domain;
	/** 【共享文件服务器】地址 */
	protected String host;
	/** 【共享文件服务器】用户名 */
	protected String username = ANONYMOUS_LOGIN;
	/** 【共享文件服务器】密码 ：注意事项：登陆服务器的密码不支持强密码（如密码含有 &……%￥# 等字符，都当成分隔符处理）*/
	protected String password;
	/** 【共享文件服务器】根共享目录*/
	protected String sharedDir;
	/** 启用或禁用用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查 */
	protected boolean allowUserInteraction = true;
	/** 启用或禁用数据流方式上传/下载时是否在缓冲发送/接收完成自动刷新缓存区；大文件上传下载时比较有用;默认false */
	protected boolean autoFlush = false;
	/** 数据流方式上传/下载时缓存区达到可自动刷新的最小阀值；仅当 autoflush 为true 时才有效；默认与默认缓存区大小相同即 8M */
	protected int autoFlushBlockSize = DEFAULT_BUFFER_SIZE;
	/** 内部缓冲区大小;默认 8M */
	protected int bufferSize = DEFAULT_BUFFER_SIZE;
	/** 文件通道读取缓冲区大小;默认 2M */
	protected int channelReadBufferSize = DEFAULT_CHANNEL_SIZE;
	/** 文件通道写出缓冲区大小;默认 2M */
	protected int channelWriteBufferSize = DEFAULT_CHANNEL_SIZE;
	/** 连接超时时间，单位为秒，默认30秒 */
	protected int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
	/** 进行存储时/检索操作时数据处理进度监听对象 */
	protected CopyStreamProcessListener copyStreamProcessListener;
	/** TCP进行存储时/检索操作时数据处理进度监听对象类路径 */
	protected String copyStreamProcessListenerName;
	/** 从数据读取的超时时间，单位（毫秒）；默认 30000 毫秒 */
	protected int readTimeout = DEFAULT_READ_TIMEOUT;
	/** 是否使用Log4j记录命令信息,默认打印出命令，如果开启日志则关闭打印;默认 false */
	protected boolean logDebug = false;
	/** 启用或禁用在条件允许情况下允许协议使用缓存 */
	protected boolean usecaches = false;

	public SMBClientConfig(){
		
	}
	
	public SMBClientConfig(Properties properties){
		SMBConfigUtils.initPropertiesConfig(this, properties);
	}
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAllowUserInteraction() {
		return allowUserInteraction;
	}

	public void setAllowUserInteraction(boolean allowUserInteraction) {
		this.allowUserInteraction = allowUserInteraction;
	}

	public boolean isAutoFlush() {
		return autoFlush;
	}

	public void setAutoFlush(boolean autoFlush) {
		this.autoFlush = autoFlush;
	}

	public int getAutoFlushBlockSize() {
		return autoFlushBlockSize;
	}

	public void setAutoFlushBlockSize(int autoFlushBlockSize) {
		this.autoFlushBlockSize = autoFlushBlockSize;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getChannelReadBufferSize() {
		return channelReadBufferSize;
	}

	public void setChannelReadBufferSize(int channelReadBufferSize) {
		this.channelReadBufferSize = channelReadBufferSize;
	}

	public int getChannelWriteBufferSize() {
		return channelWriteBufferSize;
	}

	public void setChannelWriteBufferSize(int channelWriteBufferSize) {
		this.channelWriteBufferSize = channelWriteBufferSize;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public CopyStreamProcessListener getCopyStreamProcessListener() {
		return copyStreamProcessListener;
	}

	public void setCopyStreamProcessListener(
			CopyStreamProcessListener copyStreamProcessListener) {
		this.copyStreamProcessListener = copyStreamProcessListener;
	}

	public String getCopyStreamProcessListenerName() {
		return copyStreamProcessListenerName;
	}

	public void setCopyStreamProcessListenerName(
			String copyStreamProcessListenerName) {
		this.copyStreamProcessListenerName = copyStreamProcessListenerName;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	public String getSharedDir() {
		return sharedDir;
	}

	public void setSharedDir(String sharedDir) {
		this.sharedDir = sharedDir;
	}

	public boolean isLogDebug() {
		return logDebug;
	}

	public void setLogDebug(boolean logDebug) {
		this.logDebug = logDebug;
	}

	public boolean isUsecaches() {
		return usecaches;
	}

	public void setUsecaches(boolean usecaches) {
		this.usecaches = usecaches;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("FTPClientConfig [");
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor propDes : propertyDescriptors) {
				// 如果是class属性 跳过
				if (propDes.getName().equals("class")
						|| propDes.getReadMethod() == null
						|| propDes.getWriteMethod() == null) {
					continue;
				}
				builder.append(propDes.getName()).append("=").append(
						propDes.getReadMethod().invoke(this)).append("\n ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		builder.append("]");
		return builder.toString();
	}

}
