package com.woshidaniu.niuca.tp.cas.validation;

import com.woshidaniu.niuca.tp.cas.authentication.AttributePrincipal;
import com.woshidaniu.niuca.tp.cas.authentication.AttributePrincipalImpl;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLException;
import org.opensaml.SAMLResponse;
import org.opensaml.SAMLStatement;
import org.opensaml.SAMLSubject;

public final class Saml11TicketValidator extends AbstractUrlBasedTicketValidator {
    private long tolerance = 1000L;

    public Saml11TicketValidator(String casServerUrlPrefix) {
        super(casServerUrlPrefix);
    }

    protected String getUrlSuffix() {
        return "samlValidate";
    }

    protected void populateUrlAttributeMap(Map urlParameters) {
        String service = (String)urlParameters.get("service");
        urlParameters.remove("service");
        urlParameters.remove("ticket");
        urlParameters.put("TARGET", service);
    }

    protected Assertion parseResponseFromServer(String response) throws TicketValidationException {
        try {
            String removeStartOfSoapBody = response.substring(response.indexOf("<SOAP-ENV:Body>") + 15);
            String removeEndOfSoapBody = removeStartOfSoapBody.substring(0, removeStartOfSoapBody.indexOf("</SOAP-ENV:Body>"));
            SAMLResponse samlResponse = new SAMLResponse(new ByteArrayInputStream(removeEndOfSoapBody.getBytes()));
            if (!samlResponse.getAssertions().hasNext()) {
                throw new TicketValidationException("No assertions found.");
            }

            Iterator iter = samlResponse.getAssertions();

            while(iter.hasNext()) {
                SAMLAssertion assertion = (SAMLAssertion)iter.next();
                if (this.isValidAssertion(assertion)) {
                    SAMLAuthenticationStatement authenticationStatement = this.getSAMLAuthenticationStatement(assertion);
                    if (authenticationStatement == null) {
                        throw new TicketValidationException("No AuthentiationStatement found in SAML Assertion.");
                    }

                    SAMLSubject subject = authenticationStatement.getSubject();
                    if (subject == null) {
                        throw new TicketValidationException("No Subject found in SAML Assertion.");
                    }

                    SAMLAttribute[] attributes = this.getAttributesFor(assertion, subject);
                    Map personAttributes = new HashMap();

                    for(int i = 0; i < attributes.length; ++i) {
                        SAMLAttribute samlAttribute = attributes[i];
                        List values = this.getValuesFrom(samlAttribute);
                        personAttributes.put(samlAttribute.getName(), values.size() == 1 ? values.get(0) : values);
                    }

                    AttributePrincipal principal = new AttributePrincipalImpl(subject.getNameIdentifier().getName(), personAttributes);
                    Map authenticationAttributes = new HashMap();
                    authenticationAttributes.put("samlAuthenticationStatement::authMethod", authenticationStatement.getAuthMethod());
                    Assertion casAssertion = new AssertionImpl(principal, authenticationAttributes);
                    return casAssertion;
                }
            }
        } catch (SAMLException var14) {
            throw new TicketValidationException(var14);
        }

        throw new TicketValidationException("No valid assertions from the SAML response found.");
    }

    private boolean isValidAssertion(SAMLAssertion assertion) {
        Date notBefore = assertion.getNotBefore();
        Date notOnOrAfter = assertion.getNotOnOrAfter();
        if (assertion.getNotBefore() != null && assertion.getNotOnOrAfter() != null) {
            long currentTime = (new Date()).getTime();
            if (currentTime + this.tolerance < notBefore.getTime()) {
                this.log.debug("skipping assertion that's not yet valid...");
                return false;
            } else if (notOnOrAfter.getTime() <= currentTime - this.tolerance) {
                this.log.debug("skipping expired assertion...");
                return false;
            } else {
                return true;
            }
        } else {
            this.log.debug("Assertion has no bounding dates. Will not process.");
            return false;
        }
    }

    private SAMLAuthenticationStatement getSAMLAuthenticationStatement(SAMLAssertion assertion) {
        Iterator iter = assertion.getStatements();

        while(iter.hasNext()) {
            SAMLStatement statement = (SAMLStatement)iter.next();
            if (statement instanceof SAMLAuthenticationStatement) {
                return (SAMLAuthenticationStatement)statement;
            }
        }

        return null;
    }

    private SAMLAttribute[] getAttributesFor(SAMLAssertion assertion, SAMLSubject subject) {
        List attributes = new ArrayList();
        Iterator iter = assertion.getStatements();

        while(true) {
            SAMLAttributeStatement attributeStatement;
            do {
                SAMLStatement statement;
                do {
                    if (!iter.hasNext()) {
                        return (SAMLAttribute[])attributes.toArray(new SAMLAttribute[attributes.size()]);
                    }

                    statement = (SAMLStatement)iter.next();
                } while(!(statement instanceof SAMLAttributeStatement));

                attributeStatement = (SAMLAttributeStatement)statement;
            } while(!subject.getNameIdentifier().getName().equals(attributeStatement.getSubject().getNameIdentifier().getName()));

            Iterator iter2 = attributeStatement.getAttributes();

            while(iter2.hasNext()) {
                attributes.add(iter2.next());
            }
        }
    }

    private List getValuesFrom(SAMLAttribute attribute) {
        List list = new ArrayList();
        Iterator iter = attribute.getValues();

        while(iter.hasNext()) {
            list.add(iter.next());
        }

        return list;
    }

    protected String retrieveResponseFromServer(URL validationUrl, String ticket) {
        String MESSAGE_TO_SEND = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body><samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\"  MajorVersion=\"1\" MinorVersion=\"1\" RequestID=\"_192.168.16.51.1024506224022\" IssueInstant=\"2002-06-19T17:03:44.022Z\"><samlp:AssertionArtifact>" + ticket + "</samlp:AssertionArtifact></samlp:Request></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection)validationUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Content-Length", Integer.toString(MESSAGE_TO_SEND.length()));
            conn.setRequestProperty("SOAPAction", "http://www.oasis-open.org/committees/security");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(MESSAGE_TO_SEND);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer buffer = new StringBuffer(256);
            synchronized(buffer) {
                String line;
                while((line = in.readLine()) != null) {
                    buffer.append(line);
                }

                String var11 = buffer.toString();
                return var11;
            }
        } catch (IOException var16) {
            throw new RuntimeException(var16);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }
    }

    public void setTolerance(long tolerance) {
        this.tolerance = tolerance;
    }
}
