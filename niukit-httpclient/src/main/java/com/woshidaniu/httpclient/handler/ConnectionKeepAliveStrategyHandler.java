 package com.woshidaniu.httpclient.handler;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.httpclient.utils.HttpConfigUtils;

/**
 * 
 *@类名称	: ConnectionKeepAliveStrategyHandler.java
 *@类描述	：为了使connMgr.closeExpiredConnections();起到作用，我们需要指定连接keep alive策略，来告诉httpClient，哪些连接大概什么时候会过期
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:09:23 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ConnectionKeepAliveStrategyHandler extends DefaultConnectionKeepAliveStrategy {

	//保持连接池内的长连接时长，单位毫秒，默认30秒
	private static long keepAlive =  30 * 1000;
	 //获取是否启用连接池的标记
    protected static  boolean userManager = false;
    
	public ConnectionKeepAliveStrategyHandler(){
		try {
			userManager = StringUtils.getSafeBoolean(HttpConfigUtils.getText("http.connection.manager"), "false");
			keepAlive = StringUtils.getSafeLong(HttpConfigUtils.getText("http.connection.keepAlive"), "30000");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 @Override
     public long getKeepAliveDuration(HttpResponse response,HttpContext context) {
         long defaultKeepAlive = super.getKeepAliveDuration(response, context);
         HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
         //尝试获取指定域名的KeepAlive值;key为： 域名+"-keepAlive"
         String hostKeepAlive = HttpConfigUtils.getText(target.getHostName() + "-keepAlive");
         if(!BlankUtils.isBlank(hostKeepAlive)){
             defaultKeepAlive = StringUtils.getSafeLong(hostKeepAlive, "-1");
             if (defaultKeepAlive == -1 && userManager) {
                 //如果服务器没有设置keep-alive这个参数，我们就把它设置成指定值
                 defaultKeepAlive = keepAlive;
             }
         }
         return defaultKeepAlive;
     }
	 
}

 
