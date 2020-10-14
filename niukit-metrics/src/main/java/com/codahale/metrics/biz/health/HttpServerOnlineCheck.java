package com.codahale.metrics.biz.health;

import java.net.HttpURLConnection;
import java.net.URL;

import com.codahale.metrics.health.HealthCheck;
/**
 *	http://blog.csdn.net/paullmq/article/details/9032631
 */
public class HttpServerOnlineCheck extends HealthCheck {

	protected String httpURL;
	
	public HttpServerOnlineCheck() {
	}
	
	public HttpServerOnlineCheck(String httpURL) {
		this.httpURL = httpURL;
	}
	
	@Override
	protected Result check() throws Exception {
		try {
			URL url = new URL(getHttpURL());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			this.onPreHandle(conn);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				return HealthCheck.Result.healthy();
			}
			return HealthCheck.Result.unhealthy(conn.getResponseMessage());
		} catch (Exception error) {
			return HealthCheck.Result.unhealthy(error);
		}
	}
 
	protected void onPreHandle(HttpURLConnection conn) throws Exception {
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(3000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		// 设置编码
		conn.setRequestProperty("Charset", "UTF-8"); 
	}
	
	public String getHttpURL() {
		return httpURL;
	}

	public void setHttpURL(String httpURL) {
		this.httpURL = httpURL;
	}

}
