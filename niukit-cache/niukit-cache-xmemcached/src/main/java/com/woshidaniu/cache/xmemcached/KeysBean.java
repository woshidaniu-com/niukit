package com.woshidaniu.cache.xmemcached;

public class KeysBean {
	
	private String server;
	private long bytes;
	private long expiry;
	
	public KeysBean(String server, long bytes, long expiry) {
		this.server = server;
		this.bytes = bytes;
		this.expiry = expiry;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public long getBytes() {
		return bytes;
	}

	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	public long getExpiry() {
		return expiry;
	}

	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

}
