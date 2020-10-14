package com.woshidaniu.httpclient.utils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HttpClientSSLUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpClientSSLUtils.class);

	/**
	 * 
	 *@描述		：返回信任所有证书的HttpClient
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201611:22:07 PM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static HttpClientBuilder getInsecureSSLClient() {
		try {
			// HttpClient使用SSLSocketFactory来创建SSL连接。SSLSocketFactory允许高度定制。它可以使用javax.net.ssl.SSLContext的实例作为参数，并使用它来创建定制SSL连接。
			SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(null, TrustStrategyUtils.getAcceptAllTrustStrategy()).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf);
		} catch (KeyManagementException e) {
			LOG.error("KeyManagementException", e);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("NoSuchAlgorithmException", e);
		} catch (KeyStoreException e) {
			LOG.error("NoSuchAlgorithmException", e);
		}
		return HttpClients.custom();
	}

	/**
	 * 
	 *@描述		：返回通过证书构建的HttpClient
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201611:22:13 PM
	 *@param keystore
	 *@param storePassword
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static HttpClientBuilder getFailSafeSSLClient(File keystore,String storePassword) {
		try {
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContextBuilder.create().loadTrustMaterial(keystore, storePassword.toCharArray(),new TrustSelfSignedStrategy()).build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			return HttpClients.custom().setSSLSocketFactory(sslsf);
		} catch (KeyManagementException e) {
			LOG.error("KeyManagementException", e);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("NoSuchAlgorithmException", e);
		} catch (KeyStoreException e) {
			LOG.error("NoSuchAlgorithmException", e);
		} catch (CertificateException e) {
			LOG.error("CertificateException", e);
		} catch (IOException e) {
			LOG.error("IOException", e);
		}
		return HttpClients.custom();
	}

	public static HttpClientBuilder getFailSafeSSLClient(KeyStore keystore,String storePassword) {
		SSLContext sslContext = null;
		try {
			// Trust own CA and all self-signed certs
			sslContext = SSLContextBuilder.create().loadTrustMaterial(keystore,new TrustSelfSignedStrategy()).build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			return HttpClients.custom().setSSLSocketFactory(sslsf);
		} catch (KeyManagementException e) {
			LOG.error("KeyManagementException", e);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("NoSuchAlgorithmException", e);
		} catch (KeyStoreException e) {
			LOG.error("NoSuchAlgorithmException", e);
		}
		return HttpClients.custom();
	}

}