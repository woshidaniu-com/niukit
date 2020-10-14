package com.woshidaniu.cache.xmemcached.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.yanf4j.core.impl.StandardSocketOption;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
/**
 * 
 *@类名称	: MemcachedSimpleDistributedCacheClient.java
 *@类描述	：xmemcached缓存客户端通用调用接口实现
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 10:21:26 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class XmemcachedSimpleClient extends XmemcachedDefaultClient{
	
	protected static Logger LOG = LoggerFactory.getLogger(XmemcachedSimpleClient.class);
	protected Pattern server_host = Pattern.compile("memcached.server(?:\\d+).host");
	protected static volatile XmemcachedSimpleClient instance;  
    
    public static XmemcachedSimpleClient getInstance() { 
    	if (instance == null) {    
            synchronized (XmemcachedSimpleClient.class) {    
               if (instance == null) {    
            	   instance = new XmemcachedSimpleClient();   
               }    
            }
        }    
        return instance; 
    }  
    
    private XmemcachedSimpleClient() {
	    try {
			ResourceBundle bundle = ResourceBundle.getBundle("memcached");
			if (bundle == null) {
				throw new IllegalArgumentException("[memcached.properties] is not found!");
			}
			//memcached客户端构建器
			MemcachedClientBuilder builder = null;
			//获取类似 192.168.1.1:11211 192.168.1.2:11211 结构的配置
			String addresses = bundle.getString("memcached.servers");
			if(!BlankUtils.isBlank(addresses)){
				builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(addresses));
				if (LOG.isInfoEnabled()) {  
					LOG.info("Configure Properties:[addresses = " + addresses + "]");  
			    }
			}else{
				//获取所有的key
			    Enumeration<String> keys =  bundle.getKeys();
			    List<InetSocketAddress> addressList = new ArrayList<InetSocketAddress>();
			    List<Integer> weights = new ArrayList<Integer>();
			    while(keys.hasMoreElements()){
			    	String key = keys.nextElement();
			    	if(key != null && key.startsWith("memcached.server")){
			    		//正则匹配
			    		Matcher matcher1 = server_host.matcher(key);
			    		if (matcher1.find()) {
			    			String hostPart = bundle.getString(key);
			    			String portNum = bundle.getString("memcached.server"+matcher1.group(1)+".port");
			    			String weight = bundle.getString("memcached.server"+matcher1.group(1)+".weight");
			    			//缓存服务IP:端口
			    			addressList.add(new InetSocketAddress(hostPart, Integer.parseInt(portNum)));
			    			//缓存服务权重
			    			weights.add(Integer.parseInt(weight.trim()));
			    		}
			    	}
			    }
			    builder = new XMemcachedClientBuilder(addressList,ArrayUtils.toPrimitive(weights.toArray(new Integer[weights.size()])));
			}
			//连接池大小即客户端个数
			builder.setConnectionPoolSize(StringUtils.getSafeInt(bundle.getString("memcached.connectionPoolSize"), "5"));
			//链接超时时间，10s
			builder.setConnectTimeout(StringUtils.getSafeInt(bundle.getString("memcached.connectTimeout"), "10000"));
			//是否宕机报警
			builder.setFailureMode(StringUtils.getSafeBoolean(bundle.getString("memcached.failureMode"), "false"));
			// 使用二进制文件  
			builder.setCommandFactory(new BinaryCommandFactory());  
			// 使用一致性哈希算法（Consistent Hash Strategy）  
			builder.setSessionLocator(new KetamaMemcachedSessionLocator());  
			// 使用序列化传输编码  
			builder.setTranscoder(new SerializingTranscoder());  
			// 进行数据的压缩阀值，默认大于1KB时进行压缩  
			builder.getTranscoder().setCompressionThreshold(StringUtils.getSafeInt(bundle.getString("memcached.compressionThreshold"), "1024"));  
			//默认过期时间
			this.setExpiry(StringUtils.getSafeInt(bundle.getString("memcached.expiry"), "0"));
			
			/*1、如果你的数据较小，如在1K以下，默认的配置选项已经足够。如果你的数据较大，我会推荐你调整网络层的TCP选项，如设置socket的接收和发送缓冲区更大，启用Nagle算法等等：  */ 
			builder.setSocketOption(StandardSocketOption.SO_RCVBUF, 32* 1024);// 设置接收缓存区为32K，默认16K
            builder.setSocketOption(StandardSocketOption.SO_SNDBUF,16 *1024); // 设置发送缓冲区为16K，默认为8K
            builder.setSocketOption(StandardSocketOption.TCP_NODELAY,false); // 启用nagle算法，提高吞吐量，默认关闭
			
			memcachedClient = builder.build();
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e); 
		}
	}

}
