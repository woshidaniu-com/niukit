package com.woshidaniu.httpclient.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;

public class TrustStrategyUtils {

	private static class SSLTrustStrategy implements TrustStrategy {

		private boolean checkValidity;

		public SSLTrustStrategy(boolean checkValidity) {
			this.checkValidity = checkValidity;
		}

		@Override
		public boolean isTrusted(X509Certificate[] certificates, String authType)
				throws CertificateException {
			if (checkValidity) {
                try {
					for (X509Certificate certificate : certificates){
					    certificate.checkValidity();
					}
				} catch (Exception e) {
					return false;
				}
            }
			//信任所有
			return true;
		}

	}

	private static final TrustStrategy ACCEPT_ALL = new SSLTrustStrategy(false);

    private static final TrustStrategy CHECK_VALIDITY = new SSLTrustStrategy(true);
    
    private static final TrustStrategy SELF_SIGNED = TrustSelfSignedStrategy.INSTANCE;
    
    /**
     * Generate a TrustStrategy that performs no checks.
     * @return the TrustStrategy
     */
    public static TrustStrategy getSelfSignedTrustStrategy(){
        return SELF_SIGNED;
    }
    
    /**
     * Generate a TrustStrategy that performs no checks.
     * @return the TrustStrategy
     */
    public static TrustStrategy getAcceptAllTrustStrategy(){
        return ACCEPT_ALL;
    }

    /**
     * Generate a TrustStrategy that checks server certificates for validity,
     * but otherwise performs no checks.
     * @return the validating TrustStrategy
     */
    public static TrustStrategy getValidateCertificateTrustStrategy(){
        return CHECK_VALIDITY;
    }

}
