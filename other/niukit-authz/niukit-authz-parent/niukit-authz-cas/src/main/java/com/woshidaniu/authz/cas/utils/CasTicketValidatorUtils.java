/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.proxy.Cas20ProxyRetriever;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorageImpl;
import org.jasig.cas.client.ssl.HttpURLConnectionFactory;
import org.jasig.cas.client.ssl.HttpsURLConnectionFactory;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.ReflectUtils;
import org.jasig.cas.client.validation.Cas10TicketValidator;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Cas30ProxyTicketValidator;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.cas.config.ShiroCasProperties;

/**
 * 
 * @className	： CasTicketValidatorUtils
 * @description	： CasTicket验证工具类
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:57:09
 * @version 	V1.0
 */
public class CasTicketValidatorUtils {

	protected final static Logger logger = LoggerFactory.getLogger(CasTicketValidatorUtils.class);
	/**
	 * Storage location of ProxyGrantingTickets and Proxy Ticket IOUs.
	 */
	protected static final ProxyGrantingTicketStorage proxyGrantingTicketStorage = new ProxyGrantingTicketStorageImpl();

	/**
	 * Constructs a Cas20ServiceTicketValidator or a Cas20ProxyTicketValidator based on supplied parameters.
	 * @param filterConfig the Filter Configuration object.
	 * @return a fully constructed TicketValidator.
	 */
	public static final TicketValidator createTicketValidator(final ShiroCasProperties casProperties) {
		
		if (Protocol.CAS1 == casProperties.getProtocol()) {
            return buildCas10TicketValidator(casProperties);
        } else if (Protocol.CAS2 == casProperties.getProtocol()) {
            return buildCas20TicketValidator(casProperties);
        } else if (Protocol.CAS3 == casProperties.getProtocol()) {
            return buildCas30TicketValidator(casProperties);
        } else if (Protocol.SAML11 == casProperties.getProtocol()) {
            return buildSAMLTicketValidator(casProperties);
        } else {
            throw new AuthenticationException("Unable to initialize the TicketValidator for protocol: " + casProperties.getProtocol().name());
        }
	}
	
    protected static TicketValidator buildSAMLTicketValidator(final ShiroCasProperties casProperties) {
        final Saml11TicketValidator saml11TicketValidator = new Saml11TicketValidator(casProperties.getCasServerUrlPrefix());
        saml11TicketValidator.setTolerance(casProperties.getTolerance());
        saml11TicketValidator.setEncoding(casProperties.getEncoding());
        return saml11TicketValidator;
    }

    protected static TicketValidator buildCas30TicketValidator(final ShiroCasProperties casProperties) {
        
        final boolean allowAnyProxy = casProperties.isAcceptAnyProxy();
		final String allowedProxyChains = casProperties.getAllowedProxyChains();
		final String casServerUrlPrefix = casProperties.getCasServerUrlPrefix();
		
		final Class<? extends Cas20ServiceTicketValidator> ticketValidatorClass = StringUtils.hasText(casProperties.getTicketValidatorClass()) ? ReflectUtils.<Cas20ServiceTicketValidator> loadClass(casProperties.getTicketValidatorClass()) : null; 
		final Cas20ServiceTicketValidator validator;

		if (allowAnyProxy || CommonUtils.isNotBlank(allowedProxyChains)) {
			final Cas20ProxyTicketValidator v = createNewTicketValidator(ticketValidatorClass, casServerUrlPrefix, Cas30ProxyTicketValidator.class);
			v.setAcceptAnyProxy(allowAnyProxy);
			v.setAllowedProxyChains(CommonUtils.createProxyList(allowedProxyChains));
			validator = v;
		} else {
			validator = createNewTicketValidator(ticketValidatorClass, casServerUrlPrefix, Cas30ServiceTicketValidator.class);
		}
		validator.setProxyCallbackUrl(casProperties.getProxyCallbackUrl());
		validator.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);

		HttpURLConnectionFactory factory = new HttpsURLConnectionFactory( HttpsURLConnection.getDefaultHostnameVerifier(), getSSLConfig(casProperties));

		validator.setURLConnectionFactory(factory);

		validator.setProxyRetriever(new Cas20ProxyRetriever(casServerUrlPrefix, casProperties.getEncoding(), factory));
		validator.setRenew(casProperties.isRenew());
		validator.setEncoding(casProperties.getEncoding());
		
        return validator;
        
    }

    protected static TicketValidator buildCas20TicketValidator(final ShiroCasProperties casProperties) {
        
        final boolean allowAnyProxy = casProperties.isAcceptAnyProxy();
		final String allowedProxyChains = casProperties.getAllowedProxyChains();
		final String casServerUrlPrefix = casProperties.getCasServerUrlPrefix();
		
		final Class<? extends Cas20ServiceTicketValidator> ticketValidatorClass = StringUtils.hasText(casProperties.getTicketValidatorClass()) ? ReflectUtils.<Cas20ServiceTicketValidator>loadClass(casProperties.getTicketValidatorClass()) : null; 
		final Cas20ServiceTicketValidator validator;

		if (allowAnyProxy || CommonUtils.isNotBlank(allowedProxyChains)) {
			final Cas20ProxyTicketValidator v = createNewTicketValidator(ticketValidatorClass, casServerUrlPrefix, Cas20ProxyTicketValidator.class);
			v.setAcceptAnyProxy(allowAnyProxy);
			v.setAllowedProxyChains(CommonUtils.createProxyList(allowedProxyChains));
			validator = v;
		} else {
			validator = createNewTicketValidator(ticketValidatorClass, casServerUrlPrefix, Cas20ServiceTicketValidator.class);
		}
		validator.setProxyCallbackUrl(casProperties.getProxyCallbackUrl());
		validator.setProxyGrantingTicketStorage(proxyGrantingTicketStorage);

		HttpURLConnectionFactory factory = new HttpsURLConnectionFactory( HttpsURLConnection.getDefaultHostnameVerifier(), getSSLConfig(casProperties));

		validator.setURLConnectionFactory(factory);

		validator.setProxyRetriever(new Cas20ProxyRetriever(casServerUrlPrefix, casProperties.getEncoding(), factory));
		validator.setRenew(casProperties.isRenew());
		validator.setEncoding(casProperties.getEncoding());
		
		
        return validator;
    }

    protected static TicketValidator buildCas10TicketValidator(final ShiroCasProperties casProperties) {
        final Cas10TicketValidator cas10TicketValidator = new Cas10TicketValidator(casProperties.getCasServerUrlPrefix());
        cas10TicketValidator.setEncoding(casProperties.getEncoding());
        return cas10TicketValidator;
    }
	
	@SuppressWarnings("unchecked")
	protected static <T> T createNewTicketValidator(final Class<? extends Cas20ServiceTicketValidator> ticketValidatorClass,
			final String casServerUrlPrefix, final Class<T> clazz) {
		if (ticketValidatorClass == null) {
			return ReflectUtils.newInstance(clazz, casServerUrlPrefix);
		}
		return (T) ReflectUtils.newInstance(ticketValidatorClass, casServerUrlPrefix);
	}

	/**
	 * Gets the ssl config to use for HTTPS connections if one is configured for
	 * this filter.
	 * 
	 * @return Properties that can contains key/trust info for Client Side
	 *         Certificates
	 */
	protected static Properties getSSLConfig(ShiroCasProperties casProperties) {
		final Properties properties = new Properties();
		final String fileName = casProperties.getSslConfigFile();

		if (fileName != null) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(fileName);
				properties.load(fis);
				logger.trace("Loaded {} entries from {}", properties.size(), fileName);
			} catch (final IOException ioe) {
				logger.error(ioe.getMessage(), ioe);
			} finally {
				CommonUtils.closeQuietly(fis);
			}
		}
		return properties;
	}

}
