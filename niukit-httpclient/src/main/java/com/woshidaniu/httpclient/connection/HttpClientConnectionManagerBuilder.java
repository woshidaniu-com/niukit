package com.woshidaniu.httpclient.connection;

import java.io.File;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.httpclient.InstrumentedHttpClientConnectionManager;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.httpclient.handler.DnsResolverHandler;
import com.woshidaniu.httpclient.utils.HttpConfigUtils;
import com.woshidaniu.httpclient.utils.HttpMessageFactoryUtils;
import com.woshidaniu.httpclient.utils.SSLContextUtils;
import com.woshidaniu.httpclient.utils.TrustManagerUtils;
import com.woshidaniu.httpclient.utils.TrustStrategyUtils;
/**
 * 
 * *******************************************************************
 * @className	： HttpClientConnectionManagerBuilder
 * @description	： Http连接池管理工具
 * @author 		： kangzhidong
 * @date		： Mar 8, 2016 11:54:14 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpClientConnectionManagerBuilder {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpClientConnectionManagerBuilder.class);
	
	private static HttpClientConnectionManagerBuilder connectionManagerBuilder = null;
    
	//连接池最大持有连接数;默认 20
	protected static int maxPoolSize = 20;
	//连接字符编码格式 可用值 UTF-8，US-ASCII，ISO-8859-1;默认 UTF-8
	protected static String charset = "UTF-8";
	//最大请求行长度的限制;默认 2000
	protected static int maxLineLength = 2000;
	//允许的最大HTTP头部信息数量;默认 200
	protected static int maxHeaderCount = 200;
	//设置httpclient是否使用NoDelay策略;默认 true
	protected static boolean tcpNoDelay = true;
	//连接读取数据超时时间；单位毫秒，默认5000
	protected static int so_timeout = 5000;
	//定时清除失效连接心跳线程执行周期；单位毫秒，默认5000
	protected static int heartbeatPeriod = 5000;
	//获取是否启用连接池的标记
    protected static boolean userManager = false;
    
    protected static String registryName = null;
    protected static MetricRegistry metricsRegistry = null;
    
    protected HttpMessageWriterFactory<HttpRequest> REQUEST_WRITER_FACTORY = HttpMessageFactoryUtils.getRequestWriterFactory();
    protected HttpMessageParserFactory<HttpResponse> RESPONSE_PARSER_FACTORY = HttpMessageFactoryUtils.getResponseParserFactory();
    protected TrustManager TRUST_ACCEPT_ALL = TrustManagerUtils.getAcceptAllTrustManager();
    protected TrustStrategy STRATEGY_ACCEPT_ALL = TrustStrategyUtils.getAcceptAllTrustStrategy();
    
    protected static IdleConnectionTimeoutThread idleThread = new IdleConnectionTimeoutThread();
    
    //私有构造器
    private HttpClientConnectionManagerBuilder(){
    	try {
    		userManager = StringUtils.getSafeBoolean(HttpConfigUtils.getText("http.connection.manager"), "false");
			maxPoolSize = StringUtils.getSafeInt(HttpConfigUtils.getText("http.connection.maxPoolSize"), "20");
	        charset = StringUtils.getSafeStr(HttpConfigUtils.getText("http.connection.config-charset"), "UTF-8");
	        maxLineLength = StringUtils.getSafeInt(HttpConfigUtils.getText("http.connection.max-line-length"), "2000");
	        maxHeaderCount = StringUtils.getSafeInt(HttpConfigUtils.getText("http.connection.max-header-count"), "200");
			tcpNoDelay = StringUtils.getSafeBoolean(HttpConfigUtils.getText("http.socket.tcpNoDelay"), "true");
		    so_timeout = StringUtils.getSafeInt(HttpConfigUtils.getText("http.socket.so_timeout"), "5000");
	        heartbeatPeriod = StringUtils.getSafeInt(HttpConfigUtils.getText("http.heartbeat.period"), "5000");
	        
	        registryName = HttpConfigUtils.getText("http.metrics.registry.name");
	        if(!StringUtils.isEmpty(registryName)){
	        	metricsRegistry = SharedMetricRegistries.getOrCreate(registryName);
	        } else{
	        	metricsRegistry = new MetricRegistry();
	        }
	        
	        /**
	         *  定时清除失效链接
			 * 	period：两次开始执行最小间隔时间
	         */
	        idleThread.setConnectionTimeout(so_timeout);
	        idleThread.setTimeoutInterval(heartbeatPeriod);
	        idleThread.start();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public static HttpClientConnectionManagerBuilder getInstance(){
		if(connectionManagerBuilder == null){
			synchronized (connectionManagerBuilder) {
				if(connectionManagerBuilder == null){
					connectionManagerBuilder = new HttpClientConnectionManagerBuilder();
				}
			}
    	}
    	return connectionManagerBuilder;
    }
    
    public static MetricRegistry getMetricsRegistry() {
		return metricsRegistry;
	}


	/**
     * 
     * @description: 创建一个不安全连接 的池管理对象;全局只有一个的对象
     * @author : kangzhidong
     * @date : Jun 30, 2015
     * @time : 5:32:30 PM 
     * @return
     */
    public PoolingHttpClientConnectionManager getUnsafeSSLConnectionManager(){
    	SSLContext sslContext = null;
		try {
			 //TLS1.0与SSL3.0基本上没有太大的差别,可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext  
			sslContext = SSLContextUtils.createSSLContext(SSLConnectionSocketFactory.TLS, null, TRUST_ACCEPT_ALL);
        } catch (Exception e) {
        	try {
        		SSLContextUtils.createSSLContext(null, STRATEGY_ACCEPT_ALL);
			} catch (Exception e1) {
				LOG.error(e.getMessage());
	            // SSL context for secure connections can be created either based on
				// system or application specific properties.
				sslContext = SSLContexts.createSystemDefault();
			}
        }
        return HttpClientConnectionManagerBuilder.getInstance().getNewConnectionManager(sslContext);
    }
    
    /**
     * 
     * @description: 创建一个 安全连接 的池管理对象;全局只有一个的对象
     * @author : kangzhidong
     * @date : Jun 30, 2015
     * @time : 5:33:55 PM 
     * @param keystore
     * @param storePassword
     * @param idleClear
     * @return
     */
    public PoolingHttpClientConnectionManager getSafeSSLConnectionManager(File keystore,String storePassword){
		//初始化证书
	    SSLContext sslContext = null;
	    String protocol = StringUtils.getSafeStr(HttpConfigUtils.getText("http.ssl.protocol"), SSLConnectionSocketFactory.TLS);
		try {
			sslContext = SSLContextUtils.createSSLContext(protocol, keystore, storePassword, STRATEGY_ACCEPT_ALL);
	    } catch (Exception e) {
            LOG.error(e.getMessage());
            // SSL context for secure connections can be created either based on
			// system or application specific properties.
			sslContext = SSLContexts.createSystemDefault();
		}
		return HttpClientConnectionManagerBuilder.getInstance().getNewConnectionManager(sslContext);
    }
	
    public PoolingHttpClientConnectionManager getSafeSSLConnectionManager(KeyStore keystore){
		//初始化证书
	    SSLContext sslContext = null;
		try {
			sslContext = SSLContextUtils.createSSLContext(keystore, STRATEGY_ACCEPT_ALL);
	    } catch (Exception e) {
            LOG.error(e.getMessage());
            // SSL context for secure connections can be created either based on
			// system or application specific properties.
			sslContext = SSLContexts.createSystemDefault();
		}
        return HttpClientConnectionManagerBuilder.getInstance().getNewConnectionManager(sslContext);
    }
    
    public PoolingHttpClientConnectionManager getHttpConnectionManager(){
	    return HttpClientConnectionManagerBuilder.getInstance().getNewConnectionManager(null);
    }
    
    /**
     * 
     * @description: 根据SSLContext 创建一个Http连接池管理对象;每次调用会产生一个新的对象
     * @author : kangzhidong
     * @date : Jun 30, 2015
     * @time : 5:34:06 PM 
     * @param sslContext
     * @return
     */
    public PoolingHttpClientConnectionManager getNewConnectionManager(SSLContext sslContext){
		
    	//CachingHttpClientBuilder.create().build()
    	
	    // Use custom DNS resolver to override the system DNS resolution.
	    DnsResolver dnsResolver = new DnsResolverHandler();
	    
	    // Create a registry of custom connection socket factories for supported
	    // protocol schemes.
	    RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
	    .register("http", PlainConnectionSocketFactory.INSTANCE);
	    //根据sslContext做出调整
	    if(sslContext != null){
	    	registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext));
	    }
	    Registry<ConnectionSocketFactory> socketFactoryRegistry = registryBuilder.build();

	    // Use a custom connection factory to customize the process of
	    // initialization of outgoing HTTP connections. Beside standard connection
	    // configuration parameters HTTP connection factory can define message
	    // parser / writer routines to be employed by individual connections.
	    HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(REQUEST_WRITER_FACTORY,RESPONSE_PARSER_FACTORY);

	    /**------------------------------设置连接池参数---------------------------------------------------------------------*/
	    
	    // Create a connection manager with custom configuration.
		PoolingHttpClientConnectionManager connectionManager = new InstrumentedHttpClientConnectionManager(
				metricsRegistry, socketFactoryRegistry, connFactory, null, dnsResolver, -1, TimeUnit.MILLISECONDS,
				null);
 
	    /**PoolingHttpClientConnectionManager维护的连接数在每个路由基础和总数上都有限制。
         * 默认，每个路由基础上的连接不超过2个，总连接数不能超过20。
         * 在实际应用中，这个限制可能会太小了，尤其是当服务器也使用Http协议时。
         * 
         * 下面调整连接池的参数：
    	 */
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connectionManager.setMaxTotal(maxPoolSize);
        // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
        connectionManager.setDefaultMaxPerRoute(connectionManager.getMaxTotal());//（目前只有一个路由，因此让他等于最大值）
		
        //connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);
        
        /**-----------------------------设置请求默认参数----------------------------------------------------------------------*/
        
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(maxHeaderCount) .setMaxLineLength(maxLineLength).build();
        // 创建默认连接参数
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset( "UTF-8".equalsIgnoreCase(charset) ? Consts.UTF_8 : ( "US-ASCII".equalsIgnoreCase(charset) ? Consts.ASCII : Consts.ISO_8859_1))
            .setMessageConstraints(messageConstraints)
            .build();
        
        /**多线程同时访问httpclient，例如同时从一个站点上下载多个文件。对于同一个HttpConnection同一个时间只能有一个线程访问，
         * 为了保证多线程工作环境下不产生冲突，httpclient使用了一个多线程连接管理器的类：MultiThreadedHttpConnectionManager，
         * 要使用这个类很简单，只需要在构造HttpClient实例的时候传入即可，代码如下：
         **/
        // MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager(); 
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        //connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);
        
        /**-----------------------------设置读取默认参数----------------------------------------------------------------------*/
	    
        // 创建默认Socket参数
        SocketConfig socketConfig = SocketConfig.custom()
        //nagle算法默认是打开的，会引起delay的问题；所以要手工关掉
        .setTcpNoDelay(tcpNoDelay)
        //设置读数据超时时间(单位毫秒) 
        .setSoTimeout(so_timeout).build();
        //设置默认Socket连接参数
        connectionManager.setDefaultSocketConfig(socketConfig);
       
        /**-------------------------------启用连接池空连接处理线程--------------------------------------------------------------------*/
       
        idleThread.addConnectionManager(connectionManager);
        
	    //返回创建的连接池
    	return connectionManager;
    }
    
}
