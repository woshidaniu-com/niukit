/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.httputils;

import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 *@类名称	: HttpConnectionConfig.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：2016年4月27日 上午11:53:34
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class HttpConnectionConfig extends HttpConnectionManagerParams {
	
	public static final String TIMEOUT_INTERVAL = "http.timeout.interval"; 
	//定时清除失效连接心跳线程执行周期(单位毫秒)，默认5000
	public static final int  DEFAULT_TIMEOUT_INTERVAL = 5000;
	
	public HttpConnectionConfig() {
		// 设置httpclient是否使用NoDelay策略;默认 true
		this.setTcpNoDelay(StringUtils.getSafeBoolean(HttpConfigUtils.getText(TCP_NODELAY), "1"));
		// 通过网络与服务器建立连接的超时时间。Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间(单位毫秒)，默认30000
		this.setConnectionTimeout(StringUtils.getSafeInt(HttpConfigUtils.getText(CONNECTION_TIMEOUT), "30000"));
		// 连接读取数据超时时间(单位毫秒)，默认60000
		this.setSoTimeout(StringUtils.getSafeInt(HttpConfigUtils.getText(SO_TIMEOUT), "60000"));
		// 每个HOST的最大连接数量
		this.setDefaultMaxConnectionsPerHost(StringUtils.getSafeInt(HttpConfigUtils.getText(MAX_HOST_CONNECTIONS), "20"));
		// 连接池的最大连接数
		this.setMaxTotalConnections(StringUtils.getSafeInt(HttpConfigUtils.getText(MAX_TOTAL_CONNECTIONS), "60"));
		//socket发送数据的缓冲大小
		this.setSendBufferSize(StringUtils.getSafeInt(HttpConfigUtils.getText(SO_SNDBUF), "1048576"));
		//socket接收数据的缓冲大小
		this.setReceiveBufferSize(StringUtils.getSafeInt(HttpConfigUtils.getText(SO_RCVBUF), "1048576"));
		//检查连接是否有效的心跳周期
		this.setTimeoutInterval(StringUtils.getSafeInt(HttpConfigUtils.getText(TIMEOUT_INTERVAL), "5000"));
	}
	 
	/**
	 * @return the timeoutInterval
	 */
	public int getTimeoutInterval() {
		return getIntParameter(TIMEOUT_INTERVAL,DEFAULT_TIMEOUT_INTERVAL);
	}
	
	/**
	 * @param timeoutInterval the timeoutInterval to set
	 */
	public void setTimeoutInterval(int timeoutInterval) {
		 setIntParameter(TIMEOUT_INTERVAL,timeoutInterval);
	}
	
	
}
