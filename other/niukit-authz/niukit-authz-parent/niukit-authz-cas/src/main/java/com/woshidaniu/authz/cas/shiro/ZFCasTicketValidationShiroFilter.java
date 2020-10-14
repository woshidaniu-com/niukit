/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.shiro;

import javax.servlet.ServletException;

import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.validation.Cas10TicketValidationFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.jasig.cas.client.validation.Saml11TicketValidationFilter;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @className	： ZFCasTicketValidationShiroFilter
 * @description	： cas ticket认证shiro代理过滤器
 * @author 		：康康（1571）
 * @date		： 2018年5月3日 上午9:28:06
 * @version 	V1.0
 */
public class ZFCasTicketValidationShiroFilter extends AbstractCasPropertiesNeedProxyShiroFilter{

	@Override
	protected void doInitDelegateAndConfigLazy(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		
		log.info("doCreateDelegateAndInitConfig:{}",this.getClass().getSimpleName());

		if (Protocol.CAS1.equals(casProperties.getProtocol())) {
			
			this.delegate = new Cas10TicketValidationFilter();
			
		} else if (Protocol.CAS2.equals(casProperties.getProtocol())) {
			
			this.delegate = new Cas20ProxyReceivingTicketValidationFilter();

			// Cas20ProxyReceivingTicketValidationFilter
			proxyFilterConfig.addInitParameter(ConfigurationKeys.ACCEPT_ANY_PROXY.getName(),Boolean.toString(casProperties.isAcceptAnyProxy()));
			
			if (StringUtils.hasText(casProperties.getAllowedProxyChains())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.ALLOWED_PROXY_CHAINS.getName(),casProperties.getAllowedProxyChains());
			}
			
			proxyFilterConfig.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST.getName(),Boolean.toString(casProperties.isArtifactParameterOverPost()));
			
			if (StringUtils.hasText(casProperties.getArtifactParameterName())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getName(),casProperties.getArtifactParameterName());
			}
			
			if (StringUtils.hasText(casProperties.getAuthenticationRedirectStrategyClass())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS.getName(),casProperties.getAuthenticationRedirectStrategyClass());
			}
			if (StringUtils.hasText(casProperties.getCipherAlgorithm())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.CIPHER_ALGORITHM.getName(),casProperties.getCipherAlgorithm());
			}
			proxyFilterConfig.addInitParameter(ConfigurationKeys.EAGERLY_CREATE_SESSIONS.getName(),Boolean.toString(casProperties.isEagerlyCreateSessions()));
			proxyFilterConfig.addInitParameter(ConfigurationKeys.GATEWAY.getName(),Boolean.toString(casProperties.isGateway()));
			if (StringUtils.hasText(casProperties.getGatewayStorageClass())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.GATEWAY_STORAGE_CLASS.getName(),casProperties.getGatewayStorageClass());
			}
			proxyFilterConfig.addInitParameter(ConfigurationKeys.IGNORE_CASE.getName(),Boolean.toString(casProperties.isIgnoreCase()));
			if (StringUtils.hasText(casProperties.getIgnorePattern())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.IGNORE_PATTERN.getName(),casProperties.getIgnorePattern());
			}
			proxyFilterConfig.addInitParameter(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE.getName(),casProperties.getIgnoreUrlPatternType().toString());
			if (StringUtils.hasText(casProperties.getLogoutParameterName())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.LOGOUT_PARAMETER_NAME.getName(),casProperties.getLogoutParameterName());
			}
			proxyFilterConfig.addInitParameter(ConfigurationKeys.MILLIS_BETWEEN_CLEAN_UPS.getName(),Long.toString(casProperties.getMillisBetweenCleanUps()));
			if (StringUtils.hasText(casProperties.getProxyReceptorUrl())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.PROXY_RECEPTOR_URL.getName(),casProperties.getProxyReceptorUrl());
			}
			if (StringUtils.hasText(casProperties.getProxyCallbackUrl())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.PROXY_CALLBACK_URL.getName(),casProperties.getProxyCallbackUrl());
			}
			if (StringUtils.hasText(casProperties.getProxyGrantingTicketStorageClass())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.PROXY_GRANTING_TICKET_STORAGE_CLASS.getName(),casProperties.getProxyGrantingTicketStorageClass());
			}
			if (StringUtils.hasText(casProperties.getRelayStateParameterName())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getName(),casProperties.getRelayStateParameterName());
			}
			if (StringUtils.hasText(casProperties.getRoleAttribute())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.ROLE_ATTRIBUTE.getName(),casProperties.getRoleAttribute());
			}
			if (StringUtils.hasText(casProperties.getSecretKey())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.SECRET_KEY.getName(),casProperties.getSecretKey());
			}
			if (StringUtils.hasText(casProperties.getTicketValidatorClass())) {
				proxyFilterConfig.addInitParameter(ConfigurationKeys.TICKET_VALIDATOR_CLASS.getName(),casProperties.getTicketValidatorClass());
			}
			//ticket验证成功后是否重定向
			proxyFilterConfig.addInitParameter(ConfigurationKeys.REDIRECT_AFTER_VALIDATION.getName(),Boolean.toString(casProperties.isRedirectAfterValidation()));
			proxyFilterConfig.addInitParameter(ConfigurationKeys.TOLERANCE.getName(),Long.toString(casProperties.getTolerance()));

		} else if (Protocol.CAS3.equals(casProperties.getProtocol())) {
			
			this.delegate = new Cas30ProxyReceivingTicketValidationFilter();
			
		} else if (Protocol.SAML11.equals(casProperties.getProtocol())) {
			
			this.delegate = new Saml11TicketValidationFilter();
			// Saml11TicketValidationFilter
			proxyFilterConfig.addInitParameter(ConfigurationKeys.TOLERANCE.getName(),Long.toString(casProperties.getTolerance()));
		}else {
			log.error("not set protocol");
		}

		// Cas10TicketValidationFilter、Cas20ProxyReceivingTicketValidationFilter、Cas30ProxyReceivingTicketValidationFilter、Saml11TicketValidationFilter
		proxyFilterConfig.addInitParameter(ConfigurationKeys.ENCODE_SERVICE_URL.getName(),Boolean.toString(casProperties.isEncodeServiceUrl()));
		if (StringUtils.hasText(casProperties.getEncoding())) {
			proxyFilterConfig.addInitParameter(ConfigurationKeys.ENCODING.getName(), casProperties.getEncoding());
		}
		proxyFilterConfig.addInitParameter(ConfigurationKeys.EXCEPTION_ON_VALIDATION_FAILURE.getName(),Boolean.toString(casProperties.isExceptionOnValidationFailure()));
		proxyFilterConfig.addInitParameter(ConfigurationKeys.CAS_SERVER_LOGIN_URL.getName(),casProperties.getCasServerLoginUrl());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(),casProperties.getCasServerUrlPrefix());
		if (StringUtils.hasText(casProperties.getHostnameVerifier())) {
			proxyFilterConfig.addInitParameter(ConfigurationKeys.HOSTNAME_VERIFIER.getName(),casProperties.getHostnameVerifier());
		}
		if (StringUtils.hasText(casProperties.getHostnameVerifierConfig())) {
			proxyFilterConfig.addInitParameter(ConfigurationKeys.HOSTNAME_VERIFIER_CONFIG.getName(),casProperties.getHostnameVerifierConfig());
		}
		proxyFilterConfig.addInitParameter(ConfigurationKeys.REDIRECT_AFTER_VALIDATION.getName(),Boolean.toString(casProperties.isRedirectAfterValidation()));
		// proxyFilterConfig.addInitParameter(ConfigurationKeys.RENEW.getName(),
		// Boolean.toString(properties.isRenew()));
		if (StringUtils.hasText(casProperties.getServerName())) {
			proxyFilterConfig.addInitParameter(ConfigurationKeys.SERVER_NAME.getName(), casProperties.getServerName());
		} else if (StringUtils.hasText(casProperties.getService())) {
			proxyFilterConfig.addInitParameter(ConfigurationKeys.SERVICE.getName(), casProperties.getService());
		}
		if (StringUtils.hasText(casProperties.getSslConfigFile())) {
			proxyFilterConfig.addInitParameter(ConfigurationKeys.SSL_CONFIG_FILE.getName(),casProperties.getSslConfigFile());
		}
		proxyFilterConfig.addInitParameter(ConfigurationKeys.USE_SESSION.getName(),Boolean.toString(casProperties.isUseSession()));
		
	}

}
