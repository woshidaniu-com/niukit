package com.woshidaniu.niuca.tp.cas.validation;


import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractUrlBasedTicketValidator implements TicketValidator {
    protected final Log log = LogFactory.getLog(this.getClass());
    private final String casServerUrlPrefix;
    private boolean renew;
    private Map customParameters;

    protected AbstractUrlBasedTicketValidator(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
        CommonUtils.assertNotNull(this.casServerUrlPrefix, "casServerUrlPrefix cannot be null.");
    }

    protected void populateUrlAttributeMap(Map urlParameters) {
    }

    protected abstract String getUrlSuffix();

    protected final String constructValidationUrl(String ticket, String serviceUrl) {
        Map urlParameters = new HashMap();
        this.log.debug("Placing URL parameters in map.");
        urlParameters.put("ticket", ticket);
        urlParameters.put("service", this.encodeUrl(serviceUrl));
        if (this.renew) {
            urlParameters.put("renew", "true");
        }

        this.log.debug("Calling template URL attribute map.");
        this.populateUrlAttributeMap(urlParameters);
        this.log.debug("Loading custom parameters from configuration.");
        if (this.customParameters != null) {
            urlParameters.putAll(this.customParameters);
        }

        String suffix = this.getUrlSuffix();
        StringBuffer buffer = new StringBuffer(urlParameters.size() * 10 + this.casServerUrlPrefix.length() + suffix.length() + 1);
        int i = 0;
        synchronized(buffer) {
            buffer.append(this.casServerUrlPrefix);
            if (!this.casServerUrlPrefix.endsWith("/")) {
                buffer.append("/");
            }

            buffer.append(suffix);
            Iterator iter = urlParameters.entrySet().iterator();

            while(iter.hasNext()) {
                buffer.append(i++ == 0 ? "?" : "&");
                Entry entry = (Entry)iter.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                if (value != null) {
                    buffer.append(key);
                    buffer.append("=");
                    buffer.append(value);
                }
            }

            return buffer.toString();
        }
    }

    protected final String encodeUrl(String url) {
        if (url == null) {
            return null;
        } else {
            try {
                return URLEncoder.encode(url, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                return url;
            }
        }
    }

    protected abstract Assertion parseResponseFromServer(String var1) throws TicketValidationException;

    protected abstract String retrieveResponseFromServer(URL var1, String var2);

    public Assertion validate(String ticket, String service) throws TicketValidationException {
        String validationUrl = this.constructValidationUrl(ticket, service);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Constructing validation url: " + validationUrl);
        }

        try {
            this.log.debug("Retrieving response from server.");
            String serverResponse = this.retrieveResponseFromServer(new URL(validationUrl), ticket);
            if (serverResponse == null) {
                throw new TicketValidationException("The CAS server returned no response.");
            } else {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Server response: " + serverResponse);
                }

                return this.parseResponseFromServer(serverResponse);
            }
        } catch (MalformedURLException var5) {
            throw new TicketValidationException(var5);
        }
    }

    public void setRenew(boolean renew) {
        this.renew = renew;
    }

    public void setCustomParameters(Map customParameters) {
        this.customParameters = customParameters;
    }
}
