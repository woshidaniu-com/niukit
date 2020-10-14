package com.woshidaniu.httpclient.utils;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContexts;


/**
 * General utilities for SSLContext.
 * @since 3.0
 */
public class SSLContextUtils {

    private SSLContextUtils() {
        // Not instantiable
    }

    /**
     * Create and initialise an SSLContext.
     * @param protocol the protocol used to instatiate the context
     * @param keyManager the key manager, may be {@code null}
     * @param trustManager the trust manager, may be {@code null}
     * @return the initialised context.
     * @throws IOException this is used to wrap any {@link GeneralSecurityException} that occurs
     */
    public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager)
            throws IOException {
        return createSSLContext(protocol,
                keyManager == null ? null : new KeyManager[] { keyManager },
                trustManager == null ? null : new TrustManager[] { trustManager });
    }

    /**
     * Create and initialise an SSLContext.
     * @param protocol the protocol used to instatiate the context
     * @param keyManagers the array of key managers, may be {@code null} but array entries must not be {@code null}
     * @param trustManagers the array of trust managers, may be {@code null} but array entries must not be {@code null}
     * @return the initialised context.
     * @throws IOException this is used to wrap any {@link GeneralSecurityException} that occurs
     */
    public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers)
        throws IOException {
        SSLContext ctx;
        try {
			/**
			 * HttpClient使用SSLSocketFactory来创建SSL连接。SSLSocketFactory允许高度定制。
			 * 它可以使用javax.net.ssl.SSLContext的实例作为参数，并使用它来创建定制SSL连接。
			 * */
            ctx = SSLContexts.custom().useProtocol(protocol).build();
            //使用TrustManager来初始化该上下文,TrustManager只是被SSL的Socket所使用  
            ctx.init(keyManagers, trustManagers, /*SecureRandom*/ null);
        } catch (GeneralSecurityException e) {
            IOException ioe = new IOException("Could not initialize SSL context");
            ioe.initCause(e);
            throw ioe;
        }
        return ctx;
    }
    
    
    public static SSLContext createSSLContext(KeyStore keystore, TrustStrategy trustStrategy)
	    throws IOException {
    	//初始化证书
	    SSLContext ctx;
	    try {
	        ctx = SSLContexts.custom().loadTrustMaterial(keystore,trustStrategy).build();
	    } catch (GeneralSecurityException e) {
	        IOException ioe = new IOException("Could not initialize SSL context");
	        ioe.initCause(e);
	        throw ioe;
	    }
	    return ctx;
	}
    
    public static SSLContext createSSLContext(String protocol, File keystore,String storePassword, TrustStrategy trustStrategy)
	    throws IOException {
		//初始化证书
	    SSLContext ctx;
	    try {
	        ctx = SSLContexts.custom().useProtocol(protocol).loadTrustMaterial(keystore, storePassword.toCharArray(),trustStrategy).build();
	    } catch (GeneralSecurityException e) {
	        IOException ioe = new IOException("Could not initialize SSL context");
	        ioe.initCause(e);
	        throw ioe;
	    }
	    return ctx;
	}
    
    public static SSLContext createDefaultSSLContext(){
    	return SSLContexts.createSystemDefault();
    }
    
}

