package com.woshidaniu.niuca.tp.cas.proxy;


import com.woshidaniu.niuca.tp.cas.util.SecureURL;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyTicketReceptor extends HttpServlet {
    private static final String PGT_IOU_PARAM = "pgtIou";
    private static final String PGT_ID_PARAM = "pgtId";
    private static Map pgt;
    private static String casProxyUrl;

    public ProxyTicketReceptor() {
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Class var2 = ProxyTicketReceptor.class;
        synchronized(ProxyTicketReceptor.class) {
            if (pgt == null) {
                pgt = new HashMap();
            }

            if (casProxyUrl == null) {
                ServletContext app = config.getServletContext();
                casProxyUrl = app.getInitParameter("com.woshidaniu.niuca.tp.cas.proxyUrl");
                if (casProxyUrl == null) {
                    throw new ServletException("need com.woshidaniu.niuca.tp.cas.proxyUrl");
                }
            }

        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pgtId = request.getParameter("pgtId");
        String pgtIou = request.getParameter("pgtIou");
        if (pgtId != null && pgtIou != null) {
            synchronized(pgt) {
                pgt.put(pgtIou, pgtId);
            }
        }

        PrintWriter out = response.getWriter();
        out.println("<casClient:proxySuccess xmlns:casClient=\"http://www.yale.edu/tp/casClient\"/>");
        out.flush();
    }

    public static String getProxyTicket(String pgtIou, String target) throws IOException {
        Class pgtId = ProxyTicketReceptor.class;
        synchronized(ProxyTicketReceptor.class) {
            if (casProxyUrl == null || pgt == null) {
                throw new IllegalStateException("getProxyTicket() only works after servlet has been initialized");
            }
        }

        pgtId = null;
        String pgtId2;
        synchronized(pgt) {
            pgtId2 = (String)pgt.get(pgtIou);
        }

        if (pgtId2 == null) {
            return null;
        } else {
            String url = casProxyUrl + "?pgt=" + pgtId2 + "&targetService=" + target;
            String response = SecureURL.retrieve(url);
            if (response.indexOf("<cas:proxySuccess>") != -1 && response.indexOf("<cas:proxyTicket>") != -1) {
                int startIndex = response.indexOf("<cas:proxyTicket>") + "<cas:proxyTicket>".length();
                int endIndex = response.indexOf("</cas:proxyTicket>");
                return response.substring(startIndex, endIndex);
            } else {
                return null;
            }
        }
    }
}
