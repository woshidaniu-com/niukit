#作者:大康
#日期:2016-01-14

#===============================================================================
#=============FTPResourceRequestFilter参数配置====================================================
#===============================================================================

#文件本地存储路径
ftpclient.web.tmpdir = tmpdir
#是否缓存FTP文件到本地存储路径 -->
ftpclient.web.cacheLocal = false
#FTP文件在本地缓存的时间;默认10分钟-->
ftpclient.web.cacheExpiry = 600000
#请求过滤前缀;默认 /ftp/ -->
ftpclient.web.requestPrefix = /ftp/


#===============================================================================
#=============FTPClient参数配置====================================================
#===============================================================================

#ftp服务器地址
ftpclient.host = 10.71.33.212
#ftp服务器端口
ftpclient.port = 21
#ftp服务器用户名
ftpclient.username = www
#ftp服务器密码
ftpclient.password = woshidaniu
#ftp服务器根路径
ftpclient.rootdir = /u01/ftpdir
#在主动模式下的外部IP地址
ftpclient.activeExternalHost = 
#在主动模式客户端起始端口
ftpclient.activeMinPort = 
#在主动模式客户端最大端口
ftpclient.activeMaxPort = 
#启用或禁用服务器自动编码检测（只支持UTF-8支持）;默认false 
ftpclient.autodetectUTF8 = false
#启用或禁用数据流方式上传/下载时是否在缓冲发送/接收完成自动刷新缓存区；大文件上传下载时比较有用;默认false
ftpclient.autoFlush = false
#数据流方式上传/下载时缓存区达到可自动刷新的最小阀值；仅当 autoflush 为true 时才有效；默认与默认缓存区大小相同即 8M
ftpclient.autoFlushBlockSize = 8192
#为缓冲数据流而设置内部缓冲器区大小;默认 8M
ftpclient.bufferSize = 8388608
#文件通道读取缓冲区大小;默认 2M
ftpclient.channelReadBufferSize = 2097152
#文件通道写出缓冲区大小;默认 2M
ftpclient.channelWriteBufferSize = 2097152
#Socket使用的字符集;默认UTF-8
ftpclient.charset = UTF-8
#ftp客户端对象类型：FTPClient,FTPSClient,FTPHTTPClient
ftpclient.clientType=FTPClient
#连接超时时间，单位为毫秒，默认30000毫秒
ftpclient.connectTimeout = 30000
#服务端编码格式;默认ISO-8859-1
ftpclient.controlEncoding = ISO-8859-1
#控制保活消息回复等待时间,必须设置，防止长时间连接没响应，单位毫秒;默认1000毫秒；大多数FTP服务器不支持并发控制和数据连接使用
ftpclient.controlKeepAliveReplyTimeout = 1000
#发送处理文件上载或下载时，控制连接保持活动消息之间的等待时间，单位毫秒;默认1000毫秒
ftpclient.controlKeepAliveTimeout = 1000
#TCP进行存储时/检索操作时数据处理进度监听对象类路径
ftpclient.copyStreamProcessListenerName = com.woshidaniu.ftpclient.io.PrintCopyStreamProcessListener
#从数据连接读取数据的 超时时间，单位（毫秒）；默认 30000 毫秒
ftpclient.dataTimeout = 30000
#文件格式：telnet,carriage_control,non_print
ftpclient.fileFormat = telnet
#文件结构：file,record,page
ftpclient.fileStructure = file
#文件传输模式 ：stream,block,compressed
ftpclient.fileTransferMode = stream
#文件传输类型：ascii,ebcdic,binary,local;默认 binary;注：用FTPClient部署在Linux上出现下载的文件小于FTP服务器实际文件的问题解决方法是设置以二进制形式传输
ftpclient.fileType = binary
#ftp服务器显示风格 一般为unix 或者nt
ftpclient.ftpStyle = unix
#表示TCP是否监视连接是否有效,值为 false时不活动的客户端可能会永远存在下去, 而不会注意到服务器已经崩溃.默认值为 false
ftpclient.keepAlive = true
#是否获取隐藏文件，如果想获得隐藏的文件则需要设置为true,默认false
ftpclient.listHiddenFiles = false
#是否本地主动模式 ;默认 true
ftpclient.localActiveMode = false
#本地编码格式 ;默认GBK
ftpclient.localEncoding= UTF-8
#是否本地被动模式 ,常用于服务器有防火墙的情况;默认 true
ftpclient.localPassiveMode = true
#是否使用Log4j记录命令信息,默认打印出命令，如果开启日志则关闭打印;默认 false
ftpclient.logDebug = true
#网络超时的时限，单位为毫秒，默认60000毫秒 
ftpclient.networkTimeout = 60000
#被动模式下使用的本地IP地址
ftpclient.passiveLocalHost = 
#启用或禁用在被动模式下使用NAT（Network Address Translation，网络地址转换）解决方案。默认true
ftpclient.passiveNatWorkaround = false
#是否打印出FTP命令，默认 true
ftpclient.printDebug = true
#Socket用于接收数据的缓冲区大小,默认是8KB.此值必须大于 0； 增大接收缓存大小可以增大大量连接的网络 I/O 的性能，而减小它有助于减少传入数据的 backlog。
#计算TCP缓冲区大小
#假设没有网络拥塞和丢包，则网络吞吐量直接和TCP缓冲区大小和网络延迟有关。网络延迟是一个包在网络中传输所用的时间。计算出吞吐量为：
#吞吐量 = 缓冲区大小 / 网络延迟
#举例来说，从Sunnyvale 到Reston 的网络延迟为40ms，windowsXP的默认TCP缓冲区为17520bytes，那么
#17520 bytes / 0.04 seconds = 3.5 Mbits / second
#Mac OS X 是64K，所以其能达到 65936 B / 0.04 s = 13Mb / s
#大多数网络专家认为较优的TCP缓冲区大小是网络的两倍延迟(delay times)乘以带宽
#buffer size = 2 * delay * bandwidth
#使用ping命令可以得到一次RTT时间，也就是2倍延迟，那么
#buffer size = RTT * bandwidth
#继续上面例子，ping返回的值为80ms，所以TCP缓冲区大小应该为
#0.08s * 100Mbps / 8 = 1MByte
ftpclient.receiveBufferSize = 8192
#FTPClient接收数据的缓冲区大小,默认是8KB.
ftpclient.receiveDataSocketBufferSize = 8192
#远程被动模式远程端IP地址 
ftpclient.remoteActiveHost = 
#远程被动模式远程端端口
ftpclient.remoteActivePort = 
#是否远程被动模式 ;默认 false; 仅用于服务器到服务器的数据传输此方法。
#这种方法发出PASV命令到服务器，告诉它打开一个数据端口的活动服务器将连接进行数据传输。 您必须调用这个摆在每一个服务器到服务器传输尝试的方法。
#该FTPClient不会自动继续发行PASV命令。 你还必须记住调用enterLocalActiveMode()如果你想返回到正常的数据连接模式。
ftpclient.remotePassiveMode = false
#启用或禁用核实，利用远程主机的数据连接部分是作为控制连接到该连接的主机是相同的；默认true
ftpclient.remoteVerificationEnabled = false
#主动模式下在报告EPRT/PORT命令时使用的外部IP地址；在多网卡下很有用
ftpclient.reportActiveExternalHost =
#Socket发送数据的缓冲区大小,,默认是8KB. 如果底层 Socket 不支持 SO_SNDBUF 选项,setSendBufferSize() 方法会抛出 SocketException.
ftpclient.sendBufferSize = 8192
#FTPClient发送数据的缓冲区大小,默认是8KB.
ftpclient.sendDataSocketBufferSize = 8192
#Socket调用InputStream 读数据的等待超时时间，以毫秒为单位, 默认5秒，值为 0表示会无限等待, 永远不会超时.
#如果超过这个时候，会抛出java.net.SocketTimeoutException。
#当输入流的read方法被阻塞时，如果设置timeout（timeout的单位是毫秒），那么系统在等待了timeout毫秒后会抛出一个InterruptedIOException例外。
#在抛出例外后，输入流并未关闭，你可以继续通过read方法读取数据。
#当底层的Socket实现不支持SO_TIMEOUT选项时，这两个方法将抛出SocketException例外。
#不能将timeout设为负数，否则setSoTimeout方法将抛出IllegalArgumentException例外
ftpclient.so_timeout = 10000
#Socket关闭后，SO_LINGER 延迟关闭时间;单位毫秒，默认0
ftpclient.solinger_timeout = 0
#启用/禁用SO_LINGER延迟关闭
ftpclient.solingerEnabled = true
#是否严格多行解析；默认false
ftpclient.strictMultilineParsing = false
#FTPClient是否使用NoDelay策略;默认 true;nagle算法默认是打开的，会引起delay的问题；所以要手工关掉
ftpclient.tcpNoDelay = false
#设置是否使用与IPv4 EPSV。 也许值得在某些情况下启用。 例如，当使用IPv4和NAT它可能与某些罕见的配置。 
#例如，如果FTP服务器有一个静态的使用PASV地址（外部网）和客户端是来自另一个内部网络。 在这种情况下，PASV命令后，数据连接会失败，而EPSV将使客户获得成功，采取公正的端口。 
ftpclient.useEPSVwithIPv4 = false

#===============================================================================
#=============FTPSClient参数配置===================================================
#===============================================================================

#安全模式. （True - 隐式模式/False - 显性模式）
ftpsclient.isImplicit = false
#安全的Socket使用的协议，使用SSL或TLS;默认 TLS
ftpsclient.protocol = TLS
#AUTH命令使用的值;默认TLS
ftpsclient.auth = TLS
#当前Socket是否可以创建一个新的SSL会话；默认true
ftpsclient.enabledSessionCreation = true
#是否使用客户端模式；默认true
ftpsclient.useClientMode = true
#是否需要客户端身份验证；默认false
ftpsclient.needClientAuth = false
#是否希望客户端身份验证；默认false
ftpsclient.wantClientAuth = false
#当前连接使用的特定密码组，多个使用 ",; \t\n"分割；服务器协商之前调用 ;如 123,124
ftpsclient.enabledCipherSuites = 
#当前连接使用的特定协议组，多个使用 ",; \t\n"分割；服务器协商之前调用;如TLS,SSL
ftpsclient.enabledProtocols = 
#是否使用HTTPS终端自动检查算法。默认false。仅在客户端模式的连接进行此项检查（需Java1.7+）
ftpsclient.tlsEndpointChecking = false

#===============================================================================
#=============FTPHTTPClient参数配置================================================
#===============================================================================

#HTTP代理主机IP地址
ftpclient.httpProxyHost =
#HTTP代理主机端口  
ftpclient.httpProxyPort = 
#HTTP代理主机账户名
ftpclient.httpProxyUsername =
#HTTP代理主机密码  
ftpclient.httpProxyPassword = 

#===============================================================================
#=============FTPClient对象池配置===================================================
#===============================================================================
 
#maxActive" -> "maxTotal" and "maxWait" -> "maxWaitMillis
#borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；是否启用后进先出, 默认true
ftpclient.pool.lifo = true
#连接池中最少空闲的连接数,默认为0.
ftpclient.pool.minIdle = 0
#最大能够保持idel状态的对象数
#控制一个pool最多有多少个状态为idle的ftpclient实例；
ftpclient.pool.maxIdle = 8 
#控制一个pool可分配多少个ftpclient实例，通过pool.getResource()来获取；  
#如果赋值为-1，则表示不限制；如果pool已经分配了maxTotal个ftpclient实例，则此时pool的状态为exhausted(耗尽)。
ftpclient.pool.maxTotal = 500 
#当池内没有返回对象时，最大等待时间获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
#表示当borrow一个ftpclient实例时，最大的等待时间，如果超过等待时间，则直接抛出ftpclientConnectionException；
ftpclient.pool.maxWaitMillis = -1
#连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
ftpclient.pool.blockWhenExhausted = true
#逐出连接的最小空闲时间 默认1800000毫秒(30分钟)，达到此值后空闲连接将可能会被移除。负值(-1)表示不移除。
#表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
ftpclient.pool.minEvictableIdleTimeMillis = -1
#连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1.
#对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
#在minEvictableIdleTimeMillis基础上，加入了至少 minIdle个对象已经在pool里面了。
#如果为-1，evicted不会根据idle time驱逐任何对象。
#如果minEvictableIdleTimeMillis>0，则此项设置无意义，且只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
ftpclient.pool.softMinEvictableIdleTimeMillis = -1
#每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 .
#表示idle object evitor每次扫描的最多的对象数；
ftpclient.pool.numTestsPerEvictionRun = 3
#向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值.
#在borrow一个ftpclient实例时，是否提前进行alidate操作；如果为true，则得到的ftpclient实例均是可用的；
ftpclient.pool.testOnBorrow = true
#向连接池“归还”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
ftpclient.pool.testOnReturn = true 
#向连接池“获取”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
ftpclient.pool.testOnCreate = true 
#向调用者输出“链接”对象时，是否检测它的空闲超时；默认为false。如果“链接”空闲超时，将会被移除。建议保持默认值.
#如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
ftpclient.pool.testWhileIdle = true
#“空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1.
#表示idle object evitor两次扫描之间要sleep的毫秒数；
ftpclient.pool.timeBetweenEvictionRunsMillis = -1
#是否启用pool的jmx管理功能, 默认true -->
ftpclient.pool.jmxEnabled = true
#/MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i)
# 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好. -->
ftpclient.pool.jmxNamePrefix = pool
#表示当pool中的ftpclient实例都被allocated完时，pool要采取的操作；
#默认有三种
#WHEN_EXHAUSTED_FAIL（表示无ftpclient实例时，直接抛出NoSuchElementException）、
#WHEN_EXHAUSTED_BLOCK（则表示阻塞住，或者达到maxWait时抛出ftpclientConnectionException）、 
#WHEN_EXHAUSTED_GROW（则表示新建一个ftpclient实例，也就说设置的maxActive无用）；
ftpclient.pool.whenExhaustedAction=WHEN_EXHAUSTED_GROW
#设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
ftpclient.pool.evictionPolicyClassName=org.apache.commons.pool2.impl.DefaultEvictionPolicy
