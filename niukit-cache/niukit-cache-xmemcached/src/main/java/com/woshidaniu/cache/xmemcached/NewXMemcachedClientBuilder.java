package com.woshidaniu.cache.xmemcached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.XMemcachedClientBuilder;

import org.apache.commons.lang.ArrayUtils;

import com.woshidaniu.basicutils.Assert;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.basicutils.net.AddressUtils;
import com.woshidaniu.beanutils.reflection.ReflectionUtils;

/***
 * 
 *@类名称	: NewXMemcachedClientBuilder.java
 *@类描述	：重写XMemcachedClientBuilder，方便缓存配置
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 11:09:24 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class NewXMemcachedClientBuilder extends XMemcachedClientBuilder {

	public NewXMemcachedClientBuilder() {
		super();
	}
	
	public NewXMemcachedClientBuilder(String addressList) {
		//获得以 ",; \t\n"分割的  IP:PORT:WEIGHT 字符数组
		this(StringUtils.tokenizeToStringArray(addressList));
	}
	
	/**
	 * 缓存节点 格式 IP:PORT:WEIGHT 如 192.168.137.2:11211:1 
	 * @throws Exception 
	 */
	public NewXMemcachedClientBuilder(String... addressArr) {
		if(addressArr == null || addressArr.length == 0){
			throw new IllegalArgumentException("Configure at least one cache node.");
		}
		String[] serverArr = new String[0];
		if (serverArr != null) {
			List<String> addressList = new ArrayList<String>();
			for(String address : addressArr){
				Assert.hasText(address, "server must not be empty");
				addressList.add(address);
			}
			//对处理后的路径进行处理
			serverArr = new String[addressList.size()];
			for (int i = 0; i < addressList.size(); i++) {
				serverArr[i] = addressList.get(i);
			}
		}
		List<InetSocketAddress> addressList = new ArrayList<InetSocketAddress>();
		List<Integer> weights = new ArrayList<Integer>();
		for (String node : serverArr) {
			String[] nodeArgs = node.split(":");
			switch (nodeArgs.length) {
				case 2:{// IP:PORT
					addressList.add(AddressUtils.getOneAddress(node));
					weights.add(1);
				};break;
				case 3:{//IP:PORT:WEIGHT
					addressList.add(AddressUtils.getOneAddress(node));
					weights.add(Integer.parseInt(StringUtils.trim(nodeArgs[2])));
				};break;
				default:{
				};break;
			}
		}
		
		Map<InetSocketAddress, InetSocketAddress> addressMap = AddressUtils.getAddressMap(addressList);
		int[] weightArr = ArrayUtils.toPrimitive(weights.toArray(new Integer[weights.size()]));
		
		ReflectionUtils.setField("addressMap", this, addressMap);
		ReflectionUtils.setField("weights", this, weightArr);
		
	}

	public NewXMemcachedClientBuilder(List<InetSocketAddress> addressList) {
		super(addressList);
	}

	public NewXMemcachedClientBuilder(List<InetSocketAddress> addressList,int[] weights) {
		super(addressList , weights);
	}

	public NewXMemcachedClientBuilder(Map<InetSocketAddress, InetSocketAddress> addressMap) {
		super(addressMap);
	}

	public NewXMemcachedClientBuilder(Map<InetSocketAddress, InetSocketAddress> addressMap, int[] weights) {
		super(addressMap , weights);
	}
	
	
}
