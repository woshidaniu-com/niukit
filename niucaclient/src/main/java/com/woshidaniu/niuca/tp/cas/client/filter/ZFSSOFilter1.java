package com.woshidaniu.niuca.tp.cas.client.filter;


import com.woshidaniu.niuca.tp.cas.client.ProxyTicketValidator;
import com.woshidaniu.niuca.tp.cas.client.Util;
import com.woshidaniu.niuca.tp.cas.client.ZfssoBean;
import com.woshidaniu.niuca.tp.cas.client.ZfssoConfig;
import com.woshidaniu.niuca.tp.cas.client.ZfssoReadConfig;
import com.woshidaniu.niuca.tp.cas.util.Tool;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class ZFSSOFilter1 implements Filter {
    public static final String CAS_FILTER_USER = "com.woshidaniu.niuca.tp.cas.client.filter.user";
    private String casurl;
    private String casLogin;
    private String casValidate;
    private String casAuthorizedProxy;
    private String casServiceUrl;
    private String casRenew;
    private String casServerName;
    private String setsessionclass;
    private String usezfca;
    private List notCheckURLList = new ArrayList();
    private boolean wrapRequest;
    Class classType = null;
    Object objectCopy = null;
    Method themethod = null;
    Object result = null;

    public ZFSSOFilter1() {
    }

    public void init(FilterConfig config) throws ServletException {
        System.out.println("log_woshidaniu_init(1)");
        ServletContext servletContext = config.getServletContext();
        String dbpagh = servletContext.getRealPath("/WEB-INF/zfssoconfig.properties");
        new ZfssoReadConfig(dbpagh);
        System.out.println("log_woshidaniu_init(2)");
        String notCheckURLListStr = config.getInitParameter("notCheckURLList");
        if (notCheckURLListStr != null) {
            StringTokenizer st = new StringTokenizer(notCheckURLListStr, ";");
            this.notCheckURLList.clear();

            while(st.hasMoreTokens()) {
                this.notCheckURLList.add(st.nextToken());
            }
        }

        System.out.println("log_woshidaniu_init(3)");
        this.casurl = ZfssoConfig.casurl;
        this.casLogin = this.casurl + "/login";
        this.casValidate = this.casurl + "/serviceValidate";
        this.casServiceUrl = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.serviceUrl");
        this.casAuthorizedProxy = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.authorizedProxy");
        this.casRenew = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.renew");
        this.casServerName = ZfssoConfig.ywxtservername;
        this.wrapRequest = Boolean.valueOf(config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.wrapRequest"));
        this.setsessionclass = config.getInitParameter("setsessionclass");
        this.usezfca = ZfssoConfig.usezfca;
        System.out.println("log_woshidaniu_init(4)");
        if (!this.usezfca.equals("0")) {
            try {
                System.out.println("log_woshidaniu_init(5)");
                this.classType = Class.forName(this.setsessionclass);
                this.objectCopy = this.classType.newInstance();
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
        System.out.println("##################################");
        System.out.println("(1)doFilter=================");
        HttpServletRequest request1 = (HttpServletRequest)request;
        String user = "";
        ((HttpServletResponse)response).setHeader("P3P", "CP=CAO PSA OUR");
        if (this.usezfca.equals("0")) {
            this.casLogin = null;
            this.casValidate = null;
            this.casServiceUrl = null;
            this.casAuthorizedProxy = null;
            this.casRenew = null;
            this.casServerName = null;
            fc.doFilter((ServletRequest)request, response);
        } else {
            System.out.println("(2)doFilter=================");
            String yhlx = "";
            if (((ServletRequest)request).getParameter("jsName") != null) {
                yhlx = ((ServletRequest)request).getParameter("jsName");
            }

            if (!this.checkRequestURIIntNotFilterList(request1)) {
                if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
                    System.out.println("(4)doFilter=================");
                    if (this.wrapRequest) {
                        System.out.println("(5)doFilter=================");
                        request = new CASFilterRequestWrapper((HttpServletRequest)request);
                    }

                    System.out.println("(6)doFilter=================");
                    HttpSession session = ((HttpServletRequest)request).getSession();
                    if (session != null && session.getAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user") != null) {
                        fc.doFilter((ServletRequest)request, response);
                    } else {
                        String ticket = ((ServletRequest)request).getParameter("ticket");
                        System.out.println("(7)doFilter=================");
                        if (!Tool.isNull(ticket)) {
                            ProxyTicketValidator pv = this.getAuthenticatedUser((HttpServletRequest)request);
                            if (pv != null) {
                                user = pv.getUser();
                                System.out.println("user:" + user);
                                if (!this.setSession(user, yhlx, request1, (HttpServletResponse)response, session)) {
                                    if (session.getAttribute("errMsg") != null) {
                                        String errMsg = (String)session.getAttribute("errMsg");
                                        if (errMsg != null && !"".equals(errMsg)) {
                                            errMsg = URLEncoder.encode(errMsg, "UTF-8");
                                        }

                                        ((HttpServletResponse)response).sendRedirect(this.casurl + "/zfssoererr.jsp?errMsg=" + errMsg);
                                    } else {
                                        ((HttpServletResponse)response).sendRedirect(this.casurl + "/zfssoererr.jsp?errordm=3");
                                    }

                                    return;
                                }

                                if (session != null) {
                                    session.setAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user", user);
                                    session.setAttribute("edu.yale.its.tp.cas.client.filter.user", user);
                                    session.setAttribute("edu.yale.its.tp.cas.client.filter.receipt", pv);
                                }

                                System.out.println("(8)doFilter=================");
                                fc.doFilter((ServletRequest)request, response);
                                return;
                            }

                            System.out.println("user1:" + user);
                        }

                        if (this.casLogin == null) {
                            throw new ServletException("When CASFilter protects pages that do not receive a 'ticket' parameter, it needs a com.woshidaniu.niuca.tp.cas.casurl filter parameter");
                        } else {
                            System.out.println("(9)doFilter=================");
                            if (session != null && session.getAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user") != null) {
                                System.out.println("=====fc.doFilter=====");
                                fc.doFilter((ServletRequest)request, response);
                            } else {
                                String url = this.casLogin + "?service=" + this.getService((HttpServletRequest)request) + (this.casRenew != null && !this.casRenew.equals("") ? "&renew=" + this.casRenew : "");
                                System.out.println("casurl:" + url);
                                ((HttpServletResponse)response).sendRedirect(url);
                            }

                            System.out.println("(10)doFilter=================");
                        }
                    }
                } else {
                    throw new ServletException("CASFilter protects only HTTP resources");
                }
            }
        }
    }

    private boolean checkRequestURIIntNotFilterList(HttpServletRequest request) {
        String uri = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        return this.notCheckURLList.contains(uri);
    }

    public void destroy() {
        this.notCheckURLList.clear();
    }

    public Boolean setSession(String yhm, String yhlx, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        boolean res = false;

        try {
            ZfssoBean zfssobean = new ZfssoBean();
            zfssobean.setYhm(yhm);
            zfssobean.setYhlx(yhlx);
            zfssobean.setRequest(request);
            zfssobean.setResponse(response);
            zfssobean.setSession(session);
            this.themethod = this.classType.getMethod("chkUserSession", ZfssoBean.class);
            this.result = this.themethod.invoke(this.objectCopy, zfssobean);
            if ("true".equals(this.result.toString())) {
                res = true;
            }

            if (!res) {
                this.themethod = this.classType.getMethod("setUserSession", ZfssoBean.class);
                this.result = this.themethod.invoke(this.objectCopy, zfssobean);
                if ("true".equals(this.result.toString())) {
                    res = true;
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return res;
    }

    private ProxyTicketValidator getAuthenticatedUser(HttpServletRequest request) throws ServletException {
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
                return null;
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

                return pv;
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
