package com.woshidaniu.metrics.jmxtrans;

import org.jmxtrans.embedded.output.influxdb.InfluxDbOutputWriter;

import com.woshidaniu.security.algorithm.DesBase64Codec;
/**
 * 
 *@类名称		： ZFInfluxDbOutputWriter.java
 *@类描述		：使用公司统一的加解密进行连接参数解密
 *@创建人		：kangzhidong
 *@创建时间	：2017年4月1日 上午9:54:15
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class ZFInfluxDbOutputWriter extends InfluxDbOutputWriter {

	private DesBase64Codec dbencrypt = new DesBase64Codec();
	
	@Override
	public String getUrl(String url) {
		return url != null ? dbencrypt.decrypt(url.getBytes()) : url;
	}
	
	@Override
	public String getUser(String user) {
		return user != null ? dbencrypt.decrypt(user.getBytes()) : user;
	}
	
	@Override
	public String getPassword(String password) {
		return password != null ? dbencrypt.decrypt(password.getBytes()) : password;
	}
	
}
