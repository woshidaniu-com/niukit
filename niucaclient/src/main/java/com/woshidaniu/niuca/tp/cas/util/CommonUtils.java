package com.woshidaniu.niuca.tp.cas.util;


import com.woshidaniu.niuca.tp.cas.proxy.ProxyGrantingTicketStorage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class CommonUtils {
    private static final Log LOG = LogFactory.getLog(CommonUtils.class);
    private static final String PARAM_PROXY_GRANTING_TICKET_IOU = "pgtIou";
    private static final String PARAM_PROXY_GRANTING_TICKET = "pgtId";

    private CommonUtils() {
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertNotEmpty(Collection c, String message) {
        assertNotNull(c, message);
        if (c.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertTrue(boolean cond, String message) {
        if (!cond) {
            throw new IllegalArgumentException(message);
        }
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isBlank(String string) {
        return isEmpty(string) || string.trim().length() == 0;
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    public static final String constructRedirectUrl(String casServerLoginUrl, String serviceParameterName, String serviceUrl, boolean renew, boolean gateway) {
        try {
            return casServerLoginUrl + "?" + serviceParameterName + "=" + URLEncoder.encode(serviceUrl, "UTF-8") + (renew ? "&renew=true" : "") + (gateway ? "&gateway=true" : "");
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException(var6);
        }
    }

    public static final void readAndRespondToProxyReceptorRequest(HttpServletRequest request, HttpServletResponse response, ProxyGrantingTicketStorage proxyGrantingTicketStorage) throws IOException {
        String proxyGrantingTicketIou = request.getParameter("pgtIou");
        String proxyGrantingTicket = request.getParameter("pgtId");
        if (!isBlank(proxyGrantingTicket) && !isBlank(proxyGrantingTicketIou)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received proxyGrantingTicketId [" + proxyGrantingTicket + "] for proxyGrantingTicketIou [" + proxyGrantingTicketIou + "]");
            }

            proxyGrantingTicketStorage.save(proxyGrantingTicketIou, proxyGrantingTicket);
            response.getWriter().write("<?xml version=\"1.0\"?>");
            response.getWriter().write("<casClient:proxySuccess xmlns:casClient=\"http://www.yale.edu/tp/casClient\" />");
        } else {
            response.getWriter().write("");
        }
    }

    public static final String constructServiceUrl(HttpServletRequest request, HttpServletResponse response, String service, String serverName, String artifactParameterName, boolean encode) {
        if (isNotBlank(service)) {
            return encode ? response.encodeURL(service) : service;
        } else {
            StringBuffer buffer = new StringBuffer();
            synchronized(buffer) {
                if (!serverName.startsWith("https://") && !serverName.startsWith("http://")) {
                    buffer.append(request.isSecure() ? "https://" : "http://");
                }

                buffer.append(serverName);
                buffer.append(request.getRequestURI());
                if (isNotBlank(request.getQueryString())) {
                    int location = request.getQueryString().indexOf(artifactParameterName + "=");
                    if (location == 0) {
                        String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("serviceUrl generated: " + returnValue);
                        }

                        return returnValue;
                    }

                    buffer.append("?");
                    if (location == -1) {
                        buffer.append(request.getQueryString());
                    } else if (location > 0) {
                        int actualLocation = request.getQueryString().indexOf("&" + artifactParameterName + "=");
                        if (actualLocation == -1) {
                            buffer.append(request.getQueryString());
                        } else if (actualLocation > 0) {
                            buffer.append(request.getQueryString().substring(0, actualLocation));
                        }
                    }
                }
            }

            String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
            if (LOG.isDebugEnabled()) {
                LOG.debug("serviceUrl generated: " + returnValue);
            }

            return returnValue;
        }
    }
}
