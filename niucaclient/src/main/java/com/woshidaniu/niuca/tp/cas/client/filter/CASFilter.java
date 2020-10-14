package com.woshidaniu.niuca.tp.cas.client.filter;


import com.woshidaniu.niuca.tp.cas.client.ProxyTicketValidator;
import com.woshidaniu.niuca.tp.cas.client.Util;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class CASFilter implements Filter {
    public static final String CAS_FILTER_USER = "com.woshidaniu.niuca.tp.cas.client.filter.user";
    private String casLogin;
    private String casValidate;
    private String casAuthorizedProxy;
    private String casServiceUrl;
    private String casRenew;
    private String casServerName;
    private boolean wrapRequest;

    public CASFilter() {
    }

    public void init(FilterConfig config) throws ServletException {
        this.casLogin = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.loginUrl");
        this.casValidate = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.validateUrl");
        this.casServiceUrl = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.serviceUrl");
        this.casAuthorizedProxy = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.authorizedProxy");
        this.casRenew = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.renew");
        this.casServerName = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.serverName");
        this.wrapRequest = Boolean.valueOf(config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.wrapRequest"));
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            if (this.wrapRequest) {
                request = new CASFilterRequestWrapper((HttpServletRequest)request);
            }

            HttpSession session = ((HttpServletRequest)request).getSession();
            if (session != null && session.getAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user") != null) {
                fc.doFilter((ServletRequest)request, response);
            } else {
                String ticket = ((ServletRequest)request).getParameter("ticket");
                if (ticket != null && !ticket.equals("")) {
                    String user = this.getAuthenticatedUser((HttpServletRequest)request);
                    if (user == null) {
                        throw new ServletException("Unexpected CAS authentication error");
                    } else {
                        if (session != null) {
                            session.setAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user", user);
                        }

                        fc.doFilter((ServletRequest)request, response);
                    }
                } else if (this.casLogin == null) {
                    throw new ServletException("When CASFilter protects pages that do not receive a 'ticket' parameter, it needs a com.woshidaniu.niuca.tp.cas.client.filter.loginUrl filter parameter");
                } else {
                    ((HttpServletResponse)response).sendRedirect(this.casLogin + "?service=" + this.getService((HttpServletRequest)request) + (this.casRenew != null && !this.casRenew.equals("") ? "&renew=" + this.casRenew : ""));
                }
            }
        } else {
            throw new ServletException("CASFilter protects only HTTP resources");
        }
    }

    public void destroy() {
    }

    private String getAuthenticatedUser(HttpServletRequest request) throws ServletException {
        ProxyTicketValidator pv = null;

        String proxy;
        try {
            pv = new ProxyTicketValidator();
            pv.setCasValidateUrl(this.casValidate);
            pv.setServiceTicket(request.getParameter("ticket"));
            pv.setService(this.getService(request));
            pv.setRenew(Boolean.valueOf(this.casRenew));
            pv.validate();
            if (!pv.isAuthenticationSuccesful()) {
                throw new ServletException("CAS authentication error: " + pv.getErrorCode() + ": " + pv.getErrorMessage());
            } else {
                if (pv.getProxyList().size() != 0) {
                    if (this.casAuthorizedProxy == null) {
                        throw new ServletException("this page does not accept proxied tickets");
                    }

                    boolean authorized = false;
                    proxy = (String)pv.getProxyList().get(0);
                    StringTokenizer casProxies = new StringTokenizer(this.casAuthorizedProxy);

                    while(casProxies.hasMoreTokens()) {
                        if (proxy.equals(casProxies.nextToken())) {
                            authorized = true;
                            break;
                        }
                    }

                    if (!authorized) {
                        throw new ServletException("unauthorized top-level proxy: '" + pv.getProxyList().get(0) + "'");
                    }
                }

                return pv.getUser();
            }
        } catch (SAXException var6) {
            proxy = "";
            if (pv != null) {
                proxy = pv.getResponse();
            }

            throw new ServletException(var6 + " " + proxy);
        } catch (ParserConfigurationException var7) {
            throw new ServletException(var7);
        } catch (IOException var8) {
            throw new ServletException(var8);
        }
    }

    private String getService(HttpServletRequest request) throws ServletException {
        if (this.casServerName == null && this.casServiceUrl == null) {
            throw new ServletException("need one of the following configuration parameters: com.woshidaniu.niuca.tp.cas.client.filter.serviceUrl or com.woshidaniu.niuca.tp.cas.client.filter.serverName");
        } else {
            return this.casServiceUrl != null ? URLEncoder.encode(this.casServiceUrl) : Util.getService(request, this.casServerName);
        }
    }
}
