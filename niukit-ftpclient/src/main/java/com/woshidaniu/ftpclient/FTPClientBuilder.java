package com.woshidaniu.ftpclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.nio.charset.Charset;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.util.TrustManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.ExceptionUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.ftpclient.io.CopyStreamProcessListener;
import com.woshidaniu.ftpclient.io.LoggerProtocolCommandListener;
import com.woshidaniu.ftpclient.utils.FTPConfigUtils;
import com.woshidaniu.ftpclient.utils.FTPConfigurationUtils;

/**
 * 
 * @className	： FTPClientBuilder
 * @description	： FTPClient对象构建器
 * @author 		： kangzhidong
 * @date		： Jan 8, 2016 8:44:26 AM
 */
public class FTPClientBuilder extends FTPClientConfig {
	
	protected static Logger LOG = LoggerFactory.getLogger(FTPClientBuilder.class);
	protected FTPClientConfig configuration;
	
	public FTPClientBuilder() {
		this.configuration = this;
	}
	
	public FTPClientBuilder(String location) {
		try {
			FTPConfigUtils.initPropertiesConfig(this,location);
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		this.configuration = this;
	}
	
	public FTPClientBuilder(FTPClientConfig config) {
		try {
			BeanUtils.copyProperties(this, config);
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		this.configuration = config;
	}

	public FTPClient build() {
		//ftp客户端对象类型：FTPClient,FTPSClient,FTPHTTPClient
		if("FTPSClient".equalsIgnoreCase(configuration.getClientType())){
			//加密的FTPSClient
			FTPSClient ftpsClient = new FTPSClient(false);
			
			ftpsClient = new FTPSClient();
			//初始化FTPSClient
			return this.initFTPSClient(this.initFTPClient(ftpsClient));
		}else if("FTPHTTPClient".equalsIgnoreCase(configuration.getClientType())){
			//HTTP代理的FTPHTTPClient
			FTPHTTPClient ftpHttpClient = null;
			if(BlankUtils.isBlank(configuration.getHttpProxyUsername()) || BlankUtils.isBlank(configuration.getHttpProxyPassword())){
				ftpHttpClient = new FTPHTTPClient(configuration.getHttpProxyHost(),configuration.getHttpProxyPort());
			}else{
				ftpHttpClient = new FTPHTTPClient(configuration.getHttpProxyHost(),configuration.getHttpProxyPort(),configuration.getHttpProxyUsername(),configuration.getHttpProxyPassword());
			}
			//初始化FTPHTTPClient
			return this.initFTPClient(ftpHttpClient);
		}else{
			//普通的FTPClient
			FTPClient ftpClient = new FTPClient();
			//初始化FTPClient
			return this.initFTPClient(ftpClient);
		}
	}

	public void shutdown() throws IOException {

	}
	
	/**
	 * 
	 * @description	： 初始化FTPClient
	 * @author 		： kangzhidong
	 * @date 		：Jan 8, 2016 8:54:47 AM
	 * @param ftpClient
	 */
	public <T extends FTPClient> T initFTPClient(T ftpClient){
		
		try {
			//初始化基础参数
			ftpClient.configure(this);
			//设置将过程中使用到的命令输出到控制台 
			if(configuration.isPrintDebug()){
				ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			}else if(configuration.isLogDebug()){
				ftpClient.addProtocolCommandListener(new LoggerProtocolCommandListener(LOG));
			}
			//启用或禁用服务器自动编码检测（只支持UTF-8支持）
			ftpClient.setAutodetectUTF8(configuration.isAutodetectUTF8());
			//启用或禁用数据流方式上传/下载时是否在缓冲发送/接收完成立即将刷新缓存区
			ftpClient.setAutoFlush(configuration.isAutoFlush());
			//数据流方式上传/下载时缓存区达到可自动刷新的最小阀值；仅当 autoflush 为true 时才有效
			ftpClient.setAutoFlushBlockSize(configuration.getAutoFlushBlockSize());
			//为缓冲数据流而设置内部缓冲器区大小
			ftpClient.setBufferSize(configuration.getBufferSize());
			ftpClient.setBufferSize_(configuration.getBufferSize());
			//设置文件通道读取缓冲区大小
			ftpClient.setChannelReadBufferSize(configuration.getChannelReadBufferSize());
			//设置文件通道写出缓冲区大小
			ftpClient.setChannelWriteBufferSize(configuration.getChannelWriteBufferSize());
			//设置Socket使用的字符集
			ftpClient.setCharset(Charset.forName(configuration.getCharset()));
			//设置连接超时时间；参数将会传递给Socket对象的connect()方法
			ftpClient.setConnectTimeout(configuration.getConnectTimeout() );
			//设置FTP控制连接的编码方式(默认读取中文文件名时为乱码)
			ftpClient.setControlEncoding(configuration.getControlEncoding());
			//设置控制保活消息回复等待时间
			ftpClient.setControlKeepAliveReplyTimeout(configuration.getControlKeepAliveReplyTimeout()  );
			ftpClient.setControlKeepAliveReplyTimeout_(configuration.getControlKeepAliveReplyTimeout());
			//设置发送处理文件上载或下载时，控制连接保持活动消息之间的等待时间
			ftpClient.setControlKeepAliveTimeout(configuration.getControlKeepAliveTimeout()  );
			ftpClient.setControlKeepAliveTimeout_(configuration.getControlKeepAliveTimeout());
			//设置 TCP进行存储时/检索操作时keepalive连接监听对象
			ftpClient.setCopyStreamListener(configuration.getCopyStreamListener());
			ftpClient.setCopyStreamListener_(configuration.getCopyStreamListener());
			//TCP进行存储时/检索操作时数据处理进度监听对象
			if(!BlankUtils.isBlank(configuration.getCopyStreamProcessListenerName())){
				@SuppressWarnings("unchecked")
				Class<CopyStreamProcessListener> listenerClazz = (Class<CopyStreamProcessListener>) Class.forName(configuration.getCopyStreamProcessListenerName());
				ftpClient.setCopyStreamProcessListener((CopyStreamProcessListener) ConstructorUtils.invokeConstructor(listenerClazz));
			}else{
				ftpClient.setCopyStreamProcessListener(configuration.getCopyStreamProcessListener());
			}
			//设置超时时间以毫秒为单位使用时，从数据连接读
			ftpClient.setDataTimeout(configuration.getDataTimeout());
			//设置Socket默认端口
			ftpClient.setDefaultPort(configuration.getPort());
			//设置打开Socket连接超时时间
			ftpClient.setDefaultTimeout(configuration.getConnectTimeout());
			/**
			 * 您可以设置为true，如果你想获得隐藏的文件时listFiles(java.lang.String)了。
			 * 一个LIST -a会发出到FTP服务器。 这取决于您的FTP服务器，如果你需要调用这个方法，也不要期望得到消除隐藏文件，如果你调用“假”这个方法。
			 */
			ftpClient.setListHiddenFiles(configuration.isListHiddenFiles());
			//设置本地编码格式
			ftpClient.setLocalEncoding(configuration.getLocalEncoding());
			//问题的FTP MFMT命令（并非所有服务器都支持）中规定的最后修改文件的时间。
			//ftpClient.setModificationTime(pathname, timeval)
			if(!BlankUtils.isBlank(configuration.getParserFactory())){
				//设置用于创建FTPFileEntryParser对象的工厂
				ftpClient.setParserFactory(configuration.getParserFactory());
			}
			if(!BlankUtils.isBlank(configuration.getSocketProxy())){
				ftpClient.setProxy(configuration.getSocketProxy());
			}else{
				ftpClient.setSocketFactory(configuration.getSocketFactory());
			}
			//设置Socket底层用于接收数据的缓冲区大小,默认8KB
			ftpClient.setReceiveBufferSize(configuration.getReceiveBufferSize());
			//设置FTPClient接收数据的缓冲区大小,默认8KB
			ftpClient.setReceieveDataSocketBufferSize(configuration.getReceiveDataSocketBufferSize());
			//启用或禁用核实，利用远程主机的数据连接部分是作为控制连接到该连接的主机是相同的
			ftpClient.setRemoteVerificationEnabled(configuration.isRemoteVerificationEnabled());
			if(!BlankUtils.isBlank(configuration.getReportActiveExternalHost())){
				//设置主动模式下在报告EPRT/PORT命令时使用的外部IP地址；在多网卡下很有用
				ftpClient.setReportActiveExternalIPAddress(configuration.getReportActiveExternalHost());
			}
			//重置文件传输偏移量为0
			ftpClient.setRestartOffset(0);
			//设置Socket底层用于发送数据的缓冲区大小,默认8KB
			ftpClient.setSendBufferSize(configuration.getSendBufferSize());
			//设置FTPClient发送数据的缓冲区大小,默认8KB
			ftpClient.setSendDataSocketBufferSize(configuration.getSendDataSocketBufferSize());
			//设置SocketClient打开ServerSocket的连接ServerSocketFactory工厂
			ftpClient.setServerSocketFactory(configuration.getServerSocketFactory());
			//设置是否严格多行解析
			ftpClient.setStrictMultilineParsing(configuration.isStrictMultilineParsing());
			/**
			 * 设置是否使用与IPv4 EPSV。 也许值得在某些情况下启用。 例如，当使用IPv4和NAT它可能与某些罕见的配置。
			 * 例如，如果FTP服务器有一个静态的使用PASV地址（外部网）和客户端是来自另一个内部网络。
			 * 在这种情况下，PASV命令后，数据连接会失败，而EPSV将使客户获得成功，采取公正的端口。
			 */
			ftpClient.setUseEPSVwithIPv4(configuration.isUseEPSVwithIPv4());
			
			//绑定FTP参数对象
			ftpClient.setClientConfig(configuration);
			
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		return ftpClient;
	}
	
	
	public <T extends FTPClient> T initConnectionMode(T ftpClient){
		try {
			if(configuration.isRemotePassiveMode()){
				if(!BlankUtils.isBlank(configuration.getRemoteActiveHost()) && !BlankUtils.isBlank(configuration.getRemoteActivePort())){
					//设置远程被动模式下远程端IP地址和端口
					ftpClient.enterRemoteActiveMode(InetAddress.getByName(configuration.getRemoteActiveHost()), configuration.getRemoteActivePort());
				}else{
					ftpClient.enterRemotePassiveMode();
				}
			}else if (configuration.isLocalActiveMode()) {
				if(!BlankUtils.isBlank(configuration.getActiveExternalHost())){
					//设置在主动模式下的外部IP地址
					ftpClient.setActiveExternalIPAddress(configuration.getActiveExternalHost());
				}
				if(!BlankUtils.isBlank(configuration.getActiveMinPort()) && !BlankUtils.isBlank(configuration.getActiveMaxPort())){
					//设置在主动模式客户端端口范围
					ftpClient.setActivePortRange(configuration.getActiveMinPort(), configuration.getActiveMaxPort());
				}
				/**
				 * 在建立数据连接之前将数据连接模式设置为主动模式:
				 * 设置当前数据连接模式ACTIVE_LOCAL_DATA_CONNECTION_MODE 。 
				 * 没有与FTP服务器进行通信，但是这会导致所有将来的数据传输要求的FTP服务器连接到客户端的数据端口。 
				 * 此外，为了适应插座之间的差异在不同平台上实现，这种方法使客户端发出一个摆在每一个数据传输端口的命令。 
				 */
				ftpClient.enterLocalActiveMode();
			}else if (configuration.isLocalPassiveMode()) {
				if(!BlankUtils.isBlank(configuration.getPassiveLocalHost())){
					//设置在被动模式下使用的本地IP地址
					ftpClient.setPassiveLocalIPAddress(configuration.getPassiveLocalHost());
				}
				//启用或禁用在被动模式下使用NAT（Network Address Translation，网络地址转换）解决方案
				ftpClient.setPassiveNatWorkaround(configuration.isPassiveNatWorkaround());
				/**
				 * 在建立数据连接之前发送 PASV 命令至 FTP 站点，将数据连接模式设置为被动模式:
				 * 设置当前数据连接模式PASSIVE_LOCAL_DATA_CONNECTION_MODE 。
				 * 仅用于客户端和服务器之间的数据传输，此方法。 这种方法将导致使用PASV（或EPSV）命令发出到服务器之前，每一个数据连接孔， 告诉服务器来打开一个数据端口，客户端将连接进行数据传输。
				 * 该FTPClient将留在PASSIVE_LOCAL_DATA_CONNECTION_MODE直到模式是由其他的方法，例如改变调用一些enterLocalActiveMode()
				 * 注：目前可以调用任何方法将复位模式ACTIVE_LOCAL_DATA_CONNECTION_MODE。
				 */
				//设置FTPClient为被动传输模式即可解决线程挂起问题。此代码设置在登陆之后或者之前都可以。
				ftpClient.enterLocalPassiveMode();
			}
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		return ftpClient;
	}
	
	/**
	 * 
	 * @description	：  初始化已经与FTP服务器建立连接的FTPClient,因为有些参数需要在连接建立后进行通信或者需要socket对象初始化后才能设置
	 * @author 		： kangzhidong
	 * @date 		：Jan 14, 2016 9:25:57 AM
	 * @param <T>
	 * @param ftpClient
	 * @return
	 */
	public <T extends FTPClient> T initConnectedSocket(T ftpClient){
		
		try {
			
			//设置文件的结构
			ftpClient.setFileStructure(FTPConfigurationUtils.getFileStructure(configuration));
			//设置文件传输模式 
			ftpClient.setFileTransferMode(FTPConfigurationUtils.getFileTransferMode(configuration));
			//设置文件传输类型和传送格式
			//注：用FTPClient部署在Linux上出现下载的文件小于FTP服务器实际文件的问题解决方法是设置以二进制形式传输:ftpClient.setFileType(FTP.BINARY_FILE_TYPE); 
			ftpClient.setFileType(FTPConfigurationUtils.getFileTyle(configuration), FTPConfigurationUtils.getFileFormat(configuration));
			ftpClient.setFileType_(FTPConfigurationUtils.getFileTyle(configuration));
			//ftpClient.setFileType(FTPConfigurationUtils.getFileTyle(configuration));
			
			//是否保持连接
			ftpClient.setKeepAlive(configuration.isKeepAlive());
			/** 
             * 如果我们设置了linger而不小于0，那么close会等到发送的数据已经确认了才返回。 
             * 但是如果对方宕机，超时，那么会根据linger设定的时间返回。 
             * 有了后面三句，socket关闭后, 服务端也会收到信息
             * 
             * SO_LINGER 选项用来控制 Socket 关闭时的行为. 默认情况下, 执行 Socket 的 close() 方法, 该方法会立即返回, 
             * 但底层的 Socket 实际上并不立即关闭, 它会延迟一段时间, 直到发送完所有剩余的数据, 才会真正关闭 Socket, 断开连接.
			 * 如果执行以下方法:
			 * 	socket.setSoLinger(true, 0);                                                                                               
			 * 那么执行Socket 的close() 方法, 该方法也会立即返回, 并且底层的 Socket 也会立即关闭, 所有未发送完的剩余数据被丢弃.
			 * 如果执行以下方法:
			 *	socket.setSoLinger(true, 3600);                                                                                           
			 * 那么执行Socket 的 close() 方法, 该方法不会立即返回, 而是进入阻塞状态. 同时, 底层的 Socket 会尝试发送剩余的数据. 只有满足以下两个条件之一, close() 方法才返回:
			 *	      ⑴ 底层的 Socket 已经发送完所有的剩余数据;
			 *	      ⑵ 尽管底层的 Socket 还没有发送完所有的剩余数据, 但已经阻塞了 3600 秒(注意这里是秒, 而非毫秒), close() 方法的阻塞时间超过 3600 秒, 也会返回, 剩余未发送的数据被丢弃.
			 * 值得注意的是, 在以上两种情况内, 当close() 方法返回后, 底层的 Socket 会被关闭, 断开连接. 
			 * 此外, setSoLinger(boolean on, int seconds) 方法中的 seconds 参数以秒为单位, 而不是以毫秒为单位.    
			 * 如果未设置 SO_LINGER 选项, getSoLinger()  返回的结果是 -1, 
			 * 如果设置了 socket.setSoLinger(true, 80) , getSoLinger()  返回的结果是 80.
             */   
            if(configuration.isSolingerEnabled()){
            	//ftpClient.setSoLinger(true, 0) 表示 断开后及时释放端口
            	//启用/禁用具有指定逗留时间（以秒为单位）的 SO_LINGER。最大超时值是特定于平台的。 该设置仅影响套接字关闭
            	ftpClient.setSoLinger(configuration.isSolingerEnabled(), Math.min(0, configuration.getSolinger_timeout()));
            }
            ftpClient.setSoTimeout(configuration.getSo_timeout()  );
            //启用/禁用 TCP_NODELAY（启用/禁用 Nagle 算法）。  
			ftpClient.setTcpNoDelay(configuration.isTcpNoDelay());
			
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		return ftpClient;
	}
	
	/**
	 * 
	 * @description	： 初始化FTPSClient
	 * @author 		： kangzhidong
	 * @date 		：Jan 8, 2016 8:54:47 AM
	 * @param ftpClient
	 */
	public <T extends FTPSClient> T  initFTPSClient(T ftpsClient){
		
		try {
			
			//设置AUTH命令使用的值
			ftpsClient.setAuthValue(configuration.getAuth());
			//设置当前连接使用的特定密码组；服务器协商之前调用
			ftpsClient.setEnabledCipherSuites(StringUtils.tokenizeToStringArray(configuration.getEnabledCipherSuites()));
			//设置当前连接使用的特定协议组；服务器协商之前调用
			ftpsClient.setEnabledProtocols(StringUtils.tokenizeToStringArray(configuration.getEnabledProtocols()));
			//设置当前Socket是否可以创建一个新的SSL会话
			ftpsClient.setEnabledSessionCreation(configuration.isEnabledSessionCreation());
			//设置是否使用HTTPS终端自动检查算法。默认false。仅在客户端模式的连接进行此项检查（需Java1.7+）
			ftpsClient.setEndpointCheckingEnabled(configuration.isTlsEndpointChecking());
			//设置在客户端模式(post-TLS)下的连接使用的域名校验对象
			ftpsClient.setHostnameVerifier(configuration.getHostnameVerifier());
			//设置FTPS的KeyManager实现
			ftpsClient.setKeyManager(configuration.getKeyManager());
			//设置是否需要客户端身份验证
			ftpsClient.setNeedClientAuth(configuration.isNeedClientAuth());
			//解决在通过使用FTPSClient类进行sslftp的连接 可以连接成功,但list() ,listNames()或listFiles() 为null.
			if(!BlankUtils.isBlank(configuration.getSslSocketFactory())){
				ftpsClient.setSocketFactory(configuration.getSslSocketFactory());
			}else{
				ftpsClient.setSocketFactory(SSLSocketFactory.getDefault());
			}
			//设置FTPS的TrustManager实现；
			if(!BlankUtils.isBlank(configuration.getTrustManager())){
				ftpsClient.setTrustManager(configuration.getTrustManager());
			}else{
				ftpsClient.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
			}
			//设置是否使用客户端模式
			ftpsClient.setUseClientMode(configuration.isUseClientMode());
			//设置是否希望客户端身份验证
			ftpsClient.setWantClientAuth(configuration.isWantClientAuth());
			
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		return ftpsClient;
	}

	public FTPClientConfig getConfiguration() {
		return configuration;
	}
	
	
	
}
