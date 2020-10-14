package com.woshidaniu.niuca.tp.cas.validation;


import com.woshidaniu.niuca.tp.cas.proxy.Cas20ProxyRetriever;
import com.woshidaniu.niuca.tp.cas.proxy.ProxyGrantingTicketStorage;
import com.woshidaniu.niuca.tp.cas.proxy.ProxyGrantingTicketStorageImpl;
import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cas20ProxyReceivingTicketValidationFilter extends AbstractTicketValidationFilter {
    private static final String[] RESERVED_INIT_PARAMS = new String[]{"proxyReceptorUrl", "acceptAnyProxy", "allowedProxyChains", "casServerUrlPrefix", "proxyCallbackUrl", "renew", "exceptionOnValidationFailure", "redirectAfterValidation", "useSession", "serverName", "service", "artifactParameterName", "serviceParameterName", "encodeServiceUrl"};
    private String proxyReceptorUrl;
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage = new ProxyGrantingTicketStorageImpl();

    public Cas20ProxyReceivingTicketValidationFilter() {
    }

    protected void initInternal(FilterConfig filterConfig) throws ServletException {
        super.initInternal(filterConfig);
        this.setProxyReceptorUrl(this.getPropertyFromInitParams(filterConfig, "proxyReceptorUrl", (String)null));
        this.log.trace("Setting proxyReceptorUrl parameter: " + this.proxyReceptorUrl);
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.proxyGrantingTicketStorage, "proxyGrantingTicketStorage cannot be null.");
    }

    protected final TicketValidator getTicketValidator(FilterConfig filterConfig) {
        String allowAnyProxy = this.getPropertyFromInitParams(filterConfig, "acceptAnyProxy", (String)null);
        String allowedProxyChains = this.getPropertyFromInitParams(filterConfig, "allowedProxyChains", (String)null);
        String casServerUrlPrefix = this.getPropertyFromInitParams(filterConfig, "casServerUrlPrefix", (String)null);
        Object validator;
        if (!CommonUtils.isNotBlank(allowAnyProxy) && !CommonUtils.isNotBlank(allowedProxyChains)) {
            validator = new Cas20ServiceTicketValidator(casServerUrlPrefix);
        } else {
            Cas20ProxyTicketValidator v = new Cas20ProxyTicketValidator(casServerUrlPrefix);
            v.setAcceptAnyProxy(Boolean.valueOf(allowAnyProxy.toString()));
            v.setAllowedProxyChains(new ProxyList(this.constructListOfProxies(allowedProxyChains)));
            validator = v;
        }

        ((Cas20ServiceTicketValidator)validator).setProxyCallbackUrl(this.getPropertyFromInitParams(filterConfig, "proxyCallbackUrl", (String)null));
        ((Cas20ServiceTicketValidator)validator).setProxyGrantingTicketStorage(this.proxyGrantingTicketStorage);
        ((Cas20ServiceTicketValidator)validator).setProxyRetriever(new Cas20ProxyRetriever(casServerUrlPrefix));
        ((Cas20ServiceTicketValidator)validator).setRenew(Boolean.valueOf(this.getPropertyFromInitParams(filterConfig, "renew", "false").toString()));
        Map additionalParameters = new HashMap();
        List params = Arrays.asList(RESERVED_INIT_PARAMS);
        Enumeration e = filterConfig.getInitParameterNames();

        while(e.hasMoreElements()) {
            String s = (String)e.nextElement();
            if (!params.contains(s)) {
                additionalParameters.put(s, filterConfig.getInitParameter(s));
            }
        }

        ((Cas20ServiceTicketValidator)validator).setCustomParameters(additionalParameters);
        return (TicketValidator)validator;
    }

    protected final List constructListOfProxies(String proxies) {
        if (CommonUtils.isBlank(proxies)) {
            return new ArrayList();
        } else {
            String[] splitProxies = proxies.split("\n");
            List items = Arrays.asList(splitProxies);
            ProxyListEditor editor = new ProxyListEditor();
            editor.setValue(items);
            return (List)editor.getValue();
        }
    }

    protected final boolean preFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestUri = request.getRequestURI();
        if (!CommonUtils.isEmpty(this.proxyReceptorUrl) && requestUri.endsWith(this.proxyReceptorUrl)) {
            CommonUtils.readAndRespondToProxyReceptorRequest(request, response, this.proxyGrantingTicketStorage);
            return false;
        } else {
            return true;
        }
    }

    public final void setProxyReceptorUrl(String proxyReceptorUrl) {
        this.proxyReceptorUrl = proxyReceptorUrl;
    }

    public final void setProxyGrantingTicketStorage(ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
        this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
    }
}
