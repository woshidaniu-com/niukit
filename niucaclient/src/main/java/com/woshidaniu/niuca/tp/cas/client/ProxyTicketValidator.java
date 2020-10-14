package com.woshidaniu.niuca.tp.cas.client;


import com.woshidaniu.niuca.tp.cas.client.ServiceTicketValidator.Handler;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ProxyTicketValidator extends ServiceTicketValidator {
    protected List proxyList;

    public ProxyTicketValidator() {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        ProxyTicketValidator pv = new ProxyTicketValidator();
        pv.setCasValidateUrl("https://portal.yale.edu/cas/proxyValidate");
        pv.setService(args[0]);
        pv.setServiceTicket(args[1]);
        pv.validate();
        System.out.println(pv.getResponse());
        System.out.println();
        if (pv.isAuthenticationSuccesful()) {
            System.out.println("user: " + pv.getUser());
            System.out.println("proxies:\n " + pv.getProxyList());
        } else {
            System.out.println("error code: " + pv.getErrorCode());
            System.out.println("error message: " + pv.getErrorMessage());
        }

    }

    public List getProxyList() {
        return this.proxyList;
    }

    protected DefaultHandler newHandler() {
        return new ProxyTicketValidator.ProxyHandler();
    }

    protected void clear() {
        super.clear();
        this.proxyList = null;
    }

    protected class ProxyHandler extends Handler {
        protected static final String PROXIES = "cas:proxies";
        protected static final String PROXY = "cas:proxy";
        protected List proxyList = new ArrayList();
        protected boolean proxyFragment = false;

        protected ProxyHandler() {
            super(ProxyTicketValidator.this);
        }

        public void startElement(String ns, String ln, String qn, Attributes a) {
            super.startElement(ns, ln, qn, a);
            if (this.authenticationSuccess && qn.equals("cas:proxies")) {
                this.proxyFragment = true;
            }

        }

        public void endElement(String ns, String ln, String qn) throws SAXException {
            super.endElement(ns, ln, qn);
            if (qn.equals("cas:proxies")) {
                this.proxyFragment = false;
            } else if (this.proxyFragment && qn.equals("cas:proxy")) {
                this.proxyList.add(this.currentText.toString().trim());
            }

        }

        public void endDocument() throws SAXException {
            super.endDocument();
            if (this.authenticationSuccess) {
                ProxyTicketValidator.this.proxyList = this.proxyList;
            }

        }
    }
}
