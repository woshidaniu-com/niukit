 package com.woshidaniu.httpclient.handler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.ConfigUtils;
import com.woshidaniu.httpclient.interceptor.HttpRequestHeaderInterceptor;

/**
 * 
 *@类名称	: DnsResolverHandler.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:56:27 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class DnsResolverHandler extends SystemDefaultDnsResolver {

	protected static Logger LOG = LoggerFactory.getLogger(HttpRequestHeaderInterceptor.class);
	
	protected Map<String,InetAddress[]> addressMap = new HashMap<String,InetAddress[]>();
	
	public DnsResolverHandler(){
		Properties cachedProperties = ConfigUtils.getProperties(this.getClass(), "httpclient-dns.properties");
		try {
			//通过配置文件加载响应头信息
			if(!cachedProperties.isEmpty()){
				for(Object key : cachedProperties.keySet()){
					if(key != null){
						String ip = cachedProperties.getProperty(key.toString());
						if(ip != null){
							addressMap.put(key.toString(), InetAddress.getAllByName(ip));
						}
					}
				}
			}
		}catch (Exception ex) {
			LOG.warn("Could not load properties from classes/httpclient-dns.properties : " + ex.getMessage());
		}
	}
	
	@Override
    public InetAddress[] resolve(final String host) throws UnknownHostException {
		if(!addressMap.isEmpty()){
			if(addressMap.containsKey(host)){
				return addressMap.get(host);
			}else {
	            return super.resolve(host);
	        }
		}else{
			if (host.equalsIgnoreCase("localhost")) {
	            return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
	        } else {
	            return super.resolve(host);
	        }
		}
		
        
    }
	
}

 
