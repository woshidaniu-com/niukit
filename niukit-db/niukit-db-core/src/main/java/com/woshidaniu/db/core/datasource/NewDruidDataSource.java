package com.woshidaniu.db.core.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.woshidaniu.security.algorithm.DesBase64Codec;
/**
 * 
 *@类名称	: NewDruidDataSource.java
 *@类描述	：基于 Druid 数据连接池扩展公司内部加密解密工具后的数据源对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 8:53:21 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings("serial")
public class NewDruidDataSource extends DruidDataSource {

	private DesBase64Codec dbencrypt = new DesBase64Codec();
	
	@Override
	public void setUrl(String jdbcUrl) {
		super.setUrl(dbencrypt.decrypt(jdbcUrl.getBytes()));
	}

	@Override
	public void setUsername(String username) {
		super.setUsername(dbencrypt.decrypt(username.getBytes()));
	}

	@Override
	public void setPassword(String password) {
		super.setPassword(dbencrypt.decrypt(password.getBytes()));
	}


}
