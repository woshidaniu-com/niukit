package com.woshidaniu.niuca.tp.cas.validation;


import com.woshidaniu.niuca.tp.cas.util.XmlUtils;
import java.util.List;

public class Cas20ProxyTicketValidator extends Cas20ServiceTicketValidator {
    private boolean acceptAnyProxy;
    private ProxyList allowedProxyChains = new ProxyList();

    public Cas20ProxyTicketValidator(String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    public ProxyList getAllowedProxyChains() {
        return this.allowedProxyChains;
    }

    protected String getUrlSuffix() {
        return "proxyValidate";
    }

    protected void customParseResponse(String response, Assertion assertion) throws TicketValidationException {
        List proxies = XmlUtils.getTextForElements(response, "proxy");
        String[] proxiedList = (String[])proxies.toArray(new String[proxies.size()]);
        if (proxies != null && !proxies.isEmpty() && !this.acceptAnyProxy) {
            if (!this.allowedProxyChains.contains(proxiedList)) {
                throw new InvalidProxyChainTicketValidationException("Invalid proxy chain: " + proxies.toString());
            }
        }
    }

    public void setAcceptAnyProxy(boolean acceptAnyProxy) {
        this.acceptAnyProxy = acceptAnyProxy;
    }

    public void setAllowedProxyChains(ProxyList allowedProxyChains) {
        this.allowedProxyChains = allowedProxyChains;
    }
}
