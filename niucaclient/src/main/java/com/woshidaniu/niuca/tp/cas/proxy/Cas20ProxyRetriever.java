package com.woshidaniu.niuca.tp.cas.proxy;


import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import com.woshidaniu.niuca.tp.cas.util.XmlUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Cas20ProxyRetriever implements ProxyRetriever {
    private static final long serialVersionUID = 560409469568911791L;
    private final Log log = LogFactory.getLog(this.getClass());
    private final String casServerUrl;

    public Cas20ProxyRetriever(String casServerUrl) {
        CommonUtils.assertNotNull(casServerUrl, "casServerUrl cannot be null.");
        this.casServerUrl = casServerUrl;
    }

    public String getProxyTicketIdFor(String proxyGrantingTicketId, String targetService) {
        String url = this.constructUrl(proxyGrantingTicketId, targetService);
        HttpURLConnection conn = null;

        try {
            URL constructedUrl = new URL(url);
            conn = (HttpURLConnection)constructedUrl.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer(255);
            String response;
            synchronized(stringBuffer) {
                String line;
                while((line = in.readLine()) != null) {
                    stringBuffer.append(line);
                }

                response = stringBuffer.toString();
            }

            String error = XmlUtils.getTextForElement(response, "proxyFailure");
            if (!CommonUtils.isNotEmpty(error)) {
                String var12 = XmlUtils.getTextForElement(response, "proxyTicket");
                return var12;
            }

            this.log.debug(error);
        } catch (Exception var17) {
            throw new RuntimeException(var17);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return null;
    }

    private String constructUrl(String proxyGrantingTicketId, String targetService) {
        try {
            return this.casServerUrl + (this.casServerUrl.endsWith("/") ? "" : "/") + "proxy" + "?pgt=" + proxyGrantingTicketId + "&targetService=" + URLEncoder.encode(targetService, "UTF-8");
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException(var4);
        }
    }
}
