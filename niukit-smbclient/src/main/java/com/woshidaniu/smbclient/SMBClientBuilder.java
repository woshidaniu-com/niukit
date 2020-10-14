package com.woshidaniu.smbclient;

import java.io.IOException;

import jcifs.Config;
import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbSession;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.smbclient.io.CopyStreamProcessListener;
import com.woshidaniu.smbclient.utils.SMBPathUtils;

/**
 * 
 * @className	： SMBClientBuilder
 * @description	： SmbFile对象构建器
 * @author 		： kangzhidong
 * @date		： Jan 20, 2016 11:12:14 AM
 */
public class SMBClientBuilder extends SMBClientConfig {
	
	protected static Logger LOG = LoggerFactory.getLogger(SMBClientBuilder.class);
	private SMBClientConfig configuration;
	
	public SMBClientBuilder() {
		this.configuration = this;
	}
	
	public SMBClientBuilder(String location) {
		try {
			ConfigUtils.initPropertiesConfig(this,location,"smbclient");
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		this.configuration = this;
	}
	
	public SMBClientBuilder(SMBClientConfig config) {
		this.configuration = config;
	}

	public SMBClient build() throws IOException {
		
		//基于smb协议的共享文件访问对象
		SMBClient smbClient = null;
		if(!BlankUtils.isBlank(configuration.getDomain())){
			//DOMAIN_IP         域名服务（其实域名和域名服务器IP可以，不过用IP解析速度快很多。）  
			//DOMAIN_NAME       域名  
			//LOGIN_NAME        用户名  
			//PASSWORD      密码  
			UniAddress dc = UniAddress.getByName(configuration.getHost());  
			NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(configuration.getDomain(), configuration.getUsername(), configuration.getPassword());  
			Config.setProperty("jcifs.netbios.wins", configuration.getHost());
			SmbSession.logon(dc, authentication); 
			//创建基于smb协议的共享文件访问对象
			smbClient = new SMBClient( SMBPathUtils.getSharedURL(configuration.getHost(), configuration.getSharedDir()) , authentication);
		}else{
			//共享目录访问路径
			String sharedURL = SMBPathUtils.getSharedURL(configuration.getUsername(), configuration.getPassword(), configuration.getHost(), configuration.getSharedDir());
			//创建基于smb协议的共享文件访问对象
			smbClient = new SMBClient(sharedURL);
		}
		//启用或禁用用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查
		smbClient.setAllowUserInteraction(configuration.isAllowUserInteraction());
		//设置一个指定的超时值（以毫秒为单位），该值将在打开到此 URLConnection 引用的资源的通信链接时使用
		smbClient.setConnectTimeout(configuration.getConnectTimeout());
		//数据读取超时时间，以毫秒为单位
		smbClient.setReadTimeout(configuration.getReadTimeout());
		//启用或禁用在条件允许情况下允许协议使用缓存
		smbClient.setDefaultUseCaches(configuration.isUsecaches());
		smbClient.setUseCaches(configuration.isUsecaches());
		
		smbClient.setAutoFlush(configuration.isAutoFlush());
		smbClient.setAutoFlushBlockSize(configuration.getAutoFlushBlockSize());
		smbClient.setBufferSize(configuration.getBufferSize());
		smbClient.setChannelReadBufferSize(configuration.getChannelReadBufferSize());
		smbClient.setChannelWriteBufferSize(configuration.getChannelWriteBufferSize());
		smbClient.setLogDebug(configuration.isLogDebug());
		
		try {
			//进行存储时/检索操作时数据处理进度监听对象
			if(!BlankUtils.isBlank(configuration.getCopyStreamProcessListenerName())){
				smbClient.setCopyStreamProcessListener((CopyStreamProcessListener) ConstructorUtils.invokeConstructor(Class.forName(configuration.getCopyStreamProcessListenerName()), null));
			}else{
				smbClient.setCopyStreamProcessListener(configuration.getCopyStreamProcessListener());
			}
		} catch (Exception e) {
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
		
		return smbClient;
	}

	public void shutdown() throws IOException {

	}

	public SMBClientConfig getConfiguration() {
		return configuration;
	}
	
}
