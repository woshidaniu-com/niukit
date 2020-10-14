package com.woshidaniu.niuca.tp.cas.validation;


import com.woshidaniu.niuca.tp.cas.authentication.AttributePrincipal;
import com.woshidaniu.niuca.tp.cas.authentication.AttributePrincipalImpl;
import com.woshidaniu.niuca.tp.cas.proxy.Cas20ProxyRetriever;
import com.woshidaniu.niuca.tp.cas.proxy.ProxyGrantingTicketStorage;
import com.woshidaniu.niuca.tp.cas.proxy.ProxyRetriever;
import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import com.woshidaniu.niuca.tp.cas.util.XmlUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Cas20ServiceTicketValidator extends AbstractCasProtocolUrlBasedTicketValidator {
    private String proxyCallbackUrl;
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage;
    private ProxyRetriever proxyRetriever;

    public Cas20ServiceTicketValidator(String casServerUrlPrefix) {
        super(casServerUrlPrefix);
        this.proxyRetriever = new Cas20ProxyRetriever(casServerUrlPrefix);
    }

    protected final void populateUrlAttributeMap(Map urlParameters) {
        urlParameters.put("pgtUrl", this.encodeUrl(this.proxyCallbackUrl));
    }

    protected String getUrlSuffix() {
        return "serviceValidate";
    }

    protected final Assertion parseResponseFromServer(String response) throws TicketValidationException {
        String error = XmlUtils.getTextForElement(response, "authenticationFailure");
        if (CommonUtils.isNotBlank(error)) {
            throw new TicketValidationException(error);
        } else {
            String principal = XmlUtils.getTextForElement(response, "user");
            String proxyGrantingTicketIou = XmlUtils.getTextForElement(response, "proxyGrantingTicket");
            String proxyGrantingTicket = this.proxyGrantingTicketStorage != null ? this.proxyGrantingTicketStorage.retrieve(proxyGrantingTicketIou) : null;
            if (CommonUtils.isEmpty(principal)) {
                throw new TicketValidationException("No principal was found in the response from the CAS server.");
            } else {
                Map attributes = this.extractCustomAttributes(response);
                AssertionImpl assertion;
                if (CommonUtils.isNotBlank(proxyGrantingTicket)) {
                    AttributePrincipal attributePrincipal = new AttributePrincipalImpl(principal, attributes, proxyGrantingTicket, this.proxyRetriever);
                    assertion = new AssertionImpl(attributePrincipal);
                } else {
                    assertion = new AssertionImpl(new AttributePrincipalImpl(principal, attributes));
                }

                this.customParseResponse(response, assertion);
                return assertion;
            }
        }
    }

    protected Map extractCustomAttributes(String xml) {
        int pos1 = xml.indexOf("<cas:attributes>");
        int pos2 = xml.indexOf("</cas:attributes>");
        if (pos1 == -1) {
            return Collections.EMPTY_MAP;
        } else {
            String attributesText = xml.substring(pos1 + 16, pos2);
            Map attributes = new HashMap();
            BufferedReader br = new BufferedReader(new StringReader(attributesText));
            ArrayList attributeNames = new ArrayList();

            try {
                String line;
                while((line = br.readLine()) != null) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.length() > 0) {
                        int leftPos = trimmedLine.indexOf(":");
                        int rightPos = trimmedLine.indexOf(">");
                        attributeNames.add(trimmedLine.substring(leftPos + 1, rightPos));
                    }
                }

                br.close();
            } catch (IOException var12) {
            }

            Iterator iter = attributeNames.iterator();

            while(iter.hasNext()) {
                String name = (String)iter.next();
                attributes.put(name, XmlUtils.getTextForElement(xml, name));
            }

            return attributes;
        }
    }

    protected void customParseResponse(String response, Assertion assertion) throws TicketValidationException {
    }

    public final void setProxyCallbackUrl(String proxyCallbackUrl) {
        this.proxyCallbackUrl = proxyCallbackUrl;
    }

    public final void setProxyGrantingTicketStorage(ProxyGrantingTicketStorage proxyGrantingTicketStorage) {
        this.proxyGrantingTicketStorage = proxyGrantingTicketStorage;
    }

    public final void setProxyRetriever(ProxyRetriever proxyRetriever) {
        this.proxyRetriever = proxyRetriever;
    }
}
