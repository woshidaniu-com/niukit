/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.httputils;

import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @类名称 : HttpClientUtils.java
 * @类描述 ：
 * @创建人 ：kangzhidong
 * @创建时间 ：2016年4月26日 上午10:51:54
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
public abstract class HttpsClientUtils extends HttpClientUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpsClientUtils.class);
	
	static {

		try {
			
			Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);  
		    Protocol.registerProtocol("https", easyhttps);   
	        
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}
	}

}
