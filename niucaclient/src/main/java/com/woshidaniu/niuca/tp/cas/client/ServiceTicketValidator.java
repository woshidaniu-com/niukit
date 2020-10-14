package com.woshidaniu.niuca.tp.cas.client;


import com.woshidaniu.niuca.tp.cas.util.SecureURL;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ServiceTicketValidator {
    private String casValidateUrl;
    private String proxyCallbackUrl;
    private String st;
    private String service;
    private String pgtIou;
    private String user;
    private String errorCode;
    private String errorMessage;
    private String entireResponse;
    private boolean renew = false;
    private boolean attemptedAuthentication;
    private boolean successfulAuthentication;

    public ServiceTicketValidator() {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        ServiceTicketValidator sv = new ServiceTicketValidator();
        sv.setCasValidateUrl("https://niuca:8443/niuca/serviceValidate");
        sv.setProxyCallbackUrl("https://niuca:8443/casProxy/receptor");
        sv.setService(args[0]);
        sv.setServiceTicket(args[1]);
        sv.validate();
        System.out.println(sv.getResponse());
        System.out.println();
        if (sv.isAuthenticationSuccesful()) {
            System.out.println("user: " + sv.getUser());
            System.out.println("pgtIou: " + sv.getPgtIou());
        } else {
            System.out.println("error code: " + sv.getErrorCode());
            System.out.println("error message: " + sv.getErrorMessage());
        }

    }

    public void setCasValidateUrl(String x) {
        this.casValidateUrl = x;
    }

    public String getCasValidateUrl() {
        return this.casValidateUrl;
    }

    public void setProxyCallbackUrl(String x) {
        this.proxyCallbackUrl = x;
    }

    public void setRenew(boolean b) {
        this.renew = b;
    }

    public String getProxyCallbackUrl() {
        return this.proxyCallbackUrl;
    }

    public void setServiceTicket(String x) {
        this.st = x;
    }

    public void setService(String x) {
        this.service = x;
    }

    public String getUser() {
        return this.user;
    }

    public String getPgtIou() {
        return this.pgtIou;
    }

    public boolean isAuthenticationSuccesful() {
        return this.successfulAuthentication;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getResponse() {
        return this.entireResponse;
    }

    public void validate() throws IOException, SAXException, ParserConfigurationException {
        if (this.casValidateUrl != null && this.st != null) {
            this.clear();
            this.attemptedAuthentication = true;
            StringBuffer sb = new StringBuffer();
            sb.append(this.casValidateUrl);
            if (this.casValidateUrl.indexOf(63) == -1) {
                sb.append('?');
            } else {
                sb.append('&');
            }

            sb.append("service=" + this.service + "&ticket=" + this.st);
            if (this.proxyCallbackUrl != null) {
                sb.append("&pgtUrl=" + this.proxyCallbackUrl);
            }

            if (this.renew) {
                sb.append("&renew=true");
            }

            String url = sb.toString();
            String response = SecureURL.retrieve(url);
            this.entireResponse = response;
            if (response != null) {
                XMLReader r = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                r.setFeature("http://xml.org/sax/features/namespaces", false);
                r.setContentHandler(this.newHandler());
                r.parse(new InputSource(new StringReader(response)));
            }

        } else {
            throw new IllegalStateException("must set validation URL and ticket");
        }
    }

    protected DefaultHandler newHandler() {
        return new ServiceTicketValidator.Handler();
    }

    protected void clear() {
        this.user = this.pgtIou = this.errorMessage = null;
        this.attemptedAuthentication = false;
        this.successfulAuthentication = false;
    }

    protected class Handler extends DefaultHandler {
        protected static final String AUTHENTICATION_SUCCESS = "cas:authenticationSuccess";
        protected static final String AUTHENTICATION_FAILURE = "cas:authenticationFailure";
        protected static final String PROXY_GRANTING_TICKET = "cas:proxyGrantingTicket";
        protected static final String USER = "cas:user";
        protected StringBuffer currentText = new StringBuffer();
        protected boolean authenticationSuccess = false;
        protected boolean authenticationFailure = false;
        protected String netid;
        protected String pgtIou;
        protected String errorCode;
        protected String errorMessage;

        protected Handler() {
        }

        public Handler(ProxyTicketValidator proxyTicketValidator) {
        }

        public void startElement(String ns, String ln, String qn, Attributes a) {
            this.currentText = new StringBuffer();
            if (qn.equals("cas:authenticationSuccess")) {
                this.authenticationSuccess = true;
            } else if (qn.equals("cas:authenticationFailure")) {
                this.authenticationFailure = true;
                this.errorCode = a.getValue("code");
                if (this.errorCode != null) {
                    this.errorCode = this.errorCode.trim();
                }
            }

        }

        public void characters(char[] ch, int start, int length) {
            this.currentText.append(ch, start, length);
        }

        public void endElement(String ns, String ln, String qn) throws SAXException {
            if (this.authenticationSuccess) {
                if (qn.equals("cas:user")) {
                    ServiceTicketValidator.this.user = this.currentText.toString().trim();
                }

                if (qn.equals("cas:proxyGrantingTicket")) {
                    this.pgtIou = this.currentText.toString().trim();
                }
            } else if (this.authenticationFailure && qn.equals("cas:authenticationFailure")) {
                this.errorMessage = this.currentText.toString().trim();
            }

        }

        public void endDocument() throws SAXException {
            if (this.authenticationSuccess) {
                ServiceTicketValidator.this.user = ServiceTicketValidator.this.user;
                ServiceTicketValidator.this.pgtIou = this.pgtIou;
                ServiceTicketValidator.this.successfulAuthentication = true;
            } else {
                if (!this.authenticationFailure) {
                    throw new SAXException("no indication of success of failure from CAS");
                }

                ServiceTicketValidator.this.errorMessage = this.errorMessage;
                ServiceTicketValidator.this.errorCode = this.errorCode;
                ServiceTicketValidator.this.successfulAuthentication = false;
            }

        }
    }
}
