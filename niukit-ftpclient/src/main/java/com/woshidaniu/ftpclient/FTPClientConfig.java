package com.woshidaniu.ftpclient;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.Proxy;
import java.util.Properties;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;

import com.woshidaniu.ftpclient.io.CopyStreamProcessListener;
import com.woshidaniu.ftpclient.utils.FTPConfigUtils;

/**
 * 
 * @className ： FTPClientConfig
 * @description ： FTP客户端的配置
 * @author ： kangzhidong
 * @date ： Dec 24, 2015 9:59:06 AM
 */
public class FTPClientConfig extends org.apache.commons.net.ftp.FTPClientConfig {

	/** 默认连接超时时间 ：30秒*/
	public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
	/** 默认数据连接读取/发送数据超时时间 ：30秒*/
	public static final int DEFAULT_DATA_TIMEOUT = 30 * 1000;
	/** 默认socket超时时间 ：5秒 */
	public static final int DEFAULT_SOCKET_TIMEOUT = 5 * 1000;
	/** 默认网络超时时间 ：60秒*/
	public static final int DEFAULT_NETWORK_TIMEOUT = 60 * 1000;
	/** 本地字符编码  ：GBK*/
	public static final String LOCAL_CHARSET = "GBK";
	/** FTP协议里面，规定文件名编码：ISO-8859-1 */
	public static final String SERVER_CHARSET = "ISO-8859-1";
	/** 匿名账号：anonymous */
	public static final String ANONYMOUS_LOGIN = "anonymous";
	/** 默认缓存大小： 8M*/
	public static final int DEFAULT_BUFFER_SIZE = 8 * 1024 * 1024;
	/** 默认FileChannel缓存大小： 2M*/
	public static final int DEFAULT_CHANNEL_SIZE = 2 * 1024 * 1024;
	/** 默认Socket缓存大小： 8KB*/
	public static final int DEFAULT_SOCKET_SIZE = 8 * 1024;
	
	//===============================================================================
	//=============FTPClient对象池配置===================================================
	//===============================================================================
	
	/** ftp服务器地址 */
	protected String host;
	/** ftp服务器端口 */
	protected int port = 21;
	/** ftp服务器用户名 */
	protected String username = ANONYMOUS_LOGIN;
	/** ftp服务器密码 */
	protected String password;
	/** ftp服务器根路径 */
	protected String rootdir;
	/** 在主动模式下的外部IP地址 */
	protected String activeExternalHost;
	/** 在主动模式客户端起始端口 */
	protected int activeMinPort;
	/** 在主动模式客户端最大端口 */
	protected int activeMaxPort;
	/** 启用或禁用服务器自动编码检测（只支持UTF-8支持）;默认false */
	protected boolean autodetectUTF8 = false;
	/** 启用或禁用数据流方式上传/下载时是否在缓冲发送/接收完成自动刷新缓存区；大文件上传下载时比较有用;默认false */
	protected boolean autoFlush = false;
	/** 数据流方式上传/下载时缓存区达到可自动刷新的最小阀值；仅当 autoflush 为true 时才有效；默认与默认缓存区大小相同即 8M*/
	protected int autoFlushBlockSize = DEFAULT_BUFFER_SIZE;
	/** 内部缓冲区大小;默认 8M */
	protected int bufferSize = DEFAULT_BUFFER_SIZE;
	/** 文件通道读取缓冲区大小;默认 2M */
	protected int channelReadBufferSize = DEFAULT_CHANNEL_SIZE;
	/** 文件通道写出缓冲区大小;默认 2M */
	protected int channelWriteBufferSize = DEFAULT_CHANNEL_SIZE;
	/** Socket使用的字符集;默认UTF-8 */
	protected String charset = "UTF-8";
	/** ftp客户端对象类型：FTPClient,FTPSClient,FTPHTTPClient;默认 FTPClient */
	protected String clientType = "FTPClient";
	/** 连接超时时间，单位为秒，默认30秒 */
	protected int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
	/** 服务端编码格式;默认ISO-8859-1 */
	protected String controlEncoding = SERVER_CHARSET;
	/** 控制保活消息回复等待时间,必须设置，防止长时间连接没响应，单位毫秒;默认1000毫秒；大多数FTP服务器不支持并发控制和数据连接使用 */
	protected int controlKeepAliveReplyTimeout = 1000;
	/** 发送处理文件上载或下载时，控制连接保持活动消息之间的等待时间，单位毫秒;默认1000毫秒 */
	protected long controlKeepAliveTimeout = 1000;
	/** TCP进行存储时/检索操作时keepalive连接监听对象 */
	protected CopyStreamListener copyStreamListener;
	/** TCP进行存储时/检索操作时数据处理进度监听对象 */
    protected CopyStreamProcessListener copyStreamProcessListener;
    /** TCP进行存储时/检索操作时数据处理进度监听对象类路径 */
    protected String copyStreamProcessListenerName;
	/** 从数据连接读取数据的 超时时间，单位（毫秒）；默认 30000 毫秒*/
	protected int dataTimeout = DEFAULT_DATA_TIMEOUT;
	/** 文件格式：telnet,carriage_control,non_print */
	protected String fileFormat;
	/** 文件结构：file,record,page */
	protected String fileStructure;
	/** 文件传输模式 ：stream,block,compressed */
	protected String fileTransferMode;
	/** 文件传输类型：ascii,ebcdic,binary,local;默认 binary;注：用FTPClient部署在Linux上出现下载的文件小于FTP服务器实际文件的问题解决方法是设置以二进制形式传输 */
	protected String fileType = "binary";
	/** ftp服务器显示风格 一般为unix 或者nt */
	protected String ftpStyle = "nt";
	/** 表示TCP是否监视连接是否有效,值为 false时不活动的客户端可能会永远存在下去, 而不会注意到服务器已经崩溃.默认值为 false 
	 *  keepalive不是说TCP的常连接，当我们作为服务端，一个客户端连接上来，如果设置了keeplive为true，当对方没有发送任何数据过来，
	 *  超过一个时间(看系统内核参数配置)，那么我们这边会发送一个ack探测包发到对方，探测双方的TCP/IP连接是否有效(对方可能断点，断网)。
	 *  如果不设置，那么客户端宕机时，服务器永远也不知道客户端宕机了，仍然保存这个失效的连接。
	 * */
	protected boolean keepAlive = false;
	/** 是否获取隐藏文件，如果想获得隐藏的文件则需要设置为true,默认false  */
	protected boolean listHiddenFiles = false;
	
	/** 是否本地备份上传的文件：该方式有助于提高文件服务可用性，为用户下载文件省去网络开销 */
	protected boolean localBackupable = false;
	/** 本地备份路径 ;默认userdir,如果开启了本地备份功能，建议指定该目录地址 */
	protected String localBackupDir = SystemUtils.getUserDir().getAbsolutePath();
	
	/** 是否本地主动模式 ;默认 true */
	protected boolean localActiveMode = false;
	/** 本地编码格式 ;默认GBK */
	protected String localEncoding = LOCAL_CHARSET;
	/** 是否本地被动模式 ,常用于服务器有防火墙的情况;默认 true */
	protected boolean localPassiveMode = false;
	/** 是否使用Log4j记录命令信息,默认打印出命令，如果开启日志则关闭打印;默认 false */
	protected boolean logDebug = false;
	/** 网络超时的时限，单位为毫秒，默认60000毫秒 */
	protected int networkTimeout = DEFAULT_NETWORK_TIMEOUT;
	/** 用于创建FTPFileEntryParser对象的工厂 */
	protected FTPFileEntryParserFactory parserFactory;
	/** 被动模式下使用的本地IP地址 */
	protected String passiveLocalHost;
	/** 启用或禁用在被动模式下使用NAT（Network Address Translation，网络地址转换）解决方案。默认true */
	protected boolean passiveNatWorkaround = true;
	/** 是否打印出FTP命令，默认 true */
	protected boolean printDebug = true;
	/**
	 * Socket用于接收数据的缓冲区大小,默认8KB.此值必须大于 0； 增大接收缓存大小可以增大大量连接的网络 I/O 的性能，而减小它有助于减少传入数据的 backlog。
	 * 表示 Socket 的用于输入数据的缓冲区的大小. 一般说来, 传输大的连续的数据块(基于HTTP 或 FTP 协议的通信)
	 * 可以使用较大的缓冲区, 这可以减少传输数据的次数, 提高传输数据的效率. 而对于交互频繁且单次传送数据量比较小的通信方式(Telnet 和
	 * 网络游戏), 则应该采用小的缓冲区, 确保小批量的数据能及时发送给对方. 这种设定缓冲区大小的原则也同样适用于 Socket 的
	 * SO_SNDBUF 选项. 如果底层 Socket 不支持 SO_RCVBUF 选项, 那么 setReceiveBufferSize()
	 * 方法会抛出 SocketException.
	 */
	protected int receiveBufferSize = DEFAULT_SOCKET_SIZE;
	/** FTPClient接收数据的缓冲区大小,默认8KB. */
	protected int receiveDataSocketBufferSize = DEFAULT_SOCKET_SIZE;
	/** 远程被动模式远程端IP地址 */
	protected String remoteActiveHost;
	/** 远程被动模式远程端端口 */
	protected int remoteActivePort;
	/**
	 * 是否远程被动模式 ;默认 false; 仅用于服务器到服务器的数据传输此方法。
	 * 这种方法发出PASV命令到服务器，告诉它打开一个数据端口的活动服务器将连接进行数据传输。 您必须调用这个摆在每一个服务器到服务器传输尝试的方法。
	 * 该FTPClient不会自动继续发行PASV命令。 你还必须记住调用enterLocalActiveMode()如果你想返回到正常的数据连接模式。
	 */
	protected boolean remotePassiveMode = true;
	/** 启用或禁用核实，利用远程主机的数据连接部分是作为控制连接到该连接的主机是相同的；默认true */
	protected boolean remoteVerificationEnabled = true;
	/** 主动模式下在报告EPRT/PORT命令时使用的外部IP地址；在多网卡下很有用 */
	protected String reportActiveExternalHost;
	/** Socket发送数据的缓冲区大小,默认8KB. 如果底层 Socket 不支持 SO_SNDBUF 选项,setSendBufferSize() 方法会抛出 SocketException. 
	 * 计算TCP缓冲区大小
	 * 假设没有网络拥塞和丢包，则网络吞吐量直接和TCP缓冲区大小和网络延迟有关。网络延迟是一个包在网络中传输所用的时间。计算出吞吐量为：
	 * 吞吐量 = 缓冲区大小 / 网络延迟
	 * 举例来说，从Sunnyvale 到Reston 的网络延迟为40ms，windowsXP的默认TCP缓冲区为17520bytes，那么
	 * 
	 *     17520 bytes / 0.04 seconds = 3.5 Mbits / second
	 *     Mac OS X 是64K，所以其能达到 65936 B / 0.04 s = 13Mb / s
	 * 
	 * 大多数网络专家认为较优的TCP缓冲区大小是网络的两倍延迟(delay times)乘以带宽
	 * buffer size = 2 * delay * bandwidth
	 * 使用ping命令可以得到一次RTT时间，也就是2倍延迟，那么
	 * buffer size = RTT * bandwidth
	 * 继续上面例子，ping返回的值为80ms，所以TCP缓冲区大小应该为
	 * 0.08s * 100Mbps / 8 = 1MByte
	 */
	protected int sendBufferSize = DEFAULT_SOCKET_SIZE;
	/** FTPClient发送数据的缓冲区大小,默认8KB. */
	protected int sendDataSocketBufferSize = DEFAULT_SOCKET_SIZE;
	/** SocketClient打开ServerSocket的连接ServerSocketFactory工厂 */
	protected ServerSocketFactory serverSocketFactory;
	/**
	 * Socket调用InputStream 读数据的等待超时时间，以毫秒为单位, 默认5秒，值为 0表示会无限等待, 永远不会超时.
	 * 如果超过这个时候，会抛出java.net.SocketTimeoutException。
	 * 当输入流的read方法被阻塞时，如果设置timeout（timeout的单位是毫秒），那么系统在等待了timeout毫秒后会抛出一个InterruptedIOException例外。
	 * 在抛出例外后，输入流并未关闭，你可以继续通过read方法读取数据。
	 * 当底层的Socket实现不支持SO_TIMEOUT选项时，这两个方法将抛出SocketException例外。
	 * 不能将timeout设为负数，否则setSoTimeout方法将抛出IllegalArgumentException例外
	 */
	protected int so_timeout = DEFAULT_SOCKET_TIMEOUT;
	
	
	/** Socket创建工厂*/
	protected SocketFactory socketFactory;
	/** Socket代理对象*/
	protected Proxy socketProxy;
	/** Socket关闭后，SO_LINGER 延迟关闭时间;单位毫秒，默认0*/
	protected int solinger_timeout = 0;
	/** 启用/禁用SO_LINGER延迟关闭 */
	protected boolean solingerEnabled = true;
	/** 是否严格多行解析；默认false */
	protected boolean strictMultilineParsing = false;
	/** 设置FTPClient是否使用NoDelay策略;默认 true;nagle算法默认是打开的，会引起delay的问题；所以要手工关掉 */
	protected boolean tcpNoDelay = false;
	/**
	 * 设置是否使用与IPv4 EPSV。 也许值得在某些情况下启用。 例如，当使用IPv4和NAT它可能与某些罕见的配置。
	 * 例如，如果FTP服务器有一个静态的使用PASV地址（外部网）和客户端是来自另一个内部网络。
	 * 在这种情况下，PASV命令后，数据连接会失败，而EPSV将使客户获得成功，采取公正的端口。
	 */
	protected boolean useEPSVwithIPv4 = false;
	
	//===============================================================================
	//=============FTPSClient对象池配置==================================================
	//===============================================================================

	/** 安全模式. （True - 隐式模式/False - 显性模式） */
	protected boolean isImplicit = false;
	/** 安全的Socket使用的协议，使用SSL或TLS;默认 TLS. */
	protected String protocol = "TLS";
	/** AUTH命令使用的值;默认TLS */
	protected String auth = "TLS";
	/** SSLContext对象. */
	protected SSLContext sslContext;
	/** 当前Socket是否可以创建一个新的SSL会话；默认true */
	protected boolean enabledSessionCreation = true;
	/** 是否使用客户端模式；默认true. */
	protected boolean useClientMode = true;
	/** 是否需要客户端身份验证；默认false. */
	protected boolean needClientAuth = false;
	/** 是否希望客户端身份验证；默认false. */
	protected boolean wantClientAuth = false;
	/** 当前连接使用的特定密码组，多个使用 ",; \t\n"分割；服务器协商之前调用 */
	protected String enabledCipherSuites = null;
	/** 当前连接使用的特定协议组，多个使用 ",; \t\n"分割；服务器协商之前调用 */
	protected String enabledProtocols = null;
	/** FTPS的TrustManager实现；默认使用 {@link TrustManagerUtils#getValidateServerCertificateTrustManager()}. */
	protected TrustManager trustManager = TrustManagerUtils.getValidateServerCertificateTrustManager();
	/** FTPS的KeyManager实现，默认null使用系统默认. */
	protected KeyManager keyManager = null;
	/** 在客户端模式(post-TLS)下的连接使用的域名校验对象，默认为null表示不校验  */
	protected HostnameVerifier hostnameVerifier = null;
	/** 是否使用HTTPS终端自动检查算法。默认false。仅在客户端模式的连接进行此项检查（需Java1.7+） */
	protected boolean tlsEndpointChecking = false;
	/** 加密Socket创建工厂 */
	protected SSLSocketFactory sslSocketFactory = null;

	//===============================================================================
	//=============FTPHTTPClient参数配置================================================
	//===============================================================================

	/** HTTP代理主机IP地址 */
	protected String httpProxyHost;
	/** HTTP代理主机端口 */
	protected int httpProxyPort;
	/** HTTP代理主机账户名 */
	protected String httpProxyUsername;
	/** HTTP代理主机密码 */
	protected String httpProxyPassword;

	/**
	 * Convenience constructor mainly for use in testing. Constructs a UNIX
	 * configuration.
	 */
	public FTPClientConfig() {
		super();
	}

	/**
	 * The main constructor for an FTPClientConfig object
	 * 
	 * @param systemKey
	 *            key representing system type of the server being connected to. See
	 *            {@link #getServerSystemKey() serverSystemKey} If set to the empty
	 *            string, then FTPClient uses the system type returned by the
	 *            server. However this is not recommended for general use; the
	 *            correct system type should be set if it is known.
	 */
	public FTPClientConfig(String systemKey) {
		super(systemKey);
	}


	/**
	 * Constructor which allows setting of most member fields
	 * 
	 * @param systemKey
	 *            key representing system type of the server being connected to. See
	 *            {@link #getServerSystemKey() serverSystemKey}
	 * @param defaultDateFormatStr
	 *            See {@link #setDefaultDateFormatStr(String) defaultDateFormatStr}
	 * @param recentDateFormatStr
	 *            See {@link #setRecentDateFormatStr(String) recentDateFormatStr}
	 * @param serverLanguageCode
	 *            See {@link #setServerLanguageCode(String) serverLanguageCode}
	 * @param shortMonthNames
	 *            See {@link #setShortMonthNames(String) shortMonthNames}
	 * @param serverTimeZoneId
	 *            See {@link #setServerTimeZoneId(String) serverTimeZoneId}
	 */
	public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr,
			String serverLanguageCode, String shortMonthNames, String serverTimeZoneId) {
		super(systemKey, defaultDateFormatStr, recentDateFormatStr, serverLanguageCode, shortMonthNames,
				serverTimeZoneId);
	}

	/**
	 * Constructor which allows setting of all member fields
	 * 
	 * @param systemKey
	 *            key representing system type of the server being connected to. See
	 *            {@link #getServerSystemKey() serverSystemKey}
	 * @param defaultDateFormatStr
	 *            See {@link #setDefaultDateFormatStr(String) defaultDateFormatStr}
	 * @param recentDateFormatStr
	 *            See {@link #setRecentDateFormatStr(String) recentDateFormatStr}
	 * @param serverLanguageCode
	 *            See {@link #setServerLanguageCode(String) serverLanguageCode}
	 * @param shortMonthNames
	 *            See {@link #setShortMonthNames(String) shortMonthNames}
	 * @param serverTimeZoneId
	 *            See {@link #setServerTimeZoneId(String) serverTimeZoneId}
	 * @param lenientFutureDates
	 *            See {@link #setLenientFutureDates(boolean) lenientFutureDates}
	 * @param saveUnparseableEntries
	 *            See {@link #setUnparseableEntries(boolean) saveUnparseableEntries}
	 */
	public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr,
			String serverLanguageCode, String shortMonthNames, String serverTimeZoneId, boolean lenientFutureDates,
			boolean saveUnparseableEntries) {
		super(systemKey, defaultDateFormatStr, recentDateFormatStr, serverLanguageCode, shortMonthNames,
				serverTimeZoneId, lenientFutureDates, saveUnparseableEntries);
	}
	
	public FTPClientConfig(Properties properties){
		super();
		FTPConfigUtils.initPropertiesConfig(this, properties);
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	/**
	 * @return the rootdir
	 */
	public String getRootdir() {
		return rootdir;
	}

	/**
	 * @param rootdir the rootdir to set
	 */
	public void setRootdir(String rootdir) {
		this.rootdir = rootdir;
	}

	public String getActiveExternalHost() {
		return activeExternalHost;
	}

	public void setActiveExternalHost(String activeExternalHost) {
		this.activeExternalHost = activeExternalHost;
	}

	public int getActiveMinPort() {
		return activeMinPort;
	}

	public void setActiveMinPort(int activeMinPort) {
		this.activeMinPort = activeMinPort;
	}

	public int getActiveMaxPort() {
		return activeMaxPort;
	}

	public void setActiveMaxPort(int activeMaxPort) {
		this.activeMaxPort = activeMaxPort;
	}

	public boolean isAutodetectUTF8() {
		return autodetectUTF8;
	}

	public void setAutodetectUTF8(boolean autodetectUTF8) {
		this.autodetectUTF8 = autodetectUTF8;
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

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public String getControlEncoding() {
		return controlEncoding;
	}

	public void setControlEncoding(String controlEncoding) {
		this.controlEncoding = controlEncoding;
	}

	public int getControlKeepAliveReplyTimeout() {
		return controlKeepAliveReplyTimeout;
	}

	public void setControlKeepAliveReplyTimeout(int controlKeepAliveReplyTimeout) {
		this.controlKeepAliveReplyTimeout = controlKeepAliveReplyTimeout;
	}

	public long getControlKeepAliveTimeout() {
		return controlKeepAliveTimeout;
	}

	public void setControlKeepAliveTimeout(long controlKeepAliveTimeout) {
		this.controlKeepAliveTimeout = controlKeepAliveTimeout;
	}

	public CopyStreamListener getCopyStreamListener() {
		return copyStreamListener;
	}

	public void setCopyStreamListener(CopyStreamListener copyStreamListener) {
		this.copyStreamListener = copyStreamListener;
	}
	
	public CopyStreamProcessListener getCopyStreamProcessListener() {
		return copyStreamProcessListener;
	}

	public void setCopyStreamProcessListener(CopyStreamProcessListener copyStreamProcessListener) {
		this.copyStreamProcessListener = copyStreamProcessListener;
	}
	
	public String getCopyStreamProcessListenerName() {
		return copyStreamProcessListenerName;
	}

	public void setCopyStreamProcessListenerName(
			String copyStreamProcessListenerName) {
		this.copyStreamProcessListenerName = copyStreamProcessListenerName;
	}

	public int getDataTimeout() {
		return dataTimeout;
	}

	public void setDataTimeout(int dataTimeout) {
		this.dataTimeout = dataTimeout;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	
	public String getFileStructure() {
		return fileStructure;
	}

	public void setFileStructure(String fileStructure) {
		this.fileStructure = fileStructure;
	}

	public String getFileTransferMode() {
		return fileTransferMode;
	}

	public void setFileTransferMode(String fileTransferMode) {
		this.fileTransferMode = fileTransferMode;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFtpStyle() {
		return ftpStyle;
	}

	public void setFtpStyle(String ftpStyle) {
		this.ftpStyle = ftpStyle;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isListHiddenFiles() {
		return listHiddenFiles;
	}

	public void setListHiddenFiles(boolean listHiddenFiles) {
		this.listHiddenFiles = listHiddenFiles;
	}

	public boolean isLocalBackupable() {
		return localBackupable;
	}

	public void setLocalBackupable(boolean localBackupable) {
		this.localBackupable = localBackupable;
	}

	public String getLocalBackupDir() {
		return localBackupDir;
	}

	public void setLocalBackupDir(String localBackupDir) {
		this.localBackupDir = localBackupDir;
	}

	public boolean isLocalActiveMode() {
		return localActiveMode;
	}

	public void setLocalActiveMode(boolean localActiveMode) {
		this.localActiveMode = localActiveMode;
	}

	public String getLocalEncoding() {
		return localEncoding;
	}

	public void setLocalEncoding(String localEncoding) {
		this.localEncoding = localEncoding;
	}

	public boolean isLocalPassiveMode() {
		return localPassiveMode;
	}

	public void setLocalPassiveMode(boolean localPassiveMode) {
		this.localPassiveMode = localPassiveMode;
	}

	public boolean isLogDebug() {
		return logDebug;
	}

	public void setLogDebug(boolean logDebug) {
		this.logDebug = logDebug;
	}

	public int getNetworkTimeout() {
		return networkTimeout;
	}

	public void setNetworkTimeout(int networkTimeout) {
		this.networkTimeout = networkTimeout;
	}

	public FTPFileEntryParserFactory getParserFactory() {
		return parserFactory;
	}

	public void setParserFactory(FTPFileEntryParserFactory parserFactory) {
		this.parserFactory = parserFactory;
	}
 
	public String getPassiveLocalHost() {
		return passiveLocalHost;
	}

	public void setPassiveLocalHost(String passiveLocalHost) {
		this.passiveLocalHost = passiveLocalHost;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	public int getReceiveDataSocketBufferSize() {
		return receiveDataSocketBufferSize;
	}

	public void setReceiveDataSocketBufferSize(int receiveDataSocketBufferSize) {
		this.receiveDataSocketBufferSize = receiveDataSocketBufferSize;
	}

	public String getRemoteActiveHost() {
		return remoteActiveHost;
	}

	public void setRemoteActiveHost(String remoteActiveHost) {
		this.remoteActiveHost = remoteActiveHost;
	}

	public int getRemoteActivePort() {
		return remoteActivePort;
	}

	public void setRemoteActivePort(int remoteActivePort) {
		this.remoteActivePort = remoteActivePort;
	}

	public boolean isRemotePassiveMode() {
		return remotePassiveMode;
	}

	public void setRemotePassiveMode(boolean remotePassiveMode) {
		this.remotePassiveMode = remotePassiveMode;
	}

	public boolean isRemoteVerificationEnabled() {
		return remoteVerificationEnabled;
	}

	public void setRemoteVerificationEnabled(boolean remoteVerificationEnabled) {
		this.remoteVerificationEnabled = remoteVerificationEnabled;
	}

	public String getReportActiveExternalHost() {
		return reportActiveExternalHost;
	}

	public void setReportActiveExternalHost(String reportActiveExternalHost) {
		this.reportActiveExternalHost = reportActiveExternalHost;
	}

	public int getSendBufferSize() {
		return sendBufferSize;
	}

	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}

	public int getSendDataSocketBufferSize() {
		return sendDataSocketBufferSize;
	}

	public void setSendDataSocketBufferSize(int sendDataSocketBufferSize) {
		this.sendDataSocketBufferSize = sendDataSocketBufferSize;
	}

	public ServerSocketFactory getServerSocketFactory() {
		return serverSocketFactory;
	}

	public void setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
		this.serverSocketFactory = serverSocketFactory;
	}

	public int getSo_timeout() {
		return so_timeout;
	}

	public void setSo_timeout(int soTimeout) {
		so_timeout = soTimeout;
	}

	public SocketFactory getSocketFactory() {
		return socketFactory;
	}

	public void setSocketFactory(SocketFactory socketFactory) {
		this.socketFactory = socketFactory;
	}

	public Proxy getSocketProxy() {
		return socketProxy;
	}

	public void setSocketProxy(Proxy socketProxy) {
		this.socketProxy = socketProxy;
	}

	public int getSolinger_timeout() {
		return solinger_timeout;
	}

	public void setSolinger_timeout(int solingerTimeout) {
		solinger_timeout = solingerTimeout;
	}

	public boolean isSolingerEnabled() {
		return solingerEnabled;
	}

	public void setSolingerEnabled(boolean solingerEnabled) {
		this.solingerEnabled = solingerEnabled;
	}

	public boolean isStrictMultilineParsing() {
		return strictMultilineParsing;
	}

	public void setStrictMultilineParsing(boolean strictMultilineParsing) {
		this.strictMultilineParsing = strictMultilineParsing;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public boolean isUseEPSVwithIPv4() {
		return useEPSVwithIPv4;
	}

	public void setUseEPSVwithIPv4(boolean useEPSVwithIPv4) {
		this.useEPSVwithIPv4 = useEPSVwithIPv4;
	}

	public boolean isPassiveNatWorkaround() {
		return passiveNatWorkaround;
	}

	public void setPassiveNatWorkaround(boolean passiveNatWorkaround) {
		this.passiveNatWorkaround = passiveNatWorkaround;
	}

	public boolean isPrintDebug() {
		return printDebug;
	}

	public void setPrintDebug(boolean printDebug) {
		this.printDebug = printDebug;
	}

	public boolean isImplicit() {
		return isImplicit;
	}

	public void setImplicit(boolean isImplicit) {
		this.isImplicit = isImplicit;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	public boolean isEnabledSessionCreation() {
		return enabledSessionCreation;
	}

	public void setEnabledSessionCreation(boolean enabledSessionCreation) {
		this.enabledSessionCreation = enabledSessionCreation;
	}

	public boolean isUseClientMode() {
		return useClientMode;
	}

	public void setUseClientMode(boolean useClientMode) {
		this.useClientMode = useClientMode;
	}

	public boolean isNeedClientAuth() {
		return needClientAuth;
	}

	public void setNeedClientAuth(boolean needClientAuth) {
		this.needClientAuth = needClientAuth;
	}

	public boolean isWantClientAuth() {
		return wantClientAuth;
	}

	public void setWantClientAuth(boolean wantClientAuth) {
		this.wantClientAuth = wantClientAuth;
	}

	public String getEnabledCipherSuites() {
		return enabledCipherSuites;
	}

	public void setEnabledCipherSuites(String enabledCipherSuites) {
		this.enabledCipherSuites = enabledCipherSuites;
	}

	public String getEnabledProtocols() {
		return enabledProtocols;
	}

	public void setEnabledProtocols(String enabledProtocols) {
		this.enabledProtocols = enabledProtocols;
	}

	public TrustManager getTrustManager() {
		return trustManager;
	}

	public void setTrustManager(TrustManager trustManager) {
		this.trustManager = trustManager;
	}

	public KeyManager getKeyManager() {
		return keyManager;
	}

	public void setKeyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
	}

	public HostnameVerifier getHostnameVerifier() {
		return hostnameVerifier;
	}

	public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
	}

	public boolean isTlsEndpointChecking() {
		return tlsEndpointChecking;
	}

	public void setTlsEndpointChecking(boolean tlsEndpointChecking) {
		this.tlsEndpointChecking = tlsEndpointChecking;
	}

	public SSLSocketFactory getSslSocketFactory() {
		return sslSocketFactory;
	}

	public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}

	public String getHttpProxyHost() {
		return httpProxyHost;
	}

	public void setHttpProxyHost(String httpProxyHost) {
		this.httpProxyHost = httpProxyHost;
	}

	public int getHttpProxyPort() {
		return httpProxyPort;
	}

	public void setHttpProxyPort(int httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}

	public String getHttpProxyUsername() {
		return httpProxyUsername;
	}

	public void setHttpProxyUsername(String httpProxyUsername) {
		this.httpProxyUsername = httpProxyUsername;
	}

	public String getHttpProxyPassword() {
		return httpProxyPassword;
	}

	public void setHttpProxyPassword(String httpProxyPassword) {
		this.httpProxyPassword = httpProxyPassword;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("FTPClientConfig [");
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propDes : propertyDescriptors) {
				// 如果是class属性 跳过
				if ( propDes.getName().equals("class") || propDes.getReadMethod() == null || propDes.getWriteMethod() == null) {
					continue;
				}
				builder.append(propDes.getName()).append("=").append(propDes.getReadMethod().invoke(this)).append("\n ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		builder.append("]");
		return builder.toString();
	}
}
